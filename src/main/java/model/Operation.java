package model;

import java.time.LocalDateTime;

public class Operation {

    public enum Type {
        BUY("B"), SELL("S");

        private final String code;

        Type(String code) { this.code = code; }
        public String getCode() { return code; }
        public static Type fromCode(String code) {
            for (Type type : Type.values()) {
                if (type.code.equals(code)) return type;
            }
            throw new IllegalArgumentException("Tipo inválido: " + code);
        }
    }

    private long id;
    private String accountNumber;
    private String symbol;
    private Type type;
    private double quantity;
    private double price;
    private LocalDateTime dateTime;

    public Operation(long id, String accountNumber, String symbol, Type type,
                     double quantity, double price, LocalDateTime dateTime) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.symbol = symbol;
        this.type = type;
        this.quantity = quantity;
        this.price = price;
        this.dateTime = dateTime;
    }

    public long getId() { return id; }
    public String getAccountNumber() { return accountNumber; }
    public String getSymbol() { return symbol; }
    public Type getType() { return type; }
    public String getTypeCode() { return type.getCode(); }
    public double getQuantity() { return quantity; }
    public double getPrice() { return price; }
    public LocalDateTime getDateTime() { return dateTime; }
}
