package model;

public class AccountAsset {
  private final String accountNumber;
  private final String assetSymbol;
  private double quantity;

  public AccountAsset(String accountNumber, String assetSymbol, double quantity) {
    this.accountNumber = accountNumber;
    this.assetSymbol = assetSymbol;
    this.quantity = quantity;
  }

  public String getAccountNumber() {
    return accountNumber;
  }

  public String getAssetSymbol() {
    return assetSymbol;
  }

  public double getQuantity() {
    return quantity;
  }

  public void setQuantity(double quantity) {
    this.quantity = quantity;
  }
}