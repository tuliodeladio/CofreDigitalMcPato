package model;

public class Asset {
    private final String symbol;
    private final String name;
    private double currentValue;

    public Asset(String symbol, String name, double currentValue, String description) {
        this.symbol = symbol;
        this.name = name;
        this.currentValue = currentValue;
    }

    public String getName() { return name; }
    public String getSymbol() { return symbol; }
    public double getCurrentValue() { return currentValue; }
    public void setCurrentValue(double value) { this.currentValue = value; }
}
