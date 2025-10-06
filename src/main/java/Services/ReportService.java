package Services;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import Models.Operation;
import Models.Transfer;

public class ReportService {
    public void printReport(String accountNumber, List<Operation> operations, double saldoFinal, Map<String, Double> wallet, List<Transfer> transfers) {
        Locale locale = Locale.US; // Garante . como separador decimal

        System.out.println("Relatório para conta: " + accountNumber);
        System.out.println("---- Operações de Compra/Venda ----");
        for (Operation op : operations) {
            System.out.println(op.getDate() + " " + op.getType() + " " + op.getAssetSymbol() +
                    " Qtd: " + String.format(locale, "%.2f", op.getAmount()) +
                    " Preço: " + String.format(locale, "%.2f", op.getPrice()));
        }
        System.out.println("---- Transferências, Depósitos e Saques ----");
        for (Transfer transfer : transfers) {
            if (transfer.getFromAccount().equals("BANK")) {
                System.out.println("DEPÓSITO  " + transfer.getDate() +
                        " + " + String.format(locale, "%.2f", transfer.getAmount()) + " BRL");
            } else if (transfer.getToAccount().equals("CASH")) {
                System.out.println("SAQUE     " + transfer.getDate() +
                        " - " + String.format(locale, "%.2f", transfer.getAmount()) + " BRL");
            } else if (transfer.getFromAccount().equals(accountNumber)) {
                System.out.println("ENVIADA   " + transfer.getDate() +
                        " Para: " + transfer.getToAccount() + " " + transfer.getAssetSymbol() +
                        " Qtd: " + String.format(locale, "%.2f", transfer.getAmount()));
            } else if (transfer.getToAccount().equals(accountNumber)) {
                System.out.println("RECEBIDA  " + transfer.getDate() +
                        " De: " + transfer.getFromAccount() + " " + transfer.getAssetSymbol() +
                        " Qtd: " + String.format(locale, "%.2f", transfer.getAmount()));
            }
        }
        System.out.println("----- Saldo de ativos -----");
        for (String ativo : wallet.keySet()) {
            System.out.println(ativo + ": " + String.format(locale, "%.2f", wallet.get(ativo)));
        }
        System.out.println("Saldo em conta (BRL): " + String.format(locale, "%.2f", saldoFinal));
    }
}
