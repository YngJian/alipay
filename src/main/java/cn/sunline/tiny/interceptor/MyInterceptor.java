package cn.sunline.tiny.interceptor;


import cn.sunline.tiny.alipay.AliPayApiConfigKit;
import cn.sunline.tiny.controller.AbstractAliPayApiController;
import cn.sunline.tiny.web.Context;
import com.alipay.api.AlipayApiException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component("myInterceptor")
public class MyInterceptor {
    private static final Logger LOG = LoggerFactory.getLogger(MyInterceptor.class);

    @Pointcut("execution(* cn.sunline.tiny.core.control.MainCtl.doControl(..))")
    public void doControlTmlPointCut() {
    }

    @Before("doControlTmlPointCut()")
    public void Interceptor(JoinPoint point) throws AlipayApiException {
        Object controller = point.getTarget();
        AliPayApiConfigKit.setThreadLocalAliPayApiConfig(((AbstractAliPayApiController) controller).getApiConfig());
    }
}
