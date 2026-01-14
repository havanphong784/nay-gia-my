package Data;

import java.sql.Connection;

public class TestConnection {
    public static void main(String[] args) {

        // Gọi hàm kết nối
        Connection conn = DBConnection.getConnection();

        // Kiểm tra lần nữa
        if (conn != null) {
            System.out.println(">>> TEST: ĐÃ KẾT NỐI TỚI DATABASE QL_KHO_VLXD");
        } else {
            System.out.println(">>> TEST: KẾT NỐI THẤT BẠI!");
        }
    }
}