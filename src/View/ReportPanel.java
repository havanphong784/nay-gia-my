package View;

import Dao.CustomerDAO;
import Dao.MaterialDAO;
import Dao.TransactionDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.DecimalFormat;

public class ReportPanel extends JPanel {

    private JLabel lblProductCount, lblLowStock, lblInventoryValue;
    private JLabel lblTotalRevenue, lblOrderCount;
    private JLabel lblCustomerCount, lblTotalDebt;

    private MaterialDAO materialDAO = new MaterialDAO();
    private TransactionDAO transactionDAO = new TransactionDAO();
    private CustomerDAO customerDAO = new CustomerDAO();
    
    // Format tiền tệ
    private final DecimalFormat df = new DecimalFormat("#,### VNĐ");

    public ReportPanel() {
        setLayout(new BorderLayout());
        setBackground(Theme.CONTENT_BG); // Màu nền chuẩn

        // --- HEADER ---
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(Color.WHITE);
        pnlHeader.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        JLabel lblTitle = new JLabel("BÁO CÁO TỔNG QUAN");
        lblTitle.setFont(Theme.FONT_HEADER);
        lblTitle.setForeground(Theme.TEXT_PRIMARY);

        JButton btnRefresh = new JButton("Làm mới dữ liệu");
        btnRefresh.setFont(Theme.FONT_REGULAR);
        btnRefresh.setFocusPainted(false);
        btnRefresh.setBackground(Theme.BTN_INFO);
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.addActionListener(e -> loadData());

        pnlHeader.add(lblTitle, BorderLayout.WEST);
        pnlHeader.add(btnRefresh, BorderLayout.EAST);
        add(pnlHeader, BorderLayout.NORTH);

        // --- DASHBOARD CARDS --
        JPanel pnlDashboard = new JPanel(new GridLayout(2, 2, 20, 20)); // 2 hàng 2 cột
        pnlDashboard.setBackground(Theme.CONTENT_BG);
        pnlDashboard.setBorder(new EmptyBorder(20, 20, 20, 20));

        // 1. Card Sản Phẩm
        JPanel cardProduct = createCard("KHO HÀNG", new Color(46, 204, 113));
        lblProductCount = createValueLabel();
        lblLowStock = createSubLabel();
        lblInventoryValue = createSubLabel();
        
        addContentToCard(cardProduct, lblProductCount, lblLowStock, lblInventoryValue);

        // 2. Card Doanh Thu
        JPanel cardRevenue = createCard("DOANH THU BÁN HÀNG", new Color(52, 152, 219));
        lblTotalRevenue = createValueLabel();
        lblOrderCount = createSubLabel();
        
        addContentToCard(cardRevenue, lblTotalRevenue, lblOrderCount, null);

        // 3. Card Khách Hàng
        JPanel cardCustomer = createCard("KHÁCH HÀNG & CÔNG NỢ", new Color(155, 89, 182));
        lblCustomerCount = createValueLabel();
        lblTotalDebt = createSubLabel();
        
        addContentToCard(cardCustomer, lblCustomerCount, lblTotalDebt, null);
        
        // 4. Card Thông tin khác (Ví dụ)
        JPanel cardInfo = createCard("THÔNG TIN HỆ THỐNG", new Color(230, 126, 34));
        JLabel lblSystemInfo = createValueLabel();
        lblSystemInfo.setText("v1.0.0");
        JLabel lblDev = createSubLabel();
        lblDev.setText("Dev: Gia Mi");
        
        addContentToCard(cardInfo, lblSystemInfo, lblDev, null);

        pnlDashboard.add(cardProduct);
        pnlDashboard.add(cardRevenue);
        pnlDashboard.add(cardCustomer);
        pnlDashboard.add(cardInfo);

        add(pnlDashboard, BorderLayout.CENTER);

        // Load dữ liệu khi mở
        loadData();
    }

    private JPanel createCard(String title, Color barColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createMatteBorder(0, 5, 0, 0, barColor)); // Viền màu bên trái

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitle.setForeground(Color.GRAY);
        lblTitle.setBorder(new EmptyBorder(15, 20, 5, 20));

        card.add(lblTitle, BorderLayout.NORTH);
        return card;
    }
    
    private void addContentToCard(JPanel card, JLabel bigLabel, JLabel sub1, JLabel sub2) {
        JPanel content = new JPanel(new GridLayout(0, 1, 5, 5));
        content.setBackground(Color.WHITE);
        content.setBorder(new EmptyBorder(0, 20, 15, 20));
        
        content.add(bigLabel);
        if(sub1 != null) content.add(sub1);
        if(sub2 != null) content.add(sub2);
        
        card.add(content, BorderLayout.CENTER);
    }

    private JLabel createValueLabel() {
        JLabel lbl = new JLabel("...");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lbl.setForeground(new Color(33, 43, 60));
        return lbl;
    }

    private JLabel createSubLabel() {
        JLabel lbl = new JLabel("...");
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbl.setForeground(Color.DARK_GRAY);
        return lbl;
    }

    // --- HÀM TẢI DỮ LIỆU TỪ DAO ---
    private void loadData() {
        // 1. Kho hàng
        Object[] matStats = materialDAO.getOverviewStats();
        lblProductCount.setText(matStats[0] + " Sản phẩm");
        lblLowStock.setText("Sắp hết hàng: " + matStats[1]);
        lblInventoryValue.setText("Giá trị kho: " + df.format(matStats[2]));

        // 2. Doanh thu
        Object[] transStats = transactionDAO.getRevenueStats();
        lblOrderCount.setText("Tổng đơn hàng: " + transStats[0]);
        lblTotalRevenue.setText(df.format(transStats[1]));

        // 3. Khách hàng
        Object[] custStats = customerDAO.getOverviewStats();
        lblCustomerCount.setText(custStats[0] + " Khách hàng");
        lblTotalDebt.setText("Tổng công nợ: " + df.format(custStats[1]));
    }
}
