// ==========================================
// 1. IMPROVED BACKEND DATA MODEL CLASSES
// ==========================================
class Product {
    private String id, name, category;
    private double price;
    private int inventoryStock;

    public Product(String id, String name, String category, double price, int inventoryStock) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.inventoryStock = inventoryStock;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public int getInventoryStock() {
        return inventoryStock;
    }

    public void decreaseStock(int amount) {
        this.inventoryStock -= amount;
    }

    public void increaseStock(int amount) {
        this.inventoryStock += amount;
    }
}
