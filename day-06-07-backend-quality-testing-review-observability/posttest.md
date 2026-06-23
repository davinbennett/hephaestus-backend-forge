# Posttest - Backend Quality: Testing, Peer Code Review & Observability

## Objective

Posttest ini digunakan untuk mengukur pemahaman peserta setelah mempelajari testing mindset, unit testing, peer code review, structured logging, correlation ID, dan PII safety.

## Instructions

- Jawab dengan singkat dan jelas.
- Total pertanyaan: 10.

1. Kenapa testing disebut risk reduction?

Jawaban:

```text
Karena testing membantu menemukan bug lebih awal sehingga mengurangi risiko error di production dan dampak ke user
```

2. Apa perbedaan working code dan trusted code?

Jawaban:

```text
Working code cuma berjalan, sedangkan trusted code sudah diuji sehingga lebih bisa dipercaya di berbagai kondisi
```

3. Jelaskan pola Given-When-Then.

Jawaban:

```text
Given: kondisi awal
When: aksi dilakukan
Then: hasil yang diharapkan
```

4. Kenapa service layer cocok untuk unit test?

Jawaban:

```text
Karena berisi business logic utama dan bisa dites tanpa tergantung DB atau external system
```

5. Apa peran JUnit 5 dan Mockito dalam unit test?

Jawaban:

```text
JUnit 5: framework untuk menulis dan menjalankan test
Mockito: untuk mock dependency agar test fokus ke logic
```

6. Sebutkan 3 test case penting untuk `LoanApplicationService`.

Jawaban:

```text
Pengajuan berhasil saat data valid
Pengajuan ditolak saat skor kredit rendah
Validasi input gagal (misalnya nilai negatif / null)
```

7. Apa tujuan peer code review?

Jawaban:

```text
Untuk memastikan kualitas code, menemukan bug lebih awal, dan menjaga standar coding tim
```

8. Apa itu structured logging dan kenapa penting?

Jawaban:

```text
Logging dalam format terstruktur (misalnya JSON).
Penting karena memudahkan pencarian, analisis, dan monitoring di production
```

9. Apa fungsi `correlation_id` pada log dan error response?

Jawaban:

```text
Untuk melacak satu request end-to-end, terutama di sistem yang terdiri dari banyak service.
```

10. Sebutkan minimal 5 data yang tidak boleh ditulis mentah di log.

Jawaban:

```text
Password
PIN / OTP
Token / API key
Nomor kartu kredit
NIK / KTP
```

## Reflection

Apa 3 hal utama yang kamu pahami hari ini?

```text
1. Testing untuk mengurangi resiko bug
2. Unit Test fokus ke logic per method
3. Logging untuk observability
```

Apa 2 hal yang masih membingungkan?

```text
1. seberapa detail logging
2. kapan nerapin unit dan integration test
```

Apa 1 hal yang akan kamu cek saat melakukan code review?

```text
cek semua return dari log dan hasil dari unit test
```
