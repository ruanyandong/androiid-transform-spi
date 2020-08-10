package com.wings.spi.repository;

import java.util.Map;

/**
 * @author -> Wings
 * @date -> 2020/8/9
 * @email -> ruanyandongai@gmail.com
 * 729368173@qq.com
 * @phone -> 18983790146
 * @blog -> https://ruanyandong.github.io
 * -> https://blog.csdn.net/qq_34681580
 */
public class ServiceRegistry {

    private static Map<Class<?>, Object> services;

    //{
    //  services.put(, );
    //}

    private ServiceRegistry() {

    }

    @SuppressWarnings("unchecked")
    public static <T> T get(Class<T> key) {
        return (T) services.get(key);
    }
}
