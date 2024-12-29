package org.example;

import java.util.List;
public interface Restockable {
    void restock(String productName, int amount);
    List<Product> getLowStockProducts();
}
