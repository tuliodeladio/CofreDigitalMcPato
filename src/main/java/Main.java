package app;

import dao.AccountAssetDAO;
import model.*;
import service.*;
import validator.*;

import java.util.*;
import java.io.*;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

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
        FileExportService fileExportService = new FileExportService();

        // Requisito: ArrayList com pelo menos 2 classes
        List<Operation> operacoes = new ArrayList<>();
        List<Transfer> transferencias = new ArrayList<>();

        // Requisito: HashMap com pelo menos 2 classes
        Map<String, Account> accountMap = new HashMap<>();
        Map<String, Asset> assetMap = new HashMap<>();

        Account currentUser = null;
        boolean isAuthenticated = false;
        long nextOperationId = 1L;
        long nextTransferId = 1L;

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

                        assetService.updateAssets();
                        List<Asset> assets = assetService.getAssets();

                        System.out.println("Seu saldo: R$ " + String.format("%.2f", currentUser.getBalance()));
                        System.out.println("Seus ativos:");

                        for (Asset a : assets) {
                            String sym = a.getSymbol();
                            double qtd = currentUser.getAsset(sym);

                            if (qtd > 0) {
                                System.out.println(sym + ": " + String.format("%.6f", qtd));
                            }
                        }

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
                                    Transfer t = transferService.withdraw(currentUser, valorSaque);
                                    if (t != null) {
                                        transferencias.add(t);
                                    }
                                } catch (NumberFormatException e) {
                                    System.out.println("Valor inválido.");
                                }
                                break;

                            case 2:
                                try {
                                    System.out.print("Valor do depósito (BRL): ");
                                    double valorDeposito = Double.parseDouble(sc.nextLine());
                                    Transfer t = transferService.deposit(currentUser, valorDeposito);
                                    if (t != null) {
                                        transferencias.add(t);
                                    }
                                } catch (NumberFormatException e) {
                                    System.out.println("Valor inválido.");
                                }
                                break;

                            case 3:
                                Map<String, Account> allAccounts = accountMap;
                                if (allAccounts.size() <= 1) {
                                    System.out.println("Não há outras contas cadastradas para transferir.");
                                    break;
                                }

                                System.out.println("Contas disponíveis para transferência:");
                                for (Account acc : allAccounts.values()) {
                                    if (!acc.getAccountNumber().equals(currentUser.getAccountNumber())) {
                                        System.out.println("Nome: " + acc.getName()
                                                + " | Conta: " + acc.getAccountNumber());
                                    }
                                }

                                try {
                                    System.out.print("Digite o número da conta destino: ");
                                    String contaDest = sc.nextLine().trim();
                                    Account contaDestinoObj = allAccounts.get(contaDest);

                                    if (contaDestinoObj == null ||
                                            contaDestinoObj.getAccountNumber()
                                                    .equals(currentUser.getAccountNumber())) {
                                        System.out.println("Conta de destino não encontrada ou inválida!");
                                        break;
                                    }

                                    System.out.println("Ativos disponíveis para transferência:");
                                    for (String sym : assetMap.keySet()) {
                                        System.out.println(sym + " - " + assetMap.get(sym).getName());
                                    }

                                    System.out.print("Escolha o ativo: ");
                                    String ativoTransf = sc.nextLine().toUpperCase();

                                    if (!assetMap.containsKey(ativoTransf)) {
                                        System.out.println("Ativo não existente.");
                                        break;
                                    }

                                    System.out.print("Quantidade: ");
                                    double qtdTransf = Double.parseDouble(sc.nextLine());
                                    if (qtdTransf <= 0) {
                                        System.out.println("A quantidade deve ser positiva.");
                                        break;
                                    }

                                    TransferValidator.validateTransfer(currentUser, contaDestinoObj,
                                            ativoTransf, qtdTransf);

                                    Transfer t = transferService.transfer(currentUser, contaDestinoObj,
                                            ativoTransf, qtdTransf);
                                    if (t != null) {
                                        transferencias.add(t);
                                        System.out.println("Solicitação de transferência efetuada.");
                                    }

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
                        List<Transfer> minhasTransfs =
                                transferService.listByUser(currentUser.getAccountNumber());

                        reportService.printReport(
                                currentUser.getAccountNumber(),
                                minhasOps,
                                currentUser.getBalance(),
                                currentUser.getWallet(),
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
