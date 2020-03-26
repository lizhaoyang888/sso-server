package com.example.ssoserver.service;

import com.example.ssoserver.model.Result;
import com.example.ssoserver.model.User;



/**
 * 用户服务接口
 * 
 * @author Joe
 */
public interface UserService {

	Result login(String ip, String account, String password);

	User findByAccount(String account);
}
