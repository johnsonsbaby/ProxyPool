package com.gitee.jsbd.proxypool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ProxyPoolApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProxyPoolApplication.class, args);
    }

}

