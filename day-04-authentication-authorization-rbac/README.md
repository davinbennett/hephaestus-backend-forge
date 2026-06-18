# Day 4 - Authentication, Authorization & RBAC

## Goal

Peserta mampu membedakan authentication dan authorization, memahami bagaimana JWT membantu backend mengenali user, serta memahami bagaimana RBAC membatasi akses berdasarkan role.

## Why This Day Is Important

Dalam sistem finance, tidak semua user boleh akses semua data. Tidak semua action juga boleh dilakukan semua user.

Kesalahan akses bisa menyebabkan fraud, data leak, dan unauthorized approval. Access control bukan fitur tambahan, tetapi bagian dari risk control.

Backend harus memeriksa identity, role, permission, dan resource ownership. Frontend boleh menyembunyikan tombol, tetapi keputusan akses tetap harus ada di backend.

API contract harus menjelaskan auth requirement agar frontend, mobile, QA, dan backend membaca aturan akses yang sama. Error response security juga harus konsisten dengan standard error handling dari Day 2.

## Learning Objectives

Setelah menyelesaikan Day 4, peserta diharapkan mampu:

- Memahami security mindset pada finance backend.
- Memahami authentication.
- Memahami authorization.
- Memahami token-based authentication.
- Memahami JWT secara konseptual.
- Memahami authorization header.
- Memahami access token dan refresh token.
- Memahami RBAC.
- Memahami role dan permission.
- Memahami resource-level authorization.
- Memahami ownership check.
- Memahami 401 Unauthorized.
- Memahami 403 Forbidden.
- Memahami audit log untuk access control.
- Memahami prinsip least privilege.
- Memahami integrasi auth requirement dengan API contract dan Swagger.

## Expected Output

Di akhir Day 4, peserta diharapkan dapat:

- Menjelaskan perbedaan authentication dan authorization.
- Menjelaskan fungsi JWT.
- Menjelaskan kapan memakai 401 dan 403.
- Membuat RBAC matrix sederhana.
- Menjelaskan resource ownership check.
- Menambahkan auth requirement ke API contract.
- Menjelaskan access log field yang penting.

## Not Covered Today

Day 4 belum membahas:

- Full Spring Security implementation.
- Real JWT signing and verification.
- OAuth2.
- OpenID Connect deep dive.
- Password hashing implementation.
- Refresh token storage.
- Production-grade permission engine.
