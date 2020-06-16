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
import com.alipay.api.domain.AlipayOpenAuthTokenAppModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author : Yang Jian
 * @date : 2020/6/16 16:59
 */
@Component("redirect_uri_flow")
public class RedirectFlow extends JavaFlow {
    private static final Logger log = LoggerFactory.getLogger(AliPayFlow.class);

    @Autowired
    private AliPayBean aliPayBean;

    @Autowired
    private ErrorCenter errorCenter;

    /**
     * 应用授权回调
     */
    @Post
    @FlowCom(in = "true", name = "view", desc = "redirect uri")
    public Result flow_1(Context ct, PriCache pri, PubCache pub) {
        log.info("+++++++++++++++++redirect_uri_flow started+++++++++++++++++");
        JSONObject errors = AlipayApplication.errorCodeJSONS;
        try {
            JSONObject jsonObject = (JSONObject) pri.getParamObj("jsonsObj");
            if (jsonObject == null) {
                return errorCenter.getResultByEasyCodeAndLanguage(errors, "jsonObjectNull", pri);
            }
            String appId = jsonObject.getString("app_id");
            String appAuthCode = jsonObject.getString("app_auth_code");
            log.info("app_id:{}", appId);
            log.info("app_auth_code:{}", appAuthCode);
            //使用app_auth_code换取app_auth_token
            AlipayOpenAuthTokenAppModel model = new AlipayOpenAuthTokenAppModel();
            model.setGrantType("authorization_code");
            model.setCode(appAuthCode);
            return errorCenter.getResult(errors, "SUCCESS", pri, AliPayApi.openAuthTokenAppToResponse(model).getBody());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return errorCenter.getResultByEasyCodeAndLanguage(errors, "ERROR", pri);
    }

}
