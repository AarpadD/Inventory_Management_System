package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SupplierTest {

    @Test
    void testConstructorWithValidParameters() {
        // Arrange
        int id = 1;
        String name = "Valid Supplier";

        // Act
        Supplier supplier = new Supplier(id, name);

        // Assert
        assertEquals(id, supplier.getId(), "The id should be correctly initialized");
        assertEquals(name, supplier.getName(), "The name should be correctly initialized");
    }

    // Test for getId() method
    @Test
    void testGetId() {
        // Arrange
        int supplierId = 5;
        String supplierName = "Example Supplier";
        Supplier supplier = new Supplier(supplierId, supplierName);

        // Act
        int id = supplier.getId();

        // Assert
        assertEquals(supplierId, id, "The getId() method should return the correct ID");
    }

    // Test for getName() method
    @Test
    void testGetName() {
        // Arrange
        int supplierId = 8;
        String supplierName = "Supplier Name Test";
        Supplier supplier = new Supplier(supplierId, supplierName);

        // Act
        String name = supplier.getName();

        // Assert
        assertEquals(supplierName, name, "The getName() method should return the correct name");
    }

    @Test
    void testToString() {
        // Arrange
        int id = 2;
        String name = "Sample Supplier";
        Supplier supplier = new Supplier(id, name);

        // Act
        String actualString = supplier.toString();

        // Assert
        String expectedString = "Supplier{id=2, name='Sample Supplier'}";
        assertEquals(expectedString, actualString, "The toString method should return the correct format");
    }
}