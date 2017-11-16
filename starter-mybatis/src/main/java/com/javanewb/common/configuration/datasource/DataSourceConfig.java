package com.javanewb.common.configuration.datasource;

import com.alibaba.druid.filter.logging.Slf4jLogFilter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.javanewb.common.configuration.properties.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Description: com.javanewb.portal.scheduler.config
 * </p>
 * <p>
 * </p>
 *
 * @author Dean.Hwang
 */
@Configuration
@EnableTransactionManagement
@ConditionalOnProperty(prefix = "common", name = "mysqlEnabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties({MasterDBProperties.class, SlaverDBProperties.class, CommonProperties.class, UnionDBProperties.class})
@RefreshScope
public class DataSourceConfig {
    private final MasterDBProperties masterDBProperties;
    private final SlaverDBProperties slaverDBProperties;
    private final CommonProperties commonProperties;

    public DataSourceConfig(MasterDBProperties masterDBProperties, SlaverDBProperties slaverDBProperties, CommonProperties commonProperties) {
        this.masterDBProperties = masterDBProperties;
        this.slaverDBProperties = slaverDBProperties;
        this.commonProperties = commonProperties;
    }


    @Bean
    @Primary
    public DynamicDataSource dynamicDataSource(DruidDataSource masterDataSource, DruidDataSource slaverDataSource) {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        Map<Object, Object> dataSourceMap = new HashMap<>();
        if (commonProperties.isRWSplitEnabled()) {
            dataSourceMap.put("writeDataSource", masterDataSource);
            dataSourceMap.put("readDataSource", slaverDataSource);
        } else {
            dataSourceMap.put("writeDataSource", masterDataSource);
            dataSourceMap.put("readDataSource", masterDataSource);
        }
        dynamicDataSource.setTargetDataSources(dataSourceMap);
        dynamicDataSource.setDefaultTargetDataSource(masterDataSource);
        return dynamicDataSource;
    }

    @Bean
    public Slf4jLogFilter slf4jLogFilter() {
        Slf4jLogFilter slf4jLogFilter = new Slf4jLogFilter();
        slf4jLogFilter.setStatementExecutableSqlLogEnable(true);
        return slf4jLogFilter;
    }

    @Bean
    public StatFilter statFilter() {
        StatFilter statFilter = new StatFilter();
        statFilter.setSlowSqlMillis(10000L);
        statFilter.setLogSlowSql(true);
        statFilter.setMergeSql(true);
        return statFilter;
    }

    @Bean(destroyMethod = "close")
    public DruidDataSource masterDataSource() {
        return getBasicDataSource(masterDBProperties);
    }

    @Bean(destroyMethod = "close")
    public DruidDataSource druidDataSource(DruidDataSource masterDataSource) {
        return masterDataSource;
    }


    @Bean(destroyMethod = "close")
    public DruidDataSource slaverDataSource() {
        if (commonProperties.isRWSplitEnabled()) {
            return getBasicDataSource(slaverDBProperties);
        } else {
            return masterDataSource();
        }
    }

    private static DruidDataSource getBasicDataSource(DBProperties properties) {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName(properties.getDriverClassName());
        druidDataSource.setUrl(properties.getUrl());
        druidDataSource.setUsername(properties.getUsername());
        druidDataSource.setPassword(properties.getPassword());
        druidDataSource.setInitialSize(properties.getInitialSize());
        druidDataSource.setMaxActive(properties.getMaxTotal());
        druidDataSource.setMinIdle(properties.getMinIdle());
        druidDataSource.setValidationQuery(properties.getValidationQuery());
        druidDataSource.setRemoveAbandoned(properties.getRemoveAbandonedOnBorrow());
        druidDataSource.setTimeBetweenEvictionRunsMillis(properties.getTimeBetweenEvictionRunsMillis());
        druidDataSource.setTestOnBorrow(properties.getTestOnBorrow());
        druidDataSource.setTestWhileIdle(properties.getTestWhileIdle());
        druidDataSource.setMaxWait(properties.getMaxWait());
        druidDataSource.setRemoveAbandonedTimeoutMillis(properties.getRemoveAbandonedTimeout());
        druidDataSource.setTimeBetweenConnectErrorMillis(properties.getTimeBetweenEvictionRunsMillis());
        druidDataSource.setRemoveAbandoned(false);
        return druidDataSource;

    }

    @Bean
    public WriterReadDSAspector writerReadDSAspector() {
        return new WriterReadDSAspector();
    }


    @Bean
    public PlatformTransactionManager txManager(DynamicDataSource dynamicDataSource) {
        return new DataSourceTransactionManager(dynamicDataSource);
    }
}
