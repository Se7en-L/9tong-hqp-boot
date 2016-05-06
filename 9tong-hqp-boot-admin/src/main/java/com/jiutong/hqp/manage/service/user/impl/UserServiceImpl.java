package com.jiutong.hqp.manage.service.user.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jiutong.hqp.manage.dao.user.UserDao;
import com.jiutong.hqp.manage.entity.user.User;
import com.jiutong.hqp.manage.service.user.UserService;

/**
 * 
 * @author : wang.tao
 * @createTime : 2016年4月29日 下午3:27:49
 * @version : 1.0
 * @description :
 *
 */
@Service
public class UserServiceImpl implements UserService{

    @Resource
    UserDao userDao ;
    
    public User getUserByUsername(String username) {
        return userDao.findUserByUsername(username) ;
    }

}
