package com.pipichao.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/honglou")
public class TestController {

    //@RequestMapping("/login.do")
    public String login(){
        System.out.println("登录成功");
        return "登录成功";
    }
    @GetMapping("/zhugong/getData")
    public String getData(){
        return "获取到的数据";
    }

    @GetMapping("/zhuguan/getData")
    public String getZhuguanData(){
        return "wangxifeng的资源";
    }
}