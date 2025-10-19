package service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import model.Asset;

public class AssetService {
    private final Random random = new Random();
    private final Map<String, Asset> assets = new HashMap<>();

    // Inicialize os ativos dentro do construtor do AssetService
    public AssetService() {
        assets.put("BTC", new Asset("BTC", "Bitcoin", 585560.0, "Cripto mais famosa"));
        assets.put("ETH", new Asset("ETH", "Ethereum", 22071.83, "Contratos inteligentes"));
        assets.put("BNB", new Asset("BNB", "Binance Coin", 5292.16, "Token da Binance"));
        assets.put("SOL", new Asset("SOL", "Solana", 1079.02, "Blockchain de alta performance"));
        assets.put("ADA", new Asset("ADA", "Cardano", 4.31, "Contratos smart e sustentabilidade"));
        assets.put("XRP", new Asset("XRP", "Ripple", 15.00, "Pagamentos globais rápidos"));
        assets.put("USDT", new Asset("USDT", "Tether", 5.57, "Stablecoin pareada ao dólar"));
        assets.put("DOGE", new Asset("DOGE", "Dogecoin", 0.75, "Memecoin de grande comunidade"));
    }

    public Map<String, Asset> getAssets() {
        return assets;
    }

    public void updateAssetValue(Asset asset) {
        double changePercent = (random.nextDouble() - 0.5) * 0.1; // varia entre -5% e +5%
        double currentValue = asset.getCurrentValue();
        asset.setCurrentValue(currentValue * (1 + changePercent));
    }
}
