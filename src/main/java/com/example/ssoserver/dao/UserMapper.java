package com.example.ssoserver.dao;


import com.example.ssoserver.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.beans.factory.annotation.Qualifier;


/**
 * 用户持久化接口
 * 
 * @author Joe
 */
@Mapper
@Qualifier("userSqlSessionFactory")
public interface UserMapper {

    @Select("select id,name,password,lastLoginIp,loginCount,create_time,update_time from tb_user")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "name", property = "account"),
            @Result(column = "password", property = "password"),
            @Result(column = "lastLoginIp", property = "lastLoginIp"),
            @Result(column = "loginCount", property = "loginCount"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "update_time", property = "lastLoginTime")
    })
	User findUserByAccount(@Param("account") String account);

    @Update("update tb_user set lastLoginIp = #{lastLoginIp},loginCount=#{loginCount} where id=#{id}")
    int updateUser(User user);
}
