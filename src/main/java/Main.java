import Models.*;
import Services.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        AuthService authService = new AuthService();
        AssetService assetService = new AssetService();
        TwoFactorService twoFactorService = new TwoFactorService();
        OperationService operationService = new OperationService();
        TransferService transferService = new TransferService();
        ReportService reportService = new ReportService();

        Account currentUser = null;
        boolean isAuthenticated = false;

        while(true) {
            System.out.println("\n--- Cofre Digital McPato ---");
            System.out.println("1. Registrar");
            System.out.println("2. Login");
            System.out.println("3. Comprar/Vender Ativo");
            System.out.println("4. Transferência");
            System.out.println("5. Relatório");
            System.out.println("0. Sair");
            System.out.print("Escolha: ");
            int op = Integer.parseInt(sc.nextLine());

            if(op == 0) { break; }

            switch(op) {
                case 1: // Registro
                    System.out.print("Pessoa Física ou Empresa (F/E)? ");
                    String tipo = sc.nextLine().toUpperCase();
                    System.out.print("Nome: ");
                    String nome = sc.nextLine();
                    System.out.print("Email: ");
                    String email = sc.nextLine();
                    System.out.print("Senha: ");
                    String senha = sc.nextLine();
                    String hash = AuthService.hashPassword(senha);
                    Random rand = new Random();
                    String contaNum = String.format("%04d", rand.nextInt(10000));

                    if(tipo.equals("E")) {
                        System.out.print("CNPJ: ");
                        String cnpj = sc.nextLine();
                        contaNum = "E"+contaNum;
                        AccountEmpresa acc = new AccountEmpresa(contaNum, nome, email, hash, cnpj);
                        authService.register(acc);
                        System.out.println("Empresa registrada!");
                    } else {
                        System.out.print("CPF: ");
                        String cpf = sc.nextLine();
                        contaNum = "F"+contaNum;
                        AccountPessoaFisica acc = new AccountPessoaFisica(contaNum, nome, email, hash, cpf);
                        authService.register(acc);
                        System.out.println("Pessoa física registrada!");
                    }

                    break;

                case 2: // Login e 2FA
                    System.out.print("Email: ");
                    String loginEmail = sc.nextLine();
                    System.out.print("Senha: ");
                    String loginSenha = sc.nextLine();
                    Account loginAcc = authService.login(loginEmail, loginSenha);
                    if(loginAcc != null) {
                        twoFactorService.generateAndSendCode(loginEmail);
                        System.out.print("Digite o código 2FA enviado: ");
                        String inputCode = sc.nextLine();
                        if(twoFactorService.validateCode(inputCode)) {
                            currentUser = loginAcc;
                            isAuthenticated = true;
                            System.out.println("Login realizado com sucesso!");
                        } else {
                            System.out.println("2FA incorreto!");
                        }
                    } else {
                        System.out.println("Credenciais inválidas!");
                    }
                    break;

                case 3: // Compra/Venda
                    if(!isAuthenticated) {
                        System.out.println("Necessário login!");
                        break;
                    }
                    System.out.println("Seus saldo: " + String.format("%.2f", currentUser.getBalance()));
                    System.out.println("Seus ativos:");
                    Map<String, Asset> assets = assetService.getAssets();
                    for (String sym : assets.keySet()) {
                        System.out.println(sym + ": " + String.format("%.2f", currentUser.getAsset(sym)));
                    }
                    System.out.println("Ativos disponíveis:");
                    for(String sym : assets.keySet()) {
                        Asset a = assets.get(sym);
                        assetService.updateAssetValue(a);
                        System.out.println(sym + " - " + a.getName() + " (R$ " + String.format("%.2f", a.getCurrentValue()) + ")");
                    }
                    System.out.print("Escolha o ativo: ");
                    String ativo = sc.nextLine().toUpperCase();
                    Asset assetSelecionado = assets.get(ativo);
                    if(assetSelecionado == null) {
                        System.out.println("Ativo não encontrado!");
                        break;
                    }
                    System.out.print("Comprar ou Vender (C/V)? ");
                    String tipoOp = sc.nextLine().toUpperCase();
                    System.out.print("Quantidade: ");
                    double qtd = Double.parseDouble(sc.nextLine());
                    boolean resultado = false;

                    if(tipoOp.equals("C")) {
                        resultado = operationService.buyAsset(currentUser, assetSelecionado, qtd);
                        System.out.println(resultado ? "Compra realizada!" : "Saldo insuficiente!");
                    } else if(tipoOp.equals("V")) {
                        resultado = operationService.sellAsset(currentUser, assetSelecionado, qtd);
                        System.out.println(resultado ? "Venda realizada! Valor creditado na conta." : "Você não possui quantidade suficiente do ativo!");
                    }
                    break;


                case 4: // Transferências
                    if(!isAuthenticated) {
                        System.out.println("Necessário login!");
                        break;
                    }
                    System.out.println("Selecione a opção:");
                    System.out.println("1. Saque");
                    System.out.println("2. Depósito");
                    System.out.println("3. Transferência de Ativo");
                    System.out.print("Escolha: ");
                    int transfOp = Integer.parseInt(sc.nextLine());

                    Map<String, Asset> transferAssets = assetService.getAssets();

                    switch(transfOp) {
                        case 1: // Saque
                            System.out.print("Valor do saque (BRL): ");
                            double valorSaque = Double.parseDouble(sc.nextLine());
                            transferService.withdraw(currentUser, valorSaque);
                            break;
                        case 2: // Depósito
                            System.out.print("Valor do depósito (BRL): ");
                            double valorDeposito = Double.parseDouble(sc.nextLine());
                            transferService.deposit(currentUser, valorDeposito);
                            break;

                        case 3: // Transferência de Ativo
                            // Mostra contas disponíveis para escolha:
                            Map<String,Account> allAccounts = authService.getAccounts();
                            if (allAccounts.size() <= 1) {
                                System.out.println("Não há outras contas cadastradas para transferir.");
                                break;
                            }

                            System.out.println("Contas disponíveis para transferência:");
                            for (Account acc : allAccounts.values()) {
                                if (!acc.getAccountNumber().equals(currentUser.getAccountNumber())) {
                                    System.out.println("Nome: " + acc.getName() + " | Conta: " + acc.getAccountNumber());
                                }
                            }

                            System.out.print("Digite o número da conta destino: ");
                            String contaDest = sc.nextLine().trim();

                            Account contaDestinoObj = allAccounts.get(contaDest);
                            if (contaDestinoObj == null || contaDestinoObj.getAccountNumber().equals(currentUser.getAccountNumber())) {
                                System.out.println("Conta de destino não encontrada ou inválida!");
                                break;
                            }

                            System.out.println("Ativos disponíveis para transferência:");
                            for (String sym : transferAssets.keySet()) {
                                System.out.println(sym + " - " + transferAssets.get(sym).getName());
                            }
                            System.out.print("Escolha o ativo: ");
                            String ativoTransf = sc.nextLine().toUpperCase();
                            if (!transferAssets.containsKey(ativoTransf)) {
                                System.out.println("Ativo não existente.");
                                break;
                            }

                            System.out.print("Quantidade: ");
                            double qtdTransf;
                            try {
                                qtdTransf = Double.parseDouble(sc.nextLine());
                                if (qtdTransf <= 0) {
                                    System.out.println("A quantidade deve ser positiva.");
                                    break;
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Quantidade inválida.");
                                break;
                            }

                            if(transferService.transfer(currentUser, contaDestinoObj, ativoTransf, qtdTransf)) {
                                System.out.println("Solicitação de transferência efetuada.");
                            }
                            break;
                        default:
                            System.out.println("Opção inválida na tela de transferências.");
                    }
                    break;

                case 5: // Relatório
                    if(!isAuthenticated) {
                        System.out.println("Necessário login!");
                        break;
                    }
                    List<Operation> minhasOps = operationService.listByAccount(currentUser.getAccountNumber());
                    List<Transfer> minhasTransfs = transferService.listByUser(currentUser.getAccountNumber());
                    reportService.printReport(currentUser.getAccountNumber(), minhasOps, currentUser.getBalance(), currentUser.getWallet(),minhasTransfs);

                    break;

                default:
                    System.out.println("Opção inválida!");
            }
        }
        sc.close();
        System.out.println("Aplicação encerrada.");
    }
}
