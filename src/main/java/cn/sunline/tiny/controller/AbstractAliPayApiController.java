package cn.sunline.tiny.controller;

import cn.sunline.tiny.alipay.AliPayApiConfig;
import com.alipay.api.AlipayApiException;

/**
 * @author Javen
 */
public abstract class AbstractAliPayApiController {
    /**
     * 获取支付宝配置
     *
     * @return {@link AliPayApiConfig} 支付宝配置
     * @throws AlipayApiException 支付宝 Api 异常
     */
    public abstract AliPayApiConfig getApiConfig() throws AlipayApiException;
}
