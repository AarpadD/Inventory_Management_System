package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

class MainTest {

    @BeforeEach
    void setUp() {
        // Reset static fields in the Main class
        Main.setSuppliers(new HashMap<>());
        Main.setProductLookup(new HashMap<>());
        Main.setCategoriesByName(new HashMap<>());
        Main.setWarehouses(new ArrayList<>());
        Main.setRetailers(new ArrayList<>());
    }


    @Test
    void testAddSupplier() {

        Supplier supplier = new Supplier(1, "Test Supplier");

        Main.getSuppliers().put(supplier.getId(), supplier);

        assertTrue(Main.getSuppliers().containsKey(1), "Supplier should have been added to the map.");
        assertEquals("Test Supplier", Main.getSuppliers().get(1).getName(), "Supplier name should match the input.");
    }


    @Test
    void testAddRetailer() {
        // Arrange
        Retailer retailer = new Retailer("Location1", 500);

        // Act
        Main.getRetailers().add(retailer);

        // Assert
        assertEquals(1, Main.getRetailers().size(), "There should be one retailer.");
        Retailer retrievedRetailer = Main.getRetailers().get(0);
        assertEquals("Location1", retrievedRetailer.getLocation(), "Retailer location should match.");
        assertEquals(500, retrievedRetailer.getCapacity(), "Retailer capacity should match.");
    }


    @Test
    void testAddProductToCategory() {
        Supplier supplier = new Supplier(1, "Supplier1");
        Warehouse warehouse = new Warehouse(1, 100);

        Main.getSuppliers().put(1, supplier);
        Main.getWarehouses().add(warehouse);

        Category category = new Category("Electronics", supplier, warehouse.getId());
        Main.getCategoriesByName().put("Electronics", category);

        Product product = new Product("Laptop", 5, 1000.0, "Electronics");

        category.addProduct(product);

        assertTrue(Main.getCategoriesByName().containsKey("Electronics"), "Category 'Electronics' should exist.");
        assertEquals(1, category.getProducts().size(), "Category should have 1 product.");
        assertEquals("Laptop", category.getProducts().get(0).getName(), "Product name should match.");
    }


    @Test
    void testAddProductToRetailer() {
        Retailer retailer = new Retailer("Location1", 500);
        retailer.receiveStock("Apples", 100);

        Main.getRetailers().add(retailer);

        assertEquals(1, Main.getRetailers().size(), "Retailer should be added.");
        assertEquals(100, retailer.getAvailableStock(), "Retailer should have the correct available stock.");
        assertEquals(100, retailer.getStockByProduct().get("Apples"), "Stock of Apples should match.");
    }

    @Test
    void testTransferStockFromWarehouseToRetailer() {
        Warehouse warehouse = new Warehouse(1, 1000);
        warehouse.addProduct(new Product("Apples", 200, 2.5, "Fruits"));
        Main.getWarehouses().add(warehouse);

        Retailer retailer = new Retailer("Location1", 500);
        Main.getRetailers().add(retailer);

        warehouse.sellProductToRetailer("Apples", 50, retailer);

        assertEquals(150, warehouse.getProductStock("Apples"), "Warehouse should have updated stock for Apples.");
        assertEquals(50, retailer.getStockByProduct().get("Apples"), "Retailer should have added stock for Apples.");
        assertEquals(50, retailer.getAvailableStock(), "Retailer's available stock should have increased correctly.");
    }

}