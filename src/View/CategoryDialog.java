package View;

import Dao.CategoryDAO;
import Model.Category;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CategoryDialog extends JDialog {
    private JTable table;
    private DefaultTableModel model;
    private CategoryDAO dao = new CategoryDAO();

    public CategoryDialog(JFrame parent) {
        super(parent, "Quản Lý Danh Mục (Loại)", true);
        setSize(500, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        // --- TOP: Nút chức năng ---
        JPanel pnlTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAdd = new JButton("Thêm Loại");
        JButton btnEdit = new JButton("Đổi Tên");
        JButton btnDel = new JButton("Xóa");

        styleButton(btnAdd, Theme.BTN_PRIMARY);
        styleButton(btnEdit, Theme.BTN_INFO);
        styleButton(btnDel, Theme.BTN_DANGER);

        pnlTop.add(btnAdd);
        pnlTop.add(btnEdit);
        pnlTop.add(btnDel);
        add(pnlTop, BorderLayout.NORTH);

        // --- CENTER: Bảng danh sách ---
        String[] cols = {"ID", "Tên Loại", "Số Lượng SP"};
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        table.setRowHeight(25);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // --- BOTTOM: Hướng dẫn ---
        JLabel lblHint = new JLabel("  * Xóa loại sẽ thất bại nếu đang có sản phẩm thuộc loại đó.");
        lblHint.setForeground(Color.GRAY);
        lblHint.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        add(lblHint, BorderLayout.SOUTH);

        // --- SỰ KIỆN ---
        btnAdd.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(this, "Nhập tên loại mới:");
            if(name != null && !name.trim().isEmpty()) {
                Category c = new Category();
                c.setName(name);
                if(dao.insert(c)) {
                    loadData();
                    JOptionPane.showMessageDialog(this, "Thêm thành công!");
                } else {
                    JOptionPane.showMessageDialog(this, "Lỗi thêm mới!");
                }
            }
        });

        btnEdit.addActionListener(e -> {
            int r = table.getSelectedRow();
            if(r == -1) {
                JOptionPane.showMessageDialog(this, "Chọn loại cần sửa!");
                return;
            }
            int id = Integer.parseInt(table.getValueAt(r, 0).toString());
            String oldName = table.getValueAt(r, 1).toString();
            
            String newName = JOptionPane.showInputDialog(this, "Nhập tên mới:", oldName);
            if(newName != null && !newName.trim().isEmpty()) {
                Category c = new Category(id, newName);
                if(dao.update(c)) {
                    loadData();
                    JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                } else {
                    JOptionPane.showMessageDialog(this, "Lỗi cập nhật!");
                }
            }
        });

        btnDel.addActionListener(e -> {
            int r = table.getSelectedRow();
            if(r == -1) {
                JOptionPane.showMessageDialog(this, "Chọn loại cần xóa!");
                return;
            }
            int id = Integer.parseInt(table.getValueAt(r, 0).toString());
            if(JOptionPane.showConfirmDialog(this, "Bạn chắc chắn muốn xóa?") == JOptionPane.YES_OPTION) {
                if(dao.delete(id)) {
                    loadData();
                    JOptionPane.showMessageDialog(this, "Xóa thành công!");
                } else {
                    JOptionPane.showMessageDialog(this, "Không thể xóa (Đang chứa sản phẩm).");
                }
            }
        });

        loadData();
    }

    private void loadData() {
        model.setRowCount(0);
        List<Category> list = dao.getAll();
        for (Category c : list) {
            // Cột số lượng SP hiện tại chưa có, tạm để trống hoặc 0
            model.addRow(new Object[]{c.getId(), c.getName(), "-"});
        }
    }

    private void styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(Theme.FONT_BOLD);
    }
}
