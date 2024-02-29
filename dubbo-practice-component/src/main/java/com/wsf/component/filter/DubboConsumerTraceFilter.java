package com.wsf.component.filter;

import com.wsf.component.constant.TraceConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.RpcException;
import org.slf4j.MDC;

import java.util.UUID;

@Slf4j
@Activate(group = CommonConstants.CONSUMER)
public class DubboConsumerTraceFilter implements Filter, Filter.Listener {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {

        String traceId = MDC.get(TraceConstant.TRACE_ID);
        if (StringUtils.isEmpty(traceId)) {
            traceId = UUID.randomUUID().toString().replace("-", "");
        }
        MDC.put(TraceConstant.TRACE_ID, traceId);
        RpcContext.getContext().setAttachment(TraceConstant.TRACE_ID, traceId);

        String interfaceName = invoker.getInterface().getName();
        String methodName = invocation.getMethodName();
        log.info("interfaceName => {}, methodName => {}, serviceName => {}", interfaceName, methodName, invocation.getServiceName());
        String REQ_SERVICE = interfaceName + "." + methodName;
        String sourceIp = RpcContext.getContext().getLocalAddressString();

        Result result = null;
        try {
            result = invoker.invoke(invocation);
            if (result.hasException()) {
                log.error("Consumer服务业务异常: traceID=[{}] 调用方=[{}] 接口=[{}]",
                        traceId, sourceIp, REQ_SERVICE, result.getException());
            }

        } catch (Exception ex) {
            log.error("Consumer服务系统异常: traceID=[{}] 调用方=[{}] 接口=[{}]",
                    traceId, sourceIp, REQ_SERVICE, ex);
        } finally {
            MDC.clear();
        }
        return result;
    }

    @Override
    public void onResponse(Result appResponse, Invoker<?> invoker, Invocation invocation) {
        log.info("onResponse ....");
    }

    @Override
    public void onError(Throwable t, Invoker<?> invoker, Invocation invocation) {
        log.info("onError.....");
    }
}
