package Dao;

import Data.DBConnection;
import Model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    public List<Customer> getAll() {
        List<Customer> list = new ArrayList<>();
        // Giả sử bảng tên là Customer
        String sql = "SELECT * FROM Customers";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Customer(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getDouble("debt")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insert(Customer c) {
        String sql = "INSERT INTO Customers(name, phone, address, debt) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getName());
            ps.setString(2, c.getPhone());
            ps.setString(3, c.getAddress());
            ps.setDouble(4, c.getDebt()); // Mặc định nợ 0 đồng khi mới thêm

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    // 3. THỐNG KÊ KHÁCH HÀNG (Cho báo cáo)
    public Object[] getOverviewStats() {
        Object[] stats = {0, 0.0}; // [0]: Số lượng khách, [1]: Tổng nợ
        try (Connection conn = DBConnection.getConnection()) {
            // Đếm số khách
            try (PreparedStatement p1 = conn.prepareStatement("SELECT COUNT(*) FROM Customers");
                 ResultSet r1 = p1.executeQuery()) {
                if(r1.next()) stats[0] = r1.getInt(1);
            }

            // Tổng nợ
            try (PreparedStatement p2 = conn.prepareStatement("SELECT SUM(Debt) FROM Customers");
                 ResultSet r2 = p2.executeQuery()) {
                if(r2.next()) stats[1] = r2.getDouble(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stats;
    }
}