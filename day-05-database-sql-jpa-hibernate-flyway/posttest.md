# Posttest - Database, SQL, JPA, Hibernate, Flyway & Query Relationship

## Objective

Posttest ini digunakan untuk mengukur pemahaman peserta setelah mempelajari Database, SQL, JPA, Hibernate, Flyway, dan Query Relationship.

1. Apa perbedaan in-memory Map dan database?

Jawaban:

```text
Map disimpan di RAM dan hilang kalau app mati, database disimpan di disk jadi data tetap ada
```

2. Kenapa database diperlukan pada aplikasi backend production?

Jawaban:

```text
Supaya data aman, tidak hilang, bisa dipakai banyak user, dan scalable
```

3. Apa fungsi SELECT, INSERT, UPDATE, dan DELETE pada SQL?

Jawaban:

```text
SELECT ambil data, INSERT tambah data, UPDATE ubah data, DELETE hapus data
```

4. Apa fungsi WHERE dan ILIKE di PostgreSQL?

Jawaban:

```text
WHERE buat filter data, ILIKE buat cari teks tanpa peduli huruf besar/kecil
```

5. Apa itu primary key dan foreign key?

Jawaban:

```text
Primary key = ID unik tiap data, foreign key = penghubung ke tabel lain
```

6. Apa itu JPA dan Hibernate, serta apa perbedaannya?

Jawaban:

```text
JPA adalah standar dokumen, bukan libary. Dan Hibernate adalah implementasi dari JPA
```

7. Apa itu Entity dan apa fungsi anotasi `@Entity`, `@Id`, serta `@GeneratedValue`?

Jawaban:

```text
Entity adalah representasi tabel.
@Entity tandai class, @Id primary key, @GeneratedValue auto ID.
```

8. Apa fungsi `@Table` dan `@Column`?

Jawaban:

```text
@Table buat nama tabel, @Column buat mapping kol
```

9. Apa itu Repository dan apa manfaat `JpaRepository`?

Jawaban:

```text
Repository buat akses database.
JpaRepository kasih CRUD siap pakai.   
```

10. Apa itu derived query method? Berikan contoh method untuk mencari customer berdasarkan email.

Jawaban:

```text
Query otomatis dari nama method.
Contoh: findByEmail(String email)
```

11. Apa fungsi `@Query`? Jelaskan perbedaan JPQL dan native query.

Jawaban:

```text
@Query buat query manual.
JPQL pakai entity, native pakai SQL langsung.
```

12. Apa itu Flyway dan kenapa database migration penting?

Jawaban:

```text
Flyway buat ngatur perubahan database.
Biar rapi dan konsisten.
```

13. Apa maksud penamaan file migration seperti `V1__create_customers_table.sql`? Kenapa migration lama sebaiknya tidak diubah setelah dijalankan?

Jawaban:

```text
V1 = versi.
Jangan ubah karena sudah tercatat, bisa bikin error
```

14. Jelaskan relationship one-to-many dan many-to-one dengan contoh Customer dan Order.

Jawaban:

```text
1 customer punya banyak order (one-to-many).
1 order milik 1 customer (many-to-one)
```

15. Apa fungsi `@ManyToOne`, `@OneToMany`, dan `@JoinColumn`?

Jawaban:

```text
@ManyToOne = banyak ke satu
@OneToMany = satu ke banyak
@JoinColumn = foreign key
```

16. Apa perbedaan lazy loading dan eager loading? Kenapa `FetchType.LAZY` sering lebih aman sebagai default?

Jawaban:

```text
Lazy = ambil data pas dibutuhkan.
Eager = ambil langsung.
Lazy lebih aman biar tidak ambil data berlebihan
```

17. Apa itu SQL join? Jelaskan perbedaan `INNER JOIN` dan `LEFT JOIN`.

Jawaban:

```text
JOIN gabung tabel.
INNER JOIN = hanya yang cocok
LEFT JOIN = semua kiri + yang cocok kanan
```

18. Apa itu N+1 query problem dan bagaimana cara sederhana menguranginya?

Jawaban:

```text
N+1 = query terlalu banyak.
Solusi: pakai join fetch atau entity graph
```

19. Kenapa Entity sebaiknya tidak langsung dikembalikan sebagai API response? Apa manfaat DTO?

Jawaban:

```text
Entity tidak langsung ke API karena bisa bocor data & berat.
DTO biar aman dan lebih rapi
```

20. Apa fungsi `@Transactional` dan kapan menggunakan `@Transactional(readOnly = true)`?

Jawaban:

```text
@Transactional buat manage transaksi.
readOnly = true untuk query biar lebih cepat & aman
```

## Reflection

Apa 3 hal utama yang kamu pahami hari ini?

```text
1. cara connect ke database
2. bedanya lazy dan eager
3. cukup membuat repo tanpa implnya
```

Apa 2 hal yang masih membingungkan?

```text
1.
2.
```

Apa 1 pertanyaan untuk mentor?

```text
Tulis pertanyaan di sini.
```
