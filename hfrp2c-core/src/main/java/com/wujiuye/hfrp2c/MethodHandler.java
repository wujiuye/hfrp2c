package com.wujiuye.hfrp2c;

import com.wujiuye.hfrp2c.annotation.core.RespBody;
import com.wujiuye.hotkit.json.JsonUtils;
import com.wujiuye.hfrp2c.annotation.ShowLog;
import com.wujiuye.hfrp2c.okhttp.HttpRequest;
import com.wujiuye.hfrp2c.okhttp.HttpResponse;
import com.wujiuye.hfrp2c.processor.PostProcessorRegister;
import com.wujiuye.hfrp2c.annotation.RpcClient;
import com.wujiuye.hotkit.util.ReflectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * 方法拦截处理器
 *
 * @author wujiuye 2020/07/01
 */
public class MethodHandler {

    private final static Logger logger = LoggerFactory.getLogger("rpc");

    private Class<?> returnType;
    private Annotation[] methodAnnotations;
    private RpcMetadata rpcMetadata;
    private Client client;
    private boolean showRequestLog;
    private boolean showLogResponse;

    public MethodHandler(RpcClient rpcClient, Client client, Method method) {
        this.client = client;
        // 解析请求映射
        rpcMetadata = AnnotationUtils.analysisMappingAnnotation(rpcClient, method);
        if (rpcMetadata == null) {
            throw new NullPointerException("METHOD NOT FOUND MAPPING ANNOTATION!");
        }
        // 获取方法返回值类
        returnType = method.getReturnType();
        methodAnnotations = method.getAnnotations();
        ShowLog showLog = method.getAnnotation(ShowLog.class);
        if (showLog != null) {
            showRequestLog = showLog.request();
            showLogResponse = showLog.response();
        }
    }

    public Object invoke(Object[] args) throws IOException {
        String requestId = UUID.randomUUID().toString();
        HttpRequest request = new HttpRequest();
        request.setUrl(rpcMetadata.getUrl());
        request = PostProcessorRegister.REGISTER.postProcessor(request, rpcMetadata, args);
        if (showRequestLog) {
            logger.info("rpc request {} ==> url:{}, method:{}, request body:{}", requestId,
                    rpcMetadata.getUrl(), rpcMetadata.getMethod(),
                    request.getBody() == null ? null : JsonUtils.toJsonString(request.getBody()));
        }
        HttpResponse response = client.execute(request, rpcMetadata.getRetryMetadata());
        if (showLogResponse) {
            logger.info("rpc response {} ==> url:{}, method:{}, response body:{}", requestId,
                    rpcMetadata.getUrl(), rpcMetadata.getMethod(),
                    response == null ? null : JsonUtils.toJsonString(response));
        }
        if (response != null && response.getCode() == 200 && response.getBody() != null) {
            // 判断Class是否为原型（boolean、char、byte、short、int、long、float、double）
            if (ReflectUtils.isPrimitiveType(returnType)) {
                return ReflectUtils.getValueOfType(response.getBody(), returnType);
            }
            // 如果方法返回值类型不是基本数据类型，那么必须添加@RespBody注解才能解析
            if (!existAnnotation(RespBody.class, methodAnnotations)) {
                throw new RuntimeException("method return type is not primitive and method not found @RespBody annotation.");
            }
            return JsonUtils.fromJson(response.getBody(), returnType);
        }
        throw new IOException("请求失败！");
    }

    private static boolean existAnnotation(Class<?> annoClass, Annotation[] annotations) {
        if (annotations == null) {
            return false;
        }
        for (Annotation annotation : annotations) {
            if (annotation.getClass() == annoClass) {
                return true;
            }
        }
        return false;
    }

}
