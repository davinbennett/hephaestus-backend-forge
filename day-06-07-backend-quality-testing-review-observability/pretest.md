# Pretest - Backend Quality: Testing, Peer Code Review & Observability

## Objective

Pretest ini digunakan untuk mengukur pemahaman awal peserta tentang testing mindset, unit testing, peer code review, structured logging, correlation ID, dan PII safety.

## Instructions

- Jawab dengan singkat dan jelas.
- Tidak perlu membuka dokumentasi.
- Tidak dinilai hanya dari benar atau salah, tetapi dari cara berpikir.
- Total pertanyaan: 15.
- Estimasi waktu: 15-20 menit.

1. Apa perbedaan working code dan trusted code?

Jawaban:

```text
Working code itu cuma jalan di kondisi tertentu.
Trusted code itu kode yang sudah diuji (lewat test), jadi kita lebih yakin dia tetap benar walau ada perubahan atau kasus lain
```

2. Kenapa testing disebut sebagai risk reduction?

Jawaban:

```text
Karena testing bantu nemuin bug lebih awal sebelum masuk production
```

3. Apa itu Given-When-Then?

Jawaban:

```text
Given: kondisi awal
When: aksi dilakukan
Then: hasil yang diharapkan
```

4. Apa perbedaan unit test dan integration test?

Jawaban:

```text
Unit test cuma ngetes 1 bagian kecil (misalnya 1 method).
Integration test ngetes beberapa komponen yang saling terhubung (misalnya service + database)
```

5. Kenapa service layer biasanya cocok untuk unit test?

Jawaban:

```text
Karena biasanya di situ ada business logic utama.
Dan bisa dites tanpa perlu framework berat atau koneksi ke DB
```

6. Apa fungsi JUnit 5?

Jawaban:

```text
JUnit 5 itu framework buat nulis dan menjalankan test di Java
```

7. Apa fungsi Mockito?

Jawaban:

```text
Mockito dipakai buat bikin fake object (mock). Jadi kita bisa kontrol behavior dependency saat testing
```

8. Kenapa dependency seperti repository sering dimock saat unit test?

Jawaban:

```text
Test lebih cepat
Tidak tergantung database
Fokus ke logic service
```

9. Apa contoh test case penting untuk `LoanApplicationService`?

Jawaban:

```text
Pengajuan berhasil saat data valid
Ditolak kalau skor kredit rendah
Error kalau input tidak valid (misalnya nominal negatif)
```

10. Apa tujuan peer code review?

Jawaban:

```text
Untuk ngecek kualitas code, cari bug lebih awal, dan sharing knowledge antar tim
```

11. Area apa saja yang perlu dicek saat code review backend?

Jawaban:

```text
Logic sudah benar atau belum
Error handling (sudah aman belum)
Test ada atau tidak
Security (jangan bocorin data sensitif)
Logging jelas atau tidak
Readability code
```

12. Apa itu structured logging?

Jawaban:

```text
Logging yang formatnya rapi (biasanya JSON atau key-value).
Tujuannya supaya gampang dicari, difilter, dan dibaca di tools seperti Kibana
```

13. Apa fungsi `correlation_id`?

Jawaban:

```text
Untuk identifikasi satu request.
Jadi kalau ada error, kita bisa trace dari awal sampai akhir di berbagai service
```

14. Kapan menggunakan log level `info`, `warn`, dan `error`?

Jawaban:

```text
info: aktivitas normal
warn: ada hal tidak ideal tapi masih jalan
error: terjadi kegagalan yang perlu diperbaiki
```

15. Sebutkan data yang tidak boleh ditulis mentah di log.

Jawaban:

```text
Password
PIN / OTP
Token / API key
Nomor KTP / kartu kredit
Data pribadi sensitif lainnya
```

## Self Assessment

| Area | Score 1-5 |
|---|---|
| Testing mindset | 4|
| Given-When-Then | 4|
| JUnit 5 | 3|
| Mockito | 3|
| Service layer testing | 4|
| Peer code review | 4|
| Structured logging | 3|
| Correlation ID | 4|
| PII safety | 5|
