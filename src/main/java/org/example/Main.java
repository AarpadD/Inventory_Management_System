package org.example;
import java.util.*;


public class Main {

    private static HashMap<Integer, Supplier> suppliers = new HashMap<>();
    private static HashMap<String, Product> productLookup = new HashMap<>();
    private static HashMap<String, Category> categoriesByName = new HashMap<>();
    private static List<Warehouse> warehouses = new ArrayList<>();
    private static List<Retailer> retailers = new ArrayList<>();


    public static HashMap<Integer, Supplier> getSuppliers() {
        return suppliers;
    }
    public static void setSuppliers(HashMap<Integer, Supplier> newSuppliers) {
        suppliers = newSuppliers;
    }
    public static HashMap<String, Product> getProductLookup() {
        return productLookup;
    }
    public static void setProductLookup(HashMap<String, Product> newProductLookup) {
        productLookup = newProductLookup;
    }
    public static HashMap<String, Category> getCategoriesByName() {
        return categoriesByName;
    }
    public static void setCategoriesByName(HashMap<String, Category> newCategoriesByName) {
        categoriesByName = newCategoriesByName;
    }

    public static List<Warehouse> getWarehouses() {
        return warehouses;
    }
    public static void setWarehouses(List<Warehouse> newWarehouses) {
        warehouses = newWarehouses;
    }
    public static List<Retailer> getRetailers() {
        return retailers;
    }
    public static void setRetailers(List<Retailer> newRetailers) {
        retailers = newRetailers;
    }



    public static void main(String[] args) {
        final String PRESET_PASSWORD = "qiqi10";
        Scanner scanner = new Scanner(System.in);

        DataManager dataManager = new DataManager(
                suppliers,
                productLookup,
                categoriesByName,
                warehouses,
                retailers
        );

        int attempts = 0;
        final int MAX_ATTEMPTS = 3;

        while (attempts < MAX_ATTEMPTS) {
            System.out.print("Enter password to load session: ");
            String enteredPassword = scanner.nextLine();

            if (enteredPassword.equals(PRESET_PASSWORD)) {
                //load
                dataManager.loadData();
                runMenuLoop(scanner, dataManager);
                //save
                dataManager.saveData();
                return;
            } else {
                attempts++;
                System.out.println("Incorrect password. Please try again.");
                if (attempts == MAX_ATTEMPTS) {
                    System.out.println("Maximum attempts reached. Exiting application.");
                    return;
                }
            }
        }
    }



    private static void runMenuLoop(Scanner scanner, DataManager dataManager) {
        int choice;
        while (true) {
            displayMenu();
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.next();
                displayMenu();
            }
            choice = scanner.nextInt();
            scanner.nextLine();

            try {
                switch (choice) {
                    case 1:
                        enterSupplier(scanner);
                        break;
                    case 2:
                        enterProducts(scanner);
                        break;
                    case 3:
                        enterCategory(scanner);
                        break;
                    case 4:
                        enterWarehouse(scanner);
                        break;
                    case 5:
                        enterRetailer(scanner);
                        break;
                    case 6:
                        viewSupplier(scanner);
                        break;
                    case 7:
                        listProducts();
                        break;
                    case 8:
                        groupProductsByPriceRange(scanner);
                        break;
                    case 9:
                        viewCategory(scanner);
                        break;
                    case 10:
                        viewWarehouse(scanner);
                        break;
                    case 11:
                        viewRetailer(scanner);
                        break;
                    case 12:
                        retailerSellProduct(scanner);
                        break;
                    case 13:
                        WarehouseToRetailer(scanner);
                        break;
                    case 14:
                        LowStockProducts(scanner);
                        break;
                    case 15:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice. Please choose again.");
                }
            } catch (DuplicateException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static void displayMenu() {
        System.out.println("\nMenu:");
        System.out.println("1. Enter a Supplier");
        System.out.println("2. Enter a Product");
        System.out.println("3. Enter a Category");
        System.out.println("4. Enter a Warehouse");
        System.out.println("5. Enter a Retailer");
        System.out.println("---------------------------------------------");
        System.out.println("6. View Supplier");
        System.out.println("7. List Products");
        System.out.println("8. Group Products by Price Range");
        System.out.println("9. View Category");
        System.out.println("10. View Warehouse");
        System.out.println("11. View Retailer");
        System.out.println("---------------------------------------------");
        System.out.println("12. Sell product via Retailer");
        System.out.println("13. Transfer stock from Warehouse to Retailer");
        System.out.println("14. Check Low Stock Products");
        System.out.println("15. Exit");
        System.out.print("Choose an option: ");
    }

    private static void LowStockProducts(Scanner scanner) {
        System.out.println("\nChecking for low-stock products...");

        boolean foundLowStock = false;

        // loop all warehouses
        for (Warehouse warehouse : Main.getWarehouses()) {
            System.out.println("\nWarehouse ID: " + warehouse.getId());

            List<Product> lowStockProducts = warehouse.getLowStockProducts();

            if (lowStockProducts.isEmpty()) {
                System.out.println("No low-stock products in this warehouse.");
            } else {
                foundLowStock = true;
                System.out.println("Low-Stock Products:");
                for (Product product : lowStockProducts) {
                    System.out.println("  - Name: " + product.getName() +
                            ", Quantity: " + product.getQuantity() +
                            ", Threshold: 10");
                }
            }
        }
        if (!foundLowStock) {
            return;
        }

        System.out.print("\nDo you want to restock any of the products? (y/n): ");
        String response = scanner.nextLine().trim().toLowerCase();

        if ("y".equals(response)) {
            System.out.print("Enter the name of the product to restock: ");
            String productName = scanner.nextLine().trim();

            Warehouse warehouseWithProduct = null;
            for (Warehouse warehouse : Main.getWarehouses()) {
                int stock = warehouse.getProductStock(productName);
                if (stock != -1) {
                    warehouseWithProduct = warehouse;
                    break;
                }
            }

            if (warehouseWithProduct == null) {
                System.out.println("Product '" + productName + "' was not found in any warehouse.");
                return;
            }

            System.out.print("Enter the quantity to restock: ");
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a valid quantity.");
                scanner.next();
            }
            int quantity = scanner.nextInt();
            scanner.nextLine();

            //restock
            warehouseWithProduct.restock(productName, quantity);

        } else {
            System.out.println("No products were restocked.");
        }
    }


    private static void enterSupplier(Scanner scanner) throws DuplicateException {
        System.out.print("Enter supplier ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        if (suppliers.containsKey(id)) {
            throw new DuplicateException("Supplier with this ID already exists.");
        }

        System.out.print("Enter supplier name: ");
        String name = scanner.nextLine();
        suppliers.put(id, new Supplier(id, name));
        System.out.println("Supplier added successfully.");
    }

    private static void enterProducts(Scanner scanner) throws DuplicateException {
        System.out.print("Enter product name: ");
        String name = scanner.nextLine();

        if (productLookup.containsKey(name)) {
            System.out.println("Product with this name already exists.");
            return;
        }

        System.out.print("Enter quantity: ");
        int quantity = scanner.nextInt();
        System.out.print("Enter price: ");
        double price = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter category: ");
        String categoryName = scanner.nextLine();

        Product product = new Product(name, quantity, price, categoryName);
        productLookup.put(name, product);

        Category category = categoriesByName.get(categoryName);
        if (category != null) {
            category.addProduct(product);
            System.out.println("Product added to category.");

            // Also link to the warehouse of the category
            int warehouseId = category.getWarehouseId();
            Warehouse warehouse = warehouses.stream()
                    .filter(w -> w.getId() == warehouseId)
                    .findFirst()
                    .orElse(null);
            if (warehouse != null) {
                warehouse.addProduct(product); //add to warehouse
            } else {
                System.out.println("Warehouse not found for category.");
            }

        } else {
            System.out.println("Category not found. Product added without category linkage.");
        }
    }

    private static void enterCategory(Scanner scanner) throws DuplicateException {
        System.out.print("Enter category name: ");
        String name = scanner.nextLine();

        if (categoriesByName.containsKey(name)) {
            throw new DuplicateException("Category with this name already exists.");
        }

        System.out.print("Enter supplier ID: ");
        int supplierId = scanner.nextInt();

        System.out.print("Enter warehouse ID: ");
        int warehouseId = scanner.nextInt();
        scanner.nextLine();

        Supplier supplier = suppliers.get(supplierId);
        if (supplier == null) {
            System.out.println("Supplier not found.");
            return;
        }

        Category category = new Category(name, supplier, warehouseId);
        categoriesByName.put(name, category);

       //link to warehouse
        Warehouse warehouse = warehouses.stream()
                .filter(w -> w.getId() == warehouseId)
                .findFirst()
                .orElse(null);
        if (warehouse != null) {
            warehouse.addCategory(category);
            System.out.println("Category added successfully to the main.Warehouse.");
        } else {
            System.out.println("Warehouse not found. Category added without main.Warehouse linkage.");
        }
    }

    private static void listProducts() {
        System.out.println("Listing all products:");

        productLookup.values().forEach(product -> {
            System.out.println("Product Name: " + product.getName());
            System.out.println("Category: " + product.getCategory());
            System.out.println("Quantity: " + product.getQuantity());
            System.out.println("Price: " + product.getPrice());
            System.out.println();
        });
    }


    private static void groupProductsByPriceRange(Scanner scanner) {
        System.out.println("Enter price ranges: ");
        String input = scanner.nextLine();

        String[] ranges = input.split(",");
        List<String> rangeList = new ArrayList<>(Arrays.asList(ranges));

        Map<String, List<Product>> groupedProducts = new TreeMap<>();

        for (Product product : productLookup.values()) {
            double price = product.getPrice();
            for (String range : rangeList) {
                if (range.contains("-")) {
                    String[] bounds = range.split("-");
                    double min = Double.parseDouble(bounds[0]);
                    double max = Double.parseDouble(bounds[1]);
                    if (price >= min && price < max) {
                        groupedProducts.computeIfAbsent(range, k -> new ArrayList<>()).add(product);
                        break;
                    }
                }
            }
        }

        if (groupedProducts.isEmpty()) {
            System.out.println("No products found for the given price ranges.");
            return;
        }

        groupedProducts.forEach((range, products) -> {
            System.out.println("Price range: " + range);
            for (Product product : products) {
                System.out.println("  Product Name: " + product.getName());
                System.out.println("  Category: " + product.getCategory());
                System.out.println("  Quantity: " + product.getQuantity());
                System.out.println("  Price: " + product.getPrice());
                System.out.println();
            }
        });
    }


    private static void enterWarehouse(Scanner scanner) throws DuplicateException {
        System.out.print("Enter warehouse ID: ");
        int id = scanner.nextInt();

        System.out.print("Enter capacity: ");
        int capacity = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        warehouses.add(new Warehouse(id, capacity));
        System.out.println("Warehouse added successfully.");
    }

    private static void enterRetailer(Scanner scanner) throws DuplicateException {
        System.out.print("Enter retailer location: ");
        String location = scanner.nextLine().trim();


        if (location.isEmpty()) {
            System.out.println("Error: Retailer location cannot be empty. Please try again.");
            return;
        }

        boolean exists = retailers.stream()
                .anyMatch(retailer -> retailer.getLocation().equalsIgnoreCase(location));
        if (exists) {
            throw new DuplicateException("Error: A retailer with the location '" + location + "' already exists.");
        }

        System.out.print("Enter retailer capacity: ");
        int capacity = scanner.nextInt();

        if (capacity <= 0) {
            System.out.println("Error: Retailer capacity must be a positive number. Please try again.");
            return;
        }
        scanner.nextLine();

        retailers.add(new Retailer(location, capacity));
        System.out.println("Retailer added successfully.");
    }


    private static void viewCategory(Scanner scanner) {
        System.out.print("Enter category name: ");
        String name = scanner.nextLine();

        Category category = categoriesByName.get(name);
        if (category == null) {
            System.out.println("Category not found.");
            return;
        }

        System.out.println("Category Name: " + category.getCategoryName());
        System.out.println("Supplier Name: " + category.getSupplier().getName());
        System.out.println("Warehouse ID: " + category.getWarehouseId());
        System.out.println("Total Quantity: " + category.getTotalQuantity());

        System.out.println("Products:");
        for (Product product : category.getProducts()) {
            System.out.println("  Product Name: " + product.getName());
            System.out.println("  Quantity: " + product.getQuantity());
            System.out.println("  Price: " + product.getPrice());
            System.out.println();
        }
    }

    private static void viewSupplier(Scanner scanner) {
        System.out.print("Enter supplier ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Supplier supplier = suppliers.get(id);
        if (supplier == null) {
            System.out.println("Supplier not found.");
            return;
        }

        System.out.println("Supplier ID: " + supplier.getId());
        System.out.println("Supplier Name: " + supplier.getName());
    }



    private static void viewWarehouse(Scanner scanner) {
        System.out.println("Enter warehouse ID:");
        int warehouseId = scanner.nextInt();

        Warehouse warehouse = null;
        for (Warehouse wh : warehouses) {
            if (wh.getId() == warehouseId) {
                warehouse = wh;
                break;
            }
        }

        if (warehouse == null) {
            System.out.println("Warehouse with ID " + warehouseId + " not found.");
            return;
        }

        System.out.println("Warehouse ID: " + warehouse.getId());
        System.out.println("Capacity: " + warehouse.getCapacity());
        System.out.println("Total Stock: " + warehouse.getTotalStock());

        System.out.println("Categories:");
        for (Category category : warehouse.getCategories().values()) {
            System.out.println("  Category: " + category.getCategoryName());
            System.out.println("    Supplier: " + category.getSupplier().getName());
            System.out.println("    Warehouse ID: " + category.getWarehouseId());
            System.out.println("    Total Quantity: " + category.getTotalQuantity());

            System.out.println("    Products:");
            for (Product product : category.getProducts()) {
                System.out.println("      Product Name: " + product.getName());
                System.out.println("      Quantity: " + product.getQuantity());
            }
        }
    }



    private static void viewRetailer(Scanner scanner) {
        System.out.println("Enter retailer location to display details:");
        String location = scanner.nextLine();

        Retailer retailer = retailers.stream()
                .filter(r -> r.getLocation().equalsIgnoreCase(location))
                .findFirst()
                .orElse(null);

        if (retailer == null) {
            System.out.println("Retailer not found!");
            return;
        }

        System.out.println("\nRetailer Information:");
        System.out.println("  Location: " + retailer.getLocation());
        System.out.println("  Capacity: " + retailer.getCapacity());
        System.out.println("  Total Stock: " + retailer.getAvailableStock());

        System.out.println("  Products:");
        Map<String, Integer> stockByProduct = retailer.getStockByProduct();
        if (stockByProduct.isEmpty()) {
            System.out.println("    No products in stock.");
        } else {
            for (Map.Entry<String, Integer> entry : stockByProduct.entrySet()) {
                System.out.println("    Product Name: " + entry.getKey());
                System.out.println("    Quantity: " + entry.getValue());
            }
        }

        System.out.println("\n");
        retailer.viewSalesRecords();
        System.out.println("\n");
        retailer.displaySalesSummary();
    }

    private static void retailerSellProduct(Scanner scanner) {
        System.out.print("Enter retailer location: ");
        String location = scanner.nextLine().trim();

        Retailer retailer = retailers.stream()
                .filter(r -> r.getLocation().equalsIgnoreCase(location))
                .findFirst()
                .orElse(null);

        if (retailer == null) {
            System.out.println("Error: Retailer not found! Please check the location.");
            return;
        }

        System.out.print("Enter product name: ");
        String productName = scanner.nextLine().trim();

        System.out.print("Enter quantity to sell: ");
        int quantity = scanner.nextInt();

        System.out.print("Enter price per unit: ");
        double price = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        try {
            //sell
            retailer.sellProduct(productName, quantity, price);
            System.out.println("Sold " + quantity + " units of '" + productName + "' successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void WarehouseToRetailer(Scanner scanner) {
        System.out.println("Enter warehouse ID:");
        int warehouseId = scanner.nextInt();
        scanner.nextLine();

        Warehouse warehouse = warehouses.stream()
                .filter(w -> w.getId() == warehouseId)
                .findFirst()
                .orElse(null);

        if (warehouse == null) {
            System.out.println("Warehouse not found!");
            return;
        }

        System.out.println("Enter retailer location:");
        String location = scanner.nextLine();

        Retailer retailer = retailers.stream()
                .filter(r -> r.getLocation().equalsIgnoreCase(location))
                .findFirst()
                .orElse(null);

        if (retailer == null) {
            System.out.println("Retailer not found!");
            return;
        }

        System.out.print("Enter product name: ");
        String productName = scanner.nextLine().trim();

        System.out.print("Enter quantity to transfer: ");
        int quantity = scanner.nextInt();
        scanner.nextLine();

        try {
            warehouse.sellProductToRetailer(productName, quantity, retailer);
            System.out.println("Successfully transferred " + quantity + " units of '" + productName +
                    "' from warehouse ID " + warehouseId + " to retailer at " + location + ".");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


}
