package service;

import dao.AssetDAO;
import record.Asset;

import java.util.List;

public class AssetService {

    public AssetService() {
        System.out.println("[AssetService] Criando AssetService...");
        try {
            List<Asset> assets = getAssets();
            System.out.println("[AssetService] Ativos encontrados: " + assets.size());

            if (assets.isEmpty()) {
                System.out.println("[AssetService] Nenhum ativo encontrado. Criando ativos padrão...");
                createDefaultAssets();
                assets = getAssets();
                System.out.println("[AssetService] Ativos padrão criados. Total: " + assets.size());
            }
        } catch (Exception e) {
            System.err.println("[AssetService] ERRO ao inicializar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void createDefaultAssets() {
        AssetDAO dao = new AssetDAO();

        dao.insert(new Asset("BTC", "Bitcoin", 100000.00));
        dao.insert(new Asset("ETH", "Ethereum", 4000.00));
        dao.insert(new Asset("SOL", "Solana", 180.00));
        dao.insert(new Asset("ADA", "Cardano", 0.80));
        dao.insert(new Asset("PETR4", "Petrobras", 35.00));
        dao.insert(new Asset("VALE3", "Vale", 68.00));
    }

    public List<Asset> getAssets() {
        try {
            AssetDAO dao = new AssetDAO();
            return dao.listAll();
        } catch (Exception e) {
            System.err.println("[AssetService] ERRO ao obter ativos: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }

    public void updateAssets() {
        try {
            System.out.println("[AssetService] Atualizando preços dos ativos...");
            AssetDAO dao = new AssetDAO();
            dao.updateAllPrices();
            System.out.println("[AssetService] Atualização concluída!");
        } catch (Exception e) {
            System.err.println("[AssetService] ERRO ao atualizar ativos: " + e.getMessage());
            e.printStackTrace();
        }
    }
}