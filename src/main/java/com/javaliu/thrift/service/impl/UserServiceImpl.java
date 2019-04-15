package com.javaliu.thrift.service.impl;

import com.javaliu.thrift.service.ServiceException;
import com.javaliu.thrift.service.User;
import com.javaliu.thrift.service.UserService;
import org.apache.thrift.TException;

public class UserServiceImpl implements UserService.Iface {

    @Override
    public User findUserById(long id) throws ServiceException, TException {
        System.out.println("findUserId is invoked");
        User user = new User();
        user.setId(id);
        user.setCode("zhangsan");
        user.setName("张三");
        user.setEmail("zhangsan@163.com");
        return user;
    }

    @Override
    public void saveUser(User user) throws ServiceException, TException {
        System.out.println("saveUser is invoked ");
        System.out.println(user.getCode());
        System.out.println(user.getName());
        System.out.println(user.getEmail());
        System.out.println(user.getId());
    }
}
