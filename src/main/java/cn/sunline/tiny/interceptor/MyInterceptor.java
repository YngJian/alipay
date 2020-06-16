package cn.sunline.tiny.interceptor;


import cn.sunline.tiny.alipay.AliPayApiConfig;
import cn.sunline.tiny.alipay.AliPayApiConfigKit;
import com.alipay.api.AlipayApiException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component("myInterceptor")
public class MyInterceptor {
    private static final Logger LOG = LoggerFactory.getLogger(MyInterceptor.class);

    @Autowired
    private AliPayApiConfig aliPayApiConfig;

    @Pointcut("execution(* cn.sunline.tiny.core.control.MainCtl.doControl(..))")
    public void doControlTmlPointCut() {
    }

    @Before("doControlTmlPointCut()")
    public void Interceptor(JoinPoint point) throws AlipayApiException {
        // Object controller = point.getTarget();
        AliPayApiConfigKit.setThreadLocalAliPayApiConfig(aliPayApiConfig);
    }
}
