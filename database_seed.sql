USE QL_Kho_Vat_Lieu;
GO

-- 1. TẠO TÀI KHOẢN (Accounts)
INSERT INTO Accounts (Username, Password, FullName) VALUES ('admin', '123123', N'Quản Trị Viên');
INSERT INTO Accounts (Username, Password, FullName) VALUES ('nhanvien', '123', N'Trần Văn Bán Hàng');
INSERT INTO Accounts (Username, Password, FullName) VALUES ('quanly', '123123', N'Lê Thị Quản Lý');

-- 2. TẠO DANH MỤC (Category)
-- ID tự tăng, bắt đầu dự kiến từ 1
INSERT INTO Category (Name) VALUES (N'Xi Măng');       -- 1
INSERT INTO Category (Name) VALUES (N'Sắt Thép');      -- 2
INSERT INTO Category (Name) VALUES (N'Gạch Xây Dựng'); -- 3
INSERT INTO Category (Name) VALUES (N'Cát, Đá, Sỏi');  -- 4
INSERT INTO Category (Name) VALUES (N'Sơn & Chống Thấm'); -- 5
INSERT INTO Category (Name) VALUES (N'Thiết Bị Điện'); -- 6
INSERT INTO Category (Name) VALUES (N'Ống Nước & Phụ Kiện'); -- 7
INSERT INTO Category (Name) VALUES (N'Trần Thạch Cao'); -- 8

-- 3. TẠO KHÁCH HÀNG (Customers)
INSERT INTO Customers (Name, Phone, Address, Debt) VALUES (N'Nguyễn Văn An', '0909123456', N'123 Lê Lợi, Q1, TP.HCM', 0);
INSERT INTO Customers (Name, Phone, Address, Debt) VALUES (N'CTY Xây Dựng Đại Phát', '02838999888', N'KCN Tân Bình, TP.HCM', 15000000);
INSERT INTO Customers (Name, Phone, Address, Debt) VALUES (N'Trần Thị Bích', '0912345678', N'45 Nguyễn Trãi, Thanh Xuân, Hà Nội', 0);
INSERT INTO Customers (Name, Phone, Address, Debt) VALUES (N'Lê Văn Cường (Thầu)', '0987654321', N'Công trình Dự án SkyView', 5000000);
INSERT INTO Customers (Name, Phone, Address, Debt) VALUES (N'Phạm Minh Duy', '0933445566', N'12/5 Phan Văn Trị, Gò Vấp', 200000);
INSERT INTO Customers (Name, Phone, Address, Debt) VALUES (N'Hoàng Thị Em', '0901239876', N'Đà Nẵng', 0);
INSERT INTO Customers (Name, Phone, Address, Debt) VALUES (N'Vũ Trọng Phụng', '0911223344', N'Hải Phòng', 0);
INSERT INTO Customers (Name, Phone, Address, Debt) VALUES (N'Đặng Lê Nguyên', '0977889900', N'Buôn Ma Thuột', 1200000);
INSERT INTO Customers (Name, Phone, Address, Debt) VALUES (N'Khách Lẻ (Vãng Lai)', '', '', 0);

-- 4. TẠO SẢN PHẨM (Products)
-- Xi măng (CatId=1)
INSERT INTO Products (Id, Name, CategoryId, Unit, PriceIn, PriceOut, Quantity) VALUES ('XM01', N'Xi măng Hà Tiên Đa Dụng', 1, N'Bao 50kg', 82000, 92000, 200);
INSERT INTO Products (Id, Name, CategoryId, Unit, PriceIn, PriceOut, Quantity) VALUES ('XM02', N'Xi măng Insee (Holcim)', 1, N'Bao 50kg', 85000, 96000, 150);
INSERT INTO Products (Id, Name, CategoryId, Unit, PriceIn, PriceOut, Quantity) VALUES ('XM03', N'Xi măng Nghi Sơn', 1, N'Bao 50kg', 78000, 88000, 100);
INSERT INTO Products (Id, Name, CategoryId, Unit, PriceIn, PriceOut, Quantity) VALUES ('XM04', N'Xi măng Trắng Thái Bình', 1, N'Bao 40kg', 120000, 150000, 30);

-- Sắt thép (CatId=2)
INSERT INTO Products (Id, Name, CategoryId, Unit, PriceIn, PriceOut, Quantity) VALUES ('TP-D10', N'Thép Pomina D10', 2, N'Cây', 105000, 115000, 500);
INSERT INTO Products (Id, Name, CategoryId, Unit, PriceIn, PriceOut, Quantity) VALUES ('TP-D16', N'Thép Pomina D16', 2, N'Cây', 250000, 280000, 200);
INSERT INTO Products (Id, Name, CategoryId, Unit, PriceIn, PriceOut, Quantity) VALUES ('THP-D6', N'Thép cuộn Hòa Phát D6', 2, N'Kg', 18000, 22000, 1000);
INSERT INTO Products (Id, Name, CategoryId, Unit, PriceIn, PriceOut, Quantity) VALUES ('TV-D12', N'Thép Việt Nhật D12', 2, N'Cây', 140000, 160000, 300);

-- Gạch (CatId=3)
INSERT INTO Products (Id, Name, CategoryId, Unit, PriceIn, PriceOut, Quantity) VALUES ('G-ONG4', N'Gạch 4 lỗ Tuynel', 3, N'Viên', 1100, 1300, 10000);
INSERT INTO Products (Id, Name, CategoryId, Unit, PriceIn, PriceOut, Quantity) VALUES ('G-THE', N'Gạch thẻ đặc', 3, N'Viên', 1500, 1800, 5000);
INSERT INTO Products (Id, Name, CategoryId, Unit, PriceIn, PriceOut, Quantity) VALUES ('G-6060', N'Gạch lát nền 60x60 Prime', 3, N'Thùng', 160000, 210000, 80);

-- Cát Đá (CatId=4)
INSERT INTO Products (Id, Name, CategoryId, Unit, PriceIn, PriceOut, Quantity) VALUES ('C-XAY', N'Cát xây tô', 4, N'Khối', 350000, 450000, 50);
INSERT INTO Products (Id, Name, CategoryId, Unit, PriceIn, PriceOut, Quantity) VALUES ('C-BE', N'Cát bê tông vàng', 4, N'Khối', 420000, 550000, 40);
INSERT INTO Products (Id, Name, CategoryId, Unit, PriceIn, PriceOut, Quantity) VALUES ('D-1x2', N'Đá xanh 1x2', 4, N'Khối', 380000, 480000, 35);

-- Sơn (CatId=5)
INSERT INTO Products (Id, Name, CategoryId, Unit, PriceIn, PriceOut, Quantity) VALUES ('SON-DL18', N'Sơn Dulux Weathershield 18L', 5, N'Thùng', 2500000, 2900000, 10);
INSERT INTO Products (Id, Name, CategoryId, Unit, PriceIn, PriceOut, Quantity) VALUES ('SON-KVA', N'Chống thấm Kova CT-11A', 5, N'Thùng 20kg', 1900000, 2200000, 15);
INSERT INTO Products (Id, Name, CategoryId, Unit, PriceIn, PriceOut, Quantity) VALUES ('SON-NIPPON', N'Sơn nội thất Nippon 5L', 5, N'Lon', 300000, 450000, 25);

-- Điện/Nước (CatId=6, 7)
INSERT INTO Products (Id, Name, CategoryId, Unit, PriceIn, PriceOut, Quantity) VALUES ('DAY-2.5', N'Dây điện Cadivi 2.5', 6, N'Cuộn', 580000, 650000, 40);
INSERT INTO Products (Id, Name, CategoryId, Unit, PriceIn, PriceOut, Quantity) VALUES ('ONG-27', N'Ống Bình Minh phi 27', 7, N'Cây 4m', 35000, 45000, 100);


-- 5. TẠO HÓA ĐƠN LỊCH SỬ (Invoices)
-- Đơn 1: CTY Đại Phát mua nhiều
INSERT INTO Invoices (CustomerId, TotalAmount, PayMethod, CreatedAt) VALUES (2, 28000000, N'Nợ', DATEADD(day, -5, GETDATE()));
-- Chi tiết đơn 1
INSERT INTO InvoiceDetails (InvoiceId, ProductId, Quantity, Price) VALUES (1, 'TP-D16', 100, 280000);

-- Đơn 2: Khách A mua gạch cát
INSERT INTO Invoices (CustomerId, TotalAmount, PayMethod, CreatedAt) VALUES (1, 5650000, N'Đủ', DATEADD(day, -3, GETDATE()));
INSERT INTO InvoiceDetails (InvoiceId, ProductId, Quantity, Price) VALUES (2, 'G-ONG4', 1000, 1300); -- 1.3tr
INSERT INTO InvoiceDetails (InvoiceId, ProductId, Quantity, Price) VALUES (2, 'C-XAY', 5, 450000);  -- 2.25tr
INSERT INTO InvoiceDetails (InvoiceId, ProductId, Quantity, Price) VALUES (2, 'XM01', 20, 92000);   -- 1.84tr

-- Đơn 3: Khách vãng lai mua lẻ
INSERT INTO Invoices (CustomerId, TotalAmount, PayMethod, CreatedAt) VALUES (9, 137000, N'Đủ', DATEADD(day, -2, GETDATE()));
INSERT INTO InvoiceDetails (InvoiceId, ProductId, Quantity, Price) VALUES (3, 'XM01', 1, 92000);
INSERT INTO InvoiceDetails (InvoiceId, ProductId, Quantity, Price) VALUES (3, 'ONG-27', 1, 45000);

-- Đơn 4: Thầu Cường mua sơn
INSERT INTO Invoices (CustomerId, TotalAmount, PayMethod, CreatedAt) VALUES (4, 5800000, N'Đủ', DATEADD(day, -1, GETDATE()));
INSERT INTO InvoiceDetails (InvoiceId, ProductId, Quantity, Price) VALUES (4, 'SON-DL18', 2, 2900000);

-- Đơn 5: Mới sáng nay
INSERT INTO Invoices (CustomerId, TotalAmount, PayMethod, CreatedAt) VALUES (3, 22000, N'Đủ', GETDATE());
INSERT INTO InvoiceDetails (InvoiceId, ProductId, Quantity, Price) VALUES (5, 'THP-D6', 1, 22000);

GO
