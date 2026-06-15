# Posttest - Spring Boot REST API

Jawab pertanyaan berikut setelah membaca materi dan membuat Customer REST API.

### 1. Apa itu Spring Boot?

Jawaban:

```text
framework berbasis Java yang dirancang untuk mempermudah dan mempercepat proses pengembangan aplikasi
```

### 2. Apa fungsi Spring Initializr?

Jawaban:

```text
untuk menentukan dependensi, versi Java, dan konfigurasi proyek lainnya secara terpusat 
```

### 3. Saat membuat project, kenapa memilih Maven?

Jawaban:

```text
karena kemampuannya dalam manajemen dependency dan build lifecycle yang terstandarisasi
```

### 4. Apa fungsi dependency Spring Web?

Jawaban:

```text
untuk mengintegrasikan pengembangan aplikasi web ke dalam proyek Spring Boot
```

### 5. Kenapa Day 1 belum menambahkan dependency database?

Jawaban:

```text
karena day 1 masih membahas dan review tentang java fundamental, sehingga memastikan murid kelas memahami dulu tentang java dasar dan OOP, sebelum ke materi yang lebih dalam
```

### 6. Apa fungsi file pom.xml?

Jawaban:

```text
memuat informasi proyek, dependency yang digunakan, plugin build, serta konfigurasi lain yang diperlukan untuk proses kompilasi
```

### 7. Apa fungsi TrainingApplication.java?

Jawaban:

```text
dengan adanya metode main, kelas ini menginisiasi Spring Application Context dan menjalankan aplikasi
```

### 8. Apa fungsi @SpringBootApplication?

Jawaban:

```text
untuk mengaktifkan mekanisme konfigurasi otomatis, mendeteksi bean di dalam proyek, serta menginisialisasi Spring environment 
```

### 9. Bagaimana cara menjalankan Spring Boot dari IDE?

Jawaban:

```text
di VSCode, buka file main, lalu klik kanan, dan klik 'Run Code'
```

### 10. Bagaimana cara menjalankan Spring Boot dari terminal?

Jawaban:

```text
./mvnw spring-boot:run
```

### 11. Jika membuka http://localhost:8080 dan hasilnya 404, apakah selalu error? Jelaskan.

Jawaban:

```text
Tidak selalu. Kode 404 (Not Found) mengindikasikan bahwa server berhasil dihubungi, namun endpoint yang diminta tidak tersedia
```

### 12. Apa itu REST API?

Jawaban:

```text
arsitektur antarmuka aplikasi yang menggunakan protokol HTTP untuk pertukaran data antar sistem
```

### 13. Apa itu endpoint?

Jawaban:

```text
titik spesifik berupa URL atau URI yang disediakan oleh server untuk diakses oleh klien
```

### 14. Apa perbedaan request dan response?

Jawaban:

```text
request merupakan data yang dikirimkan oleh klien ke server untuk meminta aksi tertentu. Response adalah data balasan dari server kepada klien sebagai hasil pemrosesan permintaan tersebut
```

### 15. Kenapa JSON menggunakan snake_case, sedangkan Java menggunakan camelCase?

Jawaban:

```text
merupakan konvensi standar pada masing-masing teknologi
```

### 16. Apa fungsi @JsonProperty?

Jawaban:

```text
untuk memetakan nama field pada objek Java ke nama properti yang berbeda pada format JSON, sehingga menjaga konsistensi data
```

### 17. Apa tugas Controller?

Jawaban:

```text
sebagai lapisan antarmuka yang menangani permintaan HTTP, melakukan validasi awal terhadap input, serta mengarahkan permintaan tersebut ke lapisan Service
```

### 18. Apa tugas Service?

Jawaban:

```text
lapisan yang menyimpan seluruh logika bisnis (business logic) aplikasi
```

### 19. Apa itu DTO?

Jawaban:

```text
Data Transfer Object (DTO) adalah objek yang digunakan untuk membawa data antar proses
```

### 20. Apa itu Model?

Jawaban:

```text
representasi dari struktur tabel dalam basis data
```

### 21. Kenapa request body tidak langsung menggunakan model?

Jawaban:

```text
untuk menjaga keamanan dan memisahkan kebutuhan input pengguna dari struktur basis data
```

### 22. Kenapa response tidak langsung menggunakan model?

Jawaban:

```text
Penggunaan DTO dalam response untuk menyaring informasi sensitif
```

### 23. Kenapa business logic tidak boleh ditaruh di Controller?

Jawaban:

```text
memisahkan logika bisnis dari Controller memastikan kode lebih mudah diuji (testable), dipelihara (maintainable), dan digunakan kembali (reusable)
```

### 24. Kapan menggunakan 200 OK?

Jawaban:

```text
ketika sebuah request berhasil diproses oleh server dan respons berhasil dikirimkan kembali kepada klien
```

### 25. Kapan menggunakan 201 Created?

Jawaban:

```text
untuk memberikan respon bahwa sebuah data telah berhasil dibuat melalui metode POST
```

### 26. Apa bedanya path parameter dan query parameter?

Jawaban:

```text
Path parameter merupakan bagian dari jalur URL yang bersifat wajib, sedangkan query parameter bersifat opsional dan biasanya ditambahkan setelah tanda tanya (?) untuk keperluan filter
```

### 27. Jelaskan flow POST /api/v1/customers dari request sampai response.

Jawaban:

```text
Permintaan dikirim ke Controller, kemudian Controller memvalidasi dan meneruskan DTO ke Service. Service menjalankan aturan bisnis, melakukan penyimpanan ke List (ram), lalu mengembalikan hasil ke Controller. Terakhir, Controller mengirimkan response kepada klien dengan status 201 Created
```

### 28. Bagian mana yang paling sulit saat membuat Spring Boot REST API?

Jawaban:

```text
Syntax yang belum familiar karena saya berasal dari Golang
```

### 29. Error apa yang kamu temui dan bagaimana cara menyelesaikannya?

Jawaban:

```text
Tidak ada error
```

### 30. Apa yang ingin kamu pelajari berikutnya?

Jawaban:

```text
Lebih ke fitur-fitur apa saja yang dimiliki Spring boot
```
