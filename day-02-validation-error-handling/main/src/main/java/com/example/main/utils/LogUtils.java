package com.example.main.utils;

public class LogUtils {
    public static String maskNik(String nik) {
        if (nik == null || nik.length() < 6) return "******";
        return nik.substring(0, 6) + "******" + nik.substring(nik.length() - 4);
    }

    public static String maskPhone(String phone) {
        if (phone == null || phone.length() < 4) return "******";
        return phone.substring(0, 4) + "******" + phone.substring(phone.length() - 2);
    }
}