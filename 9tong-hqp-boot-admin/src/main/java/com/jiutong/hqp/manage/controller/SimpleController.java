package com.jiutong.hqp.manage.controller;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 
 * @author : wang.tao
 * @createTime : 2016年4月27日 下午4:17:15
 * @version : 1.0
 * @description :
 *
 */
@Controller
@EnableAutoConfiguration
public class SimpleController {
    
    @RequestMapping(value="/hello",method=RequestMethod.GET)
    @ResponseBody
    public String hello(){
        return "hello world" ;
    }
    
}
