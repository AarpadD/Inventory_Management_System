package org.example;

public class SalesRecord {

    private final String productName;
    private int unitsSold;
    private double totalRevenue;

    // Constructor
    public SalesRecord(String productName) {
        if (productName == null || productName.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be null or empty.");
        }

        this.productName = productName;
        this.unitsSold = 0;
        this.totalRevenue = 0.0;
    }


    public void addSale(int quantity, double price) {
        if (quantity <= 0 || price <= 0) {
            System.out.println("Invalid sale input. Quantity and price must be positive.");
            return;
        }

        this.unitsSold += quantity;
        this.totalRevenue += quantity * price;
    }


    public int getUnitsSold() {
        return unitsSold;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    @Override
    public String toString() {
        return "Sales Record:\n" +
                "  Product Name: " + productName + "\n" +
                "  Units Sold: " + unitsSold + "\n" +
                "  Total Revenue: $" + String.format("%.2f", totalRevenue);
    }
}
