package service;

import model.Account;
import model.Operation;
import model.Transfer;

import java.util.List;
import java.util.Map;

public class ReportService {

    public void printReport(String accountNumber, List<Operation> operacoes,
                            double saldo, Map<String, Double> wallet,
                            List<record.Transfer> transferencias) {

        System.out.println("\n=== RELATÓRIO COFRE DIGITAL McPATO ===");
        System.out.println("Conta: " + accountNumber);
        System.out.println("Saldo disponível: R$ " + String.format("%.2f", saldo));
        System.out.println("\nCARTEIRA DE CRIPTOATIVOS:");

        // Carteira
        if (wallet.isEmpty()) {
            System.out.println("  Nenhuma posição");
        } else {
            for (Map.Entry<String, Double> entry : wallet.entrySet()) {
                if (entry.getValue() > 0) {
                    System.out.println("  " + entry.getKey() + ": " +
                            String.format("%.6f", entry.getValue()));
                }
            }
        }

        // Operações
        System.out.println("\nÚLTIMAS OPERAÇÕES:");
        if (operacoes.isEmpty()) {
            System.out.println("  Nenhuma operação");
        } else {
            for (Operation op : operacoes) {
                // ✅ CORRIGIDO: getDateTime() ao invés de getDate()
                System.out.printf("  %s %s %.6f %s (R$ %.2f) - %s%n",
                        op.getTypeCode(),
                        op.getSymbol(),
                        op.getQuantity(),
                        op.getTypeCode().equals("B") ? "COMPRA" : "VENDA",
                        op.getPrice(),
                        op.getDateTime().toString()  // ← CORRETO
                );
            }
        }

        // Transferências
        System.out.println("\nTRANSFERÊNCIAS:");
        if (transferencias.isEmpty()) {
            System.out.println("  Nenhuma transferência");
        } else {
            for (record.Transfer t : transferencias) {
                if (t.type().equals("SAQUE")) {
                    System.out.println("  SAQUE: R$ " + t.amount());
                } else if (t.type().equals("DEPOSITO")) {
                    System.out.println("  DEPÓSITO: R$ " + t.amount());
                } else {
                    System.out.println("  TRANSFER: " + t.symbol() + " " +
                            String.format("%.6f", t.quantity()) +
                            " → " + t.toAccount());
                }
            }
        }

        System.out.println("=====================================");
    }
}
