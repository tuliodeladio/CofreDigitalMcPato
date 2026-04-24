package view.menu;

import dao.AccountAssetDAO;
import model.Account;
import model.AccountAsset;
import model.Asset;
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

        System.out.println("Seu saldo: R$ " + String.format("%.2f", currentUser.getBalance()));
        System.out.println("Seus ativos:");

        List<AccountAsset> accountAssets = accountAssetDao.listAllByAccount(currentUser.getAccountNumber());
        for (AccountAsset a : accountAssets) {
            System.out.println(a.getAssetSymbol() + ": " + String.format("%.6f", a.getQuantity()));
        }

        // Atualiza os preços dos ativos antes de carregá-los
        assetService.updateAssets();
        List<Asset> assets = assetService.getAssets();
        System.out.println("\nAtivos disponíveis:");
        for (Asset a : assets) {
            System.out.println(a.getSymbol() + " - " + a.getName() + " R$ " + String.format("%.2f", a.getCurrentValue()));
        }

        try {
            System.out.print("Escolha o ativo (código): ");
            String ativo = sc.nextLine().toUpperCase();
            Asset assetSelecionado = assets.stream().filter(a -> a.getSymbol().equals(ativo)).findFirst().orElse(null);

            if (assetSelecionado == null) {
                System.out.println("Ativo não encontrado!");
                return;
            }

            System.out.print("Comprar ou Vender (C/V)? ");
            String tipoOp = sc.nextLine().toUpperCase();

            System.out.print("Quantidade: ");
            double qtd = Double.parseDouble(sc.nextLine());

            if ("C".equals(tipoOp)) {
                operationService.buyAsset(currentUser, assetSelecionado, qtd);
            } else if ("V".equals(tipoOp)) {
                operationService.sellAsset(currentUser, assetSelecionado, qtd);
            } else {
                System.out.println("Tipo de operação inválido.");
            }

        } catch (ValidationException ve) {
            System.out.println(ve.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Quantidade inválida.");
        }
    }
}
