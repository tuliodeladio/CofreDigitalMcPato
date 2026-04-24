package record;

import java.time.LocalDateTime;

public record Operation(
    long id,
    String accountNumber,
    String symbol,
    Type type,
    double quantity,
    double price,
    LocalDateTime dateTime) {

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

    public String getTypeCode() { return type.getCode(); }
}
