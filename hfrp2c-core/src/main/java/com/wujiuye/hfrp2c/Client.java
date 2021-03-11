package com.wujiuye.hfrp2c;

import com.wujiuye.hfrp2c.okhttp.HttpRequest;
import com.wujiuye.hfrp2c.okhttp.HttpResponse;

import java.io.IOException;

/**
 * @author wujiuye 2020/07/01
 */
public interface Client {

    /**
     * 发起RPC请求
     *
     * @param request
     * @return
     */
    HttpResponse execute(HttpRequest request, RetryMetadata retryMetadata) throws IOException;

}
