# Pretest - Database, SQL, JPA, Hibernate, Flyway & Query Relationship

## Objective

Pretest ini digunakan untuk mengukur pemahaman awal peserta tentang database, SQL, JPA, Hibernate, Flyway, dan relationship antar table.

## Instructions

- Jawab dengan singkat dan jelas.
- Tidak perlu membuka dokumentasi.
- Tidak dinilai hanya dari benar atau salah, tetapi dari cara berpikir.
- Estimasi waktu: 20-30 menit.

## Section A - Database Basic

1. Apa itu database?

Jawaban:

```text
Database adalah sistem untuk menyimpan, mengelola, dan mengakses data secara terstruktur.
```

2. Apa perbedaan menyimpan data di Map dan menyimpan data di database?

Jawaban:

```text
Map hanya di memory (sementara), sedangkan database menyimpan data secara permanen dan bisa diakses banyak aplikas
```

3. Apa itu table?

Jawaban:

```text
Table adalah struktur dalam database yang menyimpan data dalam bentuk baris dan kolom
```

4. Apa itu row?

Jawaban:

```text
Row adalah satu record/baris data dalam table
```

5. Apa itu column?

Jawaban:

```text
field/atribut yang mendeskripsikan data dalam table
```

6. Apa itu primary key?

Jawaban:

```text
kolom unik untuk mengidentifikasi setiap row
```

7. Apa itu foreign key?

Jawaban:

```text
Foreign key adalah kolom yang mengacu ke primary key di table lain untuk relasi
```

8. Kenapa aplikasi backend membutuhkan database driver?

Jawaban:

```text
Driver diperlukan agar aplikasi bisa berkomunikasi dengan database
```

## Section B - SQL Basic

9. Apa fungsi SELECT?

Jawaban:

```text
SELECT digunakan untuk mengambil data
```

10. Apa fungsi INSERT?

Jawaban:

```text
Tulis jawaban di sini.
```

11. Apa fungsi UPDATE?

Jawaban:

```text
INSERT untuk menambahkan data
```

12. Apa fungsi DELETE?

Jawaban:

```text
UPDATE untuk mengubah data.
```

13. Apa fungsi WHERE?

Jawaban:

```text
DELETE untuk menghapus data.
```

14. Apa perbedaan LIKE dan ILIKE di PostgreSQL?

Jawaban:

```text
LIKE case sensitive, ILIKE case insensitive
```

15. Apa fungsi ORDER BY?

Jawaban:

```text
ORDER BY untuk mengurutkan hasil query
```

16. Apa fungsi LIMIT?

Jawaban:

```text
LIMIT untuk membatasi jumlah hasil
```

17. Apa itu JOIN?

Jawaban:

```text
JOIN untuk menggabungkan data dari beberapa table
```

18. Apa perbedaan INNER JOIN dan LEFT JOIN?

Jawaban:

```text
INNER JOIN hanya ambil data yang match di kedua table, LEFT JOIN ambil semua dari kiri meskipun tidak ada pasangan
```

## Section C - JPA & Hibernate

19. Apa itu JPA?

Jawaban:

```text
JPA adalah spesifikasi untuk ORM di Java
```

20. Apa itu Hibernate?

Jawaban:

```text
Hibernate adalah implementasi JPA.
```

21. Apa perbedaan JPA dan Hibernate?

Jawaban:

```text
JPA adalah standar, Hibernate adalah tool yang mengimplementasikan standar tersebut
```

22. Apa itu Entity?

Jawaban:

```text
representasi table dalam bentuk class Java
```

23. Apa fungsi @Entity?

Jawaban:

```text
@Entity menandakan class sebagai entity
```

24. Apa fungsi @Table?

Jawaban:

```text
@Table mengatur nama table di database
```

25. Apa fungsi @Id?

Jawaban:

```text
@Id menandakan primary key
```

26. Apa fungsi @GeneratedValue?

Jawaban:

```text
@GeneratedValue untuk auto-generate nilai ID.
```

27. Apa itu Repository?

Jawaban:

```text
Repository adalah interface untuk akses data ke database.
```

28. Apa fungsi JpaRepository?

Jawaban:

```text
JpaRepository menyediakan CRUD dan query siap pakai.
```

29. Pada Spring Boot 3, kenapa import JPA menggunakan jakarta.persistence, bukan javax.persistence?

Jawaban:

```text
Karena Spring Boot 3 mengikuti Jakarta EE, bukan Java EE lama.
```

## Section D - Query

30. Apa itu derived query method di Spring Data JPA?

Jawaban:

```text
Method query otomatis berdasarkan nama method.
```

31. Apa contoh query method untuk mencari customer berdasarkan email?

Jawaban:

```text
findByEmail(String email)
```

32. Apa fungsi @Query?

Jawaban:

```text
@Query digunakan untuk custom query.
```

33. Apa perbedaan JPQL dan native query?

Jawaban:

```text
JPQL pakai entity, native query pakai SQL langsung.
```

34. Kapan menggunakan native query?

Jawaban:

```text
Saat butuh query kompleks atau fitur database spesifik.
```

## Section E - Flyway

35. Apa itu database migration?

Jawaban:

```text
Migration adalah proses perubahan schema database secara terkontrol.
```

36. Apa itu Flyway?

Jawaban:

```text
Flyway adalah tool untuk versioning dan migration database.
```

37. Kenapa perubahan schema database perlu versioning?

Jawaban:

```text
Agar perubahan terkontrol, bisa rollback, dan konsisten antar environment.
```

38. Apa maksud file V1__create_customers_table.sql?

Jawaban:

```text
File migration versi 1 untuk membuat table customers.
```

39. Apa risiko jika struktur database diubah manual tanpa migration?

Jawaban:

```text
Data inconsistency, error, sulit tracking perubahan.
```

## Section F - Relationship & Lazy Loading

40. Apa itu relationship antar table?

Jawaban:

```text
Hubungan antar table berdasarkan foreign key.
```

41. Apa itu one-to-many?

Jawaban:

```text
Satu entity punya banyak entity lain.
```

42. Apa itu many-to-one?

Jawaban:

```text
Banyak entity mengacu ke satu entity.
```

43. Apa fungsi @ManyToOne?

Jawaban:

```text
Menandai relasi many-to-one.
```

44. Apa fungsi @OneToMany?

Jawaban:

```text
Menandai relasi one-to-many.
```

45. Apa itu lazy loading?

Jawaban:

```text
Data hanya di-load saat dibutuhkan.
```

46. Apa itu eager loading?

Jawaban:

```text
Data langsung di-load bersama entity utama.
```

47. Apa risiko lazy loading jika tidak dipahami?

Jawaban:

```text
Bisa menyebabkan banyak query tersembunyi dan performance drop.
```

48. Apa itu N+1 query problem?

Jawaban:

```text
Masalah ketika satu query memicu banyak query tambahan.
```

49. Apa itu join fetch?

Jawaban:

```text
Mengambil relasi data dalam satu query.
```

## Section G - Finance Case

50. Dalam sistem pinjaman, kenapa customer dan loan application sebaiknya dipisah ke table berbeda?

Jawaban:

```text
Agar normalisasi, menghindari duplikasi, dan mudah maintain relasi.
```

51. Dalam sistem cicilan, kenapa repayment schedule perlu table sendiri?

Jawaban:

```text
Karena satu loan punya banyak jadwal pembayaran.
```

52. Apa contoh query yang berguna untuk melihat loan berdasarkan status?

Jawaban:

```text
SELECT * FROM loan WHERE status = 'APPROVED';
```

53. Apa contoh query yang berguna untuk melihat total pembayaran customer?

Jawaban:

```text
SELECT customer_id, SUM(amount) FROM payment GROUP BY customer_id;
```

## Self Assessment

| Area | Score 1-5 |
| --- | --- |
| Database basic |5 |
| SQL basic |5 |
| JPA |4 |
| Hibernate |4 |
| Repository |5 |
| Flyway |4 |
| Relationship | 5|
| Join query |5 |
| Lazy loading | 4|
| Finance data modeling | 3|

## Notes

```text
CARA KERJA DATABASE DI FINANCE / PERUSAHAAN BESAR

```

