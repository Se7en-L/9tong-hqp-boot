package com.jiutong.hqp.manage.entity.user;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

/**
 * 用户
 * 
 * @author : tankai
 * @createTime : 2016年2月26日 上午11:54:45
 * @version :
 * @description :
 *
 */
@SuppressWarnings("all")
public class User {

    private String username;
    
    private String password;
    
    private int age;

    @NotBlank(message="用户名不能为空")
    public String getUsername() {
        return username;
    }

    @NotNull(message="密码不能为null")
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Min(value=10,message="年龄最小值为10")
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

}
