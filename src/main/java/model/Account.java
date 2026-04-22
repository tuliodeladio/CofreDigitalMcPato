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

    public void addAsset(String assetSymbol, double quantity) {
        AccountAssetDAO dao = new AccountAssetDAO();
        AccountAsset accountAsset = dao.findAccountAsset(this.accountNumber, assetSymbol);

        if (accountAsset == null) {
            accountAsset = new AccountAsset(this.accountNumber, assetSymbol, quantity);
            dao.insert(accountAsset);
        } else {
            accountAsset.setQuantity(accountAsset.getQuantity() + quantity);
            dao.update(accountAsset);
        }
    }

    public void removeAsset(String assetSymbol, double quantity) {
        AccountAssetDAO dao = new AccountAssetDAO();
        AccountAsset accountAsset = dao.findAccountAsset(this.accountNumber, assetSymbol);

        double qtdAtual = accountAsset == null ? 0.0 : accountAsset.getQuantity();

        if ((accountAsset != null) && (qtdAtual >= quantity) && (quantity > 0)) {
            accountAsset.setQuantity(qtdAtual - quantity);

            if (accountAsset.getQuantity() == 0) {
                dao.delete(accountAsset);
            } else {
                dao.update(accountAsset);
            }
        } else {
            System.out.println("A conta não possui ativos suficientes para esta transferência!");
        }
    }
}
