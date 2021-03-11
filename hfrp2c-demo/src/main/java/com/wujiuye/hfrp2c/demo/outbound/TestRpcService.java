package com.wujiuye.hfrp2c.demo.outbound;

import com.wujiuye.hfrp2c.annotation.RpcClient;
import com.wujiuye.hfrp2c.annotation.ShowLog;
import com.wujiuye.hfrp2c.annotation.core.ReqParam;
import com.wujiuye.hfrp2c.annotation.core.RpcGet;

/**
 * @author wujiuye 2021/03/11
 */
@RpcClient(url = "http://127.0.0.1:8080")
public interface TestRpcService {

    @RpcGet("/sayHello")
    @ShowLog
    String sayHello(@ReqParam("name") String name);

}
