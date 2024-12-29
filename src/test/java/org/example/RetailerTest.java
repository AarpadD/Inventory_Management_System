package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RetailerTest {

    @Test
    void testConstructorWithValidInputs() {
        // Arrange
        String location = "New York";
        int availableStock = 100;

        // Act
        Retailer retailer = new Retailer(location, availableStock);

        // Assert
        assertEquals(location, retailer.getLocation(), "The location should be set correctly.");
        assertEquals(availableStock, retailer.getAvailableStock(), "The available stock should be set correctly.");
    }


    @Test
    void testSellWhenEnoughStock() {
        // Arrange
        Retailer retailer = new Retailer("Chicago", 50);

        // Act
        retailer.sell(20);

        // Assert
        assertEquals(30, retailer.getAvailableStock(), "Stock should decrease correctly after selling.");
    }

    // Test for sell function when there isn’t enough stock
    @Test
    void testSellWhenNotEnoughStock() {
        // Arrange
        Retailer retailer = new Retailer("Texas", 10);

        // Act
        retailer.sell(20); // Attempt to sell more stock than available

        // Assert
        assertEquals(10, retailer.getAvailableStock(), "Stock should not change when selling more than available.");
    }

    // Test for `isInStock` functionality

    @Test
    void testIsInStock() {
        // Arrange
        Retailer retailer = new Retailer("Seattle", 0);

        // Act & Assert: Stock is initially 0
        assertFalse(retailer.isInStock(), "isInStock should return false when initial stock is 0.");

        // Act: Attempt an invalid operation (sell -1). This should not affect stock.
        retailer.sell(-1);

        // Assert: Stock is still 0, so isInStock should still return false
        assertFalse(retailer.isInStock(), "isInStock should remain false after an invalid sell operation (stock stays 0).");
    }
}