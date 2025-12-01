package model;

import java.time.LocalDateTime;

public class Transfer {
    private long id;
    private String fromAccount;
    private String toAccount;
    private String symbol;
    private Double quantity;
    private Double amount;
    private String type;
    private LocalDateTime dateTime;

    public Transfer(long id, String fromAccount, String toAccount, String symbol,
                    Double quantity, Double amount, String type, LocalDateTime dateTime) {
        this.id = id;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.symbol = symbol;
        this.quantity = quantity;
        this.amount = amount;
        this.type = type;
        this.dateTime = dateTime;
    }

    public long getId() { return id; }
    public String getFromAccount() { return fromAccount; }
    public String getToAccount() { return toAccount; }
    public String getSymbol() { return symbol; }
    public Double getQuantity() { return quantity; }
    public Double getAmount() { return amount; }
    public String getType() { return type; }
    public LocalDateTime getDateTime() { return dateTime; }
}
