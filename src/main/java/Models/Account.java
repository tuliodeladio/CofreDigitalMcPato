package Models;

import java.util.HashMap;
import java.util.Map;

public abstract class Account {
    protected String accountNumber;
    protected String name;
    protected String email;
    protected String passwordHash;
    protected double balance;
    protected Map<String, Double> wallet;

    public Account(String accountNumber, String name, String email, String passwordHash) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.balance = 0.0;
        this.wallet = new HashMap<>();
    }

    public String getAccountNumber() { return accountNumber; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public double getBalance() { return balance; }

    public void deposit(double amount) { balance += amount; }
    public boolean withdraw(double amount) {
        if (amount > balance) return false;
        balance -= amount;
        return true;
    }

    public Map<String, Double> getWallet() {
        return wallet;
    }

    public double getAsset(String symbol) {
        return wallet.getOrDefault(symbol, 0.0);
    }

    public void addAsset(String symbol, double quantity) {
        wallet.put(symbol, wallet.getOrDefault(symbol, 0.0) + quantity);
    }

    public void removeAsset(String symbol, double quantity) {
        double qtdAtual = wallet.getOrDefault(symbol, 0.0);
        if (qtdAtual >= quantity) {
            wallet.put(symbol, qtdAtual - quantity);
        }
    }

}
