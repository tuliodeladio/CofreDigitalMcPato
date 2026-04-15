package service;

import dao.AccountAssetDAO;
import dao.OperationDAO;
import model.Account;
import model.AccountAsset;
import model.Asset;
import model.Operation;
import validator.AssetOperationValidator;

import java.time.LocalDateTime;
import java.util.*;

public class OperationService {

    public void buyAsset(Account account, Asset asset, double quantity) {
        AssetOperationValidator.validateBuy(account, asset, quantity);

        // Calcula custo total
        double price = asset.getCurrentValue();
        double totalCost = price * quantity;
        String sym = asset.getSymbol();
        String accountNumber = account.getAccountNumber();

        // Deduz da carteira
        if (!account.withdraw(totalCost)) {
            System.out.println("Saldo insuficiente!");
        }

        AccountAssetDAO dao = new AccountAssetDAO();
        AccountAsset accountAsset = dao.findAccountAsset(accountNumber, sym);
        double qtdAtual = accountAsset != null ? accountAsset.getQuantity() : account.getAsset(sym);

        // Upsert em Account Asset
        accountAssetUpsert(accountNumber, sym, qtdAtual + quantity);

        // Atualiza carteira
        account.addAsset(sym, quantity);

        // Registra a operação no banco de dados
        adicionarOperacao(accountNumber, sym, quantity, price, Operation.Type.BUY);

        System.out.println("Compra realizada!");
    }

    public void sellAsset(Account account, Asset asset, double quantity) {
        AssetOperationValidator.validateSell(account, asset, quantity);

        double price = asset.getCurrentValue();
        String sym = asset.getSymbol();
        String accountNumber = account.getAccountNumber();

        AccountAssetDAO dao = new AccountAssetDAO();
        AccountAsset accountAsset = dao.findAccountAsset(accountNumber, sym);
        double qtdAtual = accountAsset != null ? accountAsset.getQuantity() : account.getAsset(sym);

        if (qtdAtual < quantity) {
            System.out.println("Você não possui quantidade suficiente do ativo!");
            return;
        }

        // Upsert em Account Asset
        accountAssetUpsert(accountNumber, sym, qtdAtual - quantity);

        // Atualiza carteira
        double totalValue = price * quantity;
        account.deposit(totalValue);

        // Registra a operação no banco de dados
        adicionarOperacao(accountNumber, sym, quantity, price, Operation.Type.SELL);

        System.out.println("Venda realizada! Valor creditado na conta.");
    }

    public List<Operation> listByAccount(String accountNumber) {
        // Lista todas as operações
        OperationDAO dao = new OperationDAO();
        return dao.listByAccount(accountNumber);
    }

    private void adicionarOperacao(String accountNumber, String sym, double quantity, double price, Operation.Type opType) {
        Operation operacao = new Operation(
            System.currentTimeMillis(),
            accountNumber,
            sym,
            opType,
            quantity,
            price,
            LocalDateTime.now()
        );

        OperationDAO dao = new OperationDAO();
        dao.insert(operacao);
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
