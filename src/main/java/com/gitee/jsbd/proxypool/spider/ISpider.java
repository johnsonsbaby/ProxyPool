package com.gitee.jsbd.proxypool.spider;

import java.util.List;

public interface ISpider {

    /**
     * 抓取数据接口
     *
     * @return ip:port列表
     */
    List<String> crawl();


}
