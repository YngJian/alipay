package cn.sunline.tiny.utils;

import cn.sunline.tiny.mq.SendMqMsgUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.ArrayList;
import java.util.List;

@Component
public class CrashLogUtil {
    private static final Logger LOG = LoggerFactory.getLogger(CrashLogUtil.class);

    @Value("${spring.application.name}")
    private String applicationName;
    @Value("${crashlogPath}")
    private String crashlogPath;
    @Autowired
    private SendMqMsgUtil sendMqMsgUtil;

    public String getCrashlog() {
        String name = ManagementFactory.getRuntimeMXBean().getName();
        LOG.info(name);
        // get pid
        String pid = name.split("@")[0];
        StringBuffer cmd = new StringBuffer();
        cmd.append("jstack -l " + pid);
        try {
            Process process = Runtime.getRuntime().exec(cmd.toString());
            InputStream in = process.getInputStream();
            InputStreamReader reader = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(reader);
            StringBuffer sb = new StringBuffer();
            String message;
            while ((message = br.readLine()) != null) {
                sb.append(message + "\n");
            }
            addMemoryInfo(sb);
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void sendDumplog() {
        File file = new File(crashlogPath);
        if (!file.exists()) {
            LOG.debug(crashlogPath + "is not exist!");
            return;
        }
        File[] tempList = file.listFiles();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("sendDumplog:" + applicationName + ":");
        List<String> list = new ArrayList();
        for (int i = 0; i < tempList.length; i++) {
            String fileName = tempList[i].getName();
            if (fileName.endsWith(".hprof")) {
                list.add(fileName);
            }
        }
        if (list.size() > 0) {
            stringBuffer.append(String.join(":", list));
            sendMqMsgUtil.sendMessage(stringBuffer.toString(), "sendDumplog");
        }

    }

    private void addMemoryInfo(StringBuffer sb) {
        MemoryMXBean memory = ManagementFactory.getMemoryMXBean();
        MemoryUsage headMemory = memory.getHeapMemoryUsage();
        sb.append("head堆:");
        sb.append("\n初始(字节):" + headMemory.getInit());
        sb.append("\n最大(上限)(字节):" + headMemory.getMax());
        sb.append("\n当前(已使用)(字节):" + headMemory.getUsed());
        sb.append("\n提交的内存(已申请)(字节):" + headMemory.getCommitted());
        sb.append("\n使用率:" + headMemory.getUsed() * 100 / headMemory.getCommitted() + "%");

        sb.append("non-head非堆:");
        MemoryUsage nonheadMemory = memory.getNonHeapMemoryUsage();
        sb.append("\n初始(字节):" + nonheadMemory.getInit());
        sb.append("\n最大(上限)(字节):" + nonheadMemory.getMax());
        sb.append("\n当前(已使用)(字节):" + nonheadMemory.getUsed());
        sb.append("\n提交的内存(已申请)(字节):" + nonheadMemory.getCommitted());
        sb.append("\n使用率:" + nonheadMemory.getUsed() * 100 / nonheadMemory.getCommitted() + "%");
    }
}
