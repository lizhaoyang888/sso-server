package com.example.ssoserver.model;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.example.ssoserver.common.enums.TrueFalseEnum;
import lombok.Data;


/**
 * 用户
 * 
 * @author Joe
 */

@Data
public class User {

	private static final long serialVersionUID = 1106412532325860697L;

	private int id;
	/** 姓名 */
	private String account;
	/** 密码 */
	private String password;
	/** 最后登录IP */
	private String lastLoginIp;
	/** 登录总次数 */
	private Integer loginCount = Integer.valueOf(0);
	/** 最后登录时间 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date lastLoginTime;
	/** 创建时间 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;


}
