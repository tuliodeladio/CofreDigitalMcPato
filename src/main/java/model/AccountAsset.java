package model;

import dao.AssetDAO;
import record.Asset;

public class AccountAsset {
  private final String accountNumber;
  private final String assetSymbol;
  private double quantity;
  private final String assetName;

  public AccountAsset(String accountNumber, String assetSymbol, double quantity) {
    this.accountNumber = accountNumber;
    this.assetSymbol = assetSymbol;
    this.quantity = quantity;

    Asset asset = AssetDAO.findBySymbol(this.assetSymbol);
    this.assetName = asset != null ? asset.name() : null;
  }

  public String getAccountNumber() { return accountNumber; }
  public String getAssetName() { return assetName; }
  public String getAssetSymbol() { return assetSymbol; }
  public double getQuantity() { return quantity; }
  public void setQuantity(double quantity) { this.quantity = quantity; }

}
