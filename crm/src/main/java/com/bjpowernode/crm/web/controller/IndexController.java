package com.bjpowernode.crm.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

//    http://127.0.0.1:8080/crm/ => / 必须省去，否则出错
    @RequestMapping("/")
    public String index(){
        //请求转发
        return "index"; //因为是webapp下的资源，使其直接访问web-inf
    }




}
