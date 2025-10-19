package model;

import java.time.LocalDateTime;

public class Operation {
    public enum Type { BUY, SELL }
    private final String accountNumber;
    private final String assetSymbol;
    private final double amount;
    private final double price;
    private final Type type;
    private final LocalDateTime date;

    public Operation(String accountNumber, String assetSymbol, double amount, double price, Type type) {
        this.accountNumber = accountNumber;
        this.assetSymbol = assetSymbol;
        this.amount = amount;
        this.price = price;
        this.type = type;
        this.date = LocalDateTime.now();
    }

    public String getAccountNumber() { return accountNumber; }
    public String getAssetSymbol() { return assetSymbol; }
    public double getAmount() { return amount; }
    public double getPrice() { return price; }
    public Type getType() { return type; }
    public LocalDateTime getDate() { return date; }
}
