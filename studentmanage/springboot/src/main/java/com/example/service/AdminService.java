package com.example.service;
import com.example.domain.Account;
import com.example.exception.CustomException;
import com.example.mapper.AdminMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AdminService {
    @Resource
    //引用admin的mapper
    private AdminMapper adminMapper;

    //登录
    //传入Admin的admin的实体类通过数据库查出user的数据
    public Account login(Account account){
        Account dbAdmin = adminMapper.selectByUsername(account.getUsername());
        if (dbAdmin==null){
            //无用户
            throw new CustomException("账号或密码错啦");
        }
        if (!account.getPassword().equals(dbAdmin.getPassword())){
            throw new CustomException("账号或密码错啦");
        }
        return dbAdmin;
    }
}
