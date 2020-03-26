package com.example.ssoserver.service.impl;

import javax.annotation.Resource;

import com.example.ssoserver.dao.UserMapper;
import com.example.ssoserver.model.Result;
import com.example.ssoserver.model.ResultCode;
import com.example.ssoserver.model.User;
import com.example.ssoserver.service.UserService;
import org.springframework.stereotype.Service;



@Service
public class UserServiceImpl implements UserService {
	
	@Resource
	private UserMapper userMapper;

	
	public Result login(String ip, String account, String password) {
		Result result = Result.createSuccessResult();
		User user = findByAccount(account);
		if (user == null) {
			result.setCode(ResultCode.ERROR).setMessage("登录名不存在");
		} else if (!user.getPassword().equals(password)) {
			result.setCode(ResultCode.ERROR).setMessage("密码不正确");
		} else {
			user.setLastLoginIp(ip);
			user.setLoginCount(user.getLoginCount() + 1);
			userMapper.updateUser(user);
			result.setData(user);
		}
		return result;
	}

	
	public User findByAccount(String account) {
		return userMapper.findUserByAccount(account);
	}

}
