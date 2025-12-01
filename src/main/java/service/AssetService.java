package service;

import model.Asset;

import java.util.HashMap;
import java.util.Map;

public class AssetService {

    private Map<String, Asset> assets = new HashMap<>();

    public AssetService() {
        // Pode ser cripto ou ações; aqui exemplo cripto
        assets.put("BTC", new Asset("BTC", "Bitcoin", 100000.00));
        assets.put("ETH", new Asset("ETH", "Ethereum", 4000.00));
        assets.put("SOL", new Asset("SOL", "Solana", 180.00));
        assets.put("ADA", new Asset("ADA", "Cardano", 0.80));
    }

    public Map<String, Asset> getAssets() {
        return new HashMap<>(assets);
    }

    public void updateAssetValue(Asset asset) {
        // Simulação de variação simples
        double base = asset.getCurrentValue();
        double fator = 1 + ((Math.random() - 0.5) * 0.05); // ±5%
        asset.setCurrentValue(base * fator);
    }

    public Asset getAsset(String symbol) {
        return assets.get(symbol.toUpperCase());
    }

    public void addAsset(String symbol, String name, double price) {
        assets.put(symbol.toUpperCase(), new Asset(symbol.toUpperCase(), name, price));
    }
}
