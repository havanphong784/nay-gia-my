package View;

import Dao.CustomerDAO;
import Dao.MaterialDAO;
import Dao.TransactionDAO; 
import Model.Customer;
import Model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class TransactionPanel extends JPanel {
    // --- Components cho Tab Ban Hang ---
    private JComboBox<Customer> cboCustomer;
    private JComboBox<Product> cboProduct;
    private JTextField txtQty;
    private JTable tableCart;
    private DefaultTableModel modelCart;
    private JLabel lblTotal;
    
    // --- Components cho Tab Lich Su ---
    private JTable tableHistory;
    private DefaultTableModel modelHistory;

    private CustomerDAO customerDao = new CustomerDAO();
    private MaterialDAO productDao = new MaterialDAO();
    private TransactionDAO transDao = new TransactionDAO();

    public TransactionPanel() {
        setLayout(new BorderLayout());
        setBackground(Theme.CONTENT_BG);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(Theme.FONT_BOLD);

        // TAB 1: LẬP PHIẾU
        JPanel pnlCreate = createSalesPanel();
        tabbedPane.addTab("Lập Phiếu", pnlCreate);

        // TAB 2: LỊCH SỬ
        JPanel pnlHistory = createHistoryPanel();
        tabbedPane.addTab("Lịch Sử", pnlHistory);
        
        // Sự kiện khi chuyển tab
        tabbedPane.addChangeListener(e -> {
            if(tabbedPane.getSelectedIndex() == 1) {
                loadHistoryData();
            }
        });

        add(tabbedPane, BorderLayout.CENTER);
    }
    
    private JPanel createSalesPanel() {
        JPanel pnl = new JPanel(new BorderLayout(15, 15));
        pnl.setBackground(Theme.CONTENT_BG);
        pnl.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // --- INPUT ---
        JPanel pnlInput = new JPanel(new GridLayout(2, 4, 10, 10));
        pnlInput.setOpaque(false);
        pnlInput.setBorder(BorderFactory.createTitledBorder("Thông tin giao dịch"));

        cboCustomer = new JComboBox<>();
        cboProduct = new JComboBox<>();
        txtQty = new JTextField("1");
        
        JButton btnAdd = new JButton("Thêm vào giỏ");
        JButton btnRefresh = new JButton("Làm mới DS");
        
        setupButton(btnAdd, Theme.BTN_INFO);
        setupButton(btnRefresh, Theme.BTN_PRIMARY);

        pnlInput.add(new JLabel("Khách hàng:")); pnlInput.add(cboCustomer);
        pnlInput.add(new JLabel("Sản phẩm:"));   pnlInput.add(cboProduct);
        pnlInput.add(new JLabel("Số lượng:"));   pnlInput.add(txtQty);
        pnlInput.add(btnRefresh);                pnlInput.add(btnAdd);

        pnl.add(pnlInput, BorderLayout.NORTH);

        // --- CART TABLE ---
        String[] cols = {"Tên Sản Phẩm", "Đơn Giá", "Số Lượng", "Thành Tiền"};
        modelCart = new DefaultTableModel(cols, 0);
        tableCart = new JTable(modelCart);
        tableCart.setRowHeight(25);
        pnl.add(new JScrollPane(tableCart), BorderLayout.CENTER);

        // --- PAY BUTTON ---
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlBottom.setOpaque(false);
        lblTotal = new JLabel("Tổng tiền: 0 VNĐ");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 18));
        lblTotal.setForeground(Color.RED);

        JButton btnPay = new JButton("Thanh Toán & Lưu");
        btnPay.setPreferredSize(new Dimension(150, 40));
        setupButton(btnPay, Theme.BTN_DANGER);

        pnlBottom.add(lblTotal);
        pnlBottom.add(Box.createHorizontalStrut(20));
        pnlBottom.add(btnPay);
        pnl.add(pnlBottom, BorderLayout.SOUTH);
        
        // --- EVENTS ---
        btnRefresh.addActionListener(e -> loadComboBoxData());
        btnAdd.addActionListener(e -> addToCart());
        btnPay.addActionListener(e -> processPayment());
        
        loadComboBoxData();
        return pnl;
    }

    private JPanel createHistoryPanel() {
        JPanel pnl = new JPanel(new BorderLayout(15, 15));
        pnl.setBackground(Theme.CONTENT_BG);
        pnl.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // TOOLBAR
        JPanel pnlTool = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlTool.setOpaque(false);
        JButton btnRefreshHist = new JButton("Tải lại lịch sử");
        setupButton(btnRefreshHist, Theme.BTN_PRIMARY);
        btnRefreshHist.addActionListener(e -> loadHistoryData());
        pnlTool.add(btnRefreshHist);
        
        pnl.add(pnlTool, BorderLayout.NORTH);
        
        // TABLE
        String[] cols = {"Mã HĐ", "Khách Hàng", "Tổng Tiền", "Thanh Toán"};
        modelHistory = new DefaultTableModel(cols, 0);
        tableHistory = new JTable(modelHistory);
        tableHistory.setRowHeight(28);
        pnl.add(new JScrollPane(tableHistory), BorderLayout.CENTER);
        
        return pnl;
    }
    
    // --- LOGIC METHODS ---

    private void loadHistoryData() {
        modelHistory.setRowCount(0);
        List<Object[]> list = transDao.getAllInvoices();
        for(Object[] row : list) {
            // Row components: [0]Id, [1]Customer, [2]Total, [3]Status, [4]PaidAmount
            double total = (Double) row[2];
            double paid = 0;
            if (row.length > 4 && row[4] != null) { // Check if PaidAmount exists
                paid = (Double) row[4];
            }
            
            String status = (String) row[3];
            // Format Total
            String totalStr = String.format("%,.0f VNĐ", total);
            
            // Format Status with Debt Amount
            if ("Nợ".equals(status)) {
                double debt = total - paid;
                status = "Nợ (" + String.format("%,.0f", debt) + ")";
            }

            modelHistory.addRow(new Object[]{
                row[0], row[1], totalStr, status
            });
        }
    }

    public void loadComboBoxData() {
        cboCustomer.removeAllItems();
        cboProduct.removeAllItems();
        List<Customer> customers = customerDao.getAll();
        for (Customer c : customers) cboCustomer.addItem(c);
        List<Product> products = productDao.getAll();
        for (Product p : products) cboProduct.addItem(p);
    }

    private void addToCart() {
        try {
            Product p = (Product) cboProduct.getSelectedItem();
            int qty = Integer.parseInt(txtQty.getText());
            if (p == null) { JOptionPane.showMessageDialog(this, "Chưa chọn sản phẩm!"); return; }

            double total = p.getPriceOut() * qty;
            // Store the Product object directly in the first column
            modelCart.addRow(new Object[]{
                    p, // Cột 0 chứa Object Product (toString() sẽ hiển thị Tên)
                    String.format("%,.0f", p.getPriceOut()),
                    qty,
                    String.format("%,.0f", total)
            });
            updateTotal();
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Số lượng phải là số!"); }
    }

    private void updateTotal() {
        double sum = 0;
        for (int i = 0; i < modelCart.getRowCount(); i++) {
            String s = modelCart.getValueAt(i, 3).toString().replace(",", "").replace(" VNĐ", "").trim();
            sum += Double.parseDouble(s); 
        }
        lblTotal.setText("Tổng tiền: " + String.format("%,.0f", sum) + " VNĐ");
    }

    private void processPayment() {
        if (modelCart.getRowCount() == 0) { JOptionPane.showMessageDialog(this, "Giỏ hàng trống!"); return; }
        Customer cust = (Customer) cboCustomer.getSelectedItem();
        if (cust == null) { JOptionPane.showMessageDialog(this, "Chưa chọn khách!"); return; }

        // Tính tổng tiền CHÍNH XÁC từ Object (không dùng string hiển thị)
        double totalAmount = 0;
        java.util.List<Product> details = new java.util.ArrayList<>();
        
        for (int i = 0; i < modelCart.getRowCount(); i++) {
            Object obj = modelCart.getValueAt(i, 0); 
            if (obj instanceof Product) {
                Product originalP = (Product) obj;
                int qty = Integer.parseInt(modelCart.getValueAt(i, 2).toString());
                totalAmount += originalP.getPriceOut() * qty;
                
                Product p = new Product();
                p.setId(originalP.getId());
                p.setQuantity(qty);
                p.setPriceOut(originalP.getPriceOut());
                details.add(p);
            }
        }

        String input = JOptionPane.showInputDialog(this, 
            "Tổng: " + String.format("%,.0f", totalAmount) + " VNĐ\nKhách đưa?", 
            String.format("%.0f", totalAmount));
        if (input == null) return;

        try {
            // Xử lý thông minh: Loại bỏ mọi ký tự không phải số (để chấp nhận cả 100.000 và 100,000)
            String cleanInput = input.replaceAll("[^0-9]", "");
            if (cleanInput.isEmpty()) cleanInput = "0";
            
            double paid = Double.parseDouble(cleanInput);

            if (transDao.createInvoice(cust.getId(), details, paid)) {
                StringBuilder msg = new StringBuilder("Thanh toán thành công!\n");
                msg.append("Tổng tiền: ").append(String.format("%,.0f", totalAmount)).append("\n");
                msg.append("Khách đưa: ").append(String.format("%,.0f", paid)).append("\n");

                // So sánh, cho phép sai số nhỏ do làm tròn
                if (paid >= Math.round(totalAmount) - 10) {
                     msg.append("Tiền thừa: ").append(String.format("%,.0f", paid - totalAmount));
                } else {
                     msg.append("Nợ lại: ").append(String.format("%,.0f", totalAmount - paid));
                     msg.append("\n(Đã cộng vào công nợ khách hàng)");
                }

                JOptionPane.showMessageDialog(this, msg.toString());
                modelCart.setRowCount(0);
                updateTotal();
            } else { JOptionPane.showMessageDialog(this, "Lỗi server!"); }

        } catch (NumberFormatException e) { JOptionPane.showMessageDialog(this, "Số tiền sai! Nhập lại nhé."); }
    }
    
    private void setupButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(Theme.FONT_BOLD);
    }
}