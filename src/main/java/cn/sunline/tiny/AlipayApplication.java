package cn.sunline.tiny;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootApplication
@EnableBinding({Source.class, Sink.class})
public class AlipayApplication {

    public static JSONObject errorCodeJSONS = new JSONObject();

    public static void main(String[] args) {
        ApplicationContext ctx =SpringApplication.run(AlipayApplication.class, args);
        StringRedisTemplate stringRedisTemplate = ctx.getBean(StringRedisTemplate.class);
        String str_errorCodeJSONS = stringRedisTemplate.opsForValue().get("errorListJSONS");
        if (str_errorCodeJSONS != null && !"".equals(str_errorCodeJSONS)) {
            errorCodeJSONS = JSON.parseObject(str_errorCodeJSONS);
        } else {
            errorCodeJSONS = new JSONObject();
        }
    }

}
