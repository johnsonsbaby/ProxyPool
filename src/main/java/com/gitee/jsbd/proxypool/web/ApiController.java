package com.gitee.jsbd.proxypool.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

    @RequestMapping("/")
    public String index() {
        return "欢迎使用基于Spring Boot + Redis的代理池系统!";
    }

}