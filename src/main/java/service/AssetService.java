package service;

import dao.AssetDAO;
import record.Asset;
import java.util.List;

public class AssetService {

    public AssetService() {
        List<Asset> assets = getAssets();

        if (assets.isEmpty()) {
            createDefaultAssets();
        }
    }

    public void createDefaultAssets() {
        AssetDAO dao = new AssetDAO();

        // Pode ser cripto ou ações; aqui exemplo cripto
        dao.insert(new Asset("BTC", "Bitcoin", 100000.00));
        dao.insert(new Asset("ETH", "Ethereum", 4000.00));
        dao.insert(new Asset("SOL", "Solana", 180.00));
        dao.insert(new Asset("ADA", "Cardano", 0.80));
    }

    public List<Asset> getAssets() {
        AssetDAO dao = new AssetDAO();
        return dao.listAll();
    }

    public void updateAssets() {
        AssetDAO dao = new AssetDAO();
        dao.updateAllPrices();
    }
}
