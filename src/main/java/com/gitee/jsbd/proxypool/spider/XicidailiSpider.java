package com.gitee.jsbd.proxypool.spider;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * 抓取西刺代理的HTTP免费代理https://www.xicidaili.com/wt
 */
@Service
public class XicidailiSpider implements ISpider {
    private static Logger LOGGER = LoggerFactory.getLogger(XicidailiSpider.class);

    public List<String> crawl() {
        int page = 1;
        List<String> results = new LinkedList<>();
        try {
            while (page <= 100) {
                StringBuilder url = new StringBuilder("https://www.xicidaili.com/wt/");
                url.append(page);
                LOGGER.info("start crawl [{}]", url.toString());
                HttpResponse response = HttpRequest.get(url.toString()).timeout(5000).executeAsync();

                String html = response.body();
                Thread.sleep(1000);//请求慢一点，这个网站喜欢封爬虫IP

                Document document = Jsoup.parse(html);
                Elements elements = document.select("#ip_list tbody tr:gt(0)");

                if (elements != null && elements.size() > 0) {
                    for (Element element : elements) {
                        String ip = element.select("td:nth-child(2)").text();
                        String port = element.select("td:nth-child(3)").text();
                        String proxy = ip + ":" + port;
                        LOGGER.info("Get proxy[{}] from 西刺代理.", proxy);
                        results.add(proxy);
                    }
                }
                page++;
            }
        } catch (Exception e) {
            LOGGER.error("抓取西刺代理IP发生异常", e);
        } finally {
        }
        LOGGER.info("size=" + results.size());
        return results;
    }

}
