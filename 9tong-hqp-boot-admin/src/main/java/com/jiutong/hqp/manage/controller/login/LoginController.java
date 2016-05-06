package com.jiutong.hqp.manage.controller.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jiutong.hqp.manage.entity.user.User;
import com.jiutong.hqp.manage.service.user.UserService;
import com.jiutong.utils.lang.CommonUtils;

/**
 * 
 * @author : wang.tao
 * @createTime : 2016年4月28日 上午11:22:55
 * @version : 1.0
 * @description :
 *
 */
@RestController
@EnableAutoConfiguration
public class LoginController {

    @Autowired
    @Qualifier("userService")
    UserService userService ;
    
    @RequestMapping(value="/",method=RequestMethod.GET)
    public String index(ModelMap map) {
        System.out.println("===========index=================");
        return "index";
    }

    @RequestMapping("login")
    public String login(@ModelAttribute(value="user")User user ,Model model) {
        String username = user.getUsername() ;
        String password = user.getPassword() ; 
        password = CommonUtils.encodePassword(password);
//        User item = userService.getUserByUsername(username) ;
        
        model.addAttribute("username", username);
        System.out.println(user.getUsername()+":"+user.getPassword());
//        if(item != null && item.getPassword().equals(password)){
//            return "login" ;
//        }
        return "error";
    }
}
