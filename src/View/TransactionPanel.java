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
        // 2. Nút Thêm vào giỏ
        btnAdd.addActionListener(e -> addToCart());
        
        // 3. Nút Thanh Toán
        btnPay.addActionListener(e -> processPayment());

        // Load lần đầu
        loadComboBoxData();
    }

    private void processPayment() {
        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Giỏ hàng đang trống!");
            return;
        }

        Customer cust = (Customer) cboCustomer.getSelectedItem();
        if (cust == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng!");
            return;
        }

        // Tính tổng tiền
        double totalAmount = 0;
        for (int i = 0; i < model.getRowCount(); i++) {
            totalAmount += Double.parseDouble(model.getValueAt(i, 3).toString().replace(",", ""));
        }

        // Hỏi số tiền khách đưa
        String input = JOptionPane.showInputDialog(this, 
            "Tổng tiền: " + String.format("%,.0f", totalAmount) + " VNĐ\nKhách đưa bao nhiêu?", 
            String.format("%.0f", totalAmount));
            
        if (input == null) return; // Hủy bỏ

        try {
            double paid = Double.parseDouble(input.replace(",", ""));
            if (paid < 0) throw new NumberFormatException();

            // Tạo danh sách chi tiết
            java.util.List<Product> details = new java.util.ArrayList<>();
            for (int i = 0; i < model.getRowCount(); i++) {
                String name = model.getValueAt(i, 0).toString();
                int qty = Integer.parseInt(model.getValueAt(i, 2).toString());
                
                // Vì bảng không lưu ID, ta phải tìm lại ID từ tên (hoặc lưu ID ẩn trong model).
                // Cách đơn giản nhất hiện tại: Lấy ID từ ComboBox (nhưng rủi ro nếu user đổi selection)
                // -> FIX: Tìm trong danh sách Product của ComboBox xem cái nào trùng tên.
                // Để an toàn và nhanh, ta sẽ tìm trong ProductDAO hoặc loop qua ComboBox.
                // Ở đây demo loop qua ComboBox items (lưu ý hiệu năng nếu list lớn)
                String prodId = "";
                double priceOut = 0;
                
                for(int j=0; j< cboProduct.getItemCount(); j++) {
                     Product p = cboProduct.getItemAt(j);
                     if(p.getName().equals(name)) {
                         prodId = p.getId();
                         priceOut = p.getPriceOut();
                         break;
                     }
                }
                
                if(!prodId.isEmpty()) {
                    Product p = new Product();
                    p.setId(prodId);
                    p.setQuantity(qty);
                    p.setPriceOut(priceOut);
                    details.add(p);
                }
            }

            // Gọi DAO
            Dao.TransactionDAO transDao = new Dao.TransactionDAO();
            if (transDao.createInvoice(cust.getId(), details, paid)) {
                StringBuilder msg = new StringBuilder("Thanh toán thành công!\n");
                msg.append("Tổng tiền: ").append(String.format("%,.0f", totalAmount)).append("\n");
                msg.append("Khách đưa: ").append(String.format("%,.0f", paid)).append("\n");
                
                if (paid >= totalAmount) {
                    msg.append("Tiền thừa: ").append(String.format("%,.0f", paid - totalAmount));
                } else {
                    msg.append("Nợ lại: ").append(String.format("%,.0f", totalAmount - paid));
                    msg.append("\n(Đã cộng vào công nợ khách hàng)");
                }
                
                JOptionPane.showMessageDialog(this, msg.toString());
                
                // Clear giỏ hàng
                model.setRowCount(0);
                updateTotal();
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi khi lưu hóa đơn!");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số tiền không hợp lệ!");
        }
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