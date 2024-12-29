package org.example;

public class Supplier {
    private final int id;
    private final String name;

    public Supplier(int id, String name) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID must be a positive integer.");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank.");
        }
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Supplier{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
