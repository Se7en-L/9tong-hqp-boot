package com.jiutong.hqp.manage.dao.user.impl;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;

import com.jiutong.hqp.manage.dao.user.UserDao;
import com.jiutong.hqp.manage.entity.user.User;

public class UserDaoImpl implements UserDao{

	@Resource
	SqlSessionTemplate sqlSessionTemplet;

    @Override
    public int saveUser(User user) {
        
        return 0;
    }

    @Override
    public int deleteUser(String userAccount) {
        
        return 0;
    }

    @Override
    public User findUserByUsername(String username) {
        Object obj = sqlSessionTemplet.selectOne("User.findUserByUsername", username) ;
        return obj == null ? new User() : (User)obj;
    }

}
