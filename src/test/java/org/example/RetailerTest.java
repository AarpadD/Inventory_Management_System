package org.example;

import java.util.Map;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RetailerTest {

    @Test
    void testConstructorWithValidInputs() {
        String location = "New York";
        int capacity = 100;

        Retailer retailer = new Retailer(location, capacity);

        assertEquals(location, retailer.getLocation(), "The location should be set correctly.");
        assertEquals(0, retailer.getAvailableStock(), "Initial available stock should be 0.");
        assertEquals(capacity, retailer.getCapacity(), "The capacity should be set correctly.");
    }

    @Test
    void testConstructorWithInvalidCapacity() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> new Retailer("Chicago", 0),
                "Constructor should throw IllegalArgumentException when capacity is 0 or negative."
        );

        assertEquals("Capacity must be a positive integer.", exception.getMessage(),
                "Exception message should match expected for invalid capacity.");
    }

    @Test
    void testSellWhenEnoughStock() {
        Retailer retailer = new Retailer("Texas", 50);

        retailer.receiveStock("Product1", 50); // Add stock
        retailer.sellProduct("Product1", 20, 5.0); // Sell stock

        assertEquals(30, retailer.getAvailableStock(),
                "Stock should decrease correctly when selling enough stock.");
    }

    @Test
    void testSellWhenNotEnoughStock() {
        Retailer retailer = new Retailer("Seattle", 30);

        retailer.receiveStock("Product1", 10); // Add stock
        retailer.sellProduct("Product1", 20, 3.0); // Try to sell more stock than available

        assertEquals(10, retailer.getAvailableStock(),
                "Stock should not change when trying to sell more than available.");
    }

    @Test
    void testIsInStock() {
        Retailer retailer = new Retailer("Seattle", 50);

        assertFalse(retailer.isInStock(),
                "isInStock should return false when initial stock is 0.");

        retailer.receiveStock("Product1", 10); // Add stock
        assertTrue(retailer.isInStock(),
                "isInStock should return true when stock is greater than 0.");
    }

    @Test
    void testReceiveStockExceedingCapacity() {
        Retailer retailer = new Retailer("Los Angeles", 100);

        retailer.receiveStock("Product1", 120); // Try adding more stock than capacity
        assertEquals(100, retailer.getAvailableStock(),
                "Stock should not exceed the retailer's capacity.");
        assertEquals(100, retailer.getStockForProduct("Product1"),
                "Specific product stock should not exceed the retailer's capacity.");
    }

    @Test
    void testReceiveStockWithInvalidInputs() {
        Retailer retailer = new Retailer("Boston", 100);

        retailer.receiveStock("default", 10); // Using invalid product name
        assertEquals(0, retailer.getAvailableStock(),
                "Available stock should remain 0 when receiving invalid product.");

        retailer.receiveStock("Product1", -5); // Providing an invalid quantity
        assertEquals(0, retailer.getAvailableStock(),
                "Available stock should remain 0 when receiving negative quantity.");
    }

    @Test
    void testSellProductWithInvalidInputs() {
        Retailer retailer = new Retailer("San Francisco", 50);

        retailer.receiveStock("Product1", 20); // Add stock
        retailer.sellProduct(null, 10, 10.0); // Null product name
        assertEquals(20, retailer.getAvailableStock(),
                "Stock should not change when selling invalid product name.");

        retailer.sellProduct("Product1", -5, 10.0); // Negative quantity
        assertEquals(20, retailer.getAvailableStock(),
                "Stock should not change when selling invalid quantity.");

        retailer.sellProduct("Product1", 5, -10.0); // Negative price
        assertEquals(20, retailer.getAvailableStock(),
                "Stock should not change when selling with invalid price.");
    }

    @Test
    void testSalesRecords() {
        Retailer retailer = new Retailer("Portland", 100);

        retailer.receiveStock("Product1", 50); // Add stock
        retailer.sellProduct("Product1", 10, 15.0); // Make a sale
        retailer.sellProduct("Product1", 5, 15.0); // Another sale

        assertEquals(35, retailer.getAvailableStock(),
                "Stock should correctly decrease after multiple sales.");
        Map<String, SalesRecord> salesRecords = retailer.getSalesRecords();
        assertTrue(salesRecords.containsKey("Product1"),
                "Sales records should include the sold product.");
        assertEquals(15, salesRecords.get("Product1").getUnitsSold(),
                "Sales records should reflect the correct total units sold.");
    }

    @Test
    void testGetStockForProduct() {
        Retailer retailer = new Retailer("Miami", 100);

        retailer.receiveStock("Product1", 30);
        assertEquals(30, retailer.getStockForProduct("Product1"),
                "Stock for product 'Product1' should match the quantity received.");

        assertEquals(0, retailer.getStockForProduct("Product2"),
                "Stock for a product not added should be 0.");
    }
}