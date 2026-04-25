package service;

import dao.AccountAssetDAO;
import model.AccountAsset;
import record.Operation;
import record.Transfer;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReportService {

    public void printReport(String accountNumber, List<Operation> operacoes,
                            double saldo, List<Transfer> transferencias) {

        System.out.println("\n=== RELATÓRIO COFRE DIGITAL McPATO ===");
        System.out.println("Conta: " + accountNumber);
        System.out.println("Saldo disponível: R$ " + String.format("%.2f", saldo));
        System.out.println("\nCARTEIRA DE CRIPTOATIVOS:");

        // Carteira
        AccountAssetDAO accountAssetDao = new AccountAssetDAO();
        List<AccountAsset> accountAssets = accountAssetDao.listAllByAccount(accountNumber);

        if (accountAssets.isEmpty()) {
            System.out.println("  Nenhuma posição");
        } else {
            for (AccountAsset a : accountAssets) {
                System.out.println("  " + a.getAssetSymbol() + ": " + String.format("%.6f", a.getQuantity()));
            }
        }

        // Operações
        System.out.println("\nÚLTIMAS OPERAÇÕES:");
        if (operacoes.isEmpty()) {
            System.out.println("  Nenhuma operação");
        } else {
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

            for (Operation op : operacoes) {
                String typeCode = op.getTypeCode();

                System.out.printf("  %s %s %.6f %s (R$ %.2f) - %s%n",
                        typeCode,
                        op.symbol(),
                        op.quantity(),
                        typeCode.equals("B") ? "COMPRA" : "VENDA",
                        op.price(),
                        op.dateTime().format(formato)
                );
            }
        }

        // Transferências
        System.out.println("\nTRANSFERÊNCIAS:");
        if (transferencias.isEmpty()) {
            System.out.println("  Nenhuma transferência");
        } else {
            for (Transfer t : transferencias) {
                Transfer.Type transferType = t.type();

                if (transferType.equals(Transfer.Type.WITHDRAW)) {
                    System.out.println("  SAQUE: R$ " + t.amount());
                } else if (transferType.equals(Transfer.Type.DEPOSIT)) {
                    System.out.println("  DEPÓSITO: R$ " + t.amount());
                } else {
                    System.out.println("  TRANSF.: " + t.symbol() + " " +
                            String.format("%.6f", t.quantity()) +
                            " → " + t.toAccount());
                }
            }
        }

        System.out.println("=====================================");
    }
}
