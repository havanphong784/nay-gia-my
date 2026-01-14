package Dao;

import Data.DBConnection; // Đảm bảo bạn đã có class kết nối CSDL này
import Model.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    // Đây chính là hàm getAll() mà MaterialPanel đang tìm kiếm
    public List<Category> getAll() {
        List<Category> list = new ArrayList<>();
        // Câu lệnh SQL lấy tất cả loại vật tư
        String sql = "SELECT * FROM Category";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Category c = new Category();
                c.setId(rs.getInt("id"));     // Cột id trong bảng Category
                c.setName(rs.getString("name")); // Cột name trong bảng Category
                list.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. THÊM LOẠI MỚI
    public boolean insert(Category c) {
        String sql = "INSERT INTO Category (Name) VALUES (?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getName());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 3. CẬP NHẬT LOẠI
    public boolean update(Category c) {
        String sql = "UPDATE Category SET Name = ? WHERE Id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getName());
            ps.setInt(2, c.getId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 4. XÓA LOẠI
    public boolean delete(int id) {
        String sql = "DELETE FROM Category WHERE Id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            // Có thể lỗi do ràng buộc khóa ngoại (đang có sản phẩm thuộc loại này)
            System.out.println("Không thể xóa loại đang chứa sản phẩm!");
            e.printStackTrace();
            return false;
        }
    }
}