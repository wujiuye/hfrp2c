package com.wujiuye.hfrp2c.annotation.core;

import java.lang.annotation.*;

/**
 * @author wujiuye 2020/07/01
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface RpcPost {

    /**
     * API路径
     *
     * @return
     */
    String value() default "/";

}
