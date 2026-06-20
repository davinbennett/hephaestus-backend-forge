# Materi - Database, SQL, JPA, Hibernate, Flyway & Query Relationship

## 1. Recap Previous Days

Pada Day 1 kita membuat REST API basic. Pada Day 2 kita menambahkan validation dan error handling. Pada Day 3 kita membahas API contract, testing, dan Swagger. Pada Day 4 kita membahas service, filter, authorization, dan clean response jika sudah tersedia.

Sampai tahap ini, data masih mungkin disimpan di memory menggunakan `Map`.

Problem `Map`:

- Data hilang saat aplikasi restart.
- Tidak cocok untuk production.
- Sulit query.
- Sulit relationship.
- Sulit audit data.

Day 5 memindahkan cara berpikir dari "data ada di object memory" menjadi "data disimpan di database dan diakses melalui layer yang rapi".

## 2. Kenapa Database Dibutuhkan?

Database digunakan untuk menyimpan data secara permanen dan bisa diakses kembali walaupun aplikasi restart.

Manfaat database:

- Persistent storage.
- Structured data.
- Query capability.
- Relationship antar data.
- Data integrity.
- Transaction.
- Audit trail.
- Scalability lebih baik dibanding in-memory `Map`.

Dalam backend finance, database biasanya menjadi source of truth untuk data customer, loan, schedule cicilan, payment, dan audit.

## 3. Java 17, Spring Boot 3, Jakarta, and Hibernate 6

Day 5 menggunakan Java 17 dan Spring Boot 3.x.

Hal penting:

- Spring Boot 3 menggunakan Jakarta EE namespace.
- JPA annotations memakai `jakarta.persistence`.
- Validation annotations memakai `jakarta.validation`.
- Jangan memakai `javax.persistence` pada contoh Spring Boot 3.
- Jangan memakai `javax.validation` pada contoh Spring Boot 3.
- Hibernate 6 adalah JPA provider yang umum pada Spring Boot 3.

Contoh import JPA:

```java
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
```

Contoh import validation:

```java
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Positive;
```

## 4. PostgreSQL

PostgreSQL adalah relational database yang banyak digunakan di backend production.

PostgreSQL cocok untuk aplikasi backend karena:

- Open-source.
- Powerful SQL support.
- Support relationship.
- Support indexing.
- Support transaction.
- Cocok untuk aplikasi backend.

Pada latihan ini, PostgreSQL digunakan sebagai database utama untuk finance loan case.

## 5. Database Driver

Driver adalah library yang memungkinkan aplikasi Java berkomunikasi dengan database.

```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
```

Spring Boot tidak bisa connect ke PostgreSQL tanpa driver.

## 6. Spring Data JPA Dependency

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```

Dependency ini membawa JPA, Hibernate, transaction support, dan repository support.

## 7. Flyway Dependency

```xml
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-core</artifactId>
</dependency>
```

Flyway digunakan untuk mengatur perubahan schema database dengan file migration.

## 8. application.properties

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/finance_training
spring.datasource.username=postgres
spring.datasource.password=postgres

spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration

server.port=8080
```

Penjelasan:

- `spring.datasource.url`: alamat database PostgreSQL.
- `spring.datasource.username`: username database.
- `spring.datasource.password`: password database.
- `spring.jpa.hibernate.ddl-auto=validate`: Hibernate hanya memvalidasi entity cocok dengan schema.
- `spring.jpa.show-sql=true`: menampilkan SQL yang dijalankan Hibernate.
- `spring.flyway.locations`: lokasi file migration.

Kenapa `ddl-auto` sebaiknya tidak `update` untuk project serius?

- `update` bisa mengubah schema diam-diam.
- Migration history menjadi tidak jelas.
- Flyway lebih baik untuk perubahan schema yang terkontrol.

## 9. SQL Quick Review

```sql
SELECT * FROM customers;

SELECT * FROM customers WHERE id = 1;

SELECT * FROM customers WHERE full_name ILIKE '%budi%';

INSERT INTO customers (full_name, nik, email, phone_number)
VALUES ('Budi Santoso', '3173010101900001', 'budi@mail.com', '08123456789');

UPDATE customers
SET full_name = 'Budi Updated'
WHERE id = 1;

DELETE FROM customers
WHERE id = 1;
```

Peserta yang sudah familiar SQL dapat fokus pada bagaimana SQL terhubung ke Spring Boot melalui JPA, Hibernate, Repository, Query, Relationship, dan Flyway.

## 10. Finance Case Overview

Day 5 menggunakan finance industry case.

Entities:

- Customer
- Loan Application
- Repayment Schedule
- Payment Transaction

Business flow:

- Customer mendaftar.
- Customer mengajukan loan.
- Loan memiliki repayment schedule.
- Repayment schedule bisa memiliki payment transaction.
- Backend perlu query data berdasarkan customer, status loan, dan pembayaran.

## 11. Table Design

### customers

```sql
CREATE TABLE customers (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    nik VARCHAR(30) NOT NULL UNIQUE,
    email VARCHAR(150) NOT NULL UNIQUE,
    phone_number VARCHAR(30) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);
```

### loan_applications

```sql
CREATE TABLE loan_applications (
    id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    loan_amount NUMERIC(15, 2) NOT NULL,
    tenor_month INTEGER NOT NULL,
    purpose VARCHAR(255) NOT NULL,
    status VARCHAR(30) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_loan_applications_customer
        FOREIGN KEY (customer_id)
        REFERENCES customers(id)
);
```

### repayment_schedules

```sql
CREATE TABLE repayment_schedules (
    id BIGSERIAL PRIMARY KEY,
    loan_application_id BIGINT NOT NULL,
    installment_number INTEGER NOT NULL,
    due_date DATE NOT NULL,
    principal_amount NUMERIC(15, 2) NOT NULL,
    interest_amount NUMERIC(15, 2) NOT NULL,
    total_amount NUMERIC(15, 2) NOT NULL,
    status VARCHAR(30) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_repayment_schedules_loan
        FOREIGN KEY (loan_application_id)
        REFERENCES loan_applications(id)
);
```

### payment_transactions

```sql
CREATE TABLE payment_transactions (
    id BIGSERIAL PRIMARY KEY,
    repayment_schedule_id BIGINT NOT NULL,
    payment_reference VARCHAR(100) NOT NULL UNIQUE,
    paid_amount NUMERIC(15, 2) NOT NULL,
    paid_at TIMESTAMP NOT NULL,
    status VARCHAR(30) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_payment_transactions_schedule
        FOREIGN KEY (repayment_schedule_id)
        REFERENCES repayment_schedules(id)
);
```

## 12. Flyway Migration Naming

Flyway membaca migration files dari:

```text
src/main/resources/db/migration
```

Contoh:

```text
V1__create_customers_table.sql
V2__create_loan_applications_table.sql
V3__create_repayment_schedules_table.sql
V4__create_payment_transactions_table.sql
```

Naming:

- `V1` berarti version 1.
- Double underscore memisahkan version dan description.
- Description harus jelas.
- Flyway mencatat migration history di table `flyway_schema_history`.

## 13. Entity

Entity adalah Java class yang di-map ke database table.

```java
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "customers")
public class CustomerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Column(name = "nik", nullable = false, unique = true, length = 30)
    private String nik;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "phone_number", nullable = false, length = 30)
    private String phoneNumber;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // getter and setter
}
```

Penjelasan:

- `@Entity`: class ini adalah entity JPA.
- `@Table`: nama table database.
- `@Id`: primary key.
- `@GeneratedValue`: strategi generate id.
- `@Column`: mapping field Java ke column database.
- Entity menggunakan camelCase.
- Database column menggunakan snake_case.
- Spring Boot 3 menggunakan `jakarta.persistence` imports.

## 14. JPA vs Hibernate

JPA adalah specification. Hibernate adalah implementation.

Analogi:

- JPA = interface atau rule.
- Hibernate = engine yang menjalankan perilaku ORM.

Spring Data JPA menggunakan Hibernate sebagai JPA provider.

## 15. Repository

```java
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {

    Optional<CustomerEntity> findByNik(String nik);

    Optional<CustomerEntity> findByEmail(String email);

    boolean existsByNik(String nik);

    boolean existsByEmail(String email);

    List<CustomerEntity> findByFullNameContainingIgnoreCase(String fullName);
}
```

`JpaRepository` memberi method siap pakai seperti `save`, `findById`, `findAll`, `delete`, `existsById`, dan `count`.

Derived query berarti Spring Data JPA membuat query dari nama method.

## 16. JPQL Query with @Query

```java
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Query("SELECT l FROM LoanApplicationEntity l JOIN l.customer c WHERE c.id = :customerId")
List<LoanApplicationEntity> findLoansByCustomerId(@Param("customerId") Long customerId);
```

JPQL memakai Entity name dan Java field name, bukan nama table dan column database.

## 17. Native Query

```java
@Query(value = "SELECT COALESCE(SUM(paid_amount), 0) FROM payment_transactions WHERE repayment_schedule_id = :scheduleId AND status = 'SUCCESS'", nativeQuery = true)
BigDecimal sumPaidAmountByScheduleId(@Param("scheduleId") Long scheduleId);
```

Native query memakai SQL asli, table name, dan column name.

## 18. When to Use Derived Query, JPQL, and Native Query

| Query Type | When to Use | Example |
| --- | --- | --- |
| Derived query | Simple query | `findByEmail` |
| JPQL | Medium query with entity relation | Loan by customer |
| Native query | Database-specific query or complex SQL | Payment summary |

## 19. Relationship Example

- One customer can have many loan applications.
- One loan application belongs to one customer.
- One loan application can have many repayment schedules.
- One repayment schedule can have many payment transactions.

## 20. @ManyToOne

```java
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "loan_applications")
public class LoanApplicationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "loan_amount", nullable = false)
    private BigDecimal loanAmount;

    @Column(name = "tenor_month", nullable = false)
    private Integer tenorMonth;

    @Column(name = "purpose", nullable = false)
    private String purpose;

    @Column(name = "status", nullable = false)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private CustomerEntity customer;

    // getter and setter
}
```

Many loan applications belong to one customer. `@JoinColumn` maps foreign key. `FetchType.LAZY` berarti customer data baru dimuat saat diakses.

## 21. @OneToMany

```java
@OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
private List<LoanApplicationEntity> loanApplications = new ArrayList<>();
```

One customer can have many loan applications. `mappedBy` mengarah ke field `customer` di `LoanApplicationEntity`. Hindari return Entity langsung ke API response; convert Entity ke DTO.

## 22. Lazy Loading

Lazy loading berarti related data tidak langsung dimuat.

Contoh: saat loading `LoanApplicationEntity`, customer tidak dimuat sampai `loan.getCustomer()` diakses.

Benefits:

- Menghindari load data yang tidak diperlukan.
- Bisa meningkatkan performa.

Risks:

- `LazyInitializationException`.
- Hidden extra queries.
- N+1 query problem.

## 23. Eager Loading

Eager loading berarti related data langsung dimuat.

```java
@ManyToOne(fetch = FetchType.EAGER)
private CustomerEntity customer;
```

Risks:

- Bisa load terlalu banyak data.
- Bisa lebih lambat jika relation besar.
- Tidak selalu lebih baik daripada lazy.

Recommendation: gunakan `LAZY` sebagai default untuk relationship. Gunakan join fetch atau DTO query saat related data memang dibutuhkan.

## 24. Join SQL

INNER JOIN:

```sql
SELECT
    l.id,
    l.loan_amount,
    l.tenor_month,
    l.status,
    c.full_name,
    c.nik
FROM loan_applications l
INNER JOIN customers c ON c.id = l.customer_id;
```

LEFT JOIN:

```sql
SELECT
    c.id,
    c.full_name,
    l.id AS loan_application_id,
    l.status
FROM customers c
LEFT JOIN loan_applications l ON l.customer_id = c.id;
```

`INNER JOIN` mengembalikan data yang match saja. `LEFT JOIN` mengembalikan semua row dari table kiri, walaupun relation tidak ada.

## 25. Join in JPQL

```java
@Query("SELECT l FROM LoanApplicationEntity l JOIN l.customer c WHERE c.id = :customerId")
List<LoanApplicationEntity> findLoansByCustomerId(@Param("customerId") Long customerId);
```

JPQL join memakai entity relationship, bukan manual table column.

## 26. Join Fetch

```java
@Query("SELECT l FROM LoanApplicationEntity l JOIN FETCH l.customer WHERE l.id = :id")
Optional<LoanApplicationEntity> findByIdWithCustomer(@Param("id") Long id);
```

Join fetch memuat relation dalam query yang sama. Ini berguna saat kita tahu response membutuhkan related data.

## 27. N+1 Query Problem

N+1 terjadi saat aplikasi load satu list, lalu load relation satu per satu untuk setiap item.

Contoh:

- Query 1: get 100 loans.
- Query 2-101: get customer untuk tiap loan.
- Total 101 queries.

Solusi:

- Join fetch.
- DTO projection.
- Batch size.
- Desain API response dengan hati-hati.

## 28. DTO for Relationship Response

Jangan return Entity langsung.

```json
{
  "id": 1,
  "loan_amount": 10000000,
  "tenor_month": 12,
  "purpose": "Working capital",
  "status": "APPROVED",
  "customer": {
    "id": 1,
    "full_name": "Budi Santoso",
    "nik": "3173010101900001",
    "email": "budi@mail.com"
  }
}
```

DTO melindungi API contract dan menghindari kebocoran struktur internal entity.

## 29. Transaction Basic

Transaction mengelompokkan beberapa operasi database menjadi satu unit of work.

Contoh create loan:

- Validate customer exists.
- Insert loan application.
- Generate repayment schedule.

Jika satu step gagal, transaction bisa rollback.

```java
import org.springframework.transaction.annotation.Transactional;

@Transactional
public LoanApplicationResponse createLoanApplication(CreateLoanApplicationRequest request) {
    // business logic here
}
```

`@Transactional` biasanya diletakkan di Service layer. Read operations bisa memakai `@Transactional(readOnly = true)`.

## 30. Validation Example with Jakarta Validation

```java
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class CreateLoanApplicationRequest {

    @JsonProperty("customer_id")
    @NotNull(message = "customer_id is required")
    private Long customerId;

    @JsonProperty("loan_amount")
    @NotNull(message = "loan_amount is required")
    @Positive(message = "loan_amount must be greater than 0")
    private BigDecimal loanAmount;

    @JsonProperty("tenor_month")
    @NotNull(message = "tenor_month is required")
    @Positive(message = "tenor_month must be greater than 0")
    private Integer tenorMonth;

    @JsonProperty("purpose")
    @NotBlank(message = "purpose is required")
    private String purpose;

    // getter and setter
}
```

## 31. Common Beginner Errors

- Lupa menambahkan PostgreSQL driver.
- Database belum dibuat.
- Username/password salah.
- Salah nama database.
- Salah penamaan Flyway migration.
- Mengubah migration lama yang sudah pernah jalan.
- Menggunakan `ddl-auto update` bersamaan tanpa memahami Flyway.
- Entity field tidak sesuai column.
- Lupa `@Id`.
- Lupa `@GeneratedValue`.
- Menggunakan `javax.persistence` padahal Spring Boot 3 memakai `jakarta.persistence`.
- Menggunakan `javax.validation` padahal Spring Boot 3 memakai `jakarta.validation`.
- Salah membedakan JPQL dan native query.
- Return Entity langsung ke Controller.
- `LazyInitializationException`.
- N+1 query karena loop akses relation.
- Menggunakan EAGER untuk semua relation.
- Tidak menggunakan DTO untuk response.
- Tidak menggunakan transaction saat create loan dan repayment schedule.

## 32. Summary

Database menyimpan data secara persistent. PostgreSQL membutuhkan driver agar bisa digunakan oleh Java. Java 17 cocok digunakan dengan Spring Boot 3.x. Spring Boot 3 menggunakan Jakarta namespace.

JPA adalah specification. Hibernate adalah implementation. Entity merepresentasikan table. Repository membantu akses database. Flyway membantu versioning schema database.

Derived query cocok untuk query sederhana. JPQL cocok untuk query berbasis entity. Native query cocok untuk SQL spesifik database.

Relationship perlu dipahami agar data antar table bisa dihubungkan. Lazy loading harus digunakan dengan hati-hati. Join fetch membantu mengambil relation dalam satu query. Finance case membantu memahami database modeling yang lebih realistis.

