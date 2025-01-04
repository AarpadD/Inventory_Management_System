package org.example;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class DataManager {
    private static final String SUPPLIERS_FILE = "Supplier.txt";
    private static final String PRODUCTS_FILE = "Product.txt";
    private static final String CATEGORIES_FILE = "Category.txt";
    private static final String WAREHOUSES_FILE = "Warehouse.txt";
    private static final String RETAILERS_FILE = "Retailer.txt";

    // Instance variables
    private Map<Integer, Supplier> suppliers;
    private Map<String, Product> productLookup;
    private Map<String, Category> categoriesByName;
    private List<Warehouse> warehouses;
    private List<Retailer> retailers;

    // Constructor
    public DataManager(Map<Integer, Supplier> suppliers, Map<String, Product> productLookup,
                       Map<String, Category> categoriesByName, List<Warehouse> warehouses,
                       List<Retailer> retailers) {
        this.suppliers = suppliers;
        this.productLookup = productLookup;
        this.categoriesByName = categoriesByName;
        this.warehouses = warehouses;
        this.retailers = retailers;
    }

    // Load data from files
    public void loadData() {
        try {
            loadSuppliers();
            loadProducts();
            loadCategories();
            loadWarehouses();
            loadRetailers();
            System.out.println("Data loaded successfully.");
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Save data to files
    public void saveData() {
        try {
            saveSuppliers();
            saveProducts();
            saveCategories();
            saveWarehouses();
            saveRetailers();
            System.out.println("Data saved successfully.");
        } catch (Exception e) {
            System.out.println("Error saving data: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void loadSuppliers() {
        try (BufferedReader reader = new BufferedReader(new FileReader(SUPPLIERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length == 2) {
                    try {
                        int id = Integer.parseInt(parts[0]);
                        String name = parts[1];

                        if (id > 0 && name != null && !name.isBlank()) {
                            suppliers.put(id, new Supplier(id, name));
                        } else {
                            System.out.println("Invalid supplier data: " + line);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid supplier ID format: " + parts[0]);
                    }
                } else {
                    System.out.println("Invalid supplier line: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading supplier: " + e.getMessage());
        }
    }


    private void saveSuppliers() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SUPPLIERS_FILE))) {
            if (suppliers.isEmpty()) {
                System.out.println("No supplier to save. The file will be empty.");
            } else {
                for (Supplier supplier : suppliers.values()) {
                    writer.write(supplier.getId() + "," + supplier.getName());
                    writer.newLine();
                }
                System.out.println("Suppliers saved successfully.");
            }
        } catch (IOException e) {
            System.out.println("Error saving supplier: " + e.getMessage());
        }
    }


    private void loadProducts() {
        try (BufferedReader reader = new BufferedReader(new FileReader(PRODUCTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    try {
                        String name = parts[0];
                        int quantity = Integer.parseInt(parts[1]);
                        double price = Double.parseDouble(parts[2]);
                        String category = parts[3];

                        if (!name.isBlank() && quantity >= 0 && price >= 0.0 && !category.isBlank()) {
                            productLookup.put(name, new Product(name, quantity, price, category));
                        } else {
                            System.out.println("Invalid product data: " + line);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Data formatting issue for product: " + line);
                    }
                } else {
                    System.out.println("Invalid product line: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading products: " + e.getMessage());
        }
    }


    private void saveProducts() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PRODUCTS_FILE))) {
            if (productLookup.isEmpty()) {
                System.out.println("No product to save. The file will be empty.");
            } else {
                for (Map.Entry<String, Product> entry : productLookup.entrySet()) {
                    Product product = entry.getValue(); // Accessing the Product object
                    writer.write(product.getName() + "," + product.getQuantity() + ","
                            + product.getPrice() + "," + product.getCategory());
                    writer.newLine();
                }
                System.out.println("Products saved successfully.");
            }
        } catch (IOException e) {
            System.out.println("Error saving product: " + e.getMessage());
        }
    }


    private void loadCategories() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CATEGORIES_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 3) {
                    try {
                        String categoryName = parts[0];
                        int supplierId = Integer.parseInt(parts[1]);
                        int warehouseId = Integer.parseInt(parts[2]);

                        Supplier supplier = suppliers.get(supplierId);
                        if (supplier == null) {
                            System.out.println("Warning: Supplier with ID " + supplierId + " not found.");
                            continue;
                        }

                        List<Product> products = Arrays.stream(parts, 3, parts.length)
                                .map(productLookup::get)
                                .filter(Objects::nonNull)
                                .collect(Collectors.toList());

                        categoriesByName.put(categoryName, new Category(categoryName, supplier, warehouseId,
                                products.toArray(new Product[0])));
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid formatting in category line: " + line);
                    }
                } else {
                    System.out.println("Invalid category line: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading category: " + e.getMessage());
        }
    }


    private void saveCategories() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CATEGORIES_FILE))) {
            if (categoriesByName.isEmpty()) {
                System.out.println("No category to save. The file will be empty.");
            } else {
                for (Category category : categoriesByName.values()) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(category.getCategoryName()).append(",")
                            .append(category.getSupplier().getId()).append(",")
                            .append(category.getWarehouseId());

                    for (Product product : category.getProducts()) {
                        sb.append(",").append(product.getName());
                    }
                    writer.write(sb.toString());
                    writer.newLine();
                }
                System.out.println("Categories saved successfully.");
            }
        } catch (IOException e) {
            System.out.println("Error saving category: " + e.getMessage());
        }
    }



    private void loadWarehouses() {
        try (BufferedReader reader = new BufferedReader(new FileReader(WAREHOUSES_FILE))) {
            String line;
            Warehouse currentWarehouse = null;

            while ((line = reader.readLine()) != null) {
                try {
                    String[] parts = line.split(",");

                    if (parts.length == 2) {
                        int id = Integer.parseInt(parts[0]);
                        int capacity = Integer.parseInt(parts[1]);
                        currentWarehouse = new Warehouse(id, capacity);
                        warehouses.add(currentWarehouse);

                    }
                    else if (parts.length > 3 && currentWarehouse != null) {
                        String categoryName = parts[0];
                        Category category = categoriesByName.get(categoryName);

                        if (category != null && category.getWarehouseId() == currentWarehouse.getId()) {
                            currentWarehouse.addCategory(category);
                        } else {
                            System.out.println("Category mismatch for " + categoryName);
                        }
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Error parsing warehouse/category data: " + line);
                }
            }

            for (Warehouse warehouse : warehouses) {
                for (Category category : warehouse.getCategories().values()) {
                    for (Product product : category.getProducts()) {
                        warehouse.addProduct(product);
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("Error loading warehouses: " + e.getMessage());
        }
    }


    private void saveWarehouses() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(WAREHOUSES_FILE))) {
            if (warehouses.isEmpty()) {
                System.out.println("No warehouse to save. The file will be empty.");
            } else {
                for (Warehouse warehouse : warehouses) {
                    writer.write(warehouse.getId() + "," + warehouse.getCapacity());
                    writer.newLine();

                    for (Category category : warehouse.getCategories().values()) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(category.getCategoryName()).append(",")
                                .append(category.getSupplier().getId()).append(",")
                                .append(warehouse.getId());

                        for (Product product : category.getProducts()) {
                            sb.append(",").append(product.getName());
                        }

                        writer.write(sb.toString());
                        writer.newLine();
                    }
                }
                System.out.println("Warehouses saved successfully.");
            }
        } catch (IOException e) {
            System.out.println("Error saving warehouses: " + e.getMessage());
        }
    }


    private void loadRetailers() {
        try (BufferedReader reader = new BufferedReader(new FileReader(RETAILERS_FILE))) {
            String line;
            Retailer currentRetailer = null;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("  Stock:")) {
                    String[] stockParts = line.substring(8).split(",");
                    if (stockParts.length == 2 && currentRetailer != null) {
                        String productName = stockParts[0].trim();
                        int quantity = Integer.parseInt(stockParts[1].trim());
                        currentRetailer.receiveStock(productName, quantity);
                    }
                } else if (line.startsWith("  Sales:")) {
                    String[] salesParts = line.substring(8).split(",");
                    if (salesParts.length == 3 && currentRetailer != null) {
                        String productName = salesParts[0].trim();
                        int unitsSold = Integer.parseInt(salesParts[1].trim());
                        double totalRevenue = Double.parseDouble(salesParts[2].trim());

                        SalesRecord record = currentRetailer.getSalesRecords()
                                .getOrDefault(productName, new SalesRecord(productName));
                        record.addSale(unitsSold, totalRevenue / unitsSold);
                        currentRetailer.getSalesRecords().put(productName, record);
                    }
                } else {
                    String[] parts = line.split(",");
                    if (parts.length == 3) {
                        String location = parts[0].trim();
                        int capacity = Integer.parseInt(parts[1].trim());
                        currentRetailer = new Retailer(location, capacity);
                        retailers.add(currentRetailer);
                    }
                }
            }

            //System.out.println("Retailers loaded successfully.");
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error loading retailers: " + e.getMessage());
        }
    }



    private void saveRetailers() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RETAILERS_FILE))) {
            if (retailers.isEmpty()) {
                System.out.println("No retailers to save. The file will be empty.");
            } else {
                for (Retailer retailer : retailers) {
                    writer.write(retailer.getLocation() + "," + retailer.getCapacity() + "," + retailer.getAvailableStock());
                    writer.newLine();

                    for (Map.Entry<String, Integer> stockEntry : retailer.getStockByProduct().entrySet()) {
                        writer.write("  Stock:" + stockEntry.getKey() + "," + stockEntry.getValue());
                        writer.newLine();
                    }

                    for (Map.Entry<String, SalesRecord> salesEntry : retailer.getSalesRecords().entrySet()) {
                        SalesRecord record = salesEntry.getValue();
                        writer.write("  Sales:" + salesEntry.getKey() + "," + record.getUnitsSold() + "," + record.getTotalRevenue());
                        writer.newLine();
                    }
                }
                System.out.println("Retailers saved successfully.");
            }
        } catch (IOException e) {
            System.out.println("Error saving retailers: " + e.getMessage());
        }
    }
}