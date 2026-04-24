package view.menu;

import dao.AccountAssetDAO;
import dao.AccountDAO;
import model.Account;
import model.AccountAsset;
import service.TransferService;
import validator.TransferValidator;
import validator.ValidationException;

import java.util.List;
import java.util.Scanner;

public class Transfer {
    public static void menuOption(Account currentUser) {
        Scanner sc = new Scanner(System.in);
        TransferService transferService = new TransferService();
        AccountAssetDAO accountAssetDao = new AccountAssetDAO();

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
            return;
        }

        switch (transfOp) {
            case 1:
                handleWithdraw(transferService, currentUser, sc);
                break;

            case 2:
                handleDeposit(currentUser, sc, transferService);
                break;

            case 3:
                handleAssetTransfer(currentUser, sc, accountAssetDao, transferService);
                break;

            default:
                System.out.println("Opção inválida na tela de transferências.");
        }
    }

    private static void handleAssetTransfer(Account currentUser, Scanner sc, AccountAssetDAO accountAssetDao, TransferService transferService) {
        AccountDAO accountDAO = new AccountDAO();
        List<Account> allAccounts = accountDAO.listAll();
        String currentUserAccountNumber = currentUser.getAccountNumber();

        if (allAccounts.size() <= 1) {
            System.out.println("Não há outras contas cadastradas para transferir.");
            return;
        }

        // Lista contas disponíveis
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
                return;
            }

            // Lista ativos disponíveis
            System.out.println("Ativos disponíveis para transferência:");
            List<AccountAsset> accountAssets = accountAssetDao.listAllByAccount(currentUserAccountNumber);

            for (AccountAsset asset : accountAssets) {
                System.out.println(asset.getAssetSymbol() + " - " + asset.getAssetName() + " - " + asset.getQuantity());
            }

            System.out.print("Escolha o ativo: ");
            String ativoTransf = sc.nextLine().toUpperCase().trim();

            AccountAsset ativoSelec = accountAssets.stream().filter(a -> a.getAssetSymbol().trim().equals(ativoTransf)).findFirst().orElse(null);

            if (ativoSelec == null) {
                System.out.println("Ativo não existente.");
                return;
            }

            System.out.print("Quantidade: ");
            double qtdTransf = Double.parseDouble(sc.nextLine());
            if (qtdTransf <= 0) {
                System.out.println("A quantidade deve ser positiva.");
                return;
            }

            TransferValidator.validateTransfer(currentUser, contaDestinoObj, ativoSelec, qtdTransf);
            transferService.transfer(currentUser, contaDestinoObj, ativoSelec, qtdTransf);

        } catch (ValidationException ve) {
            System.out.println(ve.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Quantidade inválida.");
        }
    }

    private static void handleDeposit(Account currentUser, Scanner sc, TransferService transferService) {
        try {
            System.out.print("Valor do depósito (BRL): ");
            double valorDeposito = Double.parseDouble(sc.nextLine());
            transferService.deposit(currentUser, valorDeposito);
            System.out.println("Saldo: " + currentUser.getBalance());
        } catch (NumberFormatException e) {
            System.out.println("Valor inválido.");
        }
    }

    public static void handleWithdraw(TransferService transferService, Account currentUser, Scanner sc) {
        try {
            System.out.print("Valor do saque (BRL): ");
            double valorSaque = Double.parseDouble(sc.nextLine());
            transferService.withdraw(currentUser, valorSaque);
            System.out.println("Saldo: " + currentUser.getBalance());
        } catch (NumberFormatException e) {
            System.out.println("Valor inválido.");
        }
    }
}
