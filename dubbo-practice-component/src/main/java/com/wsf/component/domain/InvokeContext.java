package com.wsf.component.domain;

import com.alibaba.fastjson.JSON;
import com.alibaba.ttl.TransmittableThreadLocal;
import com.wsf.component.utils.InetIpUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class InvokeContext {

    /**
     * 请求ID
     */
    private static final String REQUEST_ID = "requestId";

    /**
     * 调用方信息
     */
    private static final String INVOKE_AGENT = "invokeAgent";


    private static final ThreadLocal<InvokeContext> LOCAL = new TransmittableThreadLocal<>();

    private final Map<String, String> attachments = new HashMap<>();

    private static InvokeContext getContext() {
        InvokeContext context = LOCAL.get();

        if (context == null) {
            context = creatEmptyInvokeContext();
            LOCAL.set(context);
        }
        return context;
    }

    private static InvokeContext creatEmptyInvokeContext() {
        InvokeAgent agent = new InvokeAgent();
        agent.setAgentIp(InetIpUtil.getLocalAddress());
        agent.setClientIp(agent.getAgentIp());

        InvokeContext context = new InvokeContext();
        context.setAttachment(REQUEST_ID, UUID.randomUUID().toString().replace("-", ""));
        context.setAttachment(INVOKE_AGENT, JSON.toJSONString(agent));

        return context;
    }

    /**
     * 清理线程变量，建议线程执行完毕手动调用清理
     * <p>
     * JDK8后随着线程消亡，其内部ThreadLocalMap也随之消亡，clear可以不调用；但是涉及到线程池（线程复用），为防止数据混乱，最好调用下
     * </p>
     */
    public static void clear() {
        LOCAL.remove();
    }

    public static void setRequestId(String requestId) {
        put(REQUEST_ID, requestId);
    }

    public static String getRequestId() {
        return get(REQUEST_ID);
    }

    public static void setInvokeAgent(InvokeAgent invokeAgent) {
        put(INVOKE_AGENT, JSON.toJSONString(invokeAgent));
    }

    public static InvokeAgent getInvokeAgent() {
        return JSON.parseObject(get(INVOKE_AGENT), InvokeAgent.class);
    }

    public static void put(String key, String value) {
        getContext().setAttachment(key, value);
    }

    public static String get(String key) {
        return getContext().getAttachment(key);
    }

    public static Set<String> keys() {
        return getContext().attachments.keySet();
    }

    private void setAttachment(String key, String value) {
        if (value == null) {
            this.attachments.remove(key);
        } else {
            this.attachments.put(key, value);
        }
    }

    private String getAttachment(String key) {
        return this.attachments.get(key);
    }

}