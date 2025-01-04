package org.example;

import java.util.HashMap;
import java.util.Map;

public class Retailer implements Sellable {

    private final String location;
    private int capacity;
    private int availableStock;
    private Map<String, Integer> stockByProduct;
    private Map<String, SalesRecord> salesRecords;

    public Map<String, Integer> getStockByProduct() {
        return new HashMap<>(stockByProduct); // Return a defensive copy
    }
    public Map<String, SalesRecord> getSalesRecords() {
        return new HashMap<>(salesRecords); // Return a defensive copy
    }

    public Retailer(String location, int capacity) {
        if (location == null || location.trim().isEmpty()) {
            throw new IllegalArgumentException("Location cannot be null or empty.");
        }

        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be a positive integer.");
        }

        this.location = location;
        this.capacity = capacity;
        this.availableStock = 0;
        this.stockByProduct = new HashMap<>();
        this.salesRecords = new HashMap<>();
    }


    @Override
    public void sell(int amount) {
        System.out.println("This method is not used directly here.");
    }

    @Override
    public boolean isInStock() {
        return availableStock > 0;
    }


    public void receiveStock(String productName, int quantity) {
        if (productName == null || productName.trim().isEmpty() || productName.equalsIgnoreCase("default")) {
            //System.out.println("Invalid product name: " + productName);
            return;
        }

        if (quantity <= 0) {
            System.out.println("Invalid quantity received for " + productName + ". Quantity must be positive.");
            return;
        }

        int remainingCapacity = capacity - availableStock;
        if (quantity > remainingCapacity) {
            System.out.println("Not enough capacity to add " + quantity + " units of " + productName);
            System.out.println("Adding only " + remainingCapacity + " units.");

            quantity = remainingCapacity;
        }

        stockByProduct.put(productName, stockByProduct.getOrDefault(productName, 0) + quantity);
        availableStock += quantity;
    }


    public void sellProduct(String productName, int quantity, double price) {
        if (productName == null || productName.trim().isEmpty()) {
            System.out.println("Invalid product name.");
            return;
        }

        if (quantity <= 0 || price <= 0) {
            System.out.println("Invalid quantity or price. Both must be positive.");
            return;
        }

        int currentStock = stockByProduct.getOrDefault(productName, 0);
        if (currentStock < quantity) {
            System.out.println("Not enough stock of " + productName + " to sell.");
            System.out.println("Available stock: " + currentStock);
            return;
        }

        stockByProduct.put(productName, currentStock - quantity);
        availableStock -= quantity;

        SalesRecord record = salesRecords.getOrDefault(productName, new SalesRecord(productName));
        record.addSale(quantity, price);
        salesRecords.put(productName, record);

        System.out.println("Sold " + quantity + " units of " + productName + " at $" + price + " each.");
        System.out.println("Remaining stock for " + productName + ": " + (currentStock - quantity));
        System.out.println("Remaining total stock: " + availableStock);
    }


    public int getStockForProduct(String productName) {
        return stockByProduct.getOrDefault(productName, 0);
    }


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


    public void displaySalesSummary() {
        if (salesRecords.isEmpty()) {
            System.out.println("No sales data available.");
            return;
        }

        int totalProductsSold = 0;
        double totalRevenue = 0.0;
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


    public int getAvailableStock() {
        return availableStock;
    }


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