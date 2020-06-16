package cn.sunline.tiny.controller;

import cn.sunline.error.ErrorCenter;
import cn.sunline.tiny.AlipayApplication;
import cn.sunline.tiny.alipay.AliPayApi;
import cn.sunline.tiny.core.FlowCom;
import cn.sunline.tiny.core.JavaFlow;
import cn.sunline.tiny.core.PriCache;
import cn.sunline.tiny.core.PubCache;
import cn.sunline.tiny.core.annotation.Get;
import cn.sunline.tiny.core.annotation.Post;
import cn.sunline.tiny.entity.AliPayBean;
import cn.sunline.tiny.utils.StringUtils;
import cn.sunline.tiny.utils.Tools;
import cn.sunline.tiny.web.Context;
import cn.sunline.util.Result;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;

/**
 * @author : Yang Jian
 * @date : 2020/6/16 16:57
 */
@Component("ali_pay_flow")
public class AliPayFlow extends JavaFlow {
    private static final Logger log = LoggerFactory.getLogger(AliPayFlow.class);

    @Autowired
    private AliPayBean aliPayBean;

    @Autowired
    private ErrorCenter errorCenter;

    // 证书模式
    // private final static String NOTIFY_URL = "/ali_pay?flow=cert_notify_url";
    // private final static String RETURN_URL = "/ali_pay?flow=cert_return_url";

    /**
     * 普通公钥模式
     */
    private final static String NOTIFY_URL = "/notify_url.tml";

    /**
     * 普通公钥模式
     */
    private final static String RETURN_URL = "/return_url.tml";

    @Post
    @FlowCom(in = "true", name = "view", desc = "app payment")
    public Result flow_wap_pay(Context ct, PriCache pri, PubCache pub) {
        log.info("+++++++++++++++++alipay flow: wap pay started+++++++++++++++++");
        JSONObject jsonObject = (JSONObject) pri.getParamObj("jsonsObj");
        JSONObject errors = AlipayApplication.errorCodeJSONS;
        if (jsonObject == null) {
            return errorCenter.getResultByEasyCodeAndLanguage(errors, "jsonObjectNull", pri);
        }
        String body = aliPayBean.getBody();
        String subject = aliPayBean.getSubject();
        String totalAmount = jsonObject.getString("totalAmount");
        if (StringUtils.isBlank(totalAmount)) {
            return errorCenter.getResultByEasyCodeAndLanguage(errors, "missParam", pri);
        }
        boolean number = Tools.isNumber(totalAmount);
        if (!number) {
            return errorCenter.getResultByEasyCodeAndLanguage(errors, "wrongAmount", pri);
        }
        if (Integer.parseInt(totalAmount) > 100000000) {
            return errorCenter.getResultByEasyCodeAndLanguage(errors, "wrongAmount", pri);
        }
        String returnUrl = aliPayBean.getDomain() + RETURN_URL;
        String notifyUrl = aliPayBean.getDomain() + NOTIFY_URL;

        // String body = aliPayBean.getBody();
        // String subject = aliPayBean.getSubject();
        // String totalAmount ="0.01";

        AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
        model.setBody(body);
        model.setSubject(subject);
        model.setTotalAmount(totalAmount);
        model.setTimeoutExpress(aliPayBean.getTimeoutExpress());
        model.setPassbackParams(aliPayBean.getPassBackParams());
        String outTradeNo = StringUtils.getOutTradeNo();
        log.info("++++++++++wap outTradeNo+++++++++ {}", outTradeNo);
        model.setOutTradeNo(outTradeNo);
        model.setProductCode(aliPayBean.getProductCode());
        HttpServletResponse response = ct.getResponse();
        try {
            AliPayApi.wapPay(response, model, returnUrl, notifyUrl);
            return errorCenter.getResultByEasyCodeAndLanguage(errors, "SUCCESS", pri);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return errorCenter.getResultByEasyCodeAndLanguage(errors, "ERROR", pri);
    }

}