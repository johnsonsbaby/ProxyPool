package com.gitee.jsbd.proxypool.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @program: proxypool
 * @author: Mr.Han
 * @create: 2018-12-21 17:03
 **/
@Controller
public class IndexController {
    @RequestMapping("")
    public String index(){
        return "index";
    }
}
