# Hướng dẫn Triển khai (Deployment Guide) - Phase 1: MySQL

Thư mục `deploy/` này chứa toàn bộ hạ tầng (Infrastructure) cần thiết để chạy dự án Spring Boot cho **Phase 1: MySQL Multi-Tenant**. Mọi cấu hình đã được đóng gói chuẩn mực.

## 1. Kiến trúc Hạ tầng
Hệ thống sử dụng Docker Compose để dựng 2 cụm MySQL hoàn toàn độc lập:
- **Tenant A (Primary)**: Mở ở Port `3306`. (Dữ liệu lưu tại `volumes/db1/`)
- **Tenant B (Secondary)**: Mở ở Port `3307`. (Dữ liệu lưu tại `volumes/db2/`)

Cả 2 cụm đều được tự động mount file SQL khởi tạo từ thư mục `mysql/schema/init.sql` để tạo sẵn bảng `users` và dữ liệu mẫu.

## 2. Các Bước Triển Khai Tuần Tự

### Bước 1: Khởi động Hạ tầng Database (Docker)
Mở Terminal, di chuyển vào thư mục `deploy` và chạy lệnh sau để khởi động 2 cụm MySQL chạy ngầm:

```bash
docker compose up -d
```
> **Lưu ý:** Nếu bạn chạy lần đầu, Docker sẽ tốn khoảng 10-20 giây để pull image `mysql:8.0.41` và chạy file `init.sql`.

Để kiểm tra xem 2 database đã sẵn sàng chưa, bạn có thể chạy:
```bash
docker compose ps
```
Nếu thấy State là `Up` ở cả 2 container là thành công.

### Bước 2: Khởi động Ứng dụng Web (Spring Boot)
Giữ nguyên Terminal (hoặc mở Tab Terminal mới), lùi ra ngoài một thư mục (về gốc `p1_mysql`) và khởi chạy Spring Boot:

```bash
cd ..
mvn clean spring-boot:run
```

### Bước 3: Trải nghiệm Giao diện Multi-Tenant
Mở trình duyệt Web của bạn và truy cập vào:
👉 **[http://localhost:8081](http://localhost:8081)**

Bạn sẽ thấy một bảng điều khiển (Dashboard) chia làm 2 cột rõ rệt. Hãy thử thêm, sửa, xóa người dùng ở từng cột để thấy rõ sự cô lập dữ liệu tuyệt đối giữa 2 kết nối (Tenant A và Tenant B).

## 3. Dọn dẹp Dữ liệu (Reset)
Nếu bạn muốn xóa sạch toàn bộ dữ liệu để làm lại từ đầu (bao gồm cả volumes ẩn):
```bash
docker compose down -v
```
