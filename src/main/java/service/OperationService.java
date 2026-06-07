package service;

import dao.AccountAssetDAO;
import dao.OperationDAO;
import model.Account;
import model.AccountAsset;
import record.Asset;
import record.Operation;
import validator.AssetOperationValidator;

import java.time.LocalDateTime;
import java.util.List;

public class OperationService {

    public void buyAsset(Account account, Asset asset, double quantity) {
        try {
            AssetOperationValidator.validateBuy(account, asset, quantity);

            double price = asset.currentValue();
            double totalCost = price * quantity;
            String sym = asset.symbol();
            String accountNumber = account.getAccountNumber();

            System.out.println("[DEBUG] Comprando " + quantity + " x " + sym + " @ R$ " + price);
            System.out.println("[DEBUG] Custo total: R$ " + totalCost);
            System.out.println("[DEBUG] Saldo atual: R$ " + account.getBalance());

            if (!account.withdraw(totalCost)) {
                System.out.println("Saldo insuficiente! Necessário: R$ " + totalCost);
                return;
            }

            System.out.println("[DEBUG] Saque bem-sucedido. Novo saldo: R$ " + account.getBalance());

            account.addAsset(sym, quantity);

            System.out.println("[DEBUG] Ativo adicionado: " + sym + " = " + quantity);

            adicionarOperacao(accountNumber, sym, quantity, totalCost, Operation.Type.BUY);

            System.out.println("✓ Compra realizada com sucesso!");
            System.out.println("  Ativo: " + sym);
            System.out.println("  Quantidade: " + quantity);
            System.out.println("  Preço unitário: R$ " + String.format("%.2f", price));
            System.out.println("  Total: R$ " + String.format("%.2f", totalCost));

        } catch (Exception e) {
            System.out.println("✗ Erro na compra: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void sellAsset(Account account, Asset asset, double quantity) {
        try {
            AssetOperationValidator.validateSell(account, asset, quantity);

            double price = asset.currentValue();
            String sym = asset.symbol();
            String accountNumber = account.getAccountNumber();

            System.out.println("[DEBUG] Vendendo " + quantity + " x " + sym + " @ R$ " + price);

            double qtdAtual = account.getAssetQuantity(sym);
            System.out.println("[DEBUG] Quantidade disponível: " + qtdAtual);

            if (qtdAtual < quantity) {
                System.out.println("Você não possui quantidade suficiente do ativo!");
                System.out.println("Disponível: " + qtdAtual + ", Tenta: " + quantity);
                return;
            }

            account.removeAsset(sym, quantity);
            System.out.println("[DEBUG] Ativo removido com sucesso");

            double totalValue = price * quantity;
            account.deposit(totalValue);
            System.out.println("[DEBUG] Depósito bem-sucedido. Novo saldo: R$ " + account.getBalance());

            adicionarOperacao(accountNumber, sym, quantity, totalValue, Operation.Type.SELL);

            System.out.println("✓ Venda realizada com sucesso!");
            System.out.println("  Ativo: " + sym);
            System.out.println("  Quantidade: " + quantity);
            System.out.println("  Preço unitário: R$ " + String.format("%.2f", price));
            System.out.println("  Total recebido: R$ " + String.format("%.2f", totalValue));

        } catch (Exception e) {
            System.out.println("✗ Erro na venda: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Operation> listByAccount(String accountNumber) {
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
        System.out.println("[DEBUG] Operação registrada no banco");
    }
}