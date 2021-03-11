package com.wujiuye.playback;

import com.wujiuye.hfrp2c.MethodHandler;
import com.wujiuye.hfrp2c.filter.AbstractLinkRpcFilter;
import com.wujiuye.hfrp2c.filter.DefaultRpcFilterChainBuilder;
import com.wujiuye.hfrp2c.filter.RpcFilterChain;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * RPC适配
 *
 * @author wujiuye 2020/07/21
 */
public class PlaybackRpcFilterChainBuilder extends DefaultRpcFilterChainBuilder {

    @Override
    public RpcFilterChain build() {
        RpcFilterChain chain = super.build();
        chain.addLast(new AbstractLinkRpcFilter() {
            @Override
            public Object doFilter(MethodHandler handler, Method method, Object[] args) throws IOException {
                try {
                    Object result = fireFilter(handler, method, args);
                    try {
                        MethodInvokeFailHandler.handle(method, args, result, null);
                    } catch (Exception e) {
                        // don't do anything
                    }
                    return result;
                } catch (Exception e) {
                    try {
                        MethodInvokeFailHandler.handle(method, args, null, e);
                    } catch (Exception e1) {
                        // don't do anything
                    }
                    throw e;
                }
            }
        });
        return chain;
    }

}
