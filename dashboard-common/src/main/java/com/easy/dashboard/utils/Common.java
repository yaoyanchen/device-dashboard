package com.easy.dashboard.utils;

public class Common {
    public static String getCodeEmailKey(String email)
    {
        return "code::email" + email;
    }

    public static Long getSecondByMinute(Integer minute)
    {
        return minute.longValue() * 60;
    }

}
