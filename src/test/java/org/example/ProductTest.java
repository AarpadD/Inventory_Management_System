package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void testConstructorWithValidParameters() {
        // Arrange
        String name = "Laptop";
        int quantity = 15;
        double price = 1200.50;
        String category = "Electronics";

        // Act
        Product product = new Product(name, quantity, price, category);

        // Assert
        assertEquals(name, product.getName(), "The name should be correctly initialized.");
        assertEquals(quantity, product.getQuantity(), "The quantity should be correctly initialized.");
        assertEquals(price, product.getPrice(), "The price should be correctly initialized.");
        assertEquals(category, product.getCategory(), "The category should be correctly initialized.");
    }

    @Test
    void testCompareTo() {
        // Arrange
        Product product1 = new Product("Product A", 5, 50.0, "Category A");
        Product product2 = new Product("Product B", 2, 100.0, "Category B");

        // Act & Assert
        assertTrue(product1.compareTo(product2) < 0, "Product1 should be cheaper than Product2.");
        assertTrue(product2.compareTo(product1) > 0, "Product2 should be more expensive than Product1.");
    }

    @Test
    void testIsLowStock() {
        // Arrange
        Product lowStockProduct = new Product("Milk", 5, 2.5, "Dairy");
        boolean isLowStockResult1 = lowStockProduct.isLowStock();
        System.out.println("Test case 1 - Low stock (quantity < 10): " + isLowStockResult1);
        assertTrue(isLowStockResult1, "isLowStock should return true for quantity < 10.");

        Product sufficientStockProduct = new Product("Bread", 12, 1.5, "Bakery");
        boolean isLowStockResult2 = sufficientStockProduct.isLowStock();
        System.out.println("Test case 2 - Sufficient stock (quantity >= 10): " + isLowStockResult2);
        assertFalse(isLowStockResult2, "isLowStock should return false for quantity >= 10.");
    }

    @Test
    void testSetQuantity() {
        // Arrange
        Product product = new Product("Book", 8, 15.99, "Stationery");

        // Act
        product.setQuantity(20);

        // Assert
        assertEquals(20, product.getQuantity(), "The quantity should be updated to 20.");
        assertFalse(product.isLowStock(), "After updating quantity to 20, isLowStock should return false.");
    }
}