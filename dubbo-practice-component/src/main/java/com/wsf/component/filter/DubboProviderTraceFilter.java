package com.wsf.component.filter;

import com.wsf.component.constant.TraceConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.rpc.*;
import org.slf4j.MDC;

import java.util.UUID;

/**
 * @author wangshangfei
 */
@Slf4j
@Activate(group = CommonConstants.PROVIDER)
public class DubboProviderTraceFilter implements Filter, Filter.Listener {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String traceId = RpcContext.getContext().getAttachment(TraceConstant.TRACE_ID);
        if (StringUtils.isBlank(traceId)) {
            traceId = UUID.randomUUID().toString().replace("-", "");
        }
        MDC.put(TraceConstant.TRACE_ID, traceId);
        String interfaceName = invoker.getInterface().getName();
        String methodName = invocation.getMethodName();
        String requestService = interfaceName + "." + methodName;
        String sourceIp = RpcContext.getContext().getRemoteAddressString();

        Result result;
        try {
            result = invoker.invoke(invocation);
            if (result.hasException()) {
                log.error("Provider服务业务异常: traceID=[{}] 调用方=[{}] 接口=[{}]",
                        traceId, sourceIp, requestService, result.getException());
            }
        } catch (Exception ex) {
            log.error("Provider服务系统异常: traceID=[{}] 调用方=[{}] 接口=[{}]",
                    traceId, sourceIp, requestService, ex);
            throw ex;
        } finally {
            MDC.clear();
        }
        return result;
    }

    @Override
    public void onResponse(Result appResponse, Invoker<?> invoker, Invocation invocation) {
        log.info("onResponse...");
    }

    @Override
    public void onError(Throwable t, Invoker<?> invoker, Invocation invocation) {
        log.info("onError...");

    }


}
