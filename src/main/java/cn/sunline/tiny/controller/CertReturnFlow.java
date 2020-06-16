package cn.sunline.tiny.controller;

import cn.sunline.error.ErrorCenter;
import cn.sunline.tiny.AlipayApplication;
import cn.sunline.tiny.alipay.AliPayApi;
import cn.sunline.tiny.core.FlowCom;
import cn.sunline.tiny.core.JavaFlow;
import cn.sunline.tiny.core.PriCache;
import cn.sunline.tiny.core.PubCache;
import cn.sunline.tiny.core.annotation.Get;
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
 * @date : 2020/6/16 16:55
 */
@Component("cert_return_url_flow")
public class CertReturnFlow extends JavaFlow {
    private static final Logger log = LoggerFactory.getLogger(AliPayFlow.class);

    @Autowired
    private AliPayBean aliPayBean;

    @Autowired
    private ErrorCenter errorCenter;


    @Get
    @FlowCom(in = "true", name = "view", desc = "Check address")
    public Result flow_1(Context ct, PriCache pri, PubCache pub) {
        log.info("+++++++++++++++++cert return flow started+++++++++++++++++");
        JSONObject errors = AlipayApplication.errorCodeJSONS;
        try {
            HttpServletRequest request = ct.getRequest();
            // 获取支付宝GET过来反馈信息
            Map<String, String> map = AliPayApi.toMap(request);
            log.info("+++++++++++The returned parameters are：{}+++++++++++++++", map.toString());

            boolean verifyResult = AlipaySignature.rsaCertCheckV1(map, aliPayBean.getAliPayCertPath(), "UTF-8",
                    "RSA2");

            if (verifyResult) {
                // TODO 请在这里加上商户的业务逻辑程序代码
                //获取trade_no，out_trade_no两选一订单号存下来方便后期查询订单
                log.info("+++++++++++++++Cert_Return_Url verification success+++++++++++++++");
                return errorCenter.getResultByEasyCodeAndLanguage(errors, "SUCCESS", pri);
            } else {
                log.info("+++++++++++++++Cert_Return_Url verification failed+++++++++++++++");
                // TODO
                return errorCenter.getResultByEasyCodeAndLanguage(errors, "ERROR", pri);
            }
        } catch (AlipayApiException e) {
            log.error(e.getMessage(), e);
            return errorCenter.getResultByEasyCodeAndLanguage(errors, "ERROR", pri);
        }
    }

}
