package com.javanewb.common.configuration.datasource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

@Slf4j
public class DynamicDataSource extends AbstractRoutingDataSource {

    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    public enum JdbcType {
        READ("readDataSource"),
        WRITE("writeDataSource");
        String name;

        JdbcType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public static void setJdbcType(String jdbcType) {
        contextHolder.set(jdbcType);
    }

    public static void setReadOnly() {
        contextHolder.set(JdbcType.READ.getName());
    }


    public static void setWriteRead() {
        contextHolder.set(JdbcType.WRITE.getName());
    }

    public static void reset() {
        contextHolder.set(JdbcType.WRITE.getName());
    }

    public static String getJdbcType() {
        return contextHolder.get();
    }


    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSource.getJdbcType();
    }

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();//重要，必须先初始化动态数据源
    }
}
