package com.example.springboot.utils;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.thymeleaf.util.PatternUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@Slf4j
public enum IpUtils {
    Instance;
    private static final String IP_REGEX = "\"ip\"\\s*:\\s*\"(\\d+\\.\\d+\\.\\d+\\.\\d+)\"";
    private static final String QUERY_IP_REGEX = "\"query\"\\s*:\\s*\"(\\d+\\.\\d+\\.\\d+\\.\\d+)\"";
    private static final String CIP_REGEX = "\"cip\"\\s*:\\s*\"(\\d+\\.\\d+\\.\\d+\\.\\d+)\"";
    private static final String DYNDNS_IP_REGEX = "(\\d+\\.\\d+\\.\\d+\\.\\d+)";
    private static final String IP138_REGEX = "[\\s\\S]+您的IP地址是：\\[(.+)\\][\\s\\S]+";

    /**
     * @param * @param null
     * @return
     * @author zhanghesheng
     * @date 2018/5/25
     * @Description 获取本机公网Ip
     */
    public String getMyIpInfo() {
        AsyncHttpClient client = new AsyncHttpClient();
        List<Pair<String, String>> ipList = new ArrayList<>();
        ipList.add(Pair.of("http://ip-api.com/json/", QUERY_IP_REGEX));
        ipList.add(Pair.of("http://ip.taobao.com/service/getIpInfo.php?ip=myip", IP_REGEX));
        ipList.add(Pair.of("http://whois.pconline.com.cn/ipJson.jsp", IP_REGEX));
        ipList.add(Pair.of("http://members.3322.org/dyndns/getip", DYNDNS_IP_REGEX));
        ipList.add(Pair.of("https://ipip.yy.com/get_ip_info.php", CIP_REGEX));
        ipList.add(Pair.of("http://pv.sohu.com/cityjson?ie=utf-8", CIP_REGEX));
        ipList.add(Pair.of("http://www.ip138.com/ips1388.asp", IP138_REGEX));
        /*ipList.add(Pair.of("http://www.ip168.com/json.do?view=myipaddress", Pattern.compile("[\\s\\S]+您的IP是：\\[(.+)\\][\\s\\S]+")));
        ipList.add(Pair.of("http://1212.ip138.com/ic.asp", Pattern.compile("[\\s\\S]+您的IP是：\\[(.+)\\][\\s\\S]+")));*/
        while (true) {
            try {
                for (Pair<String, String> pair : ipList) {
                    String ipInfo = getPublicIp(client, pair.getLeft(), pair.getRight());
                    if (ipInfo == null) {
                        try {
                            TimeUnit.SECONDS.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        continue;
                    } else {
                        return ipInfo;
                    }
                }
            } finally {
                if (client != null) client.close(); // 关闭连接, 释放资源
            }
        }

    }

    /**
     * 启动时获取本机的公网地址
     *
     * @return
     */
    private String getPublicIp(AsyncHttpClient client, String url, String pattern) {
        try {
            Response response = client.prepareGet(url).execute().get();
            if (response.getStatusCode() == 200) {
                String content = new String(response.getResponseBodyAsBytes(), "GB2312");
                List<String> ipResultList = RegexUtils.regexAllPattern(pattern, content, 1);
                if (ipResultList != null && ipResultList.size() > 0) {
                    return ipResultList.get(0);
                }
            }
        } catch (Exception e) {
            log.error("action=getPublicIp url:{}, Cannot get public ip address", url, e);
        }
        return null;
    }
}
