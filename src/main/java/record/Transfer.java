package record;

import java.time.LocalDateTime;

public record Transfer(long id,
                       String fromAccount,
                       String toAccount,
                       String symbol,
                       Double  quantity,
                       Double amount,
                       Type type,
                       LocalDateTime dateTime) {

    public enum Type {
        DEPOSIT("DEPOSITO"), WITHDRAW("SAQUE"), ASSET_TRANSFER("TRANSFER_ATIVO");

        private final String code;

        Type(String code) { this.code = code; }

        public String getCode() { return code; }

        public static Transfer.Type fromCode(String code) {
            for (Transfer.Type type : Transfer.Type.values()) {
                if (type.code.equals(code)) return type;
            }
            throw new IllegalArgumentException("Tipo inválido: " + code);
        }
    }

    public String getTypeCode() { return type.getCode(); }
}
