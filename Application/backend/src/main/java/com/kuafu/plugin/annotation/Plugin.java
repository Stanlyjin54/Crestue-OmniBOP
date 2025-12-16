package com.kuafu.plugin.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 插件注解，用于标识插件类
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Plugin {
    /**
     * 插件ID
     */
    String id();
    
    /**
     * 插件名称
     */
    String name();
    
    /**
     * 插件版本
     */
    String version() default "1.0.0";
    
    /**
     * 插件描述
     */
    String description() default "";
    
    /**
     * 插件作者
     */
    String author() default "";
    
    /**
     * 插件依赖
     */
    String[] dependencies() default {};
}
