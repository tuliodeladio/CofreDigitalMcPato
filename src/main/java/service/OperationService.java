package service;

import dao.AccountAssetDAO;
import model.Account;
import model.AccountAsset;
import model.Asset;
import model.Operation;

import java.time.LocalDateTime;
import java.util.*;

public class OperationService {

    private static Map<String, List<Operation>> operacoesPorConta = new HashMap<>();

    public boolean buyAsset(Account account, Asset asset, double quantity) {
        // Calcula custo total
        // Deduz da carteira
        double totalCost = asset.getCurrentValue() * quantity;
        if (!account.withdraw(totalCost)) {
            return false;
        }

        // Upsert em Account Asset
        accountAssetUpsert(account.getAccountNumber(), asset.getSymbol(), quantity);

        // Atualiza carteira
        account.addAsset(asset.getSymbol(), quantity);

        // Adiciona operação
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

        // Upsert em Account Asset
        accountAssetUpsert(account.getAccountNumber(), asset.getSymbol(), quantity);

        // Atualiza carteira
        double totalValue = asset.getCurrentValue() * quantity;
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
        // Lista todos os operations
        return operacoesPorConta.getOrDefault(accountNumber, new ArrayList<>());
    }

    private void adicionarOperacao(String accountNumber, Operation operacao) {
        operacoesPorConta
                .computeIfAbsent(accountNumber, k -> new ArrayList<>())
                .add(operacao);
    }

    private void accountAssetUpsert(String accountNumber, String assetSymbol, double quantity) {
        AccountAssetDAO accountAssetDAO = new AccountAssetDAO();
        AccountAsset accountAsset = accountAssetDAO.findAccountAsset(accountNumber, assetSymbol);

        if (accountAsset != null) {
            if (quantity == 0) {
                accountAssetDAO.delete(accountAsset);
            } else {
                accountAsset.setQuantity(quantity);
                accountAssetDAO.update(accountAsset);
            }
        } else {
            accountAssetDAO.insert(new AccountAsset(accountNumber, assetSymbol, quantity));
        }

    }
}
