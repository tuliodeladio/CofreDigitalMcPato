package model;

import dao.AccountAssetDAO;

import java.util.HashMap;
import java.util.Map;

public abstract class Account {
    protected String accountNumber;
    protected String documentNumber;
    protected String accountType;
    protected String name;
    protected String email;
    protected String passwordHash;
    protected double balance;
    protected Map<String, Double> wallet;

    public Account(String accountNumber, String name, String email, String passwordHash, String documentNumber, String accountType) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.balance = 0.0;
        this.wallet = new HashMap<>();
        this.documentNumber = documentNumber;
        this.accountType = accountType;
    }

    public String getAccountNumber() { return accountNumber; }
    public String getDocumentNumber() { return documentNumber; }
    public String getAccountType() { return accountType; }
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

    public Map<String, Double> getWallet() { return wallet; }
    public double getAssetQuantity(String symbol) {
        AccountAssetDAO dao = new AccountAssetDAO();
        AccountAsset asset = dao.findAccountAsset(this.accountNumber, symbol);

        return asset != null ? asset.getQuantity() : 0.0;
    }

    public void addAsset(AccountAsset asset, double quantity) {
        AccountAssetDAO dao = new AccountAssetDAO();

        double qtdAtual = asset.getQuantity();
        asset.setQuantity(qtdAtual + quantity);

        dao.update(asset);
    }

    public void removeAsset(AccountAsset asset, double quantity) {
        double qtdAtual = asset.getQuantity();

        if (qtdAtual >= quantity) {
            AccountAssetDAO dao = new AccountAssetDAO();

            asset.setQuantity(qtdAtual - quantity);
            dao.update(asset);
        }
    }
}
