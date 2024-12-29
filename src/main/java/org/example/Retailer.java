package org.example;

import java.util.HashMap;
import java.util.Map;
import java.util.HashMap;
import java.util.Map;

public class Retailer implements Sellable {

    private final String location; // Location of the retailer
    private int capacity; // Maximum stock capacity for the retailer
    private int availableStock; // Current total stock in the retailer
    private Map<String, Integer> stockByProduct; // Product-specific stock tracking
    private Map<String, SalesRecord> salesRecords; // Tracks sales records for each product

    // Constructor
    public Retailer(String location, int capacity) {
        if (location == null || location.trim().isEmpty()) {
            throw new IllegalArgumentException("Location cannot be null or empty.");
        }

        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be a positive integer.");
        }

        this.location = location; // Initialize location
        this.capacity = capacity; // Set max capacity
        this.availableStock = 0; // Initializes with no stock
        this.stockByProduct = new HashMap<>(); // Initializes product stock tracking
        this.salesRecords = new HashMap<>(); // Initializes sales record tracking
    }

    // Sellable Interface Implementation (Not Used in This Context)
    @Override
    public void sell(int amount) {
        System.out.println("This method is not used directly in this implementation.");
    }

    @Override
    public boolean isInStock() {
        return availableStock > 0; // Retailer is in stock if total stock > 0
    }

    /**
     * Receive stock for a specific product, ensuring it adheres to capacity.
     * Dynamically calculates remaining capacity and adjusts the stock quantity if needed.
     *
     * @param productName Name of the product to add.
     * @param quantity    Quantity of the product to be added.
     */
    public void receiveStock(String productName, int quantity) {
        if (productName == null || productName.trim().isEmpty()) {
            System.out.println("Invalid product name.");
            return;
        }

        if (quantity <= 0) {
            System.out.println("Invalid quantity received for " + productName + ". Quantity must be positive.");
            return;
        }

        int remainingCapacity = capacity - availableStock; // Remaining space available in the retailer
        if (quantity > remainingCapacity) {
            System.out.println("Not enough capacity to add " + quantity + " units of " + productName);
            System.out.println("Adding only " + remainingCapacity + " units.");

            quantity = remainingCapacity; // Adjust quantity to fit within the remaining capacity
        }

        // Update product-specific stock
        stockByProduct.put(productName, stockByProduct.getOrDefault(productName, 0) + quantity);

        // Update total available stock
        availableStock += quantity;

        System.out.println("Retailer received stock: " + quantity + " units of " + productName);
        System.out.println("Remaining capacity: " + (capacity - availableStock));
    }

    /**
     * Sell a specific product, deducting stock and updating sales records.
     *
     * @param productName Name of the product to be sold.
     * @param quantity    Quantity of the product being sold.
     * @param price       Price per unit of the product.
     */
    public void sellProduct(String productName, int quantity, double price) {
        if (productName == null || productName.trim().isEmpty()) {
            System.out.println("Invalid product name.");
            return;
        }

        if (quantity <= 0 || price <= 0) {
            System.out.println("Invalid quantity or price. Both must be positive.");
            return;
        }

        // Check if enough stock is available for the product
        int currentStock = stockByProduct.getOrDefault(productName, 0);
        if (currentStock < quantity) {
            System.out.println("Not enough stock of " + productName + " to sell.");
            System.out.println("Available stock: " + currentStock);
            return;
        }

        // Deduct the specified quantity from the product's stock
        stockByProduct.put(productName, currentStock - quantity);

        // Update total available stock
        availableStock -= quantity;

        // Update or create a sales record for the product
        SalesRecord record = salesRecords.getOrDefault(productName, new SalesRecord(productName));
        record.addSale(quantity, price);
        salesRecords.put(productName, record);

        System.out.println("Sold " + quantity + " units of " + productName + " at $" + price + " each.");
        System.out.println("Remaining stock for " + productName + ": " + (currentStock - quantity));
        System.out.println("Remaining total stock: " + availableStock);
    }

    /**
     * Get the stock availability for a specific product.
     *
     * @param productName Name of the product to be checked.
     * @return Quantity in stock for the queried product.
     */
    public int getStockForProduct(String productName) {
        return stockByProduct.getOrDefault(productName, 0);
    }

    /**
     * View all sales records for this retailer.
     */
    public void viewSalesRecords() {
        if (salesRecords.isEmpty()) {
            System.out.println("No sales records available.");
            return;
        }

        System.out.println("Sales Records for Retailer at " + location + ":");
        for (SalesRecord record : salesRecords.values()) {
            System.out.println(record);
        }
    }

    /**
     * Display a summary of all sales including total revenue and total products sold.
     */
    public void displaySalesSummary() {
        if (salesRecords.isEmpty()) {
            System.out.println("No sales data available.");
            return;
        }

        int totalProductsSold = 0;
        double totalRevenue = 0.0;

        // Summarize all records
        for (SalesRecord record : salesRecords.values()) {
            totalProductsSold += record.getUnitsSold();
            totalRevenue += record.getTotalRevenue();
        }

        System.out.println("Sales Summary for Retailer at " + location + ":");
        System.out.println("-----------------------------------------------");
        System.out.println("Total Products Sold: " + totalProductsSold);
        System.out.println("Total Revenue: $" + totalRevenue);
        System.out.println("-----------------------------------------------");
    }

    /**
     * Get the total stock available for the retailer.
     *
     * @return Total stock across all products.
     */
    public int getAvailableStock() {
        return availableStock;
    }

    // Getters for location and capacity
    public String getLocation() {
        return location;
    }

    public int getCapacity() {
        return capacity;
    }

    @Override
    public String toString() {
        return "Retailer{" +
                "location='" + location + '\'' +
                ", capacity=" + capacity +
                ", availableStock=" + availableStock +
                ", stockByProduct=" + stockByProduct +
                '}';
    }
}