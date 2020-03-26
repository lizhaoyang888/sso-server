package com.example.ssoserver.common.db;

import com.example.ssoserver.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @Author: lichaoyang
 * @Date: 2019-06-12 11:40
 */
@Slf4j
@Configuration
@MapperScan(basePackages = "com.example.ssoserver.dao", sqlSessionFactoryRef = "userSqlSessionFactory")
public class UserMybatisConfig {

    @Resource(name = "master")
    private DruidSetting druidMasterSetting;

    @Resource(name = "slave")
    private DruidSetting druidSlaveSetting;

    @Bean("userSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactoryBean() {
        try {
            return DruidSetting.createDefaultFactoryBean(druidMasterSetting, druidSlaveSetting);
        } catch (Exception e) {
            log.error("UserConfig --> sqlSessionFactoryBean() exception==>{}", JsonUtil.object2Json(e), e);
            return null;
        }
    }

}
