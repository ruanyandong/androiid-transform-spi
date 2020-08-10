package com.wings.spi.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author -> Wings
 * @date -> 2020/8/10
 * @email -> ruanyandongai@gmail.com
 * 729368173@qq.com
 * @phone -> 18983790146
 * @blog -> https://ruanyandong.github.io
 * -> https://blog.csdn.net/qq_34681580
 *
 * // 注意：如果是AnnotationsAttribute.visibleTag，则ServiceProvider注解需要加元注解信息
 * //      如果是AnnotationsAttribute.invisibleTag,则ServiceProvider注解不能加元注解信息
 */
@Target(value = ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface ServiceProvider {
    Class<?> service();
}
