package com.example.ssoserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: lichaoyang
 * @Date: 2020-03-24 02:34
 */
@RequestMapping("/test")
@RestController
public class TestController {

    @GetMapping("/")
    public String test(){
        return "hello world";
    }
}
