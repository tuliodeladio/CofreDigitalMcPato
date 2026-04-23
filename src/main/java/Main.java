import dao.AccountAssetDAO;
import dao.AccountDAO;
import model.*;
import service.*;
import validator.*;

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
                                acc = new AccountEmpresa(contaNum, nome, email, hash, cnpj);
                            } else {
                                System.out.print("CPF: ");
                                String cpf = sc.nextLine();
                                UserValidator.validateCpf(cpf);
                                acc = new AccountPessoaFisica(contaNum, nome, email, hash, cpf);
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
                        if (!isAuthenticated) {
                            System.out.println("Necessário login!");
                            break;
                        }

                        System.out.println("Seu saldo: R$ " + String.format("%.2f", currentUser.getBalance()));
                        System.out.println("Seus ativos:");

                        accountAssets = accountAssetDao.listAllByAccount(currentUser.getAccountNumber());
                        for (AccountAsset a : accountAssets) {
                            System.out.println(a.getAssetSymbol() + ": " + String.format("%.6f", a.getQuantity()));
                        }

                        // Atualiza os preços dos ativos antes de carregá-los
                        assetService.updateAssets();
                        List<Asset> assets = assetService.getAssets();
                        System.out.println("\nAtivos disponíveis:");
                        for (Asset a : assets) {
                            System.out.println(a.getSymbol() + " - " + a.getName() + " R$ " + String.format("%.2f", a.getCurrentValue()));
                        }

                        try {
                            System.out.print("Escolha o ativo (código): ");
                            String ativo = sc.nextLine().toUpperCase();
                            Asset assetSelecionado = assets.stream().filter(a -> a.getSymbol().equals(ativo)).findFirst().orElse(null);

                            if (assetSelecionado == null) {
                                System.out.println("Ativo não encontrado!");
                                break;
                            }

                            System.out.print("Comprar ou Vender (C/V)? ");
                            String tipoOp = sc.nextLine().toUpperCase();

                            System.out.print("Quantidade: ");
                            double qtd = Double.parseDouble(sc.nextLine());

                            if ("C".equals(tipoOp)) {
                                operationService.buyAsset(currentUser, assetSelecionado, qtd);
                            } else if ("V".equals(tipoOp)) {
                                operationService.sellAsset(currentUser, assetSelecionado, qtd);
                            } else {
                                System.out.println("Tipo de operação inválido.");
                            }

                        } catch (ValidationException ve) {
                            System.out.println(ve.getMessage());
                        } catch (NumberFormatException e) {
                            System.out.println("Quantidade inválida.");
                        }
                        break;

                    case 4:
                        // Transferências (mantida, agora gravando Transfer em ArrayList)
                        if (!isAuthenticated) {
                            System.out.println("Necessário login!");
                            break;
                        }

                        System.out.println("Selecione a opção:");
                        System.out.println("1. Saque");
                        System.out.println("2. Depósito");
                        System.out.println("3. Transferência de Ativo");
                        System.out.print("Escolha: ");

                        int transfOp;
                        try {
                            transfOp = Integer.parseInt(sc.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("Opção inválida!");
                            break;
                        }

                        switch (transfOp) {
                            case 1:
                                try {
                                    System.out.print("Valor do saque (BRL): ");
                                    double valorSaque = Double.parseDouble(sc.nextLine());
                                    transferService.withdraw(currentUser, valorSaque);
                                } catch (NumberFormatException e) {
                                    System.out.println("Valor inválido.");
                                }
                                break;

                            case 2:
                                try {
                                    System.out.print("Valor do depósito (BRL): ");
                                    double valorDeposito = Double.parseDouble(sc.nextLine());
                                    transferService.deposit(currentUser, valorDeposito);
                                } catch (NumberFormatException e) {
                                    System.out.println("Valor inválido.");
                                }
                                break;

                            case 3:
                                AccountDAO accountDAO = new AccountDAO();
                                List<Account> allAccounts = accountDAO.listAll();
                                String currentUserAccountNumber = currentUser.getAccountNumber();

                                if (allAccounts.size() <= 1) {
                                    System.out.println("Não há outras contas cadastradas para transferir.");
                                    break;
                                }

                                System.out.println("Contas disponíveis para transferência:");
                                for (Account acc : allAccounts) {
                                    if (!acc.getAccountNumber().equals(currentUserAccountNumber)) {
                                        System.out.println("Nome: " + acc.getName()
                                                + " | Conta: " + acc.getAccountNumber());
                                    }
                                }

                                try {
                                    System.out.print("Digite o número da conta destino: ");
                                    String contaDest = sc.nextLine().trim();
                                    Account contaDestinoObj = allAccounts.stream().filter(a -> a.getAccountNumber().trim().equals(contaDest)).findFirst().orElse(null);

                                    if (contaDestinoObj == null ||
                                            contaDestinoObj.getAccountNumber()
                                                    .equals(currentUserAccountNumber)) {
                                        System.out.println("Conta de destino não encontrada ou inválida!");
                                        break;
                                    }

                                    System.out.println("Ativos disponíveis para transferência:");
                                    accountAssets = accountAssetDao.listAllByAccount(currentUserAccountNumber);

                                    for (AccountAsset asset : accountAssets) {
                                        System.out.println(asset.getAssetSymbol() + " - " + asset.getAssetName() + " - " + asset.getQuantity());
                                    }

                                    System.out.print("Escolha o ativo: ");
                                    String ativoTransf = sc.nextLine().toUpperCase().trim();

                                    AccountAsset ativoSelec = accountAssets.stream().filter(a -> a.getAssetSymbol().trim().equals(ativoTransf)).findFirst().orElse(null);

                                    if (ativoSelec == null) {
                                        System.out.println("Ativo não existente.");
                                        break;
                                    }

                                    System.out.print("Quantidade: ");
                                    double qtdTransf = Double.parseDouble(sc.nextLine());
                                    if (qtdTransf <= 0) {
                                        System.out.println("A quantidade deve ser positiva.");
                                        break;
                                    }

                                    TransferValidator.validateTransfer(currentUser, contaDestinoObj, ativoSelec, qtdTransf);
                                    transferService.transfer(currentUser, contaDestinoObj, ativoSelec, qtdTransf);

                                } catch (ValidationException ve) {
                                    System.out.println(ve.getMessage());
                                } catch (NumberFormatException e) {
                                    System.out.println("Quantidade inválida.");
                                }
                                break;

                            default:
                                System.out.println("Opção inválida na tela de transferências.");
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
