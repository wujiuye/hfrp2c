package com.wujiuye.hfrp2c.annotation;

import java.lang.annotation.*;

/**
 * @author wujiuye 2020/07/01
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface HttpGet {

    /**
     * API路径
     *
     * @return
     */
    String value() default "/";

}
