# Pretest - API Contract, API Testing & Swagger

## Objective

Pretest ini digunakan untuk mengukur pemahaman awal peserta tentang API contract, API testing, dan Swagger.

## Instructions

- Jawab dengan singkat dan jelas.
- Tidak perlu membuka dokumentasi.
- Tidak dinilai hanya dari benar atau salah, tetapi dari cara berpikir.
- Estimasi waktu: 20-30 menit.

## Section A - API Contract

### 1. Apa itu API contract?

Jawaban:

```text
Kesepakatan antara client dan server tentang bagaimana API digunakan, termasuk format request dan response
```

### 2. Kenapa API contract penting?

Jawaban:

```text
Agar frontend dan backend bisa bekerja konsisten tanpa miskomunikasi
```

### 3. Apa saja isi API contract?

Jawaban:

```text
Endpoint, HTTP method, request (params/body), response, status code, dan format data
```

### 4. Apa itu endpoint?

Jawaban:

```text
URL yang digunakan untuk mengakses resource API.
```

### 5. Apa itu HTTP method?

Jawaban:

```text
Jenis operasi pada API seperti GET, POST, PUT, DELETE
```

### 6. Apa itu request body?

Jawaban:

```text
Data yang dikirim client ke server dalam request
```

### 7. Apa itu response body?

Jawaban:

```text
Data yang dikirim server sebagai hasil dari request
```

### 8. Apa itu HTTP status code?

Jawaban:

```text
Kode angka yang menunjukkan hasil request (200, 404)
```

### 9. Kenapa request dan response perlu ditulis jelas?

Jawaban:

```text
Agar implementasi konsisten dan menghindari error/mismatch
```

### 10. Apa risiko jika API contract tidak jelas?

Jawaban:

```text
Bug, miskomunikasi, integrasi gagal, dan development lebih lama
```

## Section B - DTO

### 11. Apa itu DTO?

Jawaban:

```text
Object untuk transfer data antara client dan server
```

### 12. Apa itu request DTO?

Jawaban:

```text
untuk menerima data dari client
```

### 13. Apa itu response DTO?

Jawaban:

```text
untuk mengirim data ke client
```

### 14. Kenapa DTO dan model sebaiknya dipisah?

Jawaban:

```text
Agar struktur internal tidak terekspos dan lebih fleksibel dalam perubahan
```

### 15. Kenapa JSON biasanya menggunakan snake_case sedangkan Java menggunakan camelCase?

Jawaban:

```text
JSON umum pakai snake_case, Java standard camelCase
```

### 16. Apa fungsi @JsonProperty?

Jawaban:

```text
Untuk mapping nama field JSON ke field di Java
```

## Section C - HTTP Method

### 17. Apa fungsi POST?

Jawaban:

```text
Untuk membuat resource baru
```

### 18. Apa fungsi GET?

Jawaban:

```text
Untuk update sebagian data resource.
```

### 19. Apa fungsi PUT?

Jawaban:

```text
Untuk update seluruh data resource.
```

### 20. Apa fungsi PATCH?

Jawaban:

```text
Untuk update sebagian data resource.
```

### 21. Apa perbedaan PUT dan PATCH?

Jawaban:

```text
PUT mengganti seluruh data, PATCH hanya sebagian.
```

### 22. Kapan menggunakan 201 Created?

Jawaban:

```text
Saat berhasil membuat resource baru.
```

### 23. Kapan menggunakan 200 OK?

Jawaban:

```text
Saat request berhasil.
```

### 24. Kapan menggunakan 400 Bad Request?

Jawaban:

```text
Saat request invalid/salah format.
```

### 25. Kapan menggunakan 404 Not Found?

Jawaban:

```text
Saat resource tidak ditemukan.
```

## Section D - API Testing

### 26. Apa itu API testing?

Jawaban:

```text
Proses menguji endpoint API untuk memastikan sesuai kontrak.
```

### 27. Kenapa API perlu dites?

Jawaban:

```text
Untuk memastikan API bekerja benar dan tidak error.
```

### 28. Tool apa yang biasa digunakan untuk API testing?

Jawaban:

```text
Postman, Insomnia, curl.
```

### 29. Apa yang perlu dicek saat melakukan API testing?

Jawaban:

```text
Status code, response body, format data, error handling.
```

### 30. Apa itu expected response?

Jawaban:

```text
Response yang diharapkan sesuai dengan API contract.
```

## Section E - Swagger

### 31. Apa itu Swagger?

Jawaban:

```text
Tool untuk dokumentasi dan testing API secara interaktif.
```

### 32. Apa itu OpenAPI?

Jawaban:

```text
Standar spesifikasi untuk mendefinisikan API.
```

### 33. Apa manfaat Swagger UI?

Jawaban:

```text
Melihat dan mencoba API langsung dari browser.
```

### 34. Apa bedanya Postman dan Swagger UI?

Jawaban:

```text
Postman untuk testing manual; Swagger UI lebih ke dokumentasi + testing.
```

### 35. Menurut kamu, apakah Swagger bisa menggantikan dokumentasi API manual? Jelaskan.

Jawaban:

```text
Bisa sebagian, karena otomatis dan interaktif, tapi kadang tetap butuh dokumentasi tambahan untuk penjelasan bisnis.
```

## Self Assessment

| Area | Score 1-5 |
| --- | --- |
| API contract |4|
| DTO |4|
| HTTP method |4|
| API testing |4|
| Swagger UI |4|
| OpenAPI |4|

## Notes

```text
Tulis bagian yang masih membingungkan.
```
