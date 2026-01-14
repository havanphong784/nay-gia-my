package Model;

public class Product {
    private String id;
    private String name;
    private int categoryId;
    private String categoryName; // Tên loại (để hiển thị cho dễ)
    private String unit;
    private double priceIn;
    private double priceOut;
    private int quantity;
    public Product() {
    }
    public Product(String id, String name, int categoryId, String unit, double priceIn, double priceOut, int quantity) {
        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
        this.unit = unit;
        this.priceIn = priceIn;
        this.priceOut = priceOut;
        this.quantity = quantity;
    }
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public double getPriceIn() { return priceIn; }
    public void setPriceIn(double priceIn) { this.priceIn = priceIn; }

    public double getPriceOut() { return priceOut; }
    public void setPriceOut(double priceOut) { this.priceOut = priceOut; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    // --- QUAN TRỌNG: Hàm này giúp hiển thị Tên thay vì mã lỗi ---
    @Override
    public String toString() {
        return name;
    }
}

