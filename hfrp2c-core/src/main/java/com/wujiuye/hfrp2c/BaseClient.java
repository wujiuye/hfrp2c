package com.wujiuye.hfrp2c;

import com.wujiuye.hfrp2c.okhttp.HttpRequest;
import com.wujiuye.hfrp2c.okhttp.HttpResponse;
import com.wujiuye.hfrp2c.okhttp.HttpSupperRetryUtils;

import java.util.function.Function;

/**
 * 基类Client，封装execute通用逻辑
 *
 * @author wujiuye 2020/07/01
 */
public abstract class BaseClient implements Client {

    @Override
    public HttpResponse execute(HttpRequest request, RetryMetadata retryMetadata) {
        if (retryMetadata.isEnableRetry()) {
            return HttpSupperRetryUtils.retryByRetryRule(() -> toSyncTask().apply(request),
                    retryMetadata.getRetryMaxNumber(),
                    retryMetadata.getRetryStrategys());
        } else {
            return toSyncTask().apply(request);
        }
    }

    protected abstract Function<HttpRequest, HttpResponse> toSyncTask();

}
