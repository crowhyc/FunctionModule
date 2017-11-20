package com.javanewb.common.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * <p>
 * Description: com.javanewb.common.autoconfigure
 * </p>
 * <p>
 * </p>
 *
 * @author Dean.Hwang
 * date 17/2/22
 */
@Data
@ConfigurationProperties("common")
public class CommonProperties {
    private List<String> writeDBNames;
    private boolean rWSplitEnabled;
    private String[] mapperPath;
}
