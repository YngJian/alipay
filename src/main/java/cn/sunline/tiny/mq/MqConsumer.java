package cn.sunline.tiny.mq;

import cn.sunline.tiny.AlipayApplication;
import cn.sunline.tiny.utils.CrashLogUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @author : Yang Jian
 * @date : 2019/11/1 11:35
 */
@Service
public class MqConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(MqConsumer.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private SendMqMsgUtil sendMqMsgUtil;

    @Value("${spring.application.name}")
    private String applicationName;

    @Autowired
    private CrashLogUtil crashLogUtil;

    @StreamListener(Sink.INPUT)
    public void getTinyParametersRedis(String messageBody,String tag) {
        if ("updateErrorCodeRedis".equalsIgnoreCase(messageBody)) {
            if (stringRedisTemplate != null) {
                String str_errorCodeJSONS = stringRedisTemplate.opsForValue().get("errorListJSONS");
                if (str_errorCodeJSONS != null && !"".equals(str_errorCodeJSONS)) {
                    AlipayApplication.errorCodeJSONS = JSON.parseObject(str_errorCodeJSONS);
                } else {
                    AlipayApplication.errorCodeJSONS = new JSONObject();
                }
                LOGGER.info("errorCodeJSONS:{}", "success");
            }
        }
        else if (messageBody.startsWith("createCrashlog")) {
            String[] strArr = messageBody.split(":");
            String crashlog = null;
            if (strArr.length >= 2) {
                if (applicationName.equals(strArr[1])) {
                    crashlog = crashLogUtil.getCrashlog();
                }
            } else {
                crashlog = crashLogUtil.getCrashlog();
            }
            if (StringUtils.isNotBlank(crashlog)) {
                String msg = "createCrashlog:" + applicationName + ":" + crashlog;

                sendMqMsgUtil.sendMessage(msg, "crashlog");
            }
        }
        // 输出消息内容
        LOGGER.info("消费响应：msgBody : {}", messageBody);
    }

}
