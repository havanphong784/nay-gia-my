package Dao;
import Model.Product;
import Data.DBConnection;
import java.sql.*;
import java.util.List;

public class TransactionDAO {
    // --- 1. CHỨC NĂNG NHẬP HÀNG (IMPORT) ---
    public boolean createImport(List<Product> details) {
        String sqlReceipt = "INSERT INTO ImportReceipts (TotalAmount) VALUES (?)";
        String sqlDetail = "INSERT INTO ImportDetails (ReceiptId, ProductId, Quantity, Price) VALUES (?, ?, ?, ?)";
        String sqlUpdateStock = "UPDATE Products SET Quantity = Quantity + ?, PriceIn = ? WHERE Id = ?";

        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) return false;
            conn.setAutoCommit(false);

            try (PreparedStatement psReceipt = conn.prepareStatement(sqlReceipt, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement psDetail = conn.prepareStatement(sqlDetail);
                 PreparedStatement psStock = conn.prepareStatement(sqlUpdateStock)) {

                double total = 0;
                for (Product p : details) total += p.getPriceIn() * p.getQuantity();

                psReceipt.setDouble(1, total);
                psReceipt.executeUpdate();

                int receiptId = 0;
                try (ResultSet rs = psReceipt.getGeneratedKeys()) {
                    if (rs.next()) receiptId = rs.getInt(1);
                }

                for (Product p : details) {
                    psDetail.setInt(1, receiptId);
                    psDetail.setString(2, p.getId());
                    psDetail.setInt(3, p.getQuantity());
                    psDetail.setDouble(4, p.getPriceIn());
                    psDetail.addBatch();

                    psStock.setInt(1, p.getQuantity());
                    psStock.setDouble(2, p.getPriceIn());
                    psStock.setString(3, p.getId());
                    psStock.addBatch();
                }

                psDetail.executeBatch();
                psStock.executeBatch();

                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // --- 2. CHỨC NĂNG BÁN HÀNG (INVOICE) ---
    public boolean createInvoice(int customerId, List<Product> details, double paidAmount) {
        String sqlInvoice = "INSERT INTO Invoices (CustomerId, TotalAmount, PayMethod) VALUES (?, ?, ?)";
        String sqlDetail = "INSERT INTO InvoiceDetails (InvoiceId, ProductId, Quantity, Price) VALUES (?, ?, ?, ?)";
        String sqlUpdateStock = "UPDATE Products SET Quantity = Quantity - ? WHERE Id = ?";
        String sqlUpdateDebt = "UPDATE Customers SET Debt = Debt + ? WHERE Id = ?";

        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) return false;
            conn.setAutoCommit(false);

            try (PreparedStatement psInvoice = conn.prepareStatement(sqlInvoice, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement psDetail = conn.prepareStatement(sqlDetail);
                 PreparedStatement psStock = conn.prepareStatement(sqlUpdateStock)) {

                double total = 0;
                for (Product p : details) total += p.getPriceOut() * p.getQuantity();

                psInvoice.setInt(1, customerId);
                psInvoice.setDouble(2, total);
                psInvoice.setString(3, paidAmount >= total ? "Đủ" : "Nợ");
                psInvoice.executeUpdate();

                int invoiceId = 0;
                try (ResultSet rs = psInvoice.getGeneratedKeys()) {
                    if (rs.next()) invoiceId = rs.getInt(1);
                }

                for (Product p : details) {
                    psDetail.setInt(1, invoiceId);
                    psDetail.setString(2, p.getId());
                    psDetail.setInt(3, p.getQuantity());
                    psDetail.setDouble(4, p.getPriceOut());
                    psDetail.addBatch();

                    psStock.setInt(1, p.getQuantity());
                    psStock.setString(2, p.getId());
                    psStock.addBatch();
                }
                psDetail.executeBatch();
                psStock.executeBatch();

                double debt = total - paidAmount;
                if (debt > 0) {
                    try (PreparedStatement psDebt = conn.prepareStatement(sqlUpdateDebt)) {
                        psDebt.setDouble(1, debt);
                        psDebt.setInt(2, customerId);
                        psDebt.executeUpdate();
                    }
                }

                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // --- 3. THỐNG KÊ DOANH THU ---
    public Object[] getRevenueStats() {
        Object[] stats = {0, 0.0}; // [0]: Số đơn hàng, [1]: Tổng doanh thu
        try (Connection conn = DBConnection.getConnection()) {
            // Đếm số đơn bán
            try (PreparedStatement p1 = conn.prepareStatement("SELECT COUNT(*) FROM Invoices");
                 ResultSet r1 = p1.executeQuery()) {
                if(r1.next()) stats[0] = r1.getInt(1);
            }

            // Tổng tiền bán được
            try (PreparedStatement p2 = conn.prepareStatement("SELECT SUM(TotalAmount) FROM Invoices");
                 ResultSet r2 = p2.executeQuery()) {
                if(r2.next()) stats[1] = r2.getDouble(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stats;
    }
}
