# Kế hoạch Thực hiện Toàn diện Dự án Infrastructure-Connectors-Lab

Bản kế hoạch này bao quát toàn bộ các hệ thống hạ tầng (Infrastructure) phổ biến và quan trọng nhất trong các hệ thống Backend, Data Engineer và Big Data hiện nay. Các Giai đoạn (Phase) được phân loại và nhóm chặt chẽ theo từng lĩnh vực (Category) tương đồng nhau để duy trì mạch tư duy mạch lạc trong quá trình phát triển.

*Hướng dẫn: Khi hoàn thành phần nào, hãy đánh dấu `[x]` thay cho `[ ]`.*

---
### TIÊU CHUẨN KIẾN TRÚC BẮT BUỘC (CORE ARCHITECTURE STANDARD)
Vì dự án sử dụng Spring Boot làm nền tảng, mọi module (nếu có thể) đều **PHẢI** cấu hình theo kiến trúc **Multi-Connector / Multi-Tenant**. 
- **Yêu cầu kết nối đa luồng:** Tuyệt đối không dùng cấu hình auto-configuration mặc định (single connection) sơ sài. Phải tự định nghĩa các `@Bean` kết nối. Ví dụ: Khi làm về MySQL, phải tạo 2 bean `DataSource` trỏ đến 2 DB khác nhau; Làm về Redis, tạo 2 `RedisTemplate` riêng biệt.
- **Minh bạch cấu hình (Explicit Configuration):** Bắt buộc sử dụng `@Value` để tiêm (inject) rõ ràng từng giá trị cấu hình từ `application.yml` vào các class Config. Tuyệt đối KHÔNG dùng tính năng tự động map (ví dụ `@ConfigurationProperties`) để tránh việc framework "che giấu" (magic) quá trình gán biến, giúp người đọc kiểm soát chính xác tham số nào đang được dùng.
---

## Phase 0: Xây dựng Nền tảng (Project Foundation)
- [x] 1. Khởi tạo Root `pom.xml` (Java 21, Spring Boot 3.x).
- [x] 2. Viết Root `README.md` định nghĩa quy chuẩn code, tiêu chuẩn tài liệu và hướng dẫn đóng góp.
- [x] 3. Thiết lập file `.gitignore` tiêu chuẩn.
- [x] 4. Tạo khung thư mục tĩnh cho tất cả các hệ thống để dễ quản lý.

---
### NHÓM 1: CƠ SỞ DỮ LIỆU QUAN HỆ (RDBMS)
*Đây là trái tim của đa số hệ thống Backend, lưu trữ các dữ liệu core mang tính giao dịch.*

## Phase 1: Relational Database (MySQL)
- [ ] 1. Bổ sung dependency và khởi tạo `docker-compose.yml`.
- [ ] 2. `p1_mysql/jdbc`: Cấu hình kết nối thuần và `JdbcTemplate`.
- [ ] 3. `p1_mysql/hikari`: Tối ưu hoá Connection Pool (HikariCP).
- [ ] 4. `p1_mysql/transaction`: Xử lý ACID, Isolation Levels và Deadlock.
- [ ] 5. `p1_mysql/batch`: Tối ưu Batch Insert/Update hàng triệu bản ghi.
- [ ] 6. `p1_mysql/replication`: Routing tách biệt Master (Write) - Slave (Read).

## Phase 2: Relational & Object-Relational Database (PostgreSQL)
- [ ] 1. Bổ sung dependency và khởi tạo `docker-compose.yml`.
- [ ] 2. `p2_postgresql/jsonb`: Tận dụng sức mạnh của kiểu JSONB.
- [ ] 3. `p2_postgresql/partitioning`: Quản lý Table Partitioning cho dữ liệu lớn.
- [ ] 4. `p2_postgresql/postgis`: Lưu trữ và truy vấn dữ liệu không gian (Geospatial).

---
### NHÓM 2: IN-MEMORY DATA STORE & CACHE
*Các hệ thống lưu trữ trên RAM, sinh ra để xử lý các bài toán độ trễ siêu thấp (sub-millisecond) và giảm tải cho Database chính.*

## Phase 3: In-Memory Data Store (Redis)
- [ ] 1. Bổ sung dependency và khởi tạo `docker-compose.yml`.
- [ ] 2. `p3_redis/string`: Data structure căn bản, kiến trúc Cache-aside, giới hạn RAM.
- [ ] 3. `p3_redis/hash` & `p3_redis/list`: Mapping object và thiết kế Message Queue đơn giản.
- [ ] 4. `p3_redis/set` & `p3_redis/zset`: Xử lý tập hợp, bảng xếp hạng (Leaderboard).
- [ ] 5. `p3_redis/pubsub` & `p3_redis/stream`: Event-driven và Stream processing nhỏ.
- [ ] 6. `p3_redis/lock`: Distributed Lock (Redisson) giải quyết Race Condition.

## Phase 4: High-Performance Key-Value Store (Aerospike)
- [ ] 1. Bổ sung dependency và khởi tạo `docker-compose.yml`.
- [ ] 2. `p4_aerospike/basic`: Cấu hình kết nối, CRUD với Namespace, Set và Record.
- [ ] 3. `p4_aerospike/indexes`: Tối ưu Secondary Index và truy vấn tốc độ cao.
- [ ] 4. `p4_aerospike/udf`: Chạy User-Defined Functions (UDF) trực tiếp trên server để giảm I/O.

---
### NHÓM 3: CƠ SỞ DỮ LIỆU NOSQL (DOCUMENT, WIDE-COLUMN, GRAPH, TIME-SERIES)
*Nhóm cơ sở dữ liệu phi quan hệ, phục vụ các bài toán thiết kế linh hoạt, đọc/ghi lượng lớn hoặc cấu trúc dữ liệu đặc thù.*

## Phase 5: Document NoSQL Database (MongoDB)
- [ ] 1. Bổ sung dependency và khởi tạo `docker-compose.yml`.
- [ ] 2. `p5_mongodb/documents`: Indexing và CRUD trên Document.
- [ ] 3. `p5_mongodb/aggregation`: Aggregation Pipeline mạnh mẽ.
- [ ] 4. `p5_mongodb/transactions`: Multi-document ACID transactions.

## Phase 6: Wide-Column Store NoSQL (Cassandra)
- [ ] 1. Bổ sung dependency và khởi tạo `docker-compose.yml`.
- [ ] 2. `p6_cassandra/modeling`: Query-driven data modeling, Partition/Clustering keys.
- [ ] 3. `p6_cassandra/consistency`: Tối ưu Tunable Consistency (Quorum, Local_Quorum).

## Phase 7: Big Data Wide-Column Store (HBase)
- [ ] 1. Bổ sung dependency và khởi tạo `docker-compose.yml`.
- [ ] 2. `p7_hbase/rowkey`: Nguyên tắc thiết kế Rowkey chống Hotspotting.
- [ ] 3. `p7_hbase/operations`: Put, Get, Scan dữ liệu lớn.

## Phase 8: Graph Database (Neo4j)
- [ ] 1. Bổ sung dependency và khởi tạo `docker-compose.yml`.
- [ ] 2. `p8_neo4j/cypher`: Các thao tác Node, Relationship. Tối ưu truy vấn đồ thị.

## Phase 9: Time-Series Database (InfluxDB)
- [ ] 1. Bổ sung dependency và khởi tạo `docker-compose.yml`.
- [ ] 2. `p9_influxdb/metrics`: Ghi luồng dữ liệu thời gian thực (Push-based), Retention policies.

## Phase 10: Monitoring Time-Series (Prometheus)
- [ ] 1. Bổ sung dependency và khởi tạo `docker-compose.yml`.
- [ ] 2. `p10_prometheus/actuator`: Tích hợp Spring Boot Actuator & Micrometer để Expose metrics (Pull-based).
- [ ] 3. `p10_prometheus/custom-metrics`: Định nghĩa Custom Counter, Gauge, Timer cho nghiệp vụ Backend.

---
### NHÓM 4: MESSAGE BROKER & EVENT STREAMING
*Xương sống của kiến trúc Microservices và Data Pipeline, đảm nhận việc giao tiếp bất đồng bộ và truyền tải dữ liệu.*

## Phase 11: Distributed Event Streaming (Kafka)
- [ ] 1. Bổ sung dependency và khởi tạo `docker-compose.yml`.
- [ ] 2. `p11_kafka/producer`: Tối ưu throughput (Acks, Batch, Compression).
- [ ] 3. `p11_kafka/consumer`: Consumer Groups, Auto vs Manual Offset Commit.
- [ ] 4. `p11_kafka/transaction`: Exactly-Once semantics.
- [ ] 5. `p11_kafka/retry-dlq`: Xử lý lỗi với Dead Letter Queue (DLQ).

## Phase 12: Traditional Message Broker (RabbitMQ)
- [ ] 1. Bổ sung dependency và khởi tạo `docker-compose.yml`.
- [ ] 2. `p12_rabbitmq/exchanges`: Các mô hình routing (Direct, Fanout, Topic, Headers).
- [ ] 3. `p12_rabbitmq/dlx`: Dead Letter Exchanges và Xử lý Delayed Message.

---
### NHÓM 5: SEARCH ENGINE & ANALYTICS
*Chuyên trị các bài toán tìm kiếm toàn văn bản (Full-text) và phân tích thống kê siêu tốc trên lượng dữ liệu khổng lồ.*

## Phase 13: Search Engine & Analytics (Elasticsearch)
- [ ] 1. Bổ sung dependency và khởi tạo `docker-compose.yml`.
- [ ] 2. `p13_elasticsearch/indexing`: Quản lý Index lifecycle và Mapping.
- [ ] 3. `p13_elasticsearch/searching`: Text search, Match, Term, Filters.
- [ ] 4. `p13_elasticsearch/aggregation`: Phân tích thống kê (Bucketing, Metrics).
- [ ] 5. `p13_elasticsearch/bulk`: Tối ưu Bulk indexing.

## Phase 14: Real-time OLAP Database (ClickHouse)
- [ ] 1. Bổ sung dependency và khởi tạo `docker-compose.yml`.
- [ ] 2. `p14_clickhouse/engines`: Cấu hình MergeTree, ReplacingMergeTree, CollapsingMergeTree.
- [ ] 3. `p14_clickhouse/olap`: Truy vấn phân tích tổng hợp (Aggregation) khối lượng siêu lớn.

---
### NHÓM 6: STORAGE SYSTEMS
*Giải quyết bài toán lưu trữ file tĩnh (hình ảnh, video, log) và dữ liệu Big Data thô.*

## Phase 15: Object Storage (MinIO)
- [ ] 1. Bổ sung dependency và khởi tạo `docker-compose.yml`.
- [ ] 2. `p15_minio/basic`: Bucket policy, Put/Get object, Presigned URL.
- [ ] 3. `p15_minio/multipart`: Chia nhỏ file lớn để upload song song (Multipart Upload).

## Phase 16: Distributed File System (HDFS)
- [ ] 1. Bổ sung dependency và khởi tạo `docker-compose.yml`.
- [ ] 2. `p16_hdfs/webhdfs`: Đọc/Ghi file lớn thông qua REST API/Native Java Client.


