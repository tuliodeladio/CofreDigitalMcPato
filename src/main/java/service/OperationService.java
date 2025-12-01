package service;

import model.Account;
import model.Asset;
import model.Operation;

import java.time.LocalDateTime;
import java.util.*;

public class OperationService {

    private static Map<String, List<Operation>> operacoesPorConta = new HashMap<>();

    public boolean buyAsset(Account account, Asset asset, double quantity) {
        double totalCost = asset.getCurrentValue() * quantity;
        if (!account.withdraw(totalCost)) {
            return false;
        }

        account.addAsset(asset.getSymbol(), quantity);

        Operation operacao = new Operation(
                System.currentTimeMillis(),
                account.getAccountNumber(),
                asset.getSymbol(),
                Operation.Type.BUY,
                quantity,
                asset.getCurrentValue(),
                LocalDateTime.now()
        );

        adicionarOperacao(account.getAccountNumber(), operacao);
        return true;
    }

    public boolean sellAsset(Account account, Asset asset, double quantity) {
        double qtdAtual = account.getAsset(asset.getSymbol());
        if (qtdAtual < quantity) {
            return false;
        }

        double totalValue = asset.getCurrentValue() * quantity;
        account.removeAsset(asset.getSymbol(), quantity);
        account.deposit(totalValue);

        Operation operacao = new Operation(
                System.currentTimeMillis(),
                account.getAccountNumber(),
                asset.getSymbol(),
                Operation.Type.SELL,
                quantity,
                asset.getCurrentValue(),
                LocalDateTime.now()
        );

        adicionarOperacao(account.getAccountNumber(), operacao);
        return true;
    }

    public List<Operation> listByAccount(String accountNumber) {
        return operacoesPorConta.getOrDefault(accountNumber, new ArrayList<>());
    }

    private void adicionarOperacao(String accountNumber, Operation operacao) {
        operacoesPorConta
                .computeIfAbsent(accountNumber, k -> new ArrayList<>())
                .add(operacao);
    }
}
