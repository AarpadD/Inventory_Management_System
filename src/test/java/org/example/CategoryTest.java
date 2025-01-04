package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class CategoryTest {

    @Test
    void testConstructorWithProductsArray() {
        Supplier supplier = new Supplier(1, "Test Supplier");
        Product[] productsArray = {
                new Product("Product A", 10, 15.0, "Electronics"),
                new Product("Product B", 20, 25.0, "Electronics")
        };
        String categoryName = "Electronics";
        int warehouseId = 101;

        Category category = new Category(categoryName, supplier, warehouseId, productsArray);

        assertEquals(categoryName, category.getCategoryName(), "Category name should be initialized correctly");
        assertEquals(supplier, category.getSupplier(), "Supplier should be initialized correctly");
        assertEquals(warehouseId, category.getWarehouseId(), "Warehouse ID should be initialized correctly");
        assertEquals(2, category.getProducts().size(), "Products array should be converted to the list");
        assertEquals("Product A", category.getProducts().get(0).getName(), "First product name is not correct");
    }


    @Test
    void testConstructorWithoutProductsArray() {
        Supplier supplier = new Supplier(2, "Another Supplier");
        String categoryName = "Furniture";
        int warehouseId = 202;

        Category category = new Category(categoryName, supplier, warehouseId);

        assertEquals(categoryName, category.getCategoryName(), "Category name should be initialized correctly");
        assertEquals(supplier, category.getSupplier(), "Supplier should be initialized correctly");
        assertEquals(warehouseId, category.getWarehouseId(), "Warehouse ID should be initialized correctly");
        assertTrue(category.getProducts().isEmpty(), "Products list should be empty for this constructor");
    }


    @Test
    void testAddProduct() {
        Supplier supplier = new Supplier(1, "Test Supplier");
        Product newProduct = new Product("New Product", 15, 14.99, "Electronics");
        Category category = new Category("Electronics", supplier, 303);

        category.addProduct(newProduct);

        assertEquals(1, category.getProducts().size(), "Product should be added to the list");
        assertTrue(category.getProducts().contains(newProduct), "The added product should exist in the list");
    }


    @Test
    void testGetTotalQuantity() {
        Supplier supplier = new Supplier(3, "Quantity Supplier");
        Product[] productsArray = {
                new Product("Product X", 5, 20.0, "Food"),
                new Product("Product Y", 8, 22.0, "Food"),
                new Product("Product Z", 12, 15.0, "Food")
        };
        Category category = new Category("Food", supplier, 404, productsArray);

        int totalQuantity = category.getTotalQuantity();

        assertEquals(25, totalQuantity, "Total quantity should be the sum of all product quantities");
    }


    @Test
    void testCompareTo() {
        Supplier supplier1 = new Supplier(4, "Supplier 1");
        Supplier supplier2 = new Supplier(5, "Supplier 2");
        Product[] category1Products = {
                new Product("Product 1", 10, 50.0, "Electronics"),
                new Product("Product 2", 20, 80.0, "Electronics")
        };
        Product[] category2Products = {
                new Product("Product 3", 15, 70.0, "Clothing")
        };
        Category category1 = new Category("Electronics", supplier1, 505, category1Products);
        Category category2 = new Category("Clothing", supplier2, 606, category2Products);

        assertTrue(category1.compareTo(category2) > 0, "Category 1 should have a higher total quantity than Category 2");
        assertTrue(category2.compareTo(category1) < 0, "Category 2 should have a lower total quantity than Category 1");
        assertEquals(0, category1.compareTo(category1), "Category should be equal when compared to itself");
    }
}