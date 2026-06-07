package view.menu;

import dao.AccountAssetDAO;
import model.Account;
import model.AccountAsset;
import record.Asset;
import service.AssetService;
import service.OperationService;
import validator.ValidationException;

import java.util.List;
import java.util.Scanner;

public class BuyAndSell {
    public static void menuHandler(Account currentUser, Scanner sc) {
        AccountAssetDAO accountAssetDao = new AccountAssetDAO();
        AssetService assetService = new AssetService();
        OperationService operationService = new OperationService();

        System.out.println("\n=== Comprar/Vender Ativo ===");
        System.out.println("Seu saldo: R$ " + String.format("%.2f", currentUser.getBalance()));

        System.out.println("\nSeus ativos:");
        List<AccountAsset> accountAssets = accountAssetDao.listAllByAccount(currentUser.getAccountNumber());

        if (accountAssets.isEmpty()) {
            System.out.println("  (nenhum ativo)");
        } else {
            for (AccountAsset a : accountAssets) {
                System.out.println("  " + a.getAssetSymbol() + ": " + String.format("%.6f", a.getQuantity()));
            }
        }

        assetService.updateAssets();
        List<Asset> assets = assetService.getAssets();

        System.out.println("\nAtivos disponíveis:");
        for (Asset a : assets) {
            System.out.println("  " + a.symbol() + " - " + a.name() + " | R$ " + String.format("%.2f", a.currentValue()));
        }

        try {
            System.out.print("\nEscolha o ativo (código): ");
            String ativo = sc.nextLine().trim().toUpperCase();

            Asset assetSelecionado = assets.stream()
                    .filter(a -> a.symbol().trim().toUpperCase().equals(ativo))
                    .findFirst()
                    .orElse(null);

            if (assetSelecionado == null) {
                System.out.println("Ativo não encontrado!");
                return;
            }

            System.out.print("Comprar ou Vender (C/V)? ");
            String tipoOp = sc.nextLine().trim().toUpperCase();

            System.out.print("Quantidade: ");
            double qtd = Double.parseDouble(sc.nextLine().trim());

            if (qtd <= 0) {
                System.out.println("Quantidade deve ser positiva!");
                return;
            }

            if ("C".equals(tipoOp) || "COMPRA".equals(tipoOp)) {
                System.out.println("\nProcessando compra...");
                operationService.buyAsset(currentUser, assetSelecionado, qtd);
                System.out.println("\nCompra concluída!");
            } else if ("V".equals(tipoOp) || "VENDA".equals(tipoOp)) {
                System.out.println("\nProcessando venda...");
                operationService.sellAsset(currentUser, assetSelecionado, qtd);
                System.out.println("\nVenda concluída!");
            } else {
                System.out.println("Tipo de operação inválido. Use C (Comprar) ou V (Vender).");
            }

        } catch (ValidationException ve) {
            System.out.println("Erro: " + ve.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Quantidade inválida!");
        } catch (Exception e) {
            System.out.println("Erro inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }
}