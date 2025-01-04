package org.example;

import java.util.*;

public class Warehouse implements Restockable {
    private final int id;
    private int capacity;
    private List<Product> Products;
    private Map<String, Category> categories;
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
            addProduct(product, false);
        }
    }

    public void addProduct(Product product) {addProduct(product, false);}

    public void addProduct(Product product, boolean showWarnings) {
        for (Product existingProduct : Products) {
            if (existingProduct.getName().equalsIgnoreCase(product.getName())) {
                if (showWarnings) {
                    System.out.println("Product '" + product.getName() + "' already exists in the warehouse. Skipping addition.");
                }
                return;
            }
        }

        if (getTotalStock() + product.getQuantity() > capacity) {
            System.out.println("Cannot add Product. Exceeds warehouse capacity.");
        } else {
            Products.add(product);
        }
    }


    public int getTotalStock() {
        return Products.stream().mapToInt(Product::getQuantity).sum();
    }

    public List<Product> getProducts() {
        return new ArrayList<>(Products);
    }

    public Map<String, Category> getCategories() {
        return new HashMap<>(categories);
    }

    public void sellProductToRetailer(String productName, int quantity, Retailer retailer) {
        if (retailer == null) {
            throw new IllegalArgumentException("Retailer cannot be null.");
        }

        Product productToSell = null;

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

        if (productToSell.getQuantity() < quantity) {
            System.out.println("Not enough stock of " + productName + " in the warehouse.");
            return;
        }

        productToSell.setQuantity(productToSell.getQuantity() - quantity);
        System.out.println("Successfully transferred " + quantity + " units of '" + productName +
                "' from warehouse ID " + id + " to retailer at " + retailer.getLocation() + ".");

        retailer.receiveStock(productName, quantity);

        salesMap.putIfAbsent(productName, new SalesRecord(productName));
        salesMap.get(productName).addSale(quantity, productToSell.getPrice());

        if (productToSell.getQuantity() < lowStockThreshold) {
            System.out.println("ALERT: " + productToSell.getName() + " is below the stock threshold (" + lowStockThreshold + ").");
        }
    }


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


    public int getProductStock(String productName) {
        for (Product product : Products) {
            if (product.getName().equals(productName)) {
                return product.getQuantity();
            }
        }
        return -1;
    }

    private final int lowStockThreshold = 10;


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
            if (product.getQuantity() < lowStockThreshold) {
                lowStockProducts.add(product);
            }
        }
        return lowStockProducts;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Warehouse{")
                .append("id=").append(id)
                .append(", capacity=").append(capacity)
                .append(", Products=").append(Products)
                .append(", categories=[");

        // Only list the category names to avoid duplication
        categories.values().forEach(category -> sb.append(category.getCategoryName()).append(" "));
        sb.append("]}");

        return sb.toString();
    }
}