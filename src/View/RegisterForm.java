package View;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import Dao.AccountManagerDAO; 

public class RegisterForm extends JFrame {

    private JTextField nameField;
    private JTextField userField;
    private JPasswordField passField;

    // Màu sắc
    Color primaryColor = new Color(108, 92, 231);
    Color gradientColor = new Color(116, 185, 255);
    Color whiteColor = Color.WHITE;
    Color borderColor = new Color(108, 92, 231);
    Color grayText = new Color(178, 190, 195);
    Font labelFont = new Font("Segoe UI", Font.BOLD, 13);
    Font inputFont = new Font("Segoe UI", Font.PLAIN, 14);

    public RegisterForm() {
        setTitle("Đăng ký tài khoản");
        int frameWidth = 520;
        int frameHeight = 650;
        setSize(frameWidth, frameHeight);
        setLocationRelativeTo(null);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setBackground(whiteColor);

        int inputWidth = 340;
        int centerX = (frameWidth - inputWidth) / 2 - 8;

        JPanel header = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, primaryColor, getWidth(), getHeight(), gradientColor);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        header.setBounds(0, 0, frameWidth, 130);
        header.setLayout(null);
        add(header);

        JLabel title = new JLabel("CREATE ACCOUNT");
        title.setForeground(whiteColor);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setBounds(0, 35, frameWidth, 40);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        header.add(title);

        JLabel subTitle = new JLabel("Tham gia hệ thống quản lý vật tư");
        subTitle.setForeground(new Color(240, 240, 240));
        subTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subTitle.setBounds(0, 80, frameWidth, 20);
        subTitle.setHorizontalAlignment(SwingConstants.CENTER);
        header.add(subTitle);

        // 1. Họ và Tên
        JLabel nameLabel = new JLabel("Họ và Tên");
        nameLabel.setFont(labelFont);
        nameLabel.setForeground(primaryColor);
        nameLabel.setBounds(centerX, 150, 200, 20);
        add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(centerX, 175, inputWidth, 40);
        nameField.setFont(inputFont);
        nameField.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, borderColor));
        add(nameField);

        // 2. Tên đăng nhập
        JLabel userLabel = new JLabel("Tên đăng nhập (Username)");
        userLabel.setFont(labelFont);
        userLabel.setForeground(primaryColor);
        userLabel.setBounds(centerX, 230, 200, 20);
        add(userLabel);

        userField = new JTextField();
        userField.setBounds(centerX, 255, inputWidth, 40);
        userField.setFont(inputFont);
        userField.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, borderColor));
        add(userField);

        // 3. Mật khẩu
        JLabel passLabel = new JLabel("Mật khẩu");
        passLabel.setFont(labelFont);
        passLabel.setForeground(primaryColor);
        passLabel.setBounds(centerX, 310, 200, 20);
        add(passLabel);

        passField = new JPasswordField();
        passField.setBounds(centerX, 335, inputWidth, 40);
        passField.setFont(inputFont);
        passField.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, borderColor));
        add(passField);

        // --- NÚT BẤM ---
        int btnY = 420;

        JButton registerButton = new JButton("ĐĂNG KÝ NGAY");
        registerButton.setBounds(centerX, btnY, inputWidth, 50);
        registerButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        registerButton.setBackground(primaryColor);
        registerButton.setForeground(whiteColor);
        registerButton.setFocusPainted(false);
        registerButton.setBorderPainted(false);
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        add(registerButton);

        JButton backButton = new JButton("Đã có tài khoản? Đăng nhập");
        backButton.setBounds(centerX, btnY + 60, inputWidth, 30);
        backButton.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        backButton.setForeground(grayText);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        add(backButton);

        // --- XỬ LÝ SỰ KIỆN ---
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performRegister();
            }
        });

        backButton.addActionListener(e -> {
            dispose();
            new LoginForm();
        });

        setVisible(true);
    }

    private void performRegister() {
        String name = nameField.getText().trim();
        String user = userField.getText().trim();
        String pass = new String(passField.getPassword()).trim();

        if (name.isEmpty() || user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean success = AccountManagerDAO.registerAccount(user, pass, name);

        if (success) {
            JOptionPane.showMessageDialog(this, "Đăng ký thành công tài khoản: " + user);
            dispose();
            new LoginForm();
        } else {
            JOptionPane.showMessageDialog(this, "Đăng ký thất bại! Tên đăng nhập đã tồn tại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }


}