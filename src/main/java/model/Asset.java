package model;

public class Asset {
    private String symbol;
    private String name;
    private double currentValue;

    public Asset(String symbol, String name, double currentValue) {
        this.symbol = symbol;
        this.name = name;
        this.currentValue = currentValue;
    }

    public String getSymbol() { return symbol; }
    public String getName() { return name; }
    public double getCurrentValue() { return currentValue; }
    public void setCurrentValue(double currentValue) { this.currentValue = currentValue; }
}
