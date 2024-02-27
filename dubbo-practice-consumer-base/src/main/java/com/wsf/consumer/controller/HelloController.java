package com.wsf.consumer.controller;

import com.wsf.practice.GreetingService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @DubboReference(version = "1.0.0", check = false)
    private GreetingService greetingService;

    @RequestMapping("/hello")
    public String hello(String name) {
        return greetingService.sayHello(name);
    }
}
