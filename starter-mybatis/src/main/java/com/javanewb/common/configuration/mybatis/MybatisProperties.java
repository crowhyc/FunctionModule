package com.javanewb.common.configuration.mybatis;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <p>
 * Description: com.javanewb.common.configuration.mybatis
 * </p>
 * <p>
 * </p>
 *
 * @author Dean.Hwang
 * date 16/12/1
 */
@Component
@ConfigurationProperties(prefix = "mybatis.conf")
public class MybatisProperties {
    private String entityPath;
    private boolean openXmlScan;

    public boolean isOpenXmlScan() {
        return openXmlScan;
    }

    public void setOpenXmlScan(boolean openXmlScan) {
        this.openXmlScan = openXmlScan;
    }

    public String getEntityPath() {
        return entityPath;
    }

    public void setEntityPath(String entityPath) {
        this.entityPath = entityPath;
    }
}
