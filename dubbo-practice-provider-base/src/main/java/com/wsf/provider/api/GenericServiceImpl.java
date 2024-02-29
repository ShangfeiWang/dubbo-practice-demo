package com.wsf.provider.api;

import com.wsf.practice.GreetingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@DubboService(version = "1.0.0")
public class GenericServiceImpl implements GreetingService {
    @Override
    public String sayHello(String name) {

        Object traceId = RpcContext.getContext().getAttachment("traceId");
        log.info("traceId:{}", traceId);
        return "hello " + name;
    }
}
