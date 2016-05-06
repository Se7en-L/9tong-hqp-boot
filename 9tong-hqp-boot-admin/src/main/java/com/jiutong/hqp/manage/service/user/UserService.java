package com.jiutong.hqp.manage.service.user;

import com.jiutong.hqp.manage.entity.user.User;

/**
 * 
 * @author : wang.tao
 * @createTime : 2016年4月29日 下午3:27:20
 * @version : 1.0
 * @description :
 *
 */
public interface UserService {
    
    /**
     * 通过用户名查找用户是否存在
     * @param username
     * @return
     *
     * @author : wang.tao
     * @createTime : 2016年4月29日 下午3:33:12
     */
    User getUserByUsername(String username) ;

}
