package service;

import java.util.ArrayList;
import java.util.List;
import model.Account;
import model.Asset;
import model.Operation;

public class OperationService {
    private final List<Operation> operations = new ArrayList<>();

    // Mtodo para comprar ativo
    public boolean buyAsset(Account account, Asset asset, double qtd) {
        double valorTotal = qtd * asset.getCurrentValue();
        if(account.withdraw(valorTotal)) {
            account.addAsset(asset.getSymbol(), qtd);
            Operation opCompra = new Operation(account.getAccountNumber(), asset.getSymbol(), qtd, asset.getCurrentValue(), Operation.Type.BUY);
            operations.add(opCompra);
            return true;
        }
        return false;
    }

    // Mtodo para vender ativo
    public boolean sellAsset(Account account, Asset asset, double qtd) {
        if(account.getAsset(asset.getSymbol()) >= qtd) {
            account.removeAsset(asset.getSymbol(), qtd);
            double valorTotal = qtd * asset.getCurrentValue();
            account.deposit(valorTotal);
            Operation opVenda = new Operation(account.getAccountNumber(), asset.getSymbol(), qtd, asset.getCurrentValue(), Operation.Type.SELL);
            operations.add(opVenda);
            return true;
        }
        return false;
    }

    public List<Operation> listByAccount(String accountNumber) {
        List<Operation> result = new ArrayList<>();
        for (Operation o : operations) {
            if (o.getAccountNumber().equals(accountNumber)) {
                result.add(o);
            }
        }
        return result;
    }
}
