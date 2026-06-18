# Pretest - Authentication, Authorization & RBAC

## Objective

Pretest ini digunakan untuk mengukur pemahaman awal peserta tentang authentication, authorization, JWT, RBAC, dan access control pada backend finance.

## Instructions

- Jawab dengan singkat dan jelas.
- Tidak perlu membuka dokumentasi.
- Tidak dinilai hanya dari benar atau salah, tetapi dari cara berpikir.
- Estimasi waktu: 20-30 menit.

## Section A - Security Mindset

### 1. Kenapa access control penting dalam sistem finance?

Jawaban:

```text
Untuk melindungi data sensitif dan transaksi finansial agar hanya bisa diakses oleh pihak yang berwenang
```

### 2. Apa risiko jika semua user bisa mengakses semua data?

Jawaban:

```text
Kebocoran data, manipulasi transaksi, fraud
```

### 3. Apa contoh data atau action yang harus dibatasi pada sistem loan?

Jawaban:

```text
Data penghasilan nasabah, status approval loan, limit kredit, approve/reject loan, dan perubahan bunga
```

### 4. Kenapa backend tidak boleh hanya mengandalkan frontend untuk membatasi akses?

Jawaban:

```text
Frontend bisa dimanipulasi (API call langsung)
```

### 5. Apa maksud deny by default?

Jawaban:

```text
Semua akses ditolak secara default, lalu hanya akses yang secara eksplisit diizinkan yang boleh berjalan
```

## Section B - Authentication

### 6. Apa itu authentication?

Jawaban:

```text
Proses memverifikasi identitas user (siapa dia)
```

### 7. Apa contoh proses authentication?

Jawaban:

```text
Login dengan email & password lalu diverifikasi oleh backend
```

### 8. Apa itu token-based authentication?

Jawaban:

```text
Authentication menggunakan token sebagai bukti identitas setelah login
```

### 9. Apa fungsi Authorization header?

Jawaban:

```text
Mengirim token identitas user ke backend pada setiap request
```

### 10. Apa arti format Bearer token?

Jawaban:

```text
Token yang “dibawa” client dan dipercaya backend selama valid
```

### 11. Apa yang harus dilakukan backend saat menerima token?

Jawaban:

```text
Memvalidasi signature, expiry, dan klaim token.
```

### 12. Apa yang terjadi jika token tidak ada?

Jawaban:

```text
Request ditolak dengan 401 Unauthorized.
```

### 13. Apa yang terjadi jika token invalid?

Jawaban:

```text
Request ditolak dengan 401 Unauthorized.
```

### 14. Apa yang terjadi jika token expired?

Jawaban:

```text
Request ditolak, user diminta login ulang atau refresh token.
```

## Section C - JWT

### 15. Apa itu JWT?

Jawaban:

```text
Token berbentuk JSON yang ditandatangani secara kriptografis untuk authentication & authorization.
```

### 16. Apa itu claim pada JWT?

Jawaban:

```text
Informasi yang disimpan di dalam token.
```

### 17. Sebutkan contoh claim yang umum ada di JWT.

Jawaban:

```text
sub, user_id, role, exp, iat.
```

### 18. Kenapa payload JWT tidak boleh dipercaya sebelum signature divalidasi?

Jawaban:

```text
Karena payload bisa dimodifikasi oleh client.
```

### 19. Data apa saja yang tidak boleh disimpan di JWT?

Jawaban:

```text
Password, PIN, data finansial sensitif, PII detail.
```

### 20. Kenapa JWT harus punya expiry?

Jawaban:

```text
Untuk membatasi dampak token bocor dan meningkatkan keamanan.
```

### 21. Apa perbedaan access token dan refresh token?

Jawaban:

```text
Access token: short-lived, untuk akses API
Refresh token: long-lived, untuk mendapatkan access token baru
```

## Section D - Authorization and RBAC

### 22. Apa itu authorization?

Jawaban:

```text
Proses menentukan apakah user boleh melakukan suatu action.
```

### 23. Apa perbedaan authentication dan authorization?

Jawaban:

```text
Authentication = siapa user
Authorization = apa yang boleh user lakukan
```

### 24. Apa itu RBAC?

Jawaban:

```text
Role-Based Access Control, akses ditentukan berdasarkan role.
```

### 25. Apa itu role?

Jawaban:

```text
Kumpulan tanggung jawab atau posisi user dalam sistem.
```

### 26. Apa itu permission?

Jawaban:

```text
Hak spesifik untuk melakukan action tertentu.
```

### 27. Apa contoh role dalam loan system?

Jawaban:

```text
Customer, Loan Officer, Admin, Risk Analyst.
```

### 28. Apa contoh permission dalam loan system?

Jawaban:

```text
view_loan, approve_loan, edit_interest_rate.
```

### 29. Kenapa user yang sudah login belum tentu boleh melakukan semua action?

Jawaban:

```text
Karena login ≠ punya izin.
```

## Section E - Resource-Level Authorization

### 30. Apa itu resource-level authorization?

Jawaban:

```text
Pengecekan apakah user boleh mengakses resource tertentu, bukan hanya berdasarkan role.
```

### 31. Kenapa role check saja tidak cukup?

Jawaban:

```text
Karena user mungkin punya role yang sama tapi resource berbeda.
```

### 32. Apa itu ownership check?

Jawaban:

```text
Pengecekan apakah resource memang milik user tersebut.
```

### 33. Apa contoh kasus customer melihat loan milik customer lain?

Jawaban:

```text
Akses /loans/{id} tanpa validasi owner.
```

### 34. Apa risiko IDOR?

Jawaban:

```text
User bisa mengakses atau memodifikasi data milik user lain hanya dengan mengganti ID.
```

### 35. Bagaimana cara backend mencegah data leakage karena salah akses resource?

Jawaban:

```text
Ownership check, query berbasis user_id, deny by default.
```

## Section F - 401, 403, and Audit

### 36. Kapan menggunakan 401 Unauthorized?

Jawaban:

```text
Saat user belum ter-authenticated atau token invalid.
```

### 37. Kapan menggunakan 403 Forbidden?

Jawaban:

```text
Saat user ter-authenticated tapi tidak punya izin.
```

### 38. Apa bedanya 401 dan 403?

Jawaban:

```text
401: siapa kamu tidak jelas
403: kamu jelas, tapi tidak boleh
```

### 39. Kenapa access log penting?

Jawaban:

```text
Untuk audit, deteksi fraud, dan investigasi insiden.
```

### 40. Field apa saja yang penting dalam access log?

Jawaban:

```text
User ID, role, endpoint, method, timestamp, status code, IP.
```

## Self Assessment

| Area | Score 1-5 |
| --- | --- |
| Authentication |5|
| Authorization |5 |
| JWT |5 |
| RBAC |5 |
| Resource ownership |4 |
| 401 vs 403 |4 |
| Audit log | 4|

## Notes

```text

```
