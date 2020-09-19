package com.de.service;

import com.de.entity.AdminUser;

/**
 * @author gs
 * @date 2020/6/4 - 22:40
 */
public interface AdminUserService {

    AdminUser login(String userName,String password,String isManager);

    Integer register(AdminUser adminUser);

    AdminUser getUserDetailById(Integer loginUserId);

    Boolean updatePassword(Integer loginUserId,String originalPassword,String newPassword);

    Boolean updateName(Integer loginUserId,String loginUserName,String nickName);
}
