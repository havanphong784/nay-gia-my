import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import View.LoginForm;

public class App {
    public static void main(String[] args) {
        // Thiết lập giao diện hệ thống (Windows/Mac) cho đẹp
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Chạy form đăng nhập trên luồng sự kiện của Swing
        SwingUtilities.invokeLater(() -> {
            // Đúng chuẩn OOP: Tạo đối tượng Frame mới
            new LoginForm();
        });
    }
}
