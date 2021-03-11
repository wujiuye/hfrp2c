package com.wujiuye.hfrp2c.demo;

import com.wujiuye.hfrp2c.demo.outbound.TestRpcService;
import org.junit.Test;

import javax.annotation.Resource;

public class DemoApplicationTest extends SpringBootTestSuppor {

    @Resource
    private TestRpcService testRpcService;

    @Test
    public void test() {
        String result = testRpcService.sayHello("hfrp2c");
        System.out.println(result);
    }

}
