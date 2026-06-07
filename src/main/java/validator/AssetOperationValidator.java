package validator;

import model.Account;
import record.Asset;

public class AssetOperationValidator {

    public static void validateBuy(Account acc, Asset asset, double amount) {
        if (acc == null || asset == null || amount <= 0) {
            throw new ValidationException("Dados inválidos para compra!");
        }
        if (acc.getBalance() < asset.currentValue() * amount) {
            throw new ValidationException("Saldo insuficiente para comprar!");
        }
    }

    public static void validateSell(Account acc, Asset asset, double amount) {
        if (acc == null || asset == null || amount <= 0) {
            throw new ValidationException("Dados inválidos para venda!");
        }
        if (acc.getAssetQuantity(asset.symbol()) < amount) {
            throw new ValidationException("Quantidade insuficiente do ativo para venda!");
        }
    }
}