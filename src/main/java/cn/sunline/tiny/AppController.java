package cn.sunline.tiny;

import cn.sunline.tiny.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class AppController extends BaseController {
    private static final Logger LOG = LoggerFactory.getLogger(AppController.class);

    @RequestMapping(path = "/{tranCode}.tml")
    public void index(HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("start...");
        super.doPost(request, response);
    }
}

