package Dao;

import java.sql.*;
import Data.DBConnection; // Import class DBConnection của bạn để sử dụng hàm getConnection()

public class AccountManagerDAO {

    // --- 1. HÀM ĐĂNG KÝ (Lưu tài khoản mới vào SQL) ---
    public static boolean registerAccount(String username, String password, String fullname) {
        String sql = "INSERT INTO Accounts (Username, Password, FullName) VALUES (?, ?, ?)";

        // Sử dụng try-with-resources để tự động đóng kết nối sau khi xong
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {



            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, fullname);

            int result = pstmt.executeUpdate();
            return result > 0; // Trả về true nếu chèn thành công ít nhất 1 dòng

        } catch (SQLException e) {
            System.out.println("Lỗi đăng ký: Tài khoản có thể đã tồn tại hoặc lỗi SQL.");
            e.printStackTrace();
            return false;
        }
    }

    // --- 2. HÀM KIỂM TRA ĐĂNG NHẬP ---
    public static String login(String username, String password) {
        String sql = "SELECT FullName FROM Accounts WHERE Username = ? AND Password = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {



            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            // Nếu có kết quả trả về, nghĩa là đúng User và Pass
            if (rs.next()) {
                return rs.getString("FullName"); // Trả về tên đầy đủ để hiển thị lời chào
            }

        } catch (SQLException e) {
            System.out.println("Lỗi khi kiểm tra đăng nhập.");
            e.printStackTrace();
        }
        return null; // Trả về null nếu sai thông tin hoặc lỗi
    }
}