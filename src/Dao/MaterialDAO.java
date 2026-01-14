package Dao;

import Model.Product;
import Data.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MaterialDAO {

    // 1. LẤY DANH SÁCH VẬT TƯ (Kèm tên loại)
    public List<Product> getAll() {
        List<Product> list = new ArrayList<>();
        // Kết nối bảng Products với Categories để lấy tên loại
        String sql = "SELECT p.*, c.Name as CatName " +
                "FROM Products p LEFT JOIN Category c ON p.CategoryId = c.Id";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getString("Id"));
                p.setName(rs.getString("Name"));
                p.setCategoryId(rs.getInt("CategoryId"));
                p.setCategoryName(rs.getString("CatName")); // Lấy tên loại từ bảng Categories
                p.setUnit(rs.getString("Unit"));
                p.setPriceIn(rs.getDouble("PriceIn"));
                p.setPriceOut(rs.getDouble("PriceOut"));
                p.setQuantity(rs.getInt("Quantity"));
                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. THÊM VẬT TƯ MỚI
    public boolean insert(Product p) {
        String sql = "INSERT INTO Products (Id, Name, CategoryId, Unit, PriceIn, PriceOut, Quantity, Status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getId());
            ps.setString(2, p.getName());
            ps.setInt(3, p.getCategoryId());
            ps.setString(4, p.getUnit());
            ps.setDouble(5, p.getPriceIn());
            ps.setDouble(6, p.getPriceOut());
            ps.setInt(7, p.getQuantity());
            ps.setString(8, p.getQuantity() > 0 ? "Còn hàng" : "Hết hàng");

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 3. XÓA VẬT TƯ
    public boolean delete(String id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM Products WHERE Id = ?")) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 4. CẬP NHẬT VẬT TƯ (Sửa)
    public boolean update(Product p) {
        String sql = "UPDATE Products SET Name=?, CategoryId=?, Unit=?, PriceIn=?, PriceOut=?, Quantity=? WHERE Id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getName());
            ps.setInt(2, p.getCategoryId());
            ps.setString(3, p.getUnit());
            ps.setDouble(4, p.getPriceIn());
            ps.setDouble(5, p.getPriceOut());
            ps.setInt(6, p.getQuantity());
            ps.setString(7, p.getId());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 5. TÌM KIẾM
    public List<Product> search(String keyword) {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT p.*, c.Name as CatName FROM Products p " +
                "LEFT JOIN Category c ON p.CategoryId = c.Id " +
                "WHERE p.Name LIKE ? OR p.Id LIKE ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while(rs.next()) {
                    Product p = new Product();
                    p.setId(rs.getString("Id"));
                    p.setName(rs.getString("Name"));
                    p.setCategoryName(rs.getString("CatName"));
                    p.setUnit(rs.getString("Unit"));
                    p.setPriceIn(rs.getDouble("PriceIn"));
                    p.setPriceOut(rs.getDouble("PriceOut"));
                    p.setQuantity(rs.getInt("Quantity"));
                    list.add(p);
                }
            }
        } catch(Exception e) { e.printStackTrace(); }
        return list;
    }

    // 6. THỐNG KÊ NHANH (Cho màn hình chính)
    public Object[] getOverviewStats() {
        Object[] stats = {0, 0, 0.0};
        try (Connection conn = DBConnection.getConnection()) {
            // Tổng số mặt hàng
            try (PreparedStatement p1 = conn.prepareStatement("SELECT COUNT(*) FROM Products");
                 ResultSet r1 = p1.executeQuery()) {
                if(r1.next()) stats[0] = r1.getInt(1);
            }

            // Số mặt hàng sắp hết (<10)
            try (PreparedStatement p2 = conn.prepareStatement("SELECT COUNT(*) FROM Products WHERE Quantity < 10");
                 ResultSet r2 = p2.executeQuery()) {
                if(r2.next()) stats[1] = r2.getInt(1);
            }

            // Tổng giá trị kho (theo giá nhập)
            try (PreparedStatement p3 = conn.prepareStatement("SELECT SUM(Quantity * PriceIn) FROM Products");
                 ResultSet r3 = p3.executeQuery()) {
                if(r3.next()) stats[2] = r3.getDouble(1);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return stats;
    }
}