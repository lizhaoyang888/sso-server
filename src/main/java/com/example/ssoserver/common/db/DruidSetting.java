package com.example.ssoserver.common.db;

import com.alibaba.druid.pool.DruidDataSource;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.LocalCacheScope;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @Author: lichaoyang
 * @Date: 2019-09-22 16:16
 */
public class DruidSetting {

    private static final Logger log = LoggerFactory.getLogger(DruidSetting.class);
    public static final String MASTER = "master";
    public static final String SLAVE = "slave";
    private String driverClassName = "com.mysql.jdbc.Driver";
    private String url;
    private String username;
    private String password;
    private int initialSize = 10;
    private int maxActive = 50;
    private int minIdle = 3;
    private long maxWait = 10000L;
    private boolean removeAbandoned = true;
    private long removeAbandonedTimeout = 180L;
    private long timeBetweenEvictionRunsMillis = 300000L;
    private long minEvictableIdleTimeMillis = 300000L;
    private String validationQuery = "SELECT 1 FROM DUAL";
    private boolean testWhileIdle = true;
    private boolean testOnBorrow = true;
    private boolean testOnReturn = false;
    private boolean poolPreparedStatements = true;
    private int maxPoolPreparedStatementPerConnectionSize = 50;
    private String filters = "stat,wall,log4j2";

    private static DataSource createDataSource(DruidSetting druidMasterSetting, DruidSetting druidSlaveSetting) throws SQLException {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        DruidDataSource masterDataSource = druidMasterSetting.createDruidDataSource();
        DruidDataSource slaveDataSource = druidSlaveSetting.createDruidDataSource();
        dynamicDataSource.setTargetDataSources(ImmutableMap.of("master", masterDataSource, "slave", slaveDataSource));
        dynamicDataSource.setDefaultTargetDataSource(masterDataSource);
        dynamicDataSource.afterPropertiesSet();
        return dynamicDataSource;
    }

    public static SqlSessionFactory createDefaultFactoryBean(DruidSetting druidMasterSetting, DruidSetting druidSlaveSetting) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setConfiguration(new Configuration() {
            public MappedStatement getMappedStatement(String id) {
                MappedStatement mappedStatement = super.getMappedStatement(id);
                SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
                boolean dbType = !sqlCommandType.equals(SqlCommandType.SELECT) || DbType.isForceMaster();
                DruidSetting.log.debug("操作方法:{},设置为主库:{}", sqlCommandType.toString(), dbType);
                DbType.setDBType(dbType);
                return mappedStatement;
            }
        });
        bean.setDataSource(createDataSource(druidMasterSetting, druidSlaveSetting));
        SqlSessionFactory factoryBean = bean.getObject();
        factoryBean.getConfiguration().setMapUnderscoreToCamelCase(true);
        factoryBean.getConfiguration().setLocalCacheScope(LocalCacheScope.STATEMENT);
        return factoryBean;
    }

    private DruidDataSource createDruidDataSource() throws SQLException {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(this.getUrl());
        dataSource.setUsername(this.getUsername());
        dataSource.setPassword(this.getPassword());
        dataSource.setDriverClassName(this.getDriverClassName());
        if (this.getMaxActive() > 0) {
            dataSource.setMaxActive(this.getMaxActive());
        }

        if (this.getInitialSize() > 0) {
            dataSource.setInitialSize(this.getInitialSize());
        }

        if (this.getMinIdle() > 0) {
            dataSource.setMinIdle(this.getMinIdle());
        }

        if (this.getMaxWait() > 0L) {
            dataSource.setMaxWait(this.getMaxWait());
        }

        if (this.getTimeBetweenEvictionRunsMillis() > 0L) {
            dataSource.setTimeBetweenEvictionRunsMillis(this.getTimeBetweenEvictionRunsMillis());
        }

        if (this.getMinEvictableIdleTimeMillis() > 0L) {
            dataSource.setMinEvictableIdleTimeMillis(this.getMinEvictableIdleTimeMillis());
        }

        if (StringUtils.isNotEmpty(this.getValidationQuery())) {
            dataSource.setValidationQuery(this.getValidationQuery());
        }

        dataSource.setTestWhileIdle(this.isTestWhileIdle());
        dataSource.setTestOnBorrow(this.isTestOnBorrow());
        dataSource.setTestOnReturn(this.isTestOnReturn());
        dataSource.setPoolPreparedStatements(this.isPoolPreparedStatements());
        if (this.getMaxPoolPreparedStatementPerConnectionSize() > 0) {
            dataSource.setMaxOpenPreparedStatements(this.getMaxPoolPreparedStatementPerConnectionSize());
        }

        if (StringUtils.isNotEmpty(this.getFilters())) {
            dataSource.setFilters(this.getFilters());
        }

        dataSource.setRemoveAbandoned(this.isRemoveAbandoned());
        dataSource.init();
        return dataSource;
    }

    public DruidSetting() {
    }

    public String getDriverClassName() {
        return this.driverClassName;
    }

    public String getUrl() {
        return this.url;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public int getInitialSize() {
        return this.initialSize;
    }

    public int getMaxActive() {
        return this.maxActive;
    }

    public int getMinIdle() {
        return this.minIdle;
    }

    public long getMaxWait() {
        return this.maxWait;
    }

    public boolean isRemoveAbandoned() {
        return this.removeAbandoned;
    }

    public long getRemoveAbandonedTimeout() {
        return this.removeAbandonedTimeout;
    }

    public long getTimeBetweenEvictionRunsMillis() {
        return this.timeBetweenEvictionRunsMillis;
    }

    public long getMinEvictableIdleTimeMillis() {
        return this.minEvictableIdleTimeMillis;
    }

    public String getValidationQuery() {
        return this.validationQuery;
    }

    public boolean isTestWhileIdle() {
        return this.testWhileIdle;
    }

    public boolean isTestOnBorrow() {
        return this.testOnBorrow;
    }

    public boolean isTestOnReturn() {
        return this.testOnReturn;
    }

    public boolean isPoolPreparedStatements() {
        return this.poolPreparedStatements;
    }

    public int getMaxPoolPreparedStatementPerConnectionSize() {
        return this.maxPoolPreparedStatementPerConnectionSize;
    }

    public String getFilters() {
        return this.filters;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setInitialSize(int initialSize) {
        this.initialSize = initialSize;
    }

    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public void setMaxWait(long maxWait) {
        this.maxWait = maxWait;
    }

    public void setRemoveAbandoned(boolean removeAbandoned) {
        this.removeAbandoned = removeAbandoned;
    }

    public void setRemoveAbandonedTimeout(long removeAbandonedTimeout) {
        this.removeAbandonedTimeout = removeAbandonedTimeout;
    }

    public void setTimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }

    public void setMinEvictableIdleTimeMillis(long minEvictableIdleTimeMillis) {
        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
    }

    public void setValidationQuery(String validationQuery) {
        this.validationQuery = validationQuery;
    }

    public void setTestWhileIdle(boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }

    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public void setTestOnReturn(boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }

    public void setPoolPreparedStatements(boolean poolPreparedStatements) {
        this.poolPreparedStatements = poolPreparedStatements;
    }

    public void setMaxPoolPreparedStatementPerConnectionSize(int maxPoolPreparedStatementPerConnectionSize) {
        this.maxPoolPreparedStatementPerConnectionSize = maxPoolPreparedStatementPerConnectionSize;
    }

    public void setFilters(String filters) {
        this.filters = filters;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof DruidSetting)) {
            return false;
        } else {
            DruidSetting other = (DruidSetting)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label135: {
                    Object this$driverClassName = this.getDriverClassName();
                    Object other$driverClassName = other.getDriverClassName();
                    if (this$driverClassName == null) {
                        if (other$driverClassName == null) {
                            break label135;
                        }
                    } else if (this$driverClassName.equals(other$driverClassName)) {
                        break label135;
                    }

                    return false;
                }

                Object this$url = this.getUrl();
                Object other$url = other.getUrl();
                if (this$url == null) {
                    if (other$url != null) {
                        return false;
                    }
                } else if (!this$url.equals(other$url)) {
                    return false;
                }

                label121: {
                    Object this$username = this.getUsername();
                    Object other$username = other.getUsername();
                    if (this$username == null) {
                        if (other$username == null) {
                            break label121;
                        }
                    } else if (this$username.equals(other$username)) {
                        break label121;
                    }

                    return false;
                }

                Object this$password = this.getPassword();
                Object other$password = other.getPassword();
                if (this$password == null) {
                    if (other$password != null) {
                        return false;
                    }
                } else if (!this$password.equals(other$password)) {
                    return false;
                }

                if (this.getInitialSize() != other.getInitialSize()) {
                    return false;
                } else if (this.getMaxActive() != other.getMaxActive()) {
                    return false;
                } else if (this.getMinIdle() != other.getMinIdle()) {
                    return false;
                } else if (this.getMaxWait() != other.getMaxWait()) {
                    return false;
                } else if (this.isRemoveAbandoned() != other.isRemoveAbandoned()) {
                    return false;
                } else if (this.getRemoveAbandonedTimeout() != other.getRemoveAbandonedTimeout()) {
                    return false;
                } else if (this.getTimeBetweenEvictionRunsMillis() != other.getTimeBetweenEvictionRunsMillis()) {
                    return false;
                } else if (this.getMinEvictableIdleTimeMillis() != other.getMinEvictableIdleTimeMillis()) {
                    return false;
                } else {
                    label95: {
                        Object this$validationQuery = this.getValidationQuery();
                        Object other$validationQuery = other.getValidationQuery();
                        if (this$validationQuery == null) {
                            if (other$validationQuery == null) {
                                break label95;
                            }
                        } else if (this$validationQuery.equals(other$validationQuery)) {
                            break label95;
                        }

                        return false;
                    }

                    if (this.isTestWhileIdle() != other.isTestWhileIdle()) {
                        return false;
                    } else if (this.isTestOnBorrow() != other.isTestOnBorrow()) {
                        return false;
                    } else if (this.isTestOnReturn() != other.isTestOnReturn()) {
                        return false;
                    } else if (this.isPoolPreparedStatements() != other.isPoolPreparedStatements()) {
                        return false;
                    } else if (this.getMaxPoolPreparedStatementPerConnectionSize() != other.getMaxPoolPreparedStatementPerConnectionSize()) {
                        return false;
                    } else {
                        Object this$filters = this.getFilters();
                        Object other$filters = other.getFilters();
                        if (this$filters == null) {
                            if (other$filters != null) {
                                return false;
                            }
                        } else if (!this$filters.equals(other$filters)) {
                            return false;
                        }

                        return true;
                    }
                }
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof DruidSetting;
    }

    public int hashCode() {
        int result = 1;
        Object $driverClassName = this.getDriverClassName();
        result = result * 59 + ($driverClassName == null ? 43 : $driverClassName.hashCode());
        Object $url = this.getUrl();
        result = result * 59 + ($url == null ? 43 : $url.hashCode());
        Object $username = this.getUsername();
        result = result * 59 + ($username == null ? 43 : $username.hashCode());
        Object $password = this.getPassword();
        result = result * 59 + ($password == null ? 43 : $password.hashCode());
        result = result * 59 + this.getInitialSize();
        result = result * 59 + this.getMaxActive();
        result = result * 59 + this.getMinIdle();
        long $maxWait = this.getMaxWait();
        result = result * 59 + (int)($maxWait >>> 32 ^ $maxWait);
        result = result * 59 + (this.isRemoveAbandoned() ? 79 : 97);
        long $removeAbandonedTimeout = this.getRemoveAbandonedTimeout();
        result = result * 59 + (int)($removeAbandonedTimeout >>> 32 ^ $removeAbandonedTimeout);
        long $timeBetweenEvictionRunsMillis = this.getTimeBetweenEvictionRunsMillis();
        result = result * 59 + (int)($timeBetweenEvictionRunsMillis >>> 32 ^ $timeBetweenEvictionRunsMillis);
        long $minEvictableIdleTimeMillis = this.getMinEvictableIdleTimeMillis();
        result = result * 59 + (int)($minEvictableIdleTimeMillis >>> 32 ^ $minEvictableIdleTimeMillis);
        Object $validationQuery = this.getValidationQuery();
        result = result * 59 + ($validationQuery == null ? 43 : $validationQuery.hashCode());
        result = result * 59 + (this.isTestWhileIdle() ? 79 : 97);
        result = result * 59 + (this.isTestOnBorrow() ? 79 : 97);
        result = result * 59 + (this.isTestOnReturn() ? 79 : 97);
        result = result * 59 + (this.isPoolPreparedStatements() ? 79 : 97);
        result = result * 59 + this.getMaxPoolPreparedStatementPerConnectionSize();
        Object $filters = this.getFilters();
        result = result * 59 + ($filters == null ? 43 : $filters.hashCode());
        return result;
    }

    public String toString() {
        return "DruidSetting(driverClassName=" + this.getDriverClassName() + ", url=" + this.getUrl() + ", username=" + this.getUsername() + ", password=" + this.getPassword() + ", initialSize=" + this.getInitialSize() + ", maxActive=" + this.getMaxActive() + ", minIdle=" + this.getMinIdle() + ", maxWait=" + this.getMaxWait() + ", removeAbandoned=" + this.isRemoveAbandoned() + ", removeAbandonedTimeout=" + this.getRemoveAbandonedTimeout() + ", timeBetweenEvictionRunsMillis=" + this.getTimeBetweenEvictionRunsMillis() + ", minEvictableIdleTimeMillis=" + this.getMinEvictableIdleTimeMillis() + ", validationQuery=" + this.getValidationQuery() + ", testWhileIdle=" + this.isTestWhileIdle() + ", testOnBorrow=" + this.isTestOnBorrow() + ", testOnReturn=" + this.isTestOnReturn() + ", poolPreparedStatements=" + this.isPoolPreparedStatements() + ", maxPoolPreparedStatementPerConnectionSize=" + this.getMaxPoolPreparedStatementPerConnectionSize() + ", filters=" + this.getFilters() + ")";
    }
    
}
