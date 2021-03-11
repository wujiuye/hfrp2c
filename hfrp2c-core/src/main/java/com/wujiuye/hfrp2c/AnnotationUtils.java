package com.wujiuye.hfrp2c;

import com.wujiuye.hfrp2c.annotation.core.RpcGet;
import com.wujiuye.hfrp2c.annotation.core.RpcPost;
import com.wujiuye.hfrp2c.annotation.RetryStrategy;
import com.wujiuye.hfrp2c.annotation.RpcClient;
import com.wujiuye.hfrp2c.okhttp.HttpSupperRetryAction;
import com.wujiuye.hotkit.util.CollectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 * 注解解析工具类
 *
 * @author wujiuye 2020/07/01
 */
public class AnnotationUtils {

    public interface IElExpression {
        String valueBy(String key);
    }

    private static IElExpression elExpression;

    public static void registIElExpression(IElExpression elExpression) {
        AnnotationUtils.elExpression = elExpression;
    }

    /**
     * 根据EL表达式调用IElExpression接口获取
     *
     * @param elExpression
     * @return
     */
    public static String getValueByElExpression(String elExpression) {
        if (elExpression == null) {
            return elExpression;
        }
        if (!elExpression.startsWith("${") || !elExpression.endsWith("}")) {
            return elExpression;
        }
        String realEl = elExpression.substring(2, elExpression.length() - 1);
        String[] elAndDef = realEl.split(":");
        String defValue = null;
        if (elAndDef.length == 2) {
            defValue = elAndDef[1];
        }
        String value = AnnotationUtils.elExpression.valueBy(elAndDef[0]);
        return value == null ? defValue : value;
    }

    /**
     * 解析方法生成RpcMetadata
     *
     * @param method 反射获取的方法
     * @return
     */
    public static RpcMetadata analysisMappingAnnotation(RpcClient rpcClient, Method method) {
        RpcGet rpcGet = method.getAnnotation(RpcGet.class);
        RpcPost rpcPost = method.getAnnotation(RpcPost.class);
        if (rpcGet == null && rpcPost == null) {
            return null;
        }
        RpcMetadata.Builder builder = RpcMetadata.newBuilder();
        builder.url(AnnotationUtils.getValueByElExpression(rpcClient.url()));
        if (rpcGet != null) {
            builder.method(HttpMethod.GET);
            builder.path(getValueByElExpression(rpcGet.value()));
        }
        if (rpcPost != null) {
            builder.method(HttpMethod.POST);
            builder.path(getValueByElExpression(rpcPost.value()));
        }
        builder.retry(analysisRetryMetadata(rpcClient, method));
        builder.params(analysisParamMetadata(method));
        return builder.build();
    }

    private static List<ParamMetadata> analysisParamMetadata(Method method) {
        List<ParamMetadata> params = new ArrayList<>();
        Parameter[] parameters = method.getParameters();
        if (parameters != null && parameters.length > 0) {
            for (int index = 0; index < parameters.length; index++) {
                Annotation[] annotations = parameters[index].getAnnotations();
                if (annotations == null || annotations.length == 0) {
                    continue;
                }
                ParamMetadata paramMetadata = new ParamMetadata();
                paramMetadata.setParamAnnotations(annotations);
                paramMetadata.setParamIndexOnMethod(index);
                paramMetadata.setParamType(parameters[index].getType());
                paramMetadata.setPrimitive(parameters[index].getType().isPrimitive());
                params.add(paramMetadata);
            }
        }
        return params;
    }

    /**
     * 解析重试策略
     *
     * @param rpcClient
     * @param method
     * @return
     */
    private static RetryMetadata analysisRetryMetadata(RpcClient rpcClient, Method method) {
        RetryMetadata retryMetadata = new RetryMetadata();
        retryMetadata.setEnableRetry(rpcClient.enableRetry());
        retryMetadata.setRetryMaxNumber(rpcClient.retryMax());
        RetryStrategy retryStrategy = method.getAnnotation(RetryStrategy.class);
        retryMetadata.setRetryStrategys(retryStrategy != null ? toRetryRule(retryStrategy.retryStrategy()) : null);
        return retryMetadata;
    }

    private static HttpSupperRetryAction.RetryRule[] toRetryRule(RetryStrategyEnum[] retryStrategyEnums) {
        if (!CollectionUtils.isEmpty(retryStrategyEnums)) {
            HttpSupperRetryAction.RetryRule[] rules = new HttpSupperRetryAction.RetryRule[retryStrategyEnums.length];
            int index = 0;
            for (RetryStrategyEnum retryStrategyEnum : retryStrategyEnums) {
                rules[index++] = retryStrategyEnum.getRetryRule();
            }
            return rules;
        }
        return null;
    }

}
