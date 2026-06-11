# Pretest - Java Fundamental

## Objective

Pretest ini bertujuan mengukur pemahaman awal tentang Java Fundamental sebelum membaca materi dan mengerjakan exercise.

## Instructions

- Jawab dengan bahasa sendiri.
- Tidak perlu takut salah.
- Jangan melihat jawaban teman terlebih dahulu.
- Jika belum tahu, tulis apa yang kamu pahami saat ini.

Estimasi waktu: 20-30 menit.

## Section A - Basic Syntax

### 1. Apa itu variable?

Jawaban:

```text
tempat untuk menyimpan data/value dan itu didalam memori komputer
```

### 2. Apa itu data type?

Jawaban:

```text
jenis data untuk gimana data disimpan dan dioleh oleh bahasa pemrograman (int, float, double, string, char)
```

### 3. Sebutkan contoh tipe data untuk text, number, dan true/false.

Jawaban:

```text
text: char, text, string, varchar
number: float, double, int, long int, long float
true/false: bool
```

### 4. Apa perbedaan variable declaration dan assignment?

Jawaban:

```text
declaration: variabel yang hanya berisi variabel aja tanpa ada value (int i;)
assignment: variabel yang sudah diberikan suatu value (int i = 0;)
```

### 5. Apa itu camelCase?

Jawaban:

```text
suatu format penulisan dengan awalan huruf kecil, dan jika lebih dari 1 kata, maka kata berikutnya harus kapital dan disambung dengan kata sebelumnya (testCase)
```

## Section B - Class and Object

### 6. Apa itu class?

Jawaban:

```text
blueprint untuk menciptakan objek. dan didalam class ada attribute dan method
```

### 7. Apa itu object?

Jawaban:

```text
hasil dari blueprint yang sudah ada. dan object ini mempunyai attribute dan method dari class/blueprint yang sudah dibuat tadi
```

### 8. Apa perbedaan class dan object?

Jawaban:

```text
kalau class adalah blueprint/templatenya, kalau object adalah hasil dari blueprint. Jadi object bisa memakai attribute dan method dari class yang sudah dibuat
```

## Section C - Field, Method, Constructor

### 9. Apa itu field?

Jawaban:

```text
variabel/atribute yang dimiliki oleh class dan mempunyai tipe data (
    class Human:
        String name; <- ATRIBUTE
        int age;     <- ATRIBUTE
)
```

### 10. Apa itu method?

Jawaban:

```text
fungsi dalam class yang berguna untuk menjalankan suatu aktivitas terhadap atribute (
    class Human:
        String name; <- ATRIBUTE
        int age;     <- ATRIBUTE

        void eat(){}    <- method
        int sumAge(){}  <- method
)
```

### 11. Apa itu parameter?

Jawaban:

```text
variabel yang akan dipakai oleh method untuk menerima value dari luar method itu
```

### 12. Apa itu return value?

Jawaban:

```text
hasil pengembalian dari method itu setelah selesai dirun
```

### 13. Apa itu constructor?

Jawaban:

```text
method khusus dalam class yang jika class itu dipanggil maka otomatis method itu akan dijalankan
```

## Section D - Access Modifier

### 14. Apa fungsi private?

Jawaban:

```text
supaya atribute/method hanya diakses didalam class itu aja
```

### 15. Apa fungsi public?

Jawaban:

```text
supaya setiap attribute/method didalam suatu class bisa dipakai class lainnya
```

### 16. Kenapa field biasanya dibuat private?

Jawaban:

```text
Supaya class lain tidak bisa akses langsung atribute dari class yang membuat private field itu, jadi apabila ingin mengakses harus pakai getter dan setter
```

## Section E - Getter and Setter

### 17. Apa fungsi getter?

Jawaban:

```text
method untuk mendapatkan value dari atribute yang diprivate di class lain
```

### 18. Apa fungsi setter?

Jawaban:

```text
method untuk menentukan value dari atribute yang diprivate di class lain
```

### 19. Kenapa tidak semua field dibuat public?

Jawaban:

```text
karena berkaitan dengan keamanan data, dengan public maka semua class bisa mengubah atribute dari suatu class sehingga bisa menyebabkan bug. Jadi dengan di private, apabila class lain ingin akses atribute private dari class lain, harus pakai method getter/setter, tidak langsung mengakses ke attributenya
```

## Section F - Collection

### 20. Apa itu List?

Jawaban:

```text
struktur data untuk menyimpan kumpulan data di 1 variabel
```

### 21. Apa itu Map?

Jawaban:

```text
struktur data yang menyimpan kumpulan data dalam bentuk key dan value
```

### 22. Kapan menggunakan List dan kapan menggunakan Map?

Jawaban:

```text
list : kalau data sequence dan 1 nama list hanya berisi kumpulan data yang merepresentasikan nama list itu (nama=['davin', 'bennett'])

map : ketika dalam 1 nama map ada beberapa data yang perlu ditampilkan dengan memakai key-value (
    data : {
        'name': 'bennet',
        'age': 12
    }
)
```

## Section G - OOP Basic

### 23. Apa itu encapsulation?

Jawaban:

```text
metode membungkus method/attribute didalam satu class dan membatasi akses method/attribute untuk diakses di class lain
```

### 24. Apa itu interface?

Jawaban:

```text
kumpulan method yang harus diimplementasikan oleh class yang pakai class interface itu. jadi jika ada 4 method di interface a, maka class yang pakai interface a harus implementasikan 4 method itu di class itu
```

### 25. Apa itu abstract class?

Jawaban:

```text
class yang tidak bisa dibuat object secara langsung. dan didalam abstract class bisa punya method. 
```

### 26. Apa perbedaan interface dan abstract class?

Jawaban:

```text
Abstract class: 
1. hanya sebagai class dasar, jadi tidak bisa dibuat object
2. bisa berisi method yang ada isinya & abstract method
3. bisa punya atribute
4. pakai extends untuk inherit 
5. bisa punya constructor

Interface:
1. sebagai blueprint/template, jadi class lain yang implement ke interface, harus memakai semua method yang ada di interface
2. method yang di interface tanpa isi semua  
3. tidak punya constructor
4. menggunakan implements untuk inherit
```

## Self Assessment

Beri tanda sesuai pemahaman kamu saat ini.

| Topik | Belum paham | Mulai paham | Cukup paham |
| --- | --- | --- | --- |
| Basic syntax | [ ] | [ ] | [V] |
| Class and object | [ ] | [ ] | [V] |
| Field/method/constructor | [ ] | [ ] | [V] |
| Access modifier | [ ] | [ ] | [V] |
| Getter/setter | [ ] | [ ] | [V] |
| List/Map | [ ] | [ ] | [V] |
| Interface/abstract class | [ ] | [ ] | [V] |
