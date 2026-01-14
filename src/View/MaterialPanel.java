package View;

import Dao.MaterialDAO;
import Dao.CategoryDAO;
import Model.Product;
import Model.Category;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class MaterialPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private MaterialDAO dao = new MaterialDAO();
    private CategoryDAO catDao = new CategoryDAO();
    private JTextField txtSearch;

    public MaterialPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(Theme.CONTENT_BG); // Màu nền chuẩn từ Theme
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- TOP: Tìm kiếm & Nút bấm ---
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT));
        left.setOpaque(false);
        txtSearch = new JTextField(20);
        txtSearch.setPreferredSize(new Dimension(200, 35));
        txtSearch.setFont(Theme.FONT_REGULAR);
        txtSearch.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) { loadData(txtSearch.getText()); }
        });
        left.add(new JLabel("Tìm kiếm: ")); left.add(txtSearch);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        right.setOpaque(false);
        JButton btnAdd = new JButton("Thêm Mới");
        JButton btnEdit = new JButton("Sửa"); 
        JButton btnDel = new JButton("Xóa");
        JButton btnCat = new JButton("Quản Lý Loại");

        // Style nút từ Theme
        setupButton(btnAdd, Theme.BTN_PRIMARY);
        setupButton(btnEdit, Theme.BTN_INFO);
        setupButton(btnDel, Theme.BTN_DANGER);
        setupButton(btnCat, Theme.BTN_SPECIAL);

        right.add(btnAdd); right.add(btnEdit); right.add(btnDel); right.add(btnCat);

        top.add(left, BorderLayout.WEST);
        top.add(right, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);

        // --- CENTER: Bảng dữ liệu ---
        String[] cols = {"Mã VT", "Tên Vật Tư", "Loại", "Đơn Vị", "Giá Nhập", "Giá Bán", "Tồn Kho"};
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        table.setRowHeight(30);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // --- SỰ KIỆN ---
        btnAdd.addActionListener(e -> showDialog(null)); // null = Thêm mới
        btnCat.addActionListener(e -> {
            new CategoryDialog((JFrame)SwingUtilities.getWindowAncestor(this)).setVisible(true);
            // Sau khi đóng dialog quản lý loại, có thể user đã xóa/sửa loại, nên load lại data nếu cần
            loadData(""); 
        });

        btnEdit.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn vật tư cần sửa!");
                return;
            }
            // Lấy ID từ dòng đã chọn để tìm và sửa
            String id = table.getValueAt(r, 0).toString();
            // Tìm đối tượng trong list hiện tại (hoặc load lại từ DB)
            // Ở đây ta lấy nhanh từ bảng để demo, chuẩn là nên gọi DAO.getById
            Product p = new Product();
            p.setId(id);
            p.setName(table.getValueAt(r, 1).toString());
            // Lưu ý: ComboBox cần object Category chuẩn, ở đây ta sẽ xử lý trong dialog
            p.setUnit(table.getValueAt(r, 3).toString());
            p.setPriceIn(Double.parseDouble(table.getValueAt(r, 4).toString().replace(",", "")));
            p.setPriceOut(Double.parseDouble(table.getValueAt(r, 5).toString().replace(",", "")));
            p.setQuantity(Integer.parseInt(table.getValueAt(r, 6).toString()));

            showDialog(p); // Truyền object vào để sửa
        });

        btnDel.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r != -1 && JOptionPane.showConfirmDialog(this, "Xóa vật tư này?") == 0) {
                if (dao.delete(table.getValueAt(r, 0).toString())) loadData("");
            }
        });

        loadData("");
    }

    private void loadData(String key) {
        model.setRowCount(0);
        List<Product> list = key.isEmpty() ? dao.getAll() : dao.search(key);
        for (Product p : list) {
            model.addRow(new Object[]{
                    p.getId(), p.getName(), p.getCategoryName(), p.getUnit(),
                    p.getPriceIn(), p.getPriceOut(), p.getQuantity()
            });
        }
    }

    // Hàm hiển thị Dialog dùng chung cho Thêm và Sửa
    private void showDialog(Product p) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                (p == null ? "Thêm Vật Tư" : "Sửa Vật Tư"), true);
        dialog.setSize(400, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridLayout(8, 2, 10, 10));

        JTextField txtId = new JTextField(p != null ? p.getId() : "");
        JTextField txtName = new JTextField(p != null ? p.getName() : "");
        JTextField txtUnit = new JTextField(p != null ? p.getUnit() : "Cái");
        JTextField txtPriceIn = new JTextField(p != null ? String.valueOf(p.getPriceIn()) : "0");
        JTextField txtPriceOut = new JTextField(p != null ? String.valueOf(p.getPriceOut()) : "0");
        JTextField txtQty = new JTextField(p != null ? String.valueOf(p.getQuantity()) : "0");

        // ComboBox Loại vật tư
        JComboBox<Category> cboCat = new JComboBox<>();
        List<Category> cats = catDao.getAll();
        for (Category c : cats) {
            cboCat.addItem(c);
            // Nếu đang sửa, chọn lại đúng loại cũ
            if (p != null && c.getName().equals(table.getValueAt(table.getSelectedRow(), 2))) {
                cboCat.setSelectedItem(c);
            }
        }

        if (p != null) txtId.setEditable(false); // Không cho sửa ID

        dialog.add(new JLabel("  Mã Vật Tư:")); dialog.add(txtId);
        dialog.add(new JLabel("  Tên Vật Tư:")); dialog.add(txtName);
        dialog.add(new JLabel("  Loại:")); dialog.add(cboCat);
        dialog.add(new JLabel("  Đơn Vị:")); dialog.add(txtUnit);
        dialog.add(new JLabel("  Giá Nhập:")); dialog.add(txtPriceIn);
        dialog.add(new JLabel("  Giá Bán:")); dialog.add(txtPriceOut);
        dialog.add(new JLabel("  Tồn Kho:")); dialog.add(txtQty);

        JButton btnSave = new JButton("Lưu");
        dialog.add(new JLabel("")); dialog.add(btnSave);

        btnSave.addActionListener(e -> {
            try {
                Product newP = new Product();
                newP.setId(txtId.getText());
                newP.setName(txtName.getText());
                newP.setCategoryId(((Category)cboCat.getSelectedItem()).getId());
                newP.setUnit(txtUnit.getText());
                newP.setPriceIn(Double.parseDouble(txtPriceIn.getText()));
                newP.setPriceOut(Double.parseDouble(txtPriceOut.getText()));
                newP.setQuantity(Integer.parseInt(txtQty.getText()));

                boolean ok;
                if (p == null) ok = dao.insert(newP); // Thêm
                else ok = dao.update(newP); // Sửa

                if (ok) {
                    JOptionPane.showMessageDialog(dialog, "Thành công!");
                    dialog.dispose();
                    loadData("");
                } else {
                    JOptionPane.showMessageDialog(dialog, "Lỗi! Kiểm tra lại mã hoặc dữ liệu.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng nhập số hợp lệ cho giá và số lượng!");
            }
        });

        dialog.setVisible(true);
    }
    // Hàm helper để style nút gọn gàng
    private void setupButton(JButton btn, Color bgColor) {
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(Theme.FONT_BOLD);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}