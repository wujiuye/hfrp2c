package com.wujiuye.hfrp2c.annotation.core;

import java.lang.annotation.*;

/**
 * @author wujiuye 2020/07/01
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@Documented
public @interface ReqParam {

    /**
     * 参数名
     *
     * @return
     */
    String value();

}
