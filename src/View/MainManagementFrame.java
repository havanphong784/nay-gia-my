package View;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// Đảm bảo các Panel con (MaterialPanel, CustomerPanel...) nằm trong cùng package
// hoặc đã được import đúng cách. Nếu khác package, hãy thêm dòng import ví dụ: import View.*;
import View.MaterialPanel;
import View.CustomerPanel;
import View.TransactionPanel;

public class MainManagementFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JPanel menuContainer; // Chứa các nút menu chính

    // --- BẢNG MÀU GIAO DIỆN (Theme Modern Dashboard) ---
    // Màu nền Menu trái (Lấy từ Theme)
    private final Color SIDEBAR_BG = Theme.SIDEBAR_BG;
    // Màu nút khi ĐANG CHỌN (Lấy từ Theme - dùng màu chính BTN_PRIMARY hoặc BTN_INFO)
    private final Color ACTIVE_COLOR = Theme.BTN_INFO;
    // Màu nút khi di chuột vào (Hover) - Tự tính toán hoặc hardcode nhẹ
    private final Color HOVER_COLOR = new Color(52, 73, 94); // Màu tối hơn chút so với SIDEM_BG
    // Màu chữ
    private final Color TEXT_COLOR = Theme.TEXT_WHITE;

    public MainManagementFrame() {
        // 1. Cài đặt Frame chính
        setTitle("Hệ Thống Quản Lý Kho");
        setSize(1100, 650); // Kích thước rộng rãi
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Căn giữa màn hình
        setLayout(new BorderLayout());

        // 2. TẠO MENU BÊN TRÁI (SIDEBAR)
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(220, 0)); // Chiều rộng cố định cho sidebar

        // --- Header của Menu (Logo / Tên App) ---
        JLabel lblHeader = new JLabel("QUẢN LÝ KHO", SwingConstants.CENTER);
        lblHeader.setForeground(Color.WHITE);
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblHeader.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0)); // Cách trên dưới cho thoáng
        sidebar.add(lblHeader, BorderLayout.NORTH);

        // --- Container chứa các nút (Dồn lên trên) ---
        // Sử dụng một Panel trung gian để đẩy các nút lên phía trên (North) của Center
        JPanel centerMenuWrapper = new JPanel(new BorderLayout());
        centerMenuWrapper.setBackground(SIDEBAR_BG);

        menuContainer = new JPanel(new GridLayout(0, 1, 0, 8)); // 1 cột, khoảng cách giữa các nút là 8px
        menuContainer.setBackground(SIDEBAR_BG);

        // Tạo các nút menu (Có thêm icon text minh họa)
        JButton btnMaterial = createSideMenuButton("Vật Tư & Hàng Hóa");
        JButton btnCustomer = createSideMenuButton("Khách Hàng");
        JButton btnTransaction = createSideMenuButton("Nhập / Xuất Kho");
        JButton btnReport = createSideMenuButton("Báo Cáo Thống Kê");

        // Thêm nút vào container
        menuContainer.add(btnMaterial);
        menuContainer.add(btnCustomer);
        menuContainer.add(btnTransaction);
        menuContainer.add(btnReport);

        // Đẩy menuContainer lên đỉnh của vùng giữa
        centerMenuWrapper.add(menuContainer, BorderLayout.NORTH);
        sidebar.add(centerMenuWrapper, BorderLayout.CENTER);

        // --- Nút Đăng Xuất nằm dưới cùng (Bottom) ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setBackground(SIDEBAR_BG);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0)); // Cách lề dưới một chút

        JButton btnExit = createSideMenuButton("Đăng Xuất");
        // Ghi đè lại kích thước riêng cho nút thoát nếu muốn
        btnExit.setPreferredSize(new Dimension(220, 45));
        bottomPanel.add(btnExit);

        sidebar.add(bottomPanel, BorderLayout.SOUTH);

        // 3. TẠO CARD PANEL (Nội dung chính bên phải)
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(new Color(179, 185, 213)); // Màu nền xám rất nhạt cho vùng nội dung

        // --- Thêm các màn hình chức năng vào CardLayout ---
        // Lưu ý: Các class MaterialPanel, CustomerPanel... phải tồn tại
        cardPanel.add(new MaterialPanel(), "Vật tư");
        cardPanel.add(new CustomerPanel(), "Khách hàng");
        cardPanel.add(new TransactionPanel(), "Nhập / Xuất");

        // Màn hình Báo cáo (Đã xong)
        cardPanel.add(new ReportPanel(), "Báo cáo");

        // 4. Xử lý sự kiện chuyển tab & Hiệu ứng Active
        btnMaterial.addActionListener(e -> {
            cardLayout.show(cardPanel, "Vật tư");
            setActiveButton(btnMaterial);
        });

        btnCustomer.addActionListener(e -> {
            cardLayout.show(cardPanel, "Khách hàng");
            setActiveButton(btnCustomer);
        });

        btnTransaction.addActionListener(e -> {
            cardLayout.show(cardPanel, "Nhập / Xuất");
            setActiveButton(btnTransaction);
        });

        btnReport.addActionListener(e -> {
            cardLayout.show(cardPanel, "Báo cáo");
            setActiveButton(btnReport);
        });

        btnExit.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc muốn đăng xuất?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if(choice == JOptionPane.YES_OPTION) {
                // Đóng form hiện tại
                this.dispose();
                // Mở lại LoginForm
                new LoginForm();
            }
        });

        // Mặc định chọn tab đầu tiên khi mở áp
        setActiveButton(btnMaterial);

        // 5. Hoàn tất việc thêm vào Frame
        add(sidebar, BorderLayout.WEST);
        add(cardPanel, BorderLayout.CENTER);
    }

    /**
     * Hàm hỗ trợ tạo nút Menu bên trái với style thống nhất
     */
    private JButton createSideMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        btn.setForeground(TEXT_COLOR);
        btn.setBackground(SIDEBAR_BG); // Màu nền mặc định trùng với sidebar

        // Padding: Trên 12, Trái 25, Dưới 12, Phải 10 -> Tạo cảm giác thoáng
        btn.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 10));

        btn.setHorizontalAlignment(SwingConstants.LEFT); // Canh lề trái
        btn.setFocusPainted(false); // Bỏ viền focus khi bấm
        btn.setBorderPainted(false); // Bỏ viền nút
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Con trỏ tay khi di chuột vào

        // Xử lý hiệu ứng Hover (Di chuột vào thì sáng lên)
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                // Chỉ đổi màu hover nếu nút đó KHÔNG phải là nút đang được chọn (Active)
                if (!btn.getBackground().equals(ACTIVE_COLOR)) {
                    btn.setBackground(HOVER_COLOR);
                }
            }
            public void mouseExited(MouseEvent e) {
                // Trả về màu gốc nếu không phải nút đang chọn
                if (!btn.getBackground().equals(ACTIVE_COLOR)) {
                    btn.setBackground(SIDEBAR_BG);
                }
            }
        });

        return btn;
    }

    /**
     * Hàm xử lý logic "Active State": Làm nổi bật nút đang được chọn
     */
    private void setActiveButton(JButton activeBtn) {
        // 1. Reset tất cả các nút trong menuContainer về trạng thái bình thường
        for (Component comp : menuContainer.getComponents()) {
            if (comp instanceof JButton) {
                JButton btn = (JButton) comp;
                btn.setBackground(SIDEBAR_BG);
                btn.setFont(new Font("Segoe UI", Font.PLAIN, 15));
                btn.setForeground(TEXT_COLOR);
            }
        }

        // 2. Set màu nổi bật cho nút được click
        activeBtn.setBackground(ACTIVE_COLOR);
        activeBtn.setForeground(Color.WHITE);
        activeBtn.setFont(new Font("Segoe UI", Font.BOLD, 15)); // In đậm để nhấn mạnh
    }


}