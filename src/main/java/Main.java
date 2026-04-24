import dao.AccountAssetDAO;
import model.*;
import service.*;
import validator.*;
import view.menu.BuyAndSell;
import view.menu.Transfer;

import java.util.*;
import java.io.*;

public class Main {

    public static void main(String[] args) {
        Scanner sc;

        if (args.length > 0) {
            try {
                sc = new Scanner(new File(args[0]));
            } catch (FileNotFoundException e) {
                System.out.println("Arquivo não encontrado: " + args[0]);
                return;
            }
        } else {
            sc = new Scanner(System.in);
        }

        AuthService authService = new AuthService();
        AssetService assetService = new AssetService();
        TwoFactorService twoFactorService = new TwoFactorService();
        OperationService operationService = new OperationService();
        TransferService transferService = new TransferService();
        ReportService reportService = new ReportService();

        AccountAssetDAO accountAssetDao = new AccountAssetDAO();

        Account currentUser = null;
        boolean isAuthenticated = false;
        List<AccountAsset> accountAssets;

        try {
            while (true) {
                System.out.println("\n--- Cofre Digital McPato ---");
                System.out.println("1. Registrar");
                System.out.println("2. Login");
                System.out.println("3. Comprar/Vender Ativo");
                System.out.println("4. Transferência");
                System.out.println("5. Relatório");
                System.out.println("0. Sair");
                System.out.print("Escolha: ");

                int op;
                try {
                    op = Integer.parseInt(sc.nextLine());
                } catch (NoSuchElementException nse) {
                    System.out.println("Fim da entrada.");
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Opção inválida!");
                    continue;
                }

                if (op == 0) {
                    break;
                }

                switch (op) {
                    case 1:
                        // Registro PF/PJ (mantido)
                        try {
                            System.out.print("Pessoa Física ou Empresa (F/E)? ");
                            String tipo = sc.nextLine().toUpperCase();

                            System.out.print("Nome: ");
                            String nome = sc.nextLine();

                            System.out.print("Email: ");
                            String email = sc.nextLine();

                            System.out.print("Senha: ");
                            String senha = sc.nextLine();

                            UserValidator.validateEmail(email);
                            UserValidator.validatePassword(senha);

                            String hash = AuthService.hashPassword(senha);
                            Random rand = new Random();
                            String contaNum = String.format("%04d", rand.nextInt(10000));

                            Account acc;
                            if ("E".equals(tipo)) {
                                System.out.print("CNPJ: ");
                                String cnpj = sc.nextLine();
                                UserValidator.validateCnpj(cnpj);
                                acc = new AccountEmpresa(contaNum, nome, email, hash, cnpj, 0.0);
                            } else {
                                System.out.print("CPF: ");
                                String cpf = sc.nextLine();
                                UserValidator.validateCpf(cpf);
                                acc = new AccountPessoaFisica(contaNum, nome, email, hash, cpf, 0.0);
                            }

                            authService.register(acc);
                            System.out.println("Conta registrada! Número: " + acc.getAccountNumber());
                        } catch (ValidationException ve) {
                            System.out.println(ve.getMessage());
                        }
                        break;

                    case 2:
                        // Login + 2FA (mantido)
                        try {
                            System.out.print("Email: ");
                            String loginEmail = sc.nextLine();

                            System.out.print("Senha: ");
                            String loginSenha = sc.nextLine();

                            UserValidator.validateEmail(loginEmail);
                            UserValidator.validatePassword(loginSenha);

                            Account loginAcc = authService.login(loginEmail, loginSenha);
                            if (loginAcc != null) {
                                twoFactorService.generateAndSendCode(loginEmail);
                                System.out.print("Digite o código 2FA enviado: ");
                                String inputCode = sc.nextLine();

                                if (twoFactorService.validateCode(inputCode)) {
                                    currentUser = loginAcc;
                                    isAuthenticated = true;
                                    System.out.println("Login realizado com sucesso!");
                                } else {
                                    System.out.println("2FA incorreto!");
                                }
                            } else {
                                System.out.println("Credenciais inválidas!");
                            }
                        } catch (ValidationException ve) {
                            System.out.println(ve.getMessage());
                        }
                        break;

                    case 3:
                        // Compra/Venda (mantida, agora com Operation/ArrayList)
                        if (isAuthenticated) {
                            BuyAndSell.menuHandler(currentUser, sc);
                        } else {
                            System.out.println("Necessário login!");
                        }

                        break;

                    case 4:
                        // Transferências (grava e lê os registros de transferencias no banco de dados)
                        if (isAuthenticated) {
                            Transfer.menuHandler(currentUser, sc);
                        } else {
                            System.out.println("Necessário login!");
                        }

                        break;

                    case 5:
                        // Relatório (mantido)
                        if (!isAuthenticated) {
                            System.out.println("Necessário login!");
                            break;
                        }

                        List<Operation> minhasOps =
                                operationService.listByAccount(currentUser.getAccountNumber());
                        List<record.Transfer> minhasTransfs =
                                transferService.listByUser(currentUser.getAccountNumber());

                        reportService.printReport(
                                currentUser.getAccountNumber(),
                                minhasOps,
                                currentUser.getBalance(),
                                minhasTransfs
                        );
                        break;

                    default:
                        System.out.println("Opção inválida!");
                }
            }
        } finally {
            sc.close();
        }

        System.out.println("Aplicação encerrada.");
    }
}
