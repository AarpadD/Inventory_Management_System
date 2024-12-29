package org.example;

import java.util.*;

public class Warehouse implements Restockable {
    private final int id;
    private int capacity;
    private List<Product> Products;
    private Map<String, Category> categories; // Map to store categories by their names
    private Map<String, SalesRecord> salesMap;

    public Warehouse(int id, int capacity) {
        if (id <= 0) {
            throw new IllegalArgumentException("Warehouse ID must be a positive integer.");
        }
        if (capacity <= 0) {
            throw new IllegalArgumentException("Warehouse capacity must be a positive number greater than zero.");
        }

        this.id = id;
        this.capacity = capacity;
        this.Products = new ArrayList<>();
        this.categories = new HashMap<>();
        this.salesMap = new HashMap<>();
    }

    public int getId() {
        return id;
    }

    public int getCapacity() {
        return capacity;
    }

    public void addCategory(Category category) {
        String categoryName = category.getCategoryName();
        if (categories.containsKey(categoryName)) {
            System.out.println("Category already exists. Adding products to the existing Category.");
        } else {
            categories.put(categoryName, category);
        }

        category.setWarehouse(this);

        for (Product product : category.getProducts()) {
            addProduct(product);
        }
    }

    public void addProduct(Product product) {
        if (getTotalStock() + product.getQuantity() > capacity) {
            System.out.println("Cannot add main.Product. Exceeds capacity.");
        } else {
            Products.add(product);
            System.out.println("Product added. Current total stock: " + getTotalStock());
        }
    }

    public int getTotalStock() {
        return Products.stream().mapToInt(Product::getQuantity).sum();
    }

    public List<Product> getProducts() {
        return new ArrayList<>(Products); // Return a copy of the Products list to avoid modification
    }

    public Map<String, Category> getCategories() {
        return new HashMap<>(categories); // Return a copy of the categories map to avoid modification
    }

    public void sellProductToRetailer(String productName, int quantity, Retailer retailer) {
        if (retailer == null) {
            throw new IllegalArgumentException("Retailer cannot be null.");
        }

        Product productToSell = null;

        // Find the product in the warehouse
        for (Product product : Products) {
            if (product.getName().equals(productName)) {
                productToSell = product;
                break;
            }
        }

        if (productToSell == null) {
            System.out.println("Product " + productName + " not found in the warehouse.");
            return;
        }

        // Check if enough stock exists in the warehouse
        if (productToSell.getQuantity() < quantity) {
            System.out.println("Not enough stock of " + productName + " in the warehouse.");
            return;
        }

        // Deduct stock from the warehouse
        productToSell.setQuantity(productToSell.getQuantity() - quantity);
        System.out.println("Warehouse sold " + quantity + " units of " + productName +
                " to retailer. Remaining stock: " + productToSell.getQuantity());

        // Pass specific product stock to the retailer
        retailer.receiveStock(productName, quantity);

        // Update warehouse sales record
        salesMap.putIfAbsent(productName, new SalesRecord(productName));
        salesMap.get(productName).addSale(quantity, productToSell.getPrice());
    }

    // Display sales data
    public void displaySalesData() {
        if (salesMap.isEmpty()) {
            System.out.println("No sales data available.");
            return;
        }

        System.out.println("Sales Data for Warehouse ID: " + id);
        System.out.println("----------------------------------");
        salesMap.values().forEach(System.out::println);
        System.out.println("----------------------------------");
    }

    // Get stock for a specific product
    public int getProductStock(String productName) {
        for (Product product : Products) {
            if (product.getName().equals(productName)) {
                return product.getQuantity();
            }
        }
        return -1; // Indicating product not found
    }

    @Override
    public void restock(String productName, int amount) {
        for (Product product : Products) {
            if (product.getName().equals(productName)) {
                product.setQuantity(product.getQuantity() + amount);
                System.out.println("Restocked " + productName + ". New quantity: " + product.getQuantity());
                return;
            }
        }
        System.out.println("Product not found.");
    }

    @Override
    public List<Product> getLowStockProducts() {
        List<Product> lowStockProducts = new ArrayList<>();
        for (Product product : Products) {
            if (product.getQuantity() < 10) {
                lowStockProducts.add(product);
            }
        }
        return lowStockProducts;
    }

    @Override
    public String toString() {
        return "Warehouse{" +
                "id=" + id +
                ", capacity=" + capacity +
                ", Products=" + Products +
                ", categories=" + categories.values() +
                '}';
    }
}