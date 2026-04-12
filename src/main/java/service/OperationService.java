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
        String sym = asset.getSymbol();
        String accountNumber = account.getAccountNumber();

        if (!account.withdraw(totalCost)) {
            return false;
        }

        AccountAssetDAO dao = new AccountAssetDAO();
        AccountAsset accountAsset = dao.findAccountAsset(accountNumber, sym);
        double qtdAtual = accountAsset != null ? accountAsset.getQuantity() : account.getAsset(sym);

        // Upsert em Account Asset
        accountAssetUpsert(accountNumber, sym, qtdAtual + quantity);

        // Atualiza carteira
        account.addAsset(sym, quantity);

        // Adiciona operação
        Operation operacao = new Operation(
                System.currentTimeMillis(),
                accountNumber,
                sym,
                Operation.Type.BUY,
                quantity,
                asset.getCurrentValue(),
                LocalDateTime.now()
        );

        adicionarOperacao(accountNumber, operacao);
        return true;
    }

    public boolean sellAsset(Account account, Asset asset, double quantity) {
        String sym = asset.getSymbol();
        String accountNumber = account.getAccountNumber();

        AccountAssetDAO dao = new AccountAssetDAO();
        AccountAsset accountAsset = dao.findAccountAsset(accountNumber, sym);
        double qtdAtual = accountAsset != null ? accountAsset.getQuantity() : account.getAsset(sym);

        if (qtdAtual < quantity) {
            return false;
        }

        // Upsert em Account Asset
        accountAssetUpsert(accountNumber, sym, qtdAtual - quantity);

        // Atualiza carteira
        double totalValue = asset.getCurrentValue() * quantity;
        account.deposit(totalValue);

        Operation operacao = new Operation(
                System.currentTimeMillis(),
                accountNumber,
                sym,
                Operation.Type.SELL,
                quantity,
                asset.getCurrentValue(),
                LocalDateTime.now()
        );

        adicionarOperacao(accountNumber, operacao);
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
