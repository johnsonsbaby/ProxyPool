package com.gitee.jsbd.proxypool.task;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import com.gitee.jsbd.proxypool.dao.ProxyDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@ConfigurationProperties
public class ProxyValidTask {

    private static Logger LOGGER = LoggerFactory.getLogger(ProxyValidTask.class);
    private ExecutorService workerGroup = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);

    @Autowired
    private ProxyDAO proxyDAO;
    @Value("${task.valid.enabled}")
    private Boolean enabled;

    /**
     * 定时验证代理的有效性
     */
    @Scheduled(initialDelay = 10 * 1000, fixedDelay = 3 * 60 * 1000)
    public void validProxyScheduled() {

        if (enabled) {

            int pageSize = 200;
            long count = this.proxyDAO.count();
            LOGGER.info("===>>>当前代理池剩余[{}]个代理", count);

            for (long start = 0; start < count; start += pageSize) {
                long end = Long.min(start + pageSize - 1, count);
                Set<Object> results = this.proxyDAO.batchQuery(start, end);

                if (!CollectionUtils.isEmpty(results)) {
                    for (Object proxy : results) {
                        workerGroup.submit(() -> {
                            LOGGER.info(proxy.toString());
                            String[] addrArr = proxy.toString().split(":");
                            String ip = addrArr[0];
                            int port = Integer.parseInt(addrArr[1]);

                            try {
                                HttpRequest request = new HttpRequest("http://httpbin.org/get");
                                Proxy httpProxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip, port));
                                request.setProxy(httpProxy);
                                HttpResponse response = request.timeout(5000).executeAsync();

                                if (response.getStatus() == HttpStatus.HTTP_OK) {
                                    this.proxyDAO.validOk(proxy.toString());
                                } else {
                                    this.proxyDAO.decrementOrRemove(proxy.toString());
                                }
                            } catch (Exception e) {
                                LOGGER.warn("===>>>代理[{}]验证失败", proxy);
                                this.proxyDAO.decrementOrRemove(proxy.toString());
                            }
                        });
                    }
                }

            }

        }

    }


}
