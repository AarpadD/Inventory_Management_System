package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

class MainTest {

    @BeforeEach
    void setUp() {
        // Reset the static fields in the Main class using setters
        Main.setSuppliers(new HashMap<>());
        Main.setProductLookup(new HashMap<>());
        Main.setCategoriesByName(new TreeMap<>());
        Main.setWarehouses(new ArrayList<>());
        Main.setRetailers(new ArrayList<>());
    }

    // Test 1: Adding a supplier and validating the result
    @Test
    void testAddSupplier() {
        // Arrange
        Supplier supplier = new Supplier(1, "Test Supplier");

        // Act
        Main.getSuppliers().put(supplier.getId(), supplier);

        // Assert
        assertTrue(Main.getSuppliers().containsKey(1), "Supplier should have been added to the map");
        assertEquals("Test Supplier", Main.getSuppliers().get(1).getName(), "Supplier name should match the input");
    }

    // Test 2: Adding a new product and linking it to a category
    @Test
    void testAddProductToCategory() {
        // Arrange
        Supplier supplier = new Supplier(1, "Supplier1");
        Warehouse warehouse = new Warehouse(1, 100);

        Main.getSuppliers().put(1, supplier);
        Main.getWarehouses().add(warehouse);

        Category category = new Category("Electronics", supplier, warehouse.getId());
        Main.getCategoriesByName().put("Electronics", category);

        Product product = new Product("Laptop", 5, 1000.0, "Electronics");

        // Act
        category.addProduct(product);

        // Assert
        assertTrue(Main.getCategoriesByName().containsKey("Electronics"), "Category 'Electronics' should exist.");
        assertEquals(1, category.getProducts().size(), "Category should have 1 product.");
        assertEquals("Laptop", category.getProducts().get(0).getName(), "Product name should match.");
    }

    // Test 3: Grouping products by price range and verifying results
    @Test
    void testGroupProductsByPriceRange() {
        // Arrange: Add products to Main.productLookup using the getter
        Product p1 = new Product("Item1", 10, 5.0, "General");
        Product p2 = new Product("Item2", 20, 15.0, "General");
        Product p3 = new Product("Item3", 30, 25.0, "General");
        Product p4 = new Product("Item4", 5, 55.0, "General");

        Main.getProductLookup().put("Item1", p1);
        Main.getProductLookup().put("Item2", p2);
        Main.getProductLookup().put("Item3", p3);
        Main.getProductLookup().put("Item4", p4);

        // Act: Group products by price range
        Map<String, List<Product>> groupedProducts = new TreeMap<>();
        for (Product product : Main.getProductLookup().values()) {
            String range = getPriceRange(product.getPrice());
            groupedProducts.computeIfAbsent(range, k -> new ArrayList<>()).add(product);
        }

        // Assert: Verify the grouping
        assertEquals(1, groupedProducts.get("0-10").size());
        assertEquals("Item1", groupedProducts.get("0-10").get(0).getName());

        assertEquals(1, groupedProducts.get("10-20").size());
        assertEquals("Item2", groupedProducts.get("10-20").get(0).getName());

        assertEquals(1, groupedProducts.get("20-50").size());
        assertEquals("Item3", groupedProducts.get("20-50").get(0).getName());

        assertEquals(1, groupedProducts.get("50+").size());
        assertEquals("Item4", groupedProducts.get("50+").get(0).getName());
    }

    // Helper method to define price ranges
    private String getPriceRange(double price) {
        if (price < 10) {
            return "0-10";
        } else if (price < 20) {
            return "10-20";
        } else if (price < 50) {
            return "20-50";
        } else {
            return "50+";
        }
    }

    // Test 4: Sorting products by price
    @Test
    void testSortProductsByPrice() {
        // Arrange: Add products to Main.productLookup using the getter
        Product p1 = new Product("Item1", 10, 45.0, "General");
        Product p2 = new Product("Item2", 20, 15.0, "General");
        Product p3 = new Product("Item3", 30, 25.0, "General");

        Main.getProductLookup().put("Item1", p1);
        Main.getProductLookup().put("Item2", p2);
        Main.getProductLookup().put("Item3", p3);

        // Act: Sort products by price
        List<Product> sortedProducts = new ArrayList<>(Main.getProductLookup().values());
        sortedProducts.sort(Comparator.comparingDouble(Product::getPrice));

        // Assert: Verify the sorted order
        assertEquals("Item2", sortedProducts.get(0).getName(), "First product should have the lowest price.");
        assertEquals("Item3", sortedProducts.get(1).getName(), "Second product should have the mid-range price.");
        assertEquals("Item1", sortedProducts.get(2).getName(), "Last product should have the highest price.");
    }
}