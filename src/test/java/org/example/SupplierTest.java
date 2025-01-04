package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SupplierTest {

    @Test
    void testConstructorWithValidParameters() {
        int id = 1;
        String name = "Valid Supplier";

        Supplier supplier = new Supplier(id, name);

        assertEquals(id, supplier.getId(), "The id should be correctly initialized");
        assertEquals(name, supplier.getName(), "The name should be correctly initialized");
    }


    @Test
    void testGetId() {
        int supplierId = 5;
        String supplierName = "Example Supplier";
        Supplier supplier = new Supplier(supplierId, supplierName);

        int id = supplier.getId();

        assertEquals(supplierId, id, "The getId() method should return the correct ID");
    }


    @Test
    void testGetName() {
        int supplierId = 8;
        String supplierName = "Supplier Name Test";
        Supplier supplier = new Supplier(supplierId, supplierName);

        String name = supplier.getName();

        assertEquals(supplierName, name, "The getName() method should return the correct name");
    }

    @Test
    void testToString() {
        int id = 2;
        String name = "Sample Supplier";
        Supplier supplier = new Supplier(id, name);

        String actualString = supplier.toString();

        String expectedString = "Supplier{id=2, name='Sample Supplier'}";
        assertEquals(expectedString, actualString, "The toString method should return the correct format");
    }
}