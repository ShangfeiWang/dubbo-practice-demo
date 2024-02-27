package com.wsf.provider.api;

import com.wsf.practice.GreetingService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

@Service
@DubboService(version = "1.0.0")
public class GenericServiceImpl implements GreetingService {
    @Override
    public String sayHello(String name) {
        return "hello " + name;
    }
}
