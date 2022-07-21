package com.easy.dashboard.utils;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.util.List;

/**
 * @author YaoYanChen
 * @date 2022-02-22
 */
public class JSON {
    private final static ObjectMapper objectMapper = new ObjectMapper();

    private static JavaType getCollectionType(Class<?> collectionClass,
                                              Class<?>... elementClasses) {
        return JSON.objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

    /**
     * 解析json对象
     * @param json json字符串
     * @param clazz 对象类型
     * @param <T> 泛型
     * @return T clazz
     */
    public static <T> T parseObject(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 解析json集合
     * @param json json集合
     * @param clazz json泛型的类型
     * @param <T> 泛型
     * @return T clazz
     */
    public static <T> List<T> parseArray(String json, Class<T> clazz) {
        try {
            JavaType collectionType = getCollectionType(List.class, clazz);
            return objectMapper.readValue(json, collectionType);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    public static <T> List<T> parseArray(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<T>() {});
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    public static String toJSONString(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    public static JsonNode parse(String str) {
        try {
            return objectMapper.readTree(str);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

}
