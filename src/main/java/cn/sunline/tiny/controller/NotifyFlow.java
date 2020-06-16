package cn.sunline.tiny.controller;

import cn.sunline.error.ErrorCenter;
import cn.sunline.tiny.AlipayApplication;
import cn.sunline.tiny.alipay.AliPayApi;
import cn.sunline.tiny.core.FlowCom;
import cn.sunline.tiny.core.JavaFlow;
import cn.sunline.tiny.core.PriCache;
import cn.sunline.tiny.core.PubCache;
import cn.sunline.tiny.core.annotation.Post;
import cn.sunline.tiny.entity.AliPayBean;
import cn.sunline.tiny.web.Context;
import cn.sunline.util.Result;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author : Yang Jian
 * @date : 2020/6/16 16:57
 */
@Component("notify_url_flow")
public class NotifyFlow extends JavaFlow {
    private static final Logger log = LoggerFactory.getLogger(AliPayFlow.class);

    @Autowired
    private AliPayBean aliPayBean;

    @Autowired
    private ErrorCenter errorCenter;

    @Post
    @FlowCom(in = "true", name = "view", desc = "notify url")
    public Result flow_1(Context ct, PriCache pri, PubCache pub) {
        log.info("+++++++++++++++++notify flow started+++++++++++++++++");
        JSONObject errors = AlipayApplication.errorCodeJSONS;
        try {
            HttpServletRequest request = ct.getRequest();
            // 获取支付宝POST过来反馈信息
            Map<String, String> params = AliPayApi.toMap(request);
            log.info("+++++++++++The returned parameters are：{} +++++++++++++++", params.toString());

            boolean verifyResult = AlipaySignature.rsaCheckV1(params, aliPayBean.getPublicKey(), "UTF-8", "RSA2");

            if (verifyResult) {
                // TODO 请在这里加上商户的业务逻辑程序代码 异步通知可能出现订单重复通知 需要做去重处理
                log.info("+++++++++++++++Notify_Url verification success+++++++++++++++");
                return errorCenter.getResultByEasyCodeAndLanguage(errors, "SUCCESS", pri);
            } else {
                // TODO
                log.info("+++++++++++++++Notify_Url verification failed+++++++++++++++");
                return errorCenter.getResultByEasyCodeAndLanguage(errors, "ERROR", pri);
            }
        } catch (AlipayApiException e) {
            log.error(e.getMessage(), e);
            return errorCenter.getResultByEasyCodeAndLanguage(errors, "ERROR", pri);
        }
    }

}
