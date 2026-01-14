package View;
import Dao.AccountManagerDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginForm extends JFrame {

    private JTextField userTextField;
    private JPasswordField passwordField;
    private JLabel messageLabel;
    
    // Màu sắc
    Color primaryColor = new Color(108, 92, 231);
    Color gradientColor = new Color(116, 185, 255);
    Color whiteColor = Color.WHITE;
    Color borderColor = new Color(108, 92, 231);
    Color errorColor = new Color(214, 48, 49);
    Color successColor = new Color(0, 184, 148);
    Color grayText = new Color(178, 190, 195);
    Font labelFont = new Font("Segoe UI", Font.BOLD, 13);
    Font inputFont = new Font("Segoe UI", Font.PLAIN, 14);

    public LoginForm() {
        setTitle("Đăng nhập hệ thống");
        int frameWidth = 520;
        int frameHeight = 620;
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

        JLabel title = new JLabel("WELCOME");
        title.setForeground(whiteColor);
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setBounds(0, 35, frameWidth, 40);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        header.add(title);

        JLabel subTitle = new JLabel("Hệ thống quản lý vật tư xây dựng");
        subTitle.setForeground(new Color(240, 240, 240));
        subTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subTitle.setBounds(0, 80, frameWidth, 20);
        subTitle.setHorizontalAlignment(SwingConstants.CENTER);
        header.add(subTitle);

        JLabel userLabel = new JLabel("Tên đăng nhập");
        userLabel.setFont(labelFont);
        userLabel.setForeground(primaryColor);
        userLabel.setBounds(centerX, 160, 200, 20);
        add(userLabel);

        userTextField = new JTextField();
        userTextField.setBounds(centerX, 185, inputWidth, 45);
        userTextField.setFont(inputFont);
        userTextField.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, borderColor));
        userTextField.setBackground(whiteColor);
        add(userTextField);

        JLabel passLabel = new JLabel("Mật khẩu");
        passLabel.setFont(labelFont);
        passLabel.setForeground(primaryColor);
        passLabel.setBounds(centerX, 250, 200, 20);
        add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(centerX, 275, inputWidth, 45);
        passwordField.setFont(inputFont);
        passwordField.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, borderColor));
        passwordField.setBackground(whiteColor);
        add(passwordField);

        int btnY = 360;
        int btnHeight = 50;

        JButton loginButton = new JButton("ĐĂNG NHẬP");
        loginButton.setBounds(centerX, btnY, inputWidth, btnHeight);
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginButton.setBackground(primaryColor);
        loginButton.setForeground(whiteColor);
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        add(loginButton);

        JLabel orLabel = new JLabel("Hoặc", SwingConstants.CENTER);
        orLabel.setBounds(centerX, btnY + btnHeight, inputWidth, 30);
        orLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        orLabel.setForeground(grayText);
        add(orLabel);

        JButton registerButton = new JButton("ĐĂNG KÝ TÀI KHOẢN MỚI");
        registerButton.setBounds(centerX, btnY + btnHeight + 30, inputWidth, btnHeight);
        registerButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        registerButton.setBackground(whiteColor);
        registerButton.setForeground(primaryColor);
        registerButton.setFocusPainted(false);
        registerButton.setBorder(BorderFactory.createLineBorder(primaryColor, 1));
        add(registerButton);

        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setBounds(0, 530, frameWidth, 20);
        messageLabel.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        add(messageLabel);

        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (e.getSource() == loginButton) {
                    performLogin();
                }

                if (e.getSource() == registerButton) {
                    dispose();
                    new RegisterForm(); // Mở constructor mới của RegisterForm
                }
            }
        };

        loginButton.addActionListener(actionListener);
        registerButton.addActionListener(actionListener);
        getRootPane().setDefaultButton(loginButton);

        setVisible(true);
    }
    
    private void performLogin() {
        String user = userTextField.getText();
        String password = new String(passwordField.getPassword());

        String fullName = AccountManagerDAO.login(user, password);

        if (fullName != null) {
            messageLabel.setForeground(successColor);
            messageLabel.setText("Đăng nhập thành công!");

            JOptionPane.showMessageDialog(this, "Xin chào " + fullName + "!");

            dispose();
            SwingUtilities.invokeLater(() -> {
                View.MainManagementFrame mainFrame = new View.MainManagementFrame();
                mainFrame.setVisible(true);
            });
        } else {
            messageLabel.setForeground(errorColor);
            messageLabel.setText("Sai tài khoản hoặc mật khẩu!");
        }
    }


}