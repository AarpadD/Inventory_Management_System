package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RetailerTest {

    @Test
    void testConstructorWithValidInputs() {
        String location = "New York";
        int availableStock = 100;

        Retailer retailer = new Retailer(location, availableStock);

        assertEquals(location, retailer.getLocation(), "The location should be set correctly.");
        assertEquals(availableStock, retailer.getAvailableStock(), "The available stock should be set correctly.");
    }


    @Test
    void testSellWhenEnoughStock() {
        Retailer retailer = new Retailer("Chicago", 50);

        retailer.sell(20);

        assertEquals(30, retailer.getAvailableStock(), "Stock should decrease correctly after selling.");
    }


    @Test
    void testSellWhenNotEnoughStock() {
        Retailer retailer = new Retailer("Texas", 10);

        retailer.sell(20); //sell more stock than available

        assertEquals(10, retailer.getAvailableStock(), "Stock should not change when selling more than available.");
    }


    @Test
    void testIsInStock() {
        Retailer retailer = new Retailer("Seattle", 0);

        assertFalse(retailer.isInStock(), "isInStock should return false when initial stock is 0.");

        retailer.sell(-1);

        assertFalse(retailer.isInStock(), "isInStock should remain false after an invalid sell operation (stock stays 0).");
    }
}