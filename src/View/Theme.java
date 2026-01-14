package View;

import java.awt.Color;
import java.awt.Font;

public class Theme {
    // --- MÀU SẮC CHỦ ĐẠO (Palette: Modern Professional) ---
    // Nền menu bên trái (Dark Blue/Gray)
    public static final Color SIDEBAR_BG = new Color(44, 62, 80); 
    
    // Nền chính của ứng dụng (Light Gray)
    public static final Color CONTENT_BG = new Color(236, 240, 241);
    
    // Màu trắng cơ bản cho các card/panel
    public static final Color WHITE_BG = Color.WHITE;

    // --- MÀU NÚT CHỨC NĂNG ---
    // Nút Chính / Xác nhận / Thêm mới (Xanh lá - Emerald)
    public static final Color BTN_PRIMARY = new Color(46, 204, 113);
    
    // Nút Sửa / Info / Hành động phụ (Xanh dương - Peter River)
    public static final Color BTN_INFO = new Color(52, 152, 219);
    
    // Nút Xóa / Hủy / Cảnh báo (Đỏ - Alizarin)
    public static final Color BTN_DANGER = new Color(231, 76, 60);
    
    // Nút Chức năng đặc biệt (Tím - Amethyst)
    public static final Color BTN_SPECIAL = new Color(155, 89, 182);
    
    // Nút Đăng xuất / Quay lại (Xám tối)
    public static final Color BTN_SECONDARY = new Color(149, 165, 166);

    // --- MÀU CHỮ ---
    public static final Color TEXT_PRIMARY = new Color(44, 62, 80); // Chữ đen/xám đậm
    public static final Color TEXT_WHITE = Color.WHITE;       // Chữ trắng (trên nền đậm)
    public static final Color TEXT_GRAY = new Color(127, 140, 141); // Chữ gợi ý

    // --- FONTS ---
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_REGULAR = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 14);
}
