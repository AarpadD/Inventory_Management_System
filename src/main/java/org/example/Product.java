package org.example;

public class Product implements Comparable<Product>  {
    private final String name;
    private int quantity;
    private double price;
    private final String category;

    public Product(String name, int quantity, double price, String category) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty.");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative.");
        }
        if (price < 0.0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Category cannot be null or empty.");
        }

        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isLowStock() {
        return quantity < 10;
    }

    @Override
    public int compareTo(Product other) {
        return Double.compare(this.price, other.price);
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", main.Category='" + category + '\'' +
                '}';
    }
}
