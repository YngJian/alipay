package cn.sunline.tiny.config;

import cn.sunline.tiny.alipay.AliPayApiConfig;
import cn.sunline.tiny.alipay.AliPayApiConfigKit;
import cn.sunline.tiny.entity.AliPayBean;
import com.alipay.api.AlipayApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : Yang Jian
 * @date : 2020/6/15 20:22
 */
@Configuration
public class AliPayConfig {

    @Autowired
    private AliPayBean aliPayBean;

    @Bean
    public AliPayApiConfig getApiConfig() throws AlipayApiException {
        AliPayApiConfig aliPayApiConfig;
        try {
            aliPayApiConfig = AliPayApiConfigKit.getApiConfig(aliPayBean.getAppId());
        } catch (Exception e) {
            aliPayApiConfig = AliPayApiConfig.builder()
                    .setAppId(aliPayBean.getAppId())
                    .setAliPayPublicKey(aliPayBean.getPublicKey())
                    // .setAppCertPath(aliPayBean.getAppCertPath())
                    // .setAliPayCertPath(aliPayBean.getAliPayCertPath())
                    // .setAliPayRootCertPath(aliPayBean.getAliPayRootCertPath())
                    .setCharset("UTF-8")
                    .setPrivateKey(aliPayBean.getPrivateKey())
                    .setServiceUrl(aliPayBean.getServerUrl())
                    .setSignType("RSA2")
                    // 普通公钥方式
                    .build();
            // 证书模式
            // .buildByCert();
        }
        return aliPayApiConfig;
    }
}
