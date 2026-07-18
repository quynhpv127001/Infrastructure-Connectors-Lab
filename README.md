# Infrastructure Connectors Lab

Chào mừng bạn đến với **Infrastructure Connectors Lab** - kho tài liệu thực hành và mã nguồn mẫu được thiết kế chuẩn mực dành cho Backend Engineer và Data Engineer. Dự án này không phải là một ứng dụng CRUD thông thường mà là một bách khoa toàn thư về cách kết nối, tối ưu và xử lý với các hệ thống hạ tầng phân tán trong môi trường Production.

## Tôn chỉ & Tiêu chuẩn của Dự án

Dự án này tuân thủ các tiêu chuẩn kiến trúc cực kỳ nghiêm ngặt nhằm mô phỏng sát nhất với hệ thống thực tế:

1. **Multi-Connector / Multi-Tenant (Bắt buộc):**
   Tuyệt đối không sử dụng cấu hình mặc định (Auto-configuration của Spring Boot) cho các kết nối. Mọi module đều phải tự định nghĩa các `@Bean` và mô phỏng kết nối song song đến ít nhất 2 cụm độc lập (Ví dụ: 2 DataSource, 2 RedisTemplate).
   
2. **Minh bạch cấu hình (Explicit Configuration):**
   Tuyệt đối không dùng `@ConfigurationProperties` để framework tự động map các thuộc tính một cách ma thuật. Mọi tham số cấu hình phải được tiêm (inject) tường minh bằng `@Value("${...}")` để đảm bảo code dễ đọc, dễ kiểm soát 100%.

3. **Tiêu chuẩn Tài liệu (Documentation Standard):**
   Mỗi công nghệ (Module) phải có một file `README.md` riêng biệt giải thích sâu sắc về:
   - Kiến trúc và luồng hoạt động (Data flow).
   - Khi nào nên dùng và không nên dùng.
   - Best Practices & Tối ưu hiệu năng trên Production.
   - Các lỗi thường gặp (Troubleshooting) và cách xử lý.

## Danh mục Hạ tầng (Infrastructure Catalog)

Dự án bao phủ 16 Phase trải dài qua 6 nhóm hạ tầng lõi:

* **NHÓM 1: RDBMS** (MySQL, PostgreSQL)
* **NHÓM 2: In-Memory / Cache** (Redis, Aerospike)
* **NHÓM 3: NoSQL** (MongoDB, Cassandra, HBase, Neo4j, InfluxDB, Prometheus)
* **NHÓM 4: Messaging** (Kafka, RabbitMQ)
* **NHÓM 5: Search & Analytics** (Elasticsearch, ClickHouse)
* **NHÓM 6: Storage** (MinIO, HDFS)

*(Vui lòng tham khảo file `plan.md` ở thư mục gốc để xem lộ trình chi tiết)*

## Hướng dẫn sử dụng

- **Yêu cầu hệ thống:** Java 21, Maven 3.8+, Docker & Docker Compose.
- **Cách chạy:** Mỗi module con đều độc lập hoàn toàn. Bạn hãy truy cập vào thư mục của hạ tầng bạn muốn học (ví dụ `cd redis/string`), chạy `docker-compose up -d` để dựng hạ tầng local, sau đó đọc `README.md` trong đó để chạy code Spring Boot minh họa.

## Hướng dẫn đóng góp (Contributing)
Khi tạo một module mới:
1. Tạo cấu trúc thư mục đúng nhóm.
2. Viết `docker-compose.yml` chạy local.
3. Code Spring Boot tuân thủ 2 tôn chỉ ở trên.
4. Hoàn thiện file `README.md` theo chuẩn.
