package com.jiutong.hqp.manage;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.jiutong.hqp.manage.controller.login.LoginController;

/**
 * 
 * @author : wang.tao
 * @createTime : 2016年4月27日 下午5:36:08
 * @version : 1.0
 * @description :
 *
 */
@ComponentScan(basePackages={"com.jiutong.hqp.*"})
@SpringApplicationConfiguration
public class Application {
    
    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(LoginController.class, args);
        String[] beanNames = ctx.getBeanDefinitionNames();
        
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }
        
    }

}
