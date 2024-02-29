package com.wsf.component.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 调用端信息
 *
 * @author wangsf
 */
@Data
public class InvokeAgent implements Serializable {

    private static final long serialVersionUID = -8194165040305119849L;

    /**
     * 调用方IP，跟随调用链不断变化
     */
    private String agentIp;

    /**
     * 客户端的IP，发起方的IP，不会跟随调用链变化
     */
    private String clientIp;

    /**
     * 应用ID
     */
    private String appId;

    /**
     * 客户端类型;版本
     */
    private String clientAgent;

    /**
     * 终端设备ID
     */
    private String deviceId;

}
