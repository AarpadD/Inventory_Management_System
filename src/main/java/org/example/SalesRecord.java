package org.example;

public class SalesRecord {

    private final String productName;
    private int unitsSold;
    private double totalRevenue; // Total revenue generated by sales of this product

    // Constructor
    public SalesRecord(String productName) {
        if (productName == null || productName.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be null or empty.");
        }

        this.productName = productName;
        this.unitsSold = 0; // Initialize units sold
        this.totalRevenue = 0.0; // Initialize total revenue
    }

    /**
     * Adds a sale to the sales record.
     *
     * @param quantity The number of units sold.
     * @param price    The price per unit of the product.
     */
    public void addSale(int quantity, double price) {
        if (quantity <= 0 || price <= 0) {
            System.out.println("Invalid sale input. Quantity and price must be positive.");
            return;
        }

        this.unitsSold += quantity; // Add to total units sold
        this.totalRevenue += quantity * price; // Add to total revenue
    }

    /**
     * Gets the total number of units sold for this product.
     *
     * @return The total number of units sold.
     */
    public int getUnitsSold() {
        return unitsSold;
    }

    /**
     * Gets the total revenue generated by sales of this product.
     *
     * @return The total revenue.
     */
    public double getTotalRevenue() {
        return totalRevenue;
    }

    @Override
    public String toString() {
        return "SalesRecord{" +
                "productName='" + productName + '\'' +
                ", unitsSold=" + unitsSold +
                ", totalRevenue=" + totalRevenue +
                '}';
    }
}
