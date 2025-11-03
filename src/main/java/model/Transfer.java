package model;

import java.time.LocalDateTime;

public class Transfer {
    private final String fromAccount;
    private final String toAccount;
    private final String assetSymbol;
    private final double amount;
    private final LocalDateTime date;

    public Transfer(String fromAccount, String toAccount, String assetSymbol, double amount) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.assetSymbol = assetSymbol;
        this.amount = amount;
        this.date = LocalDateTime.now();
    }

    public Transfer(String fromAccount, double amount) {
        this.fromAccount = fromAccount;
        this.toAccount = "External Account";
        this.assetSymbol = "BRL";
        this.amount = amount;
        this.date = LocalDateTime.now();
    }

    public Transfer(String toAccount, double amount, String Metodo) {
        this.fromAccount = "External Account with " + Metodo;
        this.toAccount = toAccount;
        this.assetSymbol = "BRL";
        this.amount = amount;
        this.date = LocalDateTime.now();
    }

    public String getFromAccount() { return fromAccount; }
    public String getToAccount() { return toAccount; }
    public String getAssetSymbol() { return assetSymbol; }
    public double getAmount() { return amount; }
    public LocalDateTime getDate() { return date; }
}
