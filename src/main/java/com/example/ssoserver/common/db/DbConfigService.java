package com.example.ssoserver.common.db;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class DbConfigService {
  @Bean("master")
  @ConfigurationProperties(prefix = "druid.datasource.user.master")
  public DruidSetting druidMasterSetting() {
    return new DruidSetting();
  }

  @Bean("slave")
  @ConfigurationProperties(prefix = "druid.datasource.user.slave")
  public DruidSetting druidSlaveSetting() {
    return new DruidSetting();
  }

}
