# Inventory Management System
## Overview
The **Inventory Management Application** is a Java-based software designed to manage and monitor various aspects of a supply chain, including Suppliers, Products, Categories, Warehouses, and Retailers. It offers an interactive command-line interface with capabilities for adding, viewing, updating, and managing inventory and distribution workflows.
## Features
1. **Supplier Management**:
    - Add and view suppliers.
    - Link suppliers to product categories.

2. **Product Management**:
    - Add new products.
    - Organize products by categories.
    - List and group products by price ranges.

3. **Category Management**:
    - Define product categories linked to suppliers and warehouses.
    - View details of defined categories.

4. **Warehouse Management**:
    - Add warehouses and manage capacities.
    - Transfer stock between warehouses and retailers.
    - Monitor and restock low-stock products.

5. **Retailer Management**:
    - Add retailers with specific locations and capacities.
    - Manage sales and inventory at retailer locations.
    - Generate sales summaries.

6. **Session Handling**:
    - Secure login using a preset password.
    - Load and save session data for inventory continuity.

## How It Works
- The system uses **HashMaps** and **Lists** to store and organize data for suppliers, products, warehouses, categories, and retailers.
- Interaction with the application occurs via an intuitive menu-driven interface.
- Centralized `DataManager` handles persistent data storage and retrieval for session continuity.
- Multi-step operations include user validation, inventory checking, and data flow between warehousing and retailing.
