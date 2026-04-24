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

        // Upsert em Account Asset
        account.addAsset(sym, quantity);

        // Registra a operação no banco de dados
        adicionarOperacao(accountNumber, sym, quantity, totalCost, Operation.Type.BUY);

        System.out.println("Compra realizada!");
    }

    public void sellAsset(Account account, Asset asset, double quantity) {
        AssetOperationValidator.validateSell(account, asset, quantity);

        double price = asset.getCurrentValue();
        String sym = asset.getSymbol();
        String accountNumber = account.getAccountNumber();

        AccountAssetDAO dao = new AccountAssetDAO();
        AccountAsset accountAsset = dao.findAccountAsset(accountNumber, sym);

        double qtdAtual = accountAsset == null ? 0.0 : accountAsset.getQuantity();

        if ((qtdAtual == 0.0) || (qtdAtual < quantity)) {
            System.out.println("Você não possui quantidade suficiente do ativo!");
            return;
        }

        // Atualiza (ou remove) Account Asset
        account.removeAsset(sym, quantity);

        // Atualiza carteira
        double totalValue = price * quantity;
        account.deposit(totalValue);

        // Registra a operação no banco de dados
        adicionarOperacao(accountNumber, sym, quantity, totalValue, Operation.Type.SELL);

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

    private void accountAssetUpsert(AccountAsset accountAsset) {
        AccountAssetDAO accountAssetDAO = new AccountAssetDAO();

        if (accountAsset == null) {
            accountAssetDAO.insert(accountAsset);
        } else {
            if (accountAsset.getQuantity() == 0) {
                accountAssetDAO.delete(accountAsset);
            } else {
                accountAssetDAO.update(accountAsset);
            }
        }

    }
}
