package View;

import Dao.CustomerDAO;
import Dao.MaterialDAO;
import Model.Customer;
import Model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class TransactionPanel extends JPanel {
    private JComboBox<Customer> cboCustomer;
    private JComboBox<Product> cboProduct;
    private JTextField txtQty;
    private JTable table;
    private DefaultTableModel model;
    private JLabel lblTotal;

    private CustomerDAO customerDao = new CustomerDAO();
    private MaterialDAO productDao = new MaterialDAO();

    public TransactionPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(245, 246, 250));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- KHU VỰC NHẬP LIỆU (NORTH) ---
        JPanel pnlInput = new JPanel(new GridLayout(2, 4, 10, 10));
        pnlInput.setOpaque(false);
        pnlInput.setBorder(BorderFactory.createTitledBorder("Thông tin giao dịch"));

        // Các component
        cboCustomer = new JComboBox<>();
        cboProduct = new JComboBox<>();
        txtQty = new JTextField("1");

        JButton btnAdd = new JButton("Thêm vào giỏ");
        btnAdd.setBackground(new Color(9, 132, 227));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setOpaque(true);
        btnAdd.setBorderPainted(false);

        JButton btnRefresh = new JButton("Làm mới DS"); // Nút này để load lại dữ liệu mới nhập
        btnRefresh.setBackground(new Color(0, 184, 148));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setOpaque(true);
        btnRefresh.setBorderPainted(false);

        // Hàng 1
        pnlInput.add(new JLabel("Khách hàng:"));
        pnlInput.add(cboCustomer);
        pnlInput.add(new JLabel("Sản phẩm:"));
        pnlInput.add(cboProduct);

        // Hàng 2
        pnlInput.add(new JLabel("Số lượng:"));
        pnlInput.add(txtQty);
        pnlInput.add(btnRefresh); // Nút làm mới
        pnlInput.add(btnAdd);

        add(pnlInput, BorderLayout.NORTH);

        // --- KHU VỰC BẢNG GIỎ HÀNG (CENTER) ---
        String[] cols = {"Tên Sản Phẩm", "Đơn Giá", "Số Lượng", "Thành Tiền"};
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        table.setRowHeight(25);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // --- KHU VỰC TỔNG TIỀN (SOUTH) ---
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlBottom.setOpaque(false);
        lblTotal = new JLabel("Tổng tiền: 0 VNĐ");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 18));
        lblTotal.setForeground(Color.RED);

        JButton btnPay = new JButton("Thanh Toán & Lưu");
        btnPay.setPreferredSize(new Dimension(150, 40));
        btnPay.setBackground(new Color(214, 48, 49));
        btnPay.setForeground(Color.WHITE);
        btnPay.setOpaque(true);
        btnPay.setBorderPainted(false);

        pnlBottom.add(lblTotal);
        pnlBottom.add(Box.createHorizontalStrut(20));
        pnlBottom.add(btnPay);
        add(pnlBottom, BorderLayout.SOUTH);

        // --- SỰ KIỆN ---

        // 1. Nút Làm Mới: Bấm phát là load lại dữ liệu mới nhất từ DB
        btnRefresh.addActionListener(e -> loadComboBoxData());

        // 2. Nút Thêm vào giỏ
        btnAdd.addActionListener(e -> addToCart());

        // Load lần đầu
        loadComboBoxData();
    }

    // Hàm tải dữ liệu vào ComboBox
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

            if (p == null) {
                JOptionPane.showMessageDialog(this, "Chưa chọn sản phẩm!");
                return;
            }

            double total = p.getPriceOut() * qty;

            // Thêm vào bảng
            model.addRow(new Object[]{
                    p.getName(),
                    String.format("%,.0f", p.getPriceOut()),
                    qty,
                    String.format("%,.0f", total)
            });

            updateTotal();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Số lượng phải là số!");
        }
    }

    private void updateTotal() {
        double sum = 0;
        for (int i = 0; i < model.getRowCount(); i++) {
            String s = model.getValueAt(i, 3).toString().replace(",", ""); // Bỏ dấu phẩy
            sum += Double.parseDouble(s);
        }
        lblTotal.setText("Tổng tiền: " + String.format("%,.0f", sum) + " VNĐ");
    }
}