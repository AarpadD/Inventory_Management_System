package org.example;
import java.util.*;
import java.util.stream.Collectors;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    private static final String SUPPLIERS_FILE = "Supplier.txt";
    private static final String PRODUCTS_FILE = "Product.txt";
    private static final String CATEGORIES_FILE = "Category.txt";
    private static final String WAREHOUSES_FILE = "Warehouse.txt";
    private static final String RETAILERS_FILE = "Retailer.txt";

    private static Map<Integer, Supplier> suppliers = new HashMap<>();
    private static Map<String, Product> productLookup = new HashMap<>();
    private static Map<String, Category> categoriesByName = new TreeMap<>();
    private static List<Warehouse> Warehouses = new ArrayList<>();
    private static List<Retailer> Retailers = new ArrayList<>();

    public static Map<Integer, Supplier> getSuppliers() {
        return suppliers;
    }

    public static Map<String, Product> getProductLookup() {
        return productLookup;
    }

    public static Map<String, Category> getCategoriesByName() {
        return categoriesByName;
    }

    public static List<Warehouse> getWarehouses() {
        return Warehouses;
    }

    public static List<Retailer> getRetailers() {
        return Retailers;
    }

    public static void setSuppliers(Map<Integer, Supplier> newSuppliers) {
        suppliers = newSuppliers;
    }

    public static void setProductLookup(Map<String, Product> newProductLookup) {
        productLookup = newProductLookup;
    }

    public static void setCategoriesByName(Map<String, Category> newCategoriesByName) {
        categoriesByName = newCategoriesByName;
    }

    public static void setWarehouses(List<Warehouse> newWarehouses) {
        Warehouses = newWarehouses;
    }

    public static void setRetailers(List<Retailer> newRetailers) {
        Retailers = newRetailers;
    }



    public static void main(String[] args) {
        final String PRESET_PASSWORD = "qiqi10";
        Scanner scanner = new Scanner(System.in);

        int attempts = 0;
        final int MAX_ATTEMPTS = 3;

        while (attempts < MAX_ATTEMPTS) {
            System.out.print("Enter password to load session: ");
            String enteredPassword = scanner.nextLine();

            if (enteredPassword.equals(PRESET_PASSWORD)) {
                loadData();
                runMenuLoop(scanner);
                saveData();
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

    private static void runMenuLoop(Scanner scanner) {
        int choice;
        while (true) {
            displayMenu();
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.next();
                displayMenu();
            }
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

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
                        listProducts();
                        break;
                    case 5:
                        sortProductsByPrice();
                        break;
                    case 6:
                        sortCategoriesByTotalQuantity();
                        break;
                    case 7:
                        groupProductsByPriceRange();
                        break;
                    case 8:
                        enterWarehouse(scanner);
                        break;
                    case 9:
                        enterRetailer(scanner);
                        break;
                    case 10:
                        viewCategoryWithProducts(scanner);
                        break;
                    case 11:
                        viewSupplier(scanner);
                        break;
                    case 12:
                        viewWarehouseCategoriesAndStock();
                        break;
                    case 13:
                        viewWarehouseById(scanner);
                        break;
                    case 14:
                        retailerSellProduct(scanner);
                        break;
                    case 15:
                        displayRetailer(scanner);
                        break;
                    case 16:
                        transferStockFromWarehouseToRetailer(scanner);
                        break;
                    case 17:
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
        System.out.println("2. Enter Products");
        System.out.println("3. Enter a Category");
        System.out.println("4. List Products");
        System.out.println("5. Sort Products by Price");
        System.out.println("6. Sort Categories by Total Quantity");
        System.out.println("7. Group Products by Price Range");
        System.out.println("8. Enter a Warehouse");
        System.out.println("9. Enter a Retailer");
        System.out.println("10. View Category with Products");
        System.out.println("11. View Supplier");
        System.out.println("12. View Warehouse Categories and Stock");
        System.out.println("13. View Warehouse by ID");
        System.out.println("14. Sell product via Retailer");
        System.out.println("15. View Retailer");
        System.out.println("16. Transfer stock from Warehouse to Retailer");
        System.out.println("17. Exit");
        System.out.print("Choose an option: ");
    }


    private static void loadData() {
        try {
            loadSuppliers();
            loadProducts();
            loadCategories();
            loadWarehouses();
            loadRetailers();
            System.out.println("Data loaded successfully.");
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    private static void saveData() {
        try {
            saveSuppliers();
            saveProducts();
            saveCategories();
            saveWarehouses();
            saveRetailers();
            System.out.println("Data saved successfully.");
        } catch (Exception e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    private static void loadSuppliers() {
        try (BufferedReader reader = new BufferedReader(new FileReader(SUPPLIERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    int id = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    suppliers.put(id, new Supplier(id, name));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading suppliers: " + e.getMessage());
        }
    }

    private static void loadProducts() {
        try (BufferedReader reader = new BufferedReader(new FileReader(PRODUCTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String name = parts[0];
                    int quantity = Integer.parseInt(parts[1]);
                    double price = Double.parseDouble(parts[2]);
                    String category = parts[3];
                    productLookup.put(name, new Product(name, quantity, price, category));

                    // Always log the loaded product details
                    System.out.println("Loaded product: Name=" + name + ", Quantity=" + quantity +
                            ", Price=" + price + ", Category=" + category);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading products: " + e.getMessage());
        }
    }

    private static void loadCategories() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CATEGORIES_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 3) {
                    String categoryName = parts[0];
                    int supplierId = Integer.parseInt(parts[1]);
                    int warehouseId = Integer.parseInt(parts[2]);
                    Supplier supplier = suppliers.get(supplierId);

                    List<Product> products = Arrays.stream(parts, 3, parts.length)
                            .map(productLookup::get)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());

                    categoriesByName.put(categoryName, new Category(categoryName, supplier, warehouseId,
                            products.toArray(new Product[0])));

                    // Always log the loaded category details
                    System.out.println("Loaded category: Name=" + categoryName + ", Supplier ID=" + supplierId +
                            ", Warehouse ID=" + warehouseId + ", Products=" + products.size());
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading categories: " + e.getMessage());
        }
    }

    private static void loadWarehouses() {
        try (BufferedReader reader = new BufferedReader(new FileReader(WAREHOUSES_FILE))) {
            String line;
            Warehouse currentWarehouse = null;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                // First line contains warehouse details
                if (parts.length == 2) {
                    int id = Integer.parseInt(parts[0]);
                    int capacity = Integer.parseInt(parts[1]);
                    currentWarehouse = new Warehouse(id, capacity);
                    Warehouses.add(currentWarehouse);

                    // Always log the warehouse details
                    System.out.println("Loaded warehouse: ID=" + id + ", Capacity=" + capacity);
                }
                // Subsequent lines contain categories and products
                else if (parts.length > 3) {
                    String categoryName = parts[0];
                    int supplierId = Integer.parseInt(parts[1]);
                    int warehouseId = Integer.parseInt(parts[2]);

                    Supplier supplier = suppliers.get(supplierId);
                    if (supplier != null && currentWarehouse != null && currentWarehouse.getId() == warehouseId) {
                        List<Product> products = Arrays.stream(parts, 3, parts.length)
                                .map(productLookup::get)
                                .filter(Objects::nonNull)
                                .collect(Collectors.toList());

                        Category category = new Category(categoryName, supplier, warehouseId,
                                products.toArray(new Product[0]));
                        currentWarehouse.addCategory(category);

                        // Always log the loaded category within warehouse
                        System.out.println("Loaded category in warehouse: Category=" + categoryName +
                                ", Warehouse ID=" + warehouseId + ", Products=" + products.size());
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading warehouses: " + e.getMessage());
        }
    }


    private static void loadRetailers() {
        try (BufferedReader reader = new BufferedReader(new FileReader(RETAILERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String location = parts[0];
                    int availableStock = Integer.parseInt(parts[1]);
                    Retailers.add(new Retailer(location, availableStock));

                    // Always log the loaded retailer details
                    System.out.println("Loaded retailer: Location=" + location + ", Available Stock=" + availableStock);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading retailers: " + e.getMessage());
        }
    }

    private static void saveSuppliers() {
        List<String> lines = suppliers.values().stream()
                .map(Supplier -> Supplier.getId() + "," + Supplier.getName())
                .collect(Collectors.toList());
        FILE.writeToFile(SUPPLIERS_FILE, lines);
    }

    private static void saveProducts() {
        List<String> lines = productLookup.values().stream()
                .map(product -> product.getName() + "," + product.getQuantity() + "," +
                        product.getPrice() + "," + product.getCategory())
                .collect(Collectors.toList());
        FILE.writeToFile(PRODUCTS_FILE, lines);
    }

    private static void saveCategories() {
        List<String> lines = categoriesByName.values().stream()
                .map(Category -> {
                    StringBuilder sb = new StringBuilder();
                    sb.append(Category.getCategoryName()).append(",")
                            .append(Category.getSupplier().getId()).append(",")
                            .append(Category.getWarehouseId());
                    for (Product product : Category.getProducts()) {
                        sb.append(",").append(product.getName());
                    }
                    return sb.toString();
                })
                .collect(Collectors.toList());
        FILE.writeToFile(CATEGORIES_FILE, lines);
    }

    private static void saveWarehouses() {
        List<String> lines = new ArrayList<>();

        for (Warehouse warehouse : Warehouses) {
            // Save main.Warehouse details
            lines.add(warehouse.getId() + "," + warehouse.getCapacity());

            // Save categories and their products
            warehouse.getCategories().forEach((name, Category) -> {
                StringBuilder sb = new StringBuilder();
                sb.append(Category.getCategoryName()).append(",")
                        .append(Category.getSupplier().getId()).append(",")
                        .append(warehouse.getId());

                for (Product product : Category.getProducts()) {
                    sb.append(",").append(product.getName());
                }

                lines.add(sb.toString());
            });
        }

        try {
            FILE.writeToFile(WAREHOUSES_FILE, lines);
        } catch (Exception e) {
            System.out.println("Error saving warehouses: " + e.getMessage());
        }
    }


    private static void saveRetailers() {
        List<String> lines = Retailers.stream()
                .map(Retailer -> Retailer.getLocation() + "," + Retailer.getAvailableStock())
                .collect(Collectors.toList());
        FILE.writeToFile(RETAILERS_FILE, lines);
    }


    private static void enterSupplier(Scanner scanner) throws DuplicateException {
        System.out.print("Enter main.Supplier ID: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        if (suppliers.containsKey(id)) {
            throw new DuplicateException("Supplier with this ID already exists.");
        }

        System.out.print("Enter main.Supplier name: ");
        String name = scanner.nextLine();
        suppliers.put(id, new Supplier(id, name));
        System.out.println("Supplier added successfully.");
    }

    private static void enterProducts(Scanner scanner) throws DuplicateException {
        System.out.print("Enter main.Product name: ");
        String name = scanner.nextLine();

        if (productLookup.containsKey(name)) {
            System.out.println("Product with this name already exists.");
            return;
        }

        System.out.print("Enter quantity: ");
        int quantity = scanner.nextInt();

        System.out.print("Enter price: ");
        double price = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter main.Category: ");
        String categoryName = scanner.nextLine();

        Product product = new Product(name, quantity, price, categoryName);
        productLookup.put(name, product);

        Category category = categoriesByName.get(categoryName);
        if (category != null) {
            category.addProduct(product);
            System.out.println("Product added successfully to the main.Category.");

            // Also add the main.Product to the main.Warehouse that contains this main.Category
            int warehouseId = category.getWarehouseId();
            Warehouse warehouse = Warehouses.stream()
                    .filter(w -> w.getId() == warehouseId)
                    .findFirst()
                    .orElse(null);
            if (warehouse != null) {
                warehouse.addProduct(product);
            } else {
                System.out.println("Warehouse not found for the main.Product.");
            }

        } else {
            System.out.println("Category not found. Product added without main.Category linkage.");
        }
    }

    private static void enterCategory(Scanner scanner) throws DuplicateException {
        System.out.print("Enter main.Category name: ");
        String name = scanner.nextLine();

        if (categoriesByName.containsKey(name)) {
            throw new DuplicateException("Category with this name already exists.");
        }

        System.out.print("Enter main.Supplier ID: ");
        int supplierId = scanner.nextInt();

        System.out.print("Enter main.Warehouse ID: ");
        int warehouseId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        Supplier supplier = suppliers.get(supplierId);
        if (supplier == null) {
            System.out.println("Supplier not found.");
            return;
        }

        Category category = new Category(name, supplier, warehouseId);
        categoriesByName.put(name, category);

        // Link main.Category to the corresponding main.Warehouse
        Warehouse warehouse = Warehouses.stream()
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
        productLookup.values().forEach(System.out::println);
    }

    private static void sortProductsByPrice() {
        List<Product> Products = new ArrayList<>(productLookup.values());
        Products.sort(Comparator.comparingDouble(Product::getPrice));
        Products.forEach(System.out::println);
    }

    private static void sortCategoriesByTotalQuantity() {
        List<Category> categories = new ArrayList<>(categoriesByName.values());
        categories.sort(Comparator.naturalOrder());
        categories.forEach(System.out::println);
    }

    private static void groupProductsByPriceRange() {
        Map<String, List<Product>> groupedProducts = new TreeMap<>();

        for (Product product : productLookup.values()) {
            String range = getPriceRange(product.getPrice());
            groupedProducts.computeIfAbsent(range, k -> new ArrayList<>()).add(product);
        }

        groupedProducts.forEach((range, products) -> {
            System.out.println("Price range: " + range);
            products.forEach(System.out::println);
        });
    }

    private static String getPriceRange(double price) {
        if (price < 10) {
            return "0-10";
        } else if (price < 20) {
            return "10-20";
        } else if (price < 50) {
            return "20-50";
        } else {
            return "50+";
        }
    }


    private static void enterWarehouse(Scanner scanner) throws DuplicateException {
        System.out.print("Enter main.Warehouse ID: ");
        int id = scanner.nextInt();

        System.out.print("Enter capacity: ");
        int capacity = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        Warehouses.add(new Warehouse(id, capacity));
        System.out.println("Warehouse added successfully.");
    }

    private static void enterRetailer(Scanner scanner) throws DuplicateException {
        System.out.print("Enter Retailer location: ");
        String location = scanner.nextLine().trim();

        // Validate location input
        if (location.isEmpty()) {
            System.out.println("Error: Retailer location cannot be empty. Please try again.");
            return;
        }

        // Check for duplicate retailer locations
        boolean exists = Retailers.stream()
                .anyMatch(retailer -> retailer.getLocation().equalsIgnoreCase(location));
        if (exists) {
            throw new DuplicateException("Error: A retailer with the location '" + location + "' already exists.");
        }

        System.out.print("Enter Retailer capacity: ");
        int capacity = scanner.nextInt();

        // Validate capacity input
        if (capacity <= 0) {
            System.out.println("Error: Retailer capacity must be a positive number. Please try again.");
            return;
        }
        scanner.nextLine(); // Consume newline

        // Create and add the new Retailer
        Retailers.add(new Retailer(location, capacity));
        System.out.println("Retailer added successfully.");
    }


    private static void viewCategoryWithProducts(Scanner scanner) {
        System.out.print("Enter main.Category name: ");
        String name = scanner.nextLine();

        Category category = categoriesByName.get(name);
        if (category == null) {
            System.out.println("Category not found.");
            return;
        }

        System.out.println(category);
        category.getProducts().forEach(System.out::println);
    }

    private static void viewSupplier(Scanner scanner) {
        System.out.print("Enter main.Supplier ID: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        Supplier supplier = suppliers.get(id);
        if (supplier == null) {
            System.out.println("Supplier not found.");
            return;
        }

        System.out.println(supplier);
    }

    private static void viewWarehouseCategoriesAndStock() {
        for (Warehouse warehouse : Warehouses) {
            System.out.println(warehouse);
        }
    }

    private static void viewWarehouseById(Scanner scanner) {
        System.out.print("Enter main.Warehouse ID: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        for (Warehouse warehouse : Warehouses) {
            if (warehouse.getId() == id) {
                System.out.println(warehouse);
                return;
            }
        }
        System.out.println("Warehouse not found.");
    }

    private static void displaySupplier(Supplier supplier) {
        System.out.println("Supplier added: " + supplier.getName());
    }

    private static void displayWarehouse(int warehouseId) {
        Warehouse warehouse = null;
        for (Warehouse wh : Warehouses) {
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

    private static void displayRetailer(Scanner scanner) {
        System.out.println("Enter retailer location to display details:");
        String location = scanner.nextLine();

        Retailer retailer = Retailers.stream()
                .filter(r -> r.getLocation().equalsIgnoreCase(location))
                .findFirst()
                .orElse(null);

        if (retailer == null) {
            System.out.println("Retailer not found!");
            return;
        }

        System.out.println("\nRetailer Information:");
        System.out.println(retailer);

        System.out.println("\nSales Records:");
        retailer.viewSalesRecords();

        System.out.println("\nSales Summary:");
        retailer.displaySalesSummary();
    }

    private static void retailerSellProduct(Scanner scanner) {
        // Get retailer location
        System.out.print("Enter retailer location: ");
        String location = scanner.nextLine().trim();

        Retailer retailer = Retailers.stream()
                .filter(r -> r.getLocation().equalsIgnoreCase(location))
                .findFirst()
                .orElse(null);

        if (retailer == null) {
            System.out.println("Error: Retailer not found! Please check the location.");
            return;
        }

        // Get product details
        System.out.print("Enter product name: ");
        String productName = scanner.nextLine().trim();

        System.out.print("Enter quantity to sell: ");
        int quantity = scanner.nextInt();

        System.out.print("Enter price per unit: ");
        double price = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        try {
            // Selling the product
            retailer.sellProduct(productName, quantity, price);
            System.out.println("Sold " + quantity + " units of '" + productName + "' successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void transferStockFromWarehouseToRetailer(Scanner scanner) {
        System.out.println("Enter warehouse ID:");
        int warehouseId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        Warehouse warehouse = Warehouses.stream()
                .filter(w -> w.getId() == warehouseId)
                .findFirst()
                .orElse(null);

        if (warehouse == null) {
            System.out.println("Warehouse not found!");
            return;
        }

        System.out.println("Enter retailer location:");
        String location = scanner.nextLine();

        Retailer retailer = Retailers.stream()
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
        scanner.nextLine(); // Consume newline

        try {
            warehouse.sellProductToRetailer(productName, quantity, retailer);
            System.out.println("Successfully transferred " + quantity + " units of '" + productName +
                    "' from warehouse ID " + warehouseId + " to retailer at " + location + ".");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void viewAllRetailers() {
        if (Retailers.isEmpty()) {
            System.out.println("No retailers are currently available.");
            return;
        }

        System.out.println("\nAll Retailers:");
        for (Retailer retailer : Retailers) {
            System.out.println(retailer);
        }
    }

}
