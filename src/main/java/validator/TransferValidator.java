package validator;

import model.*;

public class TransferValidator {
    public static void validateTransfer(Account from, Account to, String symbol, double amount) {
        if (from == null || to == null || symbol == null || amount <= 0)
            throw new ValidationException("Dados inválidos para transferência!");
        if (from.getAccountNumber().equals(to.getAccountNumber()))
            throw new ValidationException("Conta de origem e destino são iguais!");
        if (from.getAsset(symbol) < amount)
            throw new ValidationException("Saldo insuficiente no ativo para transferir!");
    }
}
