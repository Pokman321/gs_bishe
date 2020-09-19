package com.de.dao;

import com.de.entity.AdminUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author gs
 * @date 2020/6/5 - 0:25
 */
@Repository
public interface AdminUserMapper {
    AdminUser login(@Param("userName") String userName,@Param("password") String password,@Param("isManager") Byte isManager);

    AdminUser selectByPrimaryKey(Integer adminUserId);

    AdminUser selectByName(String name);

    AdminUser selectByNickName(String nickName);

    int saveUser(AdminUser adminUser);
}
