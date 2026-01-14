package View;

import Dao.CustomerDAO;
import Model.Customer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

// QUAN TRỌNG: Phải có "extends JPanel" thì mới là giao diện được
public class CustomerPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private CustomerDAO dao = new CustomerDAO();

    public CustomerPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(245, 246, 250));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- TOP: Nút thêm ---
        JPanel top = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        top.setOpaque(false);
        JButton btnAdd = new JButton("Thêm Khách Hàng");
        btnAdd.setBackground(new Color(9, 132, 227));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setOpaque(true);
        btnAdd.setBorderPainted(false);
        top.add(btnAdd);
        add(top, BorderLayout.NORTH);

        // --- CENTER: Bảng ---
        String[] cols = {"Mã KH", "Tên Khách Hàng", "SĐT", "Địa Chỉ", "Công Nợ"};
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        table.setRowHeight(30);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // --- SỰ KIỆN ---
        btnAdd.addActionListener(e -> showDialog());

        // Load dữ liệu ban đầu
        loadData();
    }

    private void loadData() {
        model.setRowCount(0);
        for (Customer c : dao.getAll()) {
            model.addRow(new Object[]{
                    c.getId(),
                    c.getName(),
                    c.getPhone(),
                    c.getAddress(),
                    String.format("%,.0f", c.getDebt())
            });
        }
    }

    private void showDialog() {
        JTextField txtName = new JTextField();
        JTextField txtPhone = new JTextField();
        JTextField txtAddr = new JTextField();

        Object[] msg = {"Tên KH:", txtName, "SĐT:", txtPhone, "Địa chỉ:", txtAddr};

        // Giờ "this" đã là JPanel (Component) nên lệnh này sẽ không lỗi nữa
        if (JOptionPane.showConfirmDialog(this, msg, "Thêm Khách Hàng", JOptionPane.OK_CANCEL_OPTION) == 0) {
            Customer c = new Customer(0, txtName.getText(), txtPhone.getText(), txtAddr.getText(), 0);
            if (dao.insert(c)) {
                JOptionPane.showMessageDialog(this, "Thêm thành công!");
                loadData();
            }
        }
    }
}