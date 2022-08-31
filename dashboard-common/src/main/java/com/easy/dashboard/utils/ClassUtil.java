package com.easy.dashboard.utils;

public class ClassUtil extends cn.hutool.core.util.ClassUtil {
    public static String getClassTypeByObject(Object o) {
        if (o == null) {
            return "Null";
        }
        else if (o instanceof String) {
            return "String";
        }
        else if (o instanceof Integer) {
            return "Integer";
        }
        else if (o instanceof Double) {
            return "Double";
        }
        else {
            return getClassName(o, true);
        }
    }
}
