package com.jiutong.hqp.manage.dao.user;

import com.jiutong.hqp.manage.entity.user.User;

public interface UserDao {

    int saveUser(User user);

    int deleteUser(String userAccount);

    /**
     * @param username
     * @return
     *
     * @author : wang.tao
     * @createTime : 2016年4月29日 下午3:35:49
     */
    User findUserByUsername(String username);

}
