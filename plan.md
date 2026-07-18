# Kế hoạch Thực hiện Toàn diện Dự án Infrastructure-Connectors-Lab

Bản kế hoạch này bao quát toàn bộ các hệ thống hạ tầng (Infrastructure) phổ biến và quan trọng nhất trong các hệ thống Backend, Data Engineer và Big Data hiện nay. Các Giai đoạn (Phase) được phân loại và nhóm chặt chẽ theo từng lĩnh vực (Category) tương đồng nhau để duy trì mạch tư duy mạch lạc trong quá trình phát triển.

*Hướng dẫn: Khi hoàn thành phần nào, hãy đánh dấu `[x]` thay cho `[ ]`.*

---
### TIÊU CHUẨN KIẾN TRÚC BẮT BUỘC (CORE ARCHITECTURE STANDARD)
Vì dự án sử dụng Spring Boot làm nền tảng, mọi module (nếu có thể) đều **PHẢI** cấu hình theo kiến trúc **Multi-Connector / Multi-Tenant**. 
- **Yêu cầu kết nối đa luồng:** Tuyệt đối không dùng cấu hình auto-configuration mặc định (single connection) sơ sài. Phải tự định nghĩa các `@Bean` kết nối. Ví dụ: Khi làm về MySQL, phải tạo 2 bean `DataSource` trỏ đến 2 DB khác nhau; Làm về Redis, tạo 2 `RedisTemplate` riêng biệt.
- **Minh bạch cấu hình (Explicit Configuration):** Bắt buộc sử dụng `@Value` để tiêm (inject) rõ ràng từng giá trị cấu hình từ `application.yml` vào các class Config. Tuyệt đối KHÔNG dùng tính năng tự động map (ví dụ `@ConfigurationProperties`) để tránh việc framework "che giấu" (magic) quá trình gán biến, giúp người đọc kiểm soát chính xác tham số nào đang được dùng.
---

## Phase 1: Xây dựng Nền tảng (Project Foundation)
- [ ] 1. Khởi tạo Root `pom.xml` (Java 21, Spring Boot 3.x).
- [ ] 2. Viết Root `README.md` định nghĩa quy chuẩn code, tiêu chuẩn tài liệu và hướng dẫn đóng góp.
- [ ] 3. Thiết lập file `.gitignore` tiêu chuẩn.
- [ ] 4. Tạo khung thư mục tĩnh cho tất cả các hệ thống để dễ quản lý.

---
### NHÓM 1: CƠ SỞ DỮ LIỆU QUAN HỆ (RDBMS)
*Đây là trái tim của đa số hệ thống Backend, lưu trữ các dữ liệu core mang tính giao dịch.*

## Phase 2: Relational Database (MySQL)
- [ ] 1. Khởi tạo `mysql/pom.xml` & `docker-compose.yml`.
- [ ] 2. `mysql/jdbc`: Cấu hình kết nối thuần và `JdbcTemplate`.
- [ ] 3. `mysql/hikari`: Tối ưu hoá Connection Pool (HikariCP).
- [ ] 4. `mysql/transaction`: Xử lý ACID, Isolation Levels và Deadlock.
- [ ] 5. `mysql/batch`: Tối ưu Batch Insert/Update hàng triệu bản ghi.
- [ ] 6. `mysql/replication`: Routing tách biệt Master (Write) - Slave (Read).

## Phase 3: Relational & Object-Relational Database (PostgreSQL)
- [ ] 1. Khởi tạo `postgresql/pom.xml` & `docker-compose.yml`.
- [ ] 2. `postgresql/jsonb`: Tận dụng sức mạnh của kiểu JSONB.
- [ ] 3. `postgresql/partitioning`: Quản lý Table Partitioning cho dữ liệu lớn.
- [ ] 4. `postgresql/postgis`: Lưu trữ và truy vấn dữ liệu không gian (Geospatial).

---
### NHÓM 2: IN-MEMORY DATA STORE & CACHE
*Các hệ thống lưu trữ trên RAM, sinh ra để xử lý các bài toán độ trễ siêu thấp (sub-millisecond) và giảm tải cho Database chính.*

## Phase 4: In-Memory Data Store (Redis)
- [ ] 1. Khởi tạo `redis/pom.xml` & `docker-compose.yml`.
- [ ] 2. `redis/string`: Data structure căn bản, kiến trúc Cache-aside, giới hạn RAM.
- [ ] 3. `redis/hash` & `redis/list`: Mapping object và thiết kế Message Queue đơn giản.
- [ ] 4. `redis/set` & `redis/zset`: Xử lý tập hợp, bảng xếp hạng (Leaderboard).
- [ ] 5. `redis/pubsub` & `redis/stream`: Event-driven và Stream processing nhỏ.
- [ ] 6. `redis/lock`: Distributed Lock (Redisson) giải quyết Race Condition.

## Phase 5: High-Performance Key-Value Store (Aerospike)
- [ ] 1. Khởi tạo `aerospike/pom.xml` & `docker-compose.yml`.
- [ ] 2. `aerospike/basic`: Cấu hình kết nối, CRUD với Namespace, Set và Record.
- [ ] 3. `aerospike/indexes`: Tối ưu Secondary Index và truy vấn tốc độ cao.
- [ ] 4. `aerospike/udf`: Chạy User-Defined Functions (UDF) trực tiếp trên server để giảm I/O.

---
### NHÓM 3: CƠ SỞ DỮ LIỆU NOSQL (DOCUMENT, WIDE-COLUMN, GRAPH, TIME-SERIES)
*Nhóm cơ sở dữ liệu phi quan hệ, phục vụ các bài toán thiết kế linh hoạt, đọc/ghi lượng lớn hoặc cấu trúc dữ liệu đặc thù.*

## Phase 6: Document NoSQL Database (MongoDB)
- [ ] 1. Khởi tạo `mongodb/pom.xml` & `docker-compose.yml`.
- [ ] 2. `mongodb/documents`: Indexing và CRUD trên Document.
- [ ] 3. `mongodb/aggregation`: Aggregation Pipeline mạnh mẽ.
- [ ] 4. `mongodb/transactions`: Multi-document ACID transactions.

## Phase 7: Wide-Column Store NoSQL (Cassandra)
- [ ] 1. Khởi tạo `cassandra/pom.xml` & `docker-compose.yml`.
- [ ] 2. `cassandra/modeling`: Query-driven data modeling, Partition/Clustering keys.
- [ ] 3. `cassandra/consistency`: Tối ưu Tunable Consistency (Quorum, Local_Quorum).

## Phase 8: Big Data Wide-Column Store (HBase)
- [ ] 1. Khởi tạo `hbase/pom.xml` & `docker-compose.yml`.
- [ ] 2. `hbase/rowkey`: Nguyên tắc thiết kế Rowkey chống Hotspotting.
- [ ] 3. `hbase/operations`: Put, Get, Scan dữ liệu lớn.

## Phase 9: Graph Database (Neo4j)
- [ ] 1. Khởi tạo `neo4j/pom.xml` & `docker-compose.yml`.
- [ ] 2. `neo4j/cypher`: Các thao tác Node, Relationship. Tối ưu truy vấn đồ thị.

## Phase 10: Time-Series Database (InfluxDB)
- [ ] 1. Khởi tạo `influxdb/pom.xml` & `docker-compose.yml`.
- [ ] 2. `influxdb/metrics`: Ghi luồng dữ liệu thời gian thực (Push-based), Retention policies.

## Phase 11: Monitoring Time-Series (Prometheus)
- [ ] 1. Khởi tạo `prometheus/pom.xml` & `docker-compose.yml` (Prometheus + Grafana).
- [ ] 2. `prometheus/actuator`: Tích hợp Spring Boot Actuator & Micrometer để Expose metrics (Pull-based).
- [ ] 3. `prometheus/custom-metrics`: Định nghĩa Custom Counter, Gauge, Timer cho nghiệp vụ Backend.

---
### NHÓM 4: MESSAGE BROKER & EVENT STREAMING
*Xương sống của kiến trúc Microservices và Data Pipeline, đảm nhận việc giao tiếp bất đồng bộ và truyền tải dữ liệu.*

## Phase 12: Distributed Event Streaming (Kafka)
- [ ] 1. Khởi tạo `kafka/pom.xml` & `docker-compose.yml` (Kraft mode).
- [ ] 2. `kafka/producer`: Tối ưu throughput (Acks, Batch, Compression).
- [ ] 3. `kafka/consumer`: Consumer Groups, Auto vs Manual Offset Commit.
- [ ] 4. `kafka/transaction`: Exactly-Once semantics.
- [ ] 5. `kafka/retry-dlq`: Xử lý lỗi với Dead Letter Queue (DLQ).

## Phase 13: Traditional Message Broker (RabbitMQ)
- [ ] 1. Khởi tạo `rabbitmq/pom.xml` & `docker-compose.yml`.
- [ ] 2. `rabbitmq/exchanges`: Các mô hình routing (Direct, Fanout, Topic, Headers).
- [ ] 3. `rabbitmq/dlx`: Dead Letter Exchanges và Xử lý Delayed Message.

---
### NHÓM 5: SEARCH ENGINE & ANALYTICS
*Chuyên trị các bài toán tìm kiếm toàn văn bản (Full-text) và phân tích thống kê siêu tốc trên lượng dữ liệu khổng lồ.*

## Phase 14: Search Engine & Analytics (Elasticsearch)
- [ ] 1. Khởi tạo `elasticsearch/pom.xml` & `docker-compose.yml`.
- [ ] 2. `elasticsearch/indexing`: Quản lý Index lifecycle và Mapping.
- [ ] 3. `elasticsearch/searching`: Text search, Match, Term, Filters.
- [ ] 4. `elasticsearch/aggregation`: Phân tích thống kê (Bucketing, Metrics).
- [ ] 5. `elasticsearch/bulk`: Tối ưu Bulk indexing.

## Phase 15: Real-time OLAP Database (ClickHouse)
- [ ] 1. Khởi tạo `clickhouse/pom.xml` & `docker-compose.yml`.
- [ ] 2. `clickhouse/engines`: Cấu hình MergeTree, ReplacingMergeTree, CollapsingMergeTree.
- [ ] 3. `clickhouse/olap`: Truy vấn phân tích tổng hợp (Aggregation) khối lượng siêu lớn.

---
### NHÓM 6: STORAGE SYSTEMS
*Giải quyết bài toán lưu trữ file tĩnh (hình ảnh, video, log) và dữ liệu Big Data thô.*

## Phase 16: Object Storage (MinIO)
- [ ] 1. Khởi tạo `minio/pom.xml` & `docker-compose.yml`.
- [ ] 2. `minio/basic`: Bucket policy, Put/Get object, Presigned URL.
- [ ] 3. `minio/multipart`: Chia nhỏ file lớn để upload song song (Multipart Upload).

## Phase 17: Distributed File System (HDFS)
- [ ] 1. Khởi tạo `hdfs/pom.xml` & `docker-compose.yml`.
- [ ] 2. `hdfs/webhdfs`: Đọc/Ghi file lớn thông qua REST API/Native Java Client.


