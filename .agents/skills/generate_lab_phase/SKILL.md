---
name: create_lab_phase
description: "Sử dụng skill này khi cần khởi tạo hoặc xây dựng một Phase (hạ tầng) mới trong Infrastructure Connectors Lab."
---

# Hướng dẫn tạo Phase mới (Infrastructure Connectors Lab)

Skill này chứa toàn bộ các quy chuẩn, bài học kinh nghiệm và cách né bug từ Phase 1 và Phase 2 để áp dụng cho tất cả các Phase hạ tầng tiếp theo. Khi được yêu cầu tạo Phase mới (ví dụ Redis, MongoDB, Kafka, v.v.), Agent bắt buộc tuân thủ luồng làm việc này.

## 1. Cấu Trúc Dự Án (Project Structure)
- **Độc Lập**: Mỗi Phase là một thư mục riêng biệt ở thư mục gốc (ví dụ: `p3_redis`, `p4_mongodb`), chứa project Spring Boot hoàn toàn độc lập (có `pom.xml` riêng). 
- **Không Chia Sẻ Code**: Không phụ thuộc vào thư mục gốc hay các Phase trước, nhằm đảm bảo bài Lab nào cũng có thể run độc lập.

## 2. Tiêu Chuẩn Triển Khai (Deployment Standards)
- Mọi hạ tầng liên quan (Docker Compose, script khởi tạo dữ liệu, gitignore volume) PHẢI nằm trong thư mục `deploy/` của phase đó.
- Cấu trúc `deploy/`:
  - `docker-compose.yml`: Bắt buộc khai báo 2 container đại diện cho Tenant A và Tenant B với 2 port khác nhau.
  - `<infra_name>/schema/init.sql` (hoặc script tương ứng của hạ tầng đó): Khởi tạo schema và dữ liệu tự động.
  - `volumes/`: Map dữ liệu của container vào đây.
  - `.gitignore`: Bỏ qua thư mục `volumes/` để không đẩy rác lên Git.

## 3. Kiến Trúc Cấu Hình Đa Luồng (Explicit Multi-Tenant Configuration)
- **Quy tắc Vàng**: Tuyệt đối **KHÔNG** dùng cơ chế Auto-Configuration mặc định của Spring (như `spring.datasource.*`, `spring.redis.*`). 
- **Sử Dụng @Bean**: Phải tạo thủ công các `@Bean` ConnectionFactory/DataSource trong package `config` (ví dụ `DatabaseConfig.java`).
- **Sử Dụng @Value**: Mọi tham số kết nối phải được tiêm tường minh bằng `@Value` từ `application.yml` (ví dụ `custom.datasource.db1.*`).

## 4. Giao Diện & API Mặc Định (Thymeleaf UI)
- Mỗi Phase bắt buộc có một bảng điều khiển UI bằng Thymeleaf (`index.html`) và `style.css`.
- **Giao diện 2 Cột**: Hiển thị rõ rệt Tenant A (cột trái) và Tenant B (cột phải) với 5 chức năng cơ bản: `Create`, `Read`, `Update`, `Delete`, `Search`.
- **Thẩm mỹ (Glassmorphism & Tông màu)**: Giữ nguyên thiết kế Dark Mode, Glassmorphism, nhưng tuỳ biến màu chủ đạo (`--primary-color` trong CSS) theo màu logo của công nghệ đó (ví dụ: PostgreSQL là Xanh dương, Redis là Đỏ, MongoDB là Xanh lá cây).

## 5. Các Lỗi Thường Gặp Cần Né (Bug Prevention)
- **Bug 1: Treo UI do Connection Timeout (Rất Nghiêm Trọng)**
  - *Hiện tượng*: Khi Sandbox mất mạng tới container Docker, hoặc khi chưa bật DB, UI sẽ quay vòng vòng (timeout mặc định 30s) gây ức chế.
  - *Giải pháp*: 
    1. Cấu hình timeout kết nối thật thấp trong Java Config hoặc `application.yml` (ví dụ: 2000ms).
    2. Áp dụng **In-Memory Mock Fallback**: Trong `Controller`, bao bọc các lời gọi xuống Repository bằng block `try-catch (Exception e)`. Nếu lỗi, tự động return List dữ liệu giả (Mock List) lưu trên RAM để giao diện luôn hiển thị mượt mà (0ms lag).
- **Bug 2: Trùng Port giữa các Phase**
  - *Hiện tượng*: Bật Phase 2 không được vì Phase 1 đang giữ port 8080.
  - *Giải pháp*: Trong `application.yml`, luôn set cứng `server.port` khác nhau cho mỗi Phase (Phase 1 = 8081, Phase 2 = 8082, Phase 3 = 8083, v.v.).

## 6. Quy trình Nghiệm thu (Verification)
1. Chạy `mvn clean spring-boot:run`.
2. Mở `browser_subagent` tự động truy cập port của ứng dụng.
3. Chạy test thử các thao tác CRUD và quay video WebP làm minh chứng.
4. Tổng kết bằng artifact `walkthrough.md`.
