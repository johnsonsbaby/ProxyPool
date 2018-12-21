package com.gitee.jsbd.proxypool.domain;

import java.io.Serializable;

/**
 * 代理类
 */
public class Proxy implements Serializable {

    private static final long serialVersionUID = 6330033989377152626L;
    private String ip;

    private String port;

    private double score;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
