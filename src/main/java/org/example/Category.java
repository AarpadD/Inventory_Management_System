package org.example;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class Category implements Comparable<Category> {
    private final String categoryName;
    private final Supplier supplier;
    private List<Product> Products;
    private final int warehouseId; // New attribute
    private Warehouse warehouse;

    public Category(String categoryName, Supplier supplier, int warehouseId, Product[] Products) {
        if (categoryName == null || categoryName.trim().isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be null or empty.");
        }
        if (supplier == null) {
            throw new IllegalArgumentException("Supplier cannot be null.");
        }
        if (warehouseId <= 0) {
            throw new IllegalArgumentException("Warehouse ID must be a positive integer.");
        }
        if (Products == null || Products.length == 0) {
            throw new IllegalArgumentException("Products array cannot be null or empty.");
        }

        this.categoryName = categoryName;
        this.supplier = supplier;
        this.warehouseId = warehouseId;
        this.Products = new ArrayList<>(Arrays.asList(Products));
    }

    public Category(String categoryName, Supplier supplier, int warehouseId) {
        if (categoryName == null || categoryName.trim().isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be null or empty.");
        }
        if (supplier == null) {
            throw new IllegalArgumentException("Supplier cannot be null.");
        }
        if (warehouseId <= 0) {
            throw new IllegalArgumentException("Warehouse ID must be a positive integer.");
        }

        this.categoryName = categoryName;
        this.supplier = supplier;
        this.warehouseId = warehouseId;
        this.Products = new ArrayList<>();
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse; // Set the warehouse reference
    }

    public String getCategoryName() {
        return categoryName;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public List<Product> getProducts() {
        return Products;
    }

    public int getWarehouseId() {
        return warehouseId;
    }

    public void addProduct(Product product) {
        Products.add(product);

        // Synchronize with the warehouse if the associated Warehouse exists
        if (warehouse != null) {
            warehouse.addProduct(product);
        } else {
            System.out.println("Warning: No warehouse associated with this category.");
        }
    }

    @Override
    public int compareTo(Category other) {
        return Integer.compare(this.getTotalQuantity(), other.getTotalQuantity());
    }

    public int getTotalQuantity() {
        return Products.stream().mapToInt(Product::getQuantity).sum();
    }

    @Override
    public String toString() {
        return "Category{" +
                "categoryName='" + categoryName + '\'' +
                ", main.Supplier=" + supplier +
                ", warehouseId=" + warehouseId +
                ", Products=" + Products +
                '}';
    }
}
