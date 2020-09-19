package com.de.service.impl;

import com.de.dao.AdminUserMapper;
import com.de.entity.AdminUser;
import com.de.service.AdminUserService;
import com.de.util.MD5Util;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author gs
 * @date 2020/6/6 - 22:35
 */
@Service
public class AdminUserServiceImpl implements AdminUserService {

    @Resource
    private AdminUserMapper adminUserMapper;

    @Override
    public AdminUser login(String userName, String password,String ismanager) {
        String passwordMd5 = MD5Util.MD5Encode(password,"UTF-8");

        Byte isManager = (byte)( (ismanager.equals("true"))? 1 : 0);

        return adminUserMapper.login(userName,passwordMd5,isManager);
    }



    @Override
    public Integer register(AdminUser adminUser) {
        if(adminUserMapper.selectByName(adminUser.getLoginUserName())!=null){
            return 1;
        }
        else if(adminUserMapper.selectByNickName(adminUser.getNickName())!=null){
            return 2;
        }
        else if (adminUserMapper.saveUser(adminUser)>0){
            return 0;
        }
        else{
            return 3;
        }
    }

    @Override
    public AdminUser getUserDetailById(Integer loginUserId) {
        return adminUserMapper.selectByPrimaryKey(loginUserId);
    }

    @Override
    public Boolean updatePassword(Integer loginUserId, String originalPassword, String newPassword) {
        return null;
    }

    @Override
    public Boolean updateName(Integer loginUserId, String loginUserName, String nickName) {
        return null;
    }
}
