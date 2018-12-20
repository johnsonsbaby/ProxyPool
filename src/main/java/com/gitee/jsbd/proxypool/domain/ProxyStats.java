package com.gitee.jsbd.proxypool.domain;

import java.io.Serializable;

/**
 * 代理数量统计
 */
public class ProxyStats implements Serializable {

    private static final long serialVersionUID = -9138847089947379036L;
    private long all;

    private long high;

    public long getAll() {
        return all;
    }

    public void setAll(long all) {
        this.all = all;
    }

    public long getHigh() {
        return high;
    }

    public void setHigh(long high) {
        this.high = high;
    }
}
