package Model;

public class Customer {
    private int id;
    private String name;
    private String phone;
    private String address;
    private double debt; // Công nợ

    public Customer() {
    }

    public Customer(int id, String name, String phone, String address, double debt) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.debt = debt;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public double getDebt() { return debt; }
    public void setDebt(double debt) { this.debt = debt; }

    // --- QUAN TRỌNG: Hàm này giúp hiển thị Tên Khách Hàng ---
    @Override
    public String toString() {
        return name;
    }
}
