package com.wujiuye.hfrp2c.annotation;

import com.wujiuye.hfrp2c.RetryStrategyEnum;

import java.lang.annotation.*;

/**
 * 重试策略
 *
 * @author wujiuye 2020/07/01
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface RetryStrategy {

    /**
     * 使用的重试策略
     *
     * @return
     */
    RetryStrategyEnum[] retryStrategy() default {
            RetryStrategyEnum.ON_HTTP_STATUS_NOT_OK,
            RetryStrategyEnum.ON_ANY_EXCEPTION
    };

}
