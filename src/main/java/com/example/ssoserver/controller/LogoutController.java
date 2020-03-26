package com.example.ssoserver.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.example.ssoserver.common.TokenManager;
import com.example.ssoserver.util.CookieUtils;
import com.example.ssoserver.util.SessionUtils;
import com.example.ssoserver.util.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author Joe
 */
@Api(tags = "单点登出")
@Controller
@RequestMapping("/logout")
public class LogoutController {
	
	@Resource
	private TokenManager tokenManager;

	@ApiOperation("登出")
	@RequestMapping(method = RequestMethod.GET)
	public String logout(@ApiParam(value = "返回链接") String backUrl, HttpServletRequest request) {
		String token = CookieUtils.getCookie(request, TokenManager.TOKEN);
		if (StringUtils.isNotBlank(token)) {
			tokenManager.remove(token);
		}
		SessionUtils.invalidate(request);
		return "redirect:" + (StringUtils.isBlank(backUrl) ? "/admin/admin" : backUrl);
	}
}