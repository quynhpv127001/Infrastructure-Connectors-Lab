# Kế hoạch Thực hiện Toàn diện Dự án Infrastructure-Connectors-Lab

Bản kế hoạch này bao quát toàn bộ các hệ thống hạ tầng (Infrastructure) phổ biến và quan trọng nhất trong các hệ thống Backend, Data Engineer và Big Data hiện nay. Các Giai đoạn (Phase) được phân loại và nhóm chặt chẽ theo từng lĩnh vực (Category) tương đồng nhau để duy trì mạch tư duy mạch lạc trong quá trình phát triển.

*Hướng dẫn: Khi hoàn thành phần nào, hãy đánh dấu `[x]` thay cho `[ ]`.*

---
### TỔNG HỢP CÁC QUY TẮC BẮT BUỘC CHO TẤT CẢ CÁC PHASE (GLOBAL STANDARDS)
Dựa trên kinh nghiệm từ Phase 1, toàn bộ các phase sau (từ 2 đến 16) bắt buộc phải tuân thủ nghiêm ngặt các quy tắc sau:

1. **Kiến trúc & Cấu hình Code**
   - **Độc Lập Hoàn Toàn**: Mỗi hạ tầng phải là một dự án Spring Boot Maven riêng biệt (không dùng chung Root pom.xml).
   - **Tiêm Cấu Hình Thủ Công (Explicit Configuration)**: Bắt buộc sử dụng `@Value` để inject rõ ràng từng dòng cấu hình từ `application.yml` vào các class Config. 
   - **Không Dùng Auto-Configuration**: Tuyệt đối không dùng cấu hình mặc định sơ sài hoặc tính năng tự map (`@ConfigurationProperties`). Mọi đối tượng kết nối (DataSource, RedisTemplate...) phải được cấu hình thủ công bằng `@Bean`.

2. **Giao Diện & API Mặc Định (Thymeleaf)**
   - **Chuẩn 5 API**: Mỗi phase bắt buộc phải cung cấp đủ 5 đầu API CRUD cơ bản: `Create`, `Read`, `Update`, `Delete`, và `Search`. KHÔNG làm hời hợt, KHÔNG đi sâu vào các tính năng nâng cao (Replication, Transaction, Batch...) nếu không có yêu cầu.
   - **Trực Quan Hóa (UI)**: Bắt buộc cung cấp giao diện trực quan minh họa cho kiến trúc Multi-Tenant.
   - **Chia Cột Đối Xứng**: Giao diện chia rõ 2 cột (Tenant A và Tenant B) để chứng minh 2 connection chạy song song độc lập.
   - **Thẩm Mỹ Cao**: Giao diện phải đẹp, dùng Dark Mode, Glassmorphism, hoặc các style hiện đại tương tự. Hỗ trợ cơ chế Mock Fallback (Try-Catch in-memory) để test UI mượt mà ngay cả khi Database sập.

3. **Đóng Gói Triển Khai (Deployment)**
   - **Thư Mục `deploy/`**: Mọi file phục vụ hạ tầng (như `docker-compose.yml`, các file `.sh` hoặc `.sql` khởi tạo tự động, `.gitignore`) bắt buộc phải đặt trong thư mục `deploy/`.
   - **Tự Động Hóa Dữ Liệu**: Phải chủ động thiết lập sẵn các script tạo database, tạo table, import data mẫu (vd: `init.sql`). Người dùng chỉ cần gõ `docker compose up -d` là hệ thống phải tự động sẵn sàng chạy mà không cần setup tay.
   - **Volume Isolation**: Mount volume phải đưa vào thư mục `deploy/volumes/` và đảm bảo `.gitignore` (bên trong thư mục deploy) chặn không đẩy file dữ liệu lên Git.
---

## Phase 0: Xây dựng Nền tảng (Project Foundation)
- [x] 1. (Bỏ qua Root pom.xml vì mỗi hạ tầng là 1 project độc lập).
- [x] 2. Viết Root `README.md` định nghĩa quy chuẩn code, tiêu chuẩn tài liệu và hướng dẫn đóng góp.
- [x] 3. Thiết lập file `.gitignore` tiêu chuẩn.
- [x] 4. Tạo khung 16 thư mục tĩnh cho 16 dự án Spring Boot độc lập.

---
### NHÓM 1: CƠ SỞ DỮ LIỆU QUAN HỆ (RDBMS)
*Đây là trái tim của đa số hệ thống Backend, lưu trữ các dữ liệu core mang tính giao dịch.*

### Phase 1: Kiến trúc Multi-Tenant Cơ bản với MySQL (DONE)
**Mục tiêu**: Xây dựng nền tảng đa kết nối (multi-tenant) cơ bản nhất với MySQL, minh họa việc 1 worker xử lý dữ liệu cho 2 khách hàng khác nhau.

**Các bước thực hiện:**
- Thiết lập 2 database MySQL riêng biệt (bằng docker-compose).
- Cấu hình Spring Boot xử lý 2 DataSource độc lập bằng cách định nghĩa các `@Bean` thủ công thay vì dùng cấu hình auto mặc định. 
- Viết các API CRUD và Search cơ bản, nhận parameter `tenantId` để route đến đúng Repository.
- Xây dựng giao diện UI chia 2 cột để minh họa trực quan 5 API cho mỗi Tenant.

**Tiêu chí hoàn thành (DoD):** 
- Giao diện 2 cột hiển thị đầy đủ form Create, Search, Update, Delete.
- Các thao tác UI mapping chuẩn xác xuống API tương ứng (đã test thành công với in-memory fallback khi DB down).
- Không có exception hay timeout block UI.

## Phase 2: Relational & Object-Relational Database (PostgreSQL) (DONE)
- [x] 1. Khởi tạo dự án Spring Boot độc lập (pom.xml) và `docker-compose.yml` theo chuẩn Multi-Tenant.
- [x] 2. Triển khai giao diện quản lý (Thymeleaf) trực quan hóa 5 thao tác: Create, Read, Update, Delete, Search cho 2 luồng connection.

---
### NHÓM 2: IN-MEMORY DATA STORE & CACHE
*Các hệ thống lưu trữ trên RAM, sinh ra để xử lý các bài toán độ trễ siêu thấp (sub-millisecond) và giảm tải cho Database chính.*

## Phase 3: In-Memory Data Store (Redis)
- [ ] 1. Khởi tạo dự án Spring Boot độc lập (pom.xml) và `docker-compose.yml` theo chuẩn Multi-Tenant.
- [ ] 2. Triển khai giao diện quản lý (Thymeleaf) trực quan hóa 5 thao tác: Create, Read, Update, Delete, Search cho 2 luồng connection.

## Phase 4: High-Performance Key-Value Store (Aerospike)
- [ ] 1. Khởi tạo dự án Spring Boot độc lập (pom.xml) và `docker-compose.yml` theo chuẩn Multi-Tenant.
- [ ] 2. Triển khai giao diện quản lý (Thymeleaf) trực quan hóa 5 thao tác: Create, Read, Update, Delete, Search cho 2 luồng connection.

---
### NHÓM 3: CƠ SỞ DỮ LIỆU NOSQL (DOCUMENT, WIDE-COLUMN, GRAPH, TIME-SERIES)
*Nhóm cơ sở dữ liệu phi quan hệ, phục vụ các bài toán thiết kế linh hoạt, đọc/ghi lượng lớn hoặc cấu trúc dữ liệu đặc thù.*

## Phase 5: Document NoSQL Database (MongoDB)
- [ ] 1. Khởi tạo dự án Spring Boot độc lập (pom.xml) và `docker-compose.yml` theo chuẩn Multi-Tenant.
- [ ] 2. Triển khai giao diện quản lý (Thymeleaf) trực quan hóa 5 thao tác: Create, Read, Update, Delete, Search cho 2 luồng connection.

## Phase 6: Wide-Column Store NoSQL (Cassandra)
- [ ] 1. Khởi tạo dự án Spring Boot độc lập (pom.xml) và `docker-compose.yml` theo chuẩn Multi-Tenant.
- [ ] 2. Triển khai giao diện quản lý (Thymeleaf) trực quan hóa 5 thao tác: Create, Read, Update, Delete, Search cho 2 luồng connection.

## Phase 7: Big Data Wide-Column Store (HBase)
- [ ] 1. Khởi tạo dự án Spring Boot độc lập (pom.xml) và `docker-compose.yml` theo chuẩn Multi-Tenant.
- [ ] 2. Triển khai giao diện quản lý (Thymeleaf) trực quan hóa 5 thao tác: Create, Read, Update, Delete, Search cho 2 luồng connection.

## Phase 8: Graph Database (Neo4j)
- [ ] 1. Khởi tạo dự án Spring Boot độc lập (pom.xml) và `docker-compose.yml` theo chuẩn Multi-Tenant.
- [ ] 2. Triển khai giao diện quản lý (Thymeleaf) trực quan hóa 5 thao tác: Create, Read, Update, Delete, Search cho 2 luồng connection.

## Phase 9: Time-Series Database (InfluxDB)
- [ ] 1. Khởi tạo dự án Spring Boot độc lập (pom.xml) và `docker-compose.yml` theo chuẩn Multi-Tenant.
- [ ] 2. Triển khai giao diện quản lý (Thymeleaf) trực quan hóa 5 thao tác: Create, Read, Update, Delete, Search cho 2 luồng connection.

## Phase 10: Monitoring Time-Series (Prometheus)
- [ ] 1. Khởi tạo dự án Spring Boot độc lập (pom.xml) và `docker-compose.yml` theo chuẩn Multi-Tenant.
- [ ] 2. Triển khai giao diện quản lý (Thymeleaf) trực quan hóa 5 thao tác: Create, Read, Update, Delete, Search cho 2 luồng connection.

---
### NHÓM 4: MESSAGE BROKER & EVENT STREAMING
*Xương sống của kiến trúc Microservices và Data Pipeline, đảm nhận việc giao tiếp bất đồng bộ và truyền tải dữ liệu.*

## Phase 11: Distributed Event Streaming (Kafka)
- [ ] 1. Khởi tạo dự án Spring Boot độc lập (pom.xml) và `docker-compose.yml` theo chuẩn Multi-Tenant.
- [ ] 2. Triển khai giao diện quản lý (Thymeleaf) trực quan hóa 5 thao tác: Create, Read, Update, Delete, Search cho 2 luồng connection.

## Phase 12: Traditional Message Broker (RabbitMQ)
- [ ] 1. Khởi tạo dự án Spring Boot độc lập (pom.xml) và `docker-compose.yml` theo chuẩn Multi-Tenant.
- [ ] 2. Triển khai giao diện quản lý (Thymeleaf) trực quan hóa 5 thao tác: Create, Read, Update, Delete, Search cho 2 luồng connection.

---
### NHÓM 5: SEARCH ENGINE & ANALYTICS
*Chuyên trị các bài toán tìm kiếm toàn văn bản (Full-text) và phân tích thống kê siêu tốc trên lượng dữ liệu khổng lồ.*

## Phase 13: Search Engine & Analytics (Elasticsearch)
- [ ] 1. Khởi tạo dự án Spring Boot độc lập (pom.xml) và `docker-compose.yml` theo chuẩn Multi-Tenant.
- [ ] 2. Triển khai giao diện quản lý (Thymeleaf) trực quan hóa 5 thao tác: Create, Read, Update, Delete, Search cho 2 luồng connection.

## Phase 14: Real-time OLAP Database (ClickHouse)
- [ ] 1. Khởi tạo dự án Spring Boot độc lập (pom.xml) và `docker-compose.yml` theo chuẩn Multi-Tenant.
- [ ] 2. Triển khai giao diện quản lý (Thymeleaf) trực quan hóa 5 thao tác: Create, Read, Update, Delete, Search cho 2 luồng connection.

---
### NHÓM 6: STORAGE SYSTEMS
*Giải quyết bài toán lưu trữ file tĩnh (hình ảnh, video, log) và dữ liệu Big Data thô.*

## Phase 15: Object Storage (MinIO)
- [ ] 1. Khởi tạo dự án Spring Boot độc lập (pom.xml) và `docker-compose.yml` theo chuẩn Multi-Tenant.
- [ ] 2. Triển khai giao diện quản lý (Thymeleaf) trực quan hóa 5 thao tác: Create, Read, Update, Delete, Search cho 2 luồng connection.

## Phase 16: Distributed File System (HDFS)
- [ ] 1. Khởi tạo dự án Spring Boot độc lập (pom.xml) và `docker-compose.yml` theo chuẩn Multi-Tenant.
- [ ] 2. Triển khai giao diện quản lý (Thymeleaf) trực quan hóa 5 thao tác: Create, Read, Update, Delete, Search cho 2 luồng connection.


