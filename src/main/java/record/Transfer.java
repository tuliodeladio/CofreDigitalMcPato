package record;

import java.time.LocalDateTime;

public record Transfer(long id,
                       String fromAccount,
                       String toAccount,
                       String symbol,
                       double  quantity,
                       double amount,
                       String type,
                       LocalDateTime dateTime) {
}
