package com.easy.dashboard.utils;

import cn.hutool.core.util.StrUtil;

import java.util.Random;

public class StringUtil extends StrUtil {
    /**
     * 生成随机6位数编码
     * @return strings
     */
    public static String creatMailCode() {
        StringBuilder sb = new StringBuilder();
        Random rand = new Random();
        for (int i = 0; i < 6; i++) {
            sb.append(rand.nextInt(10));
        }
        return sb.toString();
    }
}
