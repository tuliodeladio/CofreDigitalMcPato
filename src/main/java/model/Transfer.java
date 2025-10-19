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

    public String getFromAccount() { return fromAccount; }
    public String getToAccount() { return toAccount; }
    public String getAssetSymbol() { return assetSymbol; }
    public double getAmount() { return amount; }
    public LocalDateTime getDate() { return date; }
}
