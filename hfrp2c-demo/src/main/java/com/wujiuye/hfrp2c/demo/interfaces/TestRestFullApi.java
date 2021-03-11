package com.wujiuye.hfrp2c.demo.interfaces;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wujiuye 2021/03/11
 */
@RestController
public class TestRestFullApi {

    @RequestMapping("/sayHello")
    public String testApi(@RequestParam("name") String name) {
        return "sayHello " + name;
    }

}
