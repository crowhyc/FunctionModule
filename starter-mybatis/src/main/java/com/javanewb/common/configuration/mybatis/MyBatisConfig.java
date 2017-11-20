package com.javanewb.common.configuration.mybatis;

import com.github.pagehelper.PageHelper;
import com.javanewb.common.configuration.datasource.DataSourceConfig;
import com.javanewb.common.configuration.datasource.DynamicDataSource;
import com.javanewb.common.exception.TkMybatisInitException;
import org.apache.ibatis.logging.log4j.Log4jImpl;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.Properties;

/**
 * @author Dean.Hwang
 * date: 2016/9/26 10:41
 */
@Configuration
@AutoConfigureAfter({MybatisProperties.class, DataSourceConfig.class})

public class MyBatisConfig {
    @Autowired
    private MybatisProperties mybatisProperties;

    @Bean
    public PageHelper pageHelper() {
        PageHelper pageHelper = new PageHelper();
        Properties p = new Properties();
        p.setProperty("offsetAsPageNum", "true");
        p.setProperty("rowBoundsWithCount", "true");
        p.setProperty("reasonable", "true");
        pageHelper.setProperties(p);
        return pageHelper;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(DynamicDataSource dynamicDataSource, PageHelper pageHelper) {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dynamicDataSource);
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(wrapperMapperLocation(resolver));
        sqlSessionFactoryBean.setTypeAliasesPackage(mybatisProperties.getEntityPath());
        org.apache.ibatis.session.Configuration conf = new org.apache.ibatis.session.Configuration();
        conf.setLazyLoadingEnabled(true);
        conf.setLogImpl(Log4jImpl.class);
        conf.setMapUnderscoreToCamelCase(true);
        conf.setCacheEnabled(true);
        sqlSessionFactoryBean.setConfiguration(conf);
        sqlSessionFactoryBean.setPlugins(new Interceptor[]{pageHelper});
        try {
            return sqlSessionFactoryBean.getObject();
        } catch (Exception e) {
            throw new TkMybatisInitException(e);
        }
    }

    public Resource[] wrapperMapperLocation(ResourcePatternResolver resolver) throws TkMybatisInitException {
        Resource[] resourcePortal;
        Resource[] resourcesLogger;
        try {
            if (mybatisProperties.isOpenXmlScan()) {
                resourcePortal = resolver.getResources("classpath:mapper/*.xml");
            } else {
                resourcePortal = new Resource[]{};
            }
            resourcesLogger = resolver.getResources("classpath*:com/calm/logger/**/*Mapper.xml");
        } catch (IOException e) {
            throw new TkMybatisInitException(e);
        }
        Resource[] resources = new Resource[resourcePortal.length + resourcesLogger.length];
        System.arraycopy(resourcePortal, 0, resources, 0, resourcePortal.length);
        System.arraycopy(resourcesLogger, 0, resources, resourcePortal.length, resourcesLogger.length);
        return resources;
    }


    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
