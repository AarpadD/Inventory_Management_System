package org.example;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class WarehouseTest {

    // Test for Constructor
    @Test
    void testConstructor() {
        // Arrange
        int warehouseId = 101;
        int capacity = 1000;

        // Act
        Warehouse warehouse = new Warehouse(warehouseId, capacity);

        // Assert
        assertEquals(warehouseId, warehouse.getId(), "Warehouse ID should be initialized correctly");
        assertEquals(capacity, warehouse.getCapacity(), "Warehouse capacity should be initialized correctly");
        assertTrue(warehouse.getProducts().isEmpty(), "Products list should be empty upon initialization");
        assertTrue(warehouse.getCategories().isEmpty(), "Categories map should be empty upon initialization");
    }

    // Test for Add Product
    @Test
    void testAddProduct() {
        // Arrange
        Warehouse warehouse = new Warehouse(101, 50); // Capacity of 50
        Product product = new Product("Laptop", 20, 1000.0, "Electronics");

        // Act
        warehouse.addProduct(product);

        // Assert
        List<Product> products = warehouse.getProducts();
        assertEquals(1, products.size(), "Product list should contain one product after addition");
        assertEquals(product, products.get(0), "The added product should be present in the list");
        assertEquals(20, warehouse.getTotalStock(), "Total stock should be updated to reflect the added product's quantity");
    }

    @Test
    void testAddProductExceedsCapacity() {
        // Arrange
        Warehouse warehouse = new Warehouse(101, 30); // Capacity of 30
        Product product = new Product("Printer", 40, 200.0, "Office Supplies");

        // Act
        warehouse.addProduct(product);

        // Assert
        assertTrue(warehouse.getProducts().isEmpty(), "Product list should remain empty as the addition exceeds capacity");
        assertEquals(0, warehouse.getTotalStock(), "Total stock should remain at 0 as addition was rejected");
    }

    // Test for Add Category
    @Test
    void testAddCategory() {
        // Arrange
        Warehouse warehouse = new Warehouse(202, 200);
        Supplier supplier = new Supplier(1, "Electronics Supplier");
        Product product1 = new Product("Phone", 10, 700.0, "Electronics");
        Product product2 = new Product("Tablet", 15, 500.0, "Electronics");
        Category category = new Category("Electronics", supplier, 202, new Product[] { product1, product2 });

        // Act
        warehouse.addCategory(category);

        // Assert
        Map<String, Category> categories = warehouse.getCategories();
        assertEquals(1, categories.size(), "Categories map should contain one category");
        assertTrue(categories.containsKey("Electronics"), "Category map should contain the added category");
        assertEquals(category, categories.get("Electronics"), "The added category should match the input category");

        List<Product> products = warehouse.getProducts();
        assertEquals(2, products.size(), "Products list should contain all products from the added category");
        assertTrue(products.contains(product1), "Products list should contain the first product");
        assertTrue(products.contains(product2), "Products list should contain the second product");
        assertEquals(25, warehouse.getTotalStock(), "Total stock should be the sum of all product quantities in the category");
    }

    // Test for Restock
    @Test
    void testRestock() {
        // Arrange
        Warehouse warehouse = new Warehouse(303, 100);
        Product product = new Product("Charger", 5, 25.0, "Accessories");
        warehouse.addProduct(product);

        // Act
        warehouse.restock("Charger", 10);

        // Assert
        List<Product> products = warehouse.getProducts();
        assertEquals(15, products.get(0).getQuantity(), "The product's quantity should be updated after restocking");
        assertEquals(15, warehouse.getTotalStock(), "Total stock should reflect the restocking quantity");
    }

    @Test
    void testRestockNonexistentProduct() {
        // Arrange
        Warehouse warehouse = new Warehouse(404, 100);

        // Act
        warehouse.restock("Nonexistent", 10);

        // Assert
        assertEquals(0, warehouse.getTotalStock(), "Total stock should remain unchanged when restocking a nonexistent product");
    }

    // Test for Get Low Stock Products
    @Test
    void testGetLowStockProducts() {
        // Arrange
        Warehouse warehouse = new Warehouse(505, 200);
        Product lowStockProduct = new Product("Pen", 5, 1.0, "Stationery");
        Product sufficientStockProduct = new Product("Notebook", 20, 2.5, "Stationery");
        warehouse.addProduct(lowStockProduct);
        warehouse.addProduct(sufficientStockProduct);

        // Act
        List<Product> lowStockProducts = warehouse.getLowStockProducts();

        // Assert
        assertEquals(1, lowStockProducts.size(), "Only low stock products should be returned");
        assertTrue(lowStockProducts.contains(lowStockProduct), "The low stock product should be included in the returned list");
        assertFalse(lowStockProducts.contains(sufficientStockProduct), "Products with sufficient stock should not be included");
    }

    // Test for ToString
    @Test
    void testToString() {
        // Arrange
        Warehouse warehouse = new Warehouse(606, 1000);
        Product product = new Product("Mouse", 15, 10.0, "Electronics");
        warehouse.addProduct(product);

        // Act
        String result = warehouse.toString();

        // Assert
        assertTrue(result.contains("606"), "ToString should include warehouse ID");
        assertTrue(result.contains("1000"), "ToString should include warehouse capacity");
        assertTrue(result.contains("Mouse"), "ToString should include product details");
    }
}