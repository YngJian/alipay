package cn.sunline.tiny.mq;

import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.support.MessageBuilder;

/**
 * @author : Yang Jian
 * @date : 2019/11/1 14:01
 */
@Configuration
public class SendMqMsgUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(SendMqMsgUtil.class);
    @Autowired
    private Source source;

    /**
     * 发送mq
     *
     * @param msg   :
     * @param tags:
     * @return b:
     * @author : Yang Jian
     * @date : 2020/4/1 0001 14:05
     */
    public boolean sendMessage(String msg, String tags) {
        boolean send = source.output().send(MessageBuilder.withPayload(msg)
                .setHeader(RocketMQHeaders.TAGS, tags).build());
        if (send) {
            LOGGER.info("send message success!!! tags:{}, message body:{}", tags, msg);
        }
        return send;
    }

    /**
     * 发送延时mq
     *
     * @param msg   :
     * @param tags:
     * @return b:
     * @author : Yang Jian
     * @date : 2020/4/1 0001 14:05
     */
    public boolean sendDelayMessage(String msg, String tags, String time) {
        boolean send = source.output().send(
                MessageBuilder.withPayload(msg)
                        .setHeader(RocketMQHeaders.TAGS, tags)
                        .setHeader("sunline_delay", time)
                        .build()
        );
        if (send) {
            LOGGER.info("send message success!!! tags:{},time:{}, message body:{}", tags, time, msg);
        }
        return send;
    }
}
