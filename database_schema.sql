IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'QL_Kho_Vat_Lieu')
BEGIN
    CREATE DATABASE QL_Kho_Vat_Lieu;
END
GO

USE QL_Kho_Vat_Lieu;
GO

-- 2. Xóa bảng cũ nếu tồn tại (để reset sạch)
-- Lưu ý: Thứ tự xóa phải ngược với thứ tự tạo (Delete con trước cha sau)
DROP TABLE IF EXISTS InvoiceDetails;
DROP TABLE IF EXISTS Invoices;
DROP TABLE IF EXISTS ImportDetails;
DROP TABLE IF EXISTS ImportReceipts;
DROP TABLE IF EXISTS Products;
DROP TABLE IF EXISTS Category;
DROP TABLE IF EXISTS Customers;
DROP TABLE IF EXISTS Accounts;
GO

-- 3. Bảng Tài Khoản (Accounts)
CREATE TABLE Accounts (
    Username VARCHAR(50) PRIMARY KEY,
    Password VARCHAR(100) NOT NULL, -- Sau này nên hash
    FullName NVARCHAR(100)
);

-- 4. Bảng Danh Mục (Category)
CREATE TABLE Category (
    Id INT IDENTITY(1,1) PRIMARY KEY,
    Name NVARCHAR(100) NOT NULL
);

-- 5. Bảng Khách Hàng (Customers)
CREATE TABLE Customers (
    Id INT IDENTITY(1,1) PRIMARY KEY,
    Name NVARCHAR(100) NOT NULL,
    Phone VARCHAR(20),
    Address NVARCHAR(200),
    Debt FLOAT DEFAULT 0 -- Công nợ
);

-- 6. Bảng Sản Phẩm / Vật Tư (Products)
CREATE TABLE Products (
    Id VARCHAR(20) PRIMARY KEY, -- Mã SP do người dùng nhập (VD: XM01)
    Name NVARCHAR(200) NOT NULL,
    CategoryId INT FOREIGN KEY REFERENCES Category(Id),
    Unit NVARCHAR(20), -- Đơn vị tính (Bao, Kg, Mét...)
    PriceIn FLOAT DEFAULT 0, -- Giá nhập
    PriceOut FLOAT DEFAULT 0, -- Giá bán
    Quantity INT DEFAULT 0, -- Tồn kho
    Status NVARCHAR(50) DEFAULT N'Còn hàng'
);

-- 7. Bảng Hóa Đơn Nhập (ImportReceipts)
CREATE TABLE ImportReceipts (
    Id INT IDENTITY(1,1) PRIMARY KEY,
    TotalAmount FLOAT,
    CreatedAt DATETIME DEFAULT GETDATE()
);

-- 8. Chi Tiết Nhập (ImportDetails)
CREATE TABLE ImportDetails (
    Id INT IDENTITY(1,1) PRIMARY KEY,
    ReceiptId INT FOREIGN KEY REFERENCES ImportReceipts(Id),
    ProductId VARCHAR(20) FOREIGN KEY REFERENCES Products(Id),
    Quantity INT,
    Price FLOAT
);

-- 9. Bảng Hóa Đơn Bán (Invoices)
CREATE TABLE Invoices (
    Id INT IDENTITY(1,1) PRIMARY KEY,
    CustomerId INT FOREIGN KEY REFERENCES Customers(Id),
    TotalAmount FLOAT,
    PaidAmount FLOAT DEFAULT 0, -- Số tiền khách thực trả
    PayMethod NVARCHAR(20), -- 'Đủ' hoặc 'Nợ'
    CreatedAt DATETIME DEFAULT GETDATE()
);

-- 10. Chi Tiết Hóa Đơn (InvoiceDetails)
CREATE TABLE InvoiceDetails (
    Id INT IDENTITY(1,1) PRIMARY KEY,
    InvoiceId INT FOREIGN KEY REFERENCES Invoices(Id),
    ProductId VARCHAR(20) FOREIGN KEY REFERENCES Products(Id),
    Quantity INT,
    Price FLOAT -- Giá bán tại thời điểm chốt đơn
);
GO
