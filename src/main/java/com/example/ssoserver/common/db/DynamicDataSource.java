package com.example.ssoserver.common.db;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;

/**
 * @Author: lichaoyang
 * @Date: 2019-09-22 16:15
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    public DynamicDataSource() {
    }

    public void afterPropertiesSet() {
        super.afterPropertiesSet();
    }

    protected Object determineCurrentLookupKey() {
        this.logger.debug("当前是否为主库:" + DbType.isMaster());
        return DbType.isMaster() ? "master" : "slave";
    }

    protected DataSource determineTargetDataSource() {
        DataSource dataSource = super.determineTargetDataSource();
        if (dataSource instanceof DruidDataSource) {
            DruidDataSource temp = (DruidDataSource)dataSource;
            this.logger.debug("当前获取的数据源属性:" + temp.getUsername());
        }

        return dataSource;
    }
}
