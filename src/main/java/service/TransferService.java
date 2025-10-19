package service;

import java.util.ArrayList;
import java.util.List;
import model.Account;
import model.Transfer;

public class TransferService {
    private List<Transfer> allTransfers = new ArrayList<>();

    // Transferência entre contas
    public boolean transfer(Account from, Account to, String assetSymbol, double quantity) {
        if (from.getAsset(assetSymbol) < quantity) {
            System.out.println("Saldo insuficiente de " + assetSymbol + " para transferência.");
            return false;
        }
        from.removeAsset(assetSymbol, quantity);
        to.addAsset(assetSymbol, quantity);
        Transfer transfer = new Transfer(from.getAccountNumber(), to.getAccountNumber(), assetSymbol, quantity);
        allTransfers.add(transfer);
        System.out.println("Transferência realizada de " + quantity + " " + assetSymbol + " de " + from.getName() + " para " + to.getName() + "!");
        return true;
    }

    // Depósito em BRL
    public boolean deposit(Account to, double amount) {
        if (amount <= 0) {
            System.out.println("Valor inválido para depósito.");
            return false;
        }
        to.deposit(amount);
        Transfer deposit = new Transfer("BANK", to.getAccountNumber(), "BRL", amount);
        allTransfers.add(deposit);
        System.out.println("Depósito realizado.");
        return true;
    }

    // Saque em BRL
    public boolean withdraw(Account from, double amount) {
        if (amount <= 0) {
            System.out.println("Valor inválido para saque.");
            return false;
        }
        if (from.withdraw(amount)) {
            Transfer saque = new Transfer(from.getAccountNumber(), "CASH", "BRL", amount);
            allTransfers.add(saque);
            System.out.println("Saque realizado.");
            return true;
        } else {
            System.out.println("Saldo insuficiente para saque.");
            return false;
        }
    }

    public List<Transfer> listByUser(String accountNumber) {
        List<Transfer> result = new ArrayList<>();
        for (Transfer t : allTransfers) {
            if (t.getFromAccount().equals(accountNumber) || t.getToAccount().equals(accountNumber)) {
                result.add(t);
            }
        }
        return result;
    }
}
