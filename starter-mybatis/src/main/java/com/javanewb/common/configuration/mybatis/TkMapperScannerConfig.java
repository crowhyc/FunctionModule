/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2016 abel533@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.javanewb.common.configuration.mybatis;

import com.javanewb.common.configuration.properties.CommonProperties;
import lombok.extern.log4j.Log4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;

import java.util.Arrays;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * MyBatis扫描接口，使用的tk.mybatis.spring.com.javanewb.portalbiz.commons.mapper.MapperScannerConfigurer，如果你不使用通用Mapper，可以改为org.xxx...
 *
 * @author Dean.Hwang
 * date: 2016/9/26 10:41
 */
@Configuration
@ConditionalOnBean({MybatisProperties.class, SqlSessionFactory.class})
@AutoConfigureAfter({MybatisProperties.class, MyBatisConfig.class})
@EnableConfigurationProperties(CommonProperties.class)
@Log4j
public class TkMapperScannerConfig {
    private final CommonProperties commonProperties;

    public TkMapperScannerConfig(CommonProperties commonProperties) {
        this.commonProperties = commonProperties;
    }

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        if (commonProperties.getMapperPath() == null || commonProperties.getMapperPath().length == 0) {
            mapperScannerConfigurer.setBasePackage("com.javanewb.**.dao,com.javanewb.**.mapper");
        } else {
            mapperScannerConfigurer.setBasePackage(Arrays.stream(commonProperties.getMapperPath()).collect(Collectors.joining(",")));
        }
        mapperScannerConfigurer.setMarkerInterface(CommonsMapper.class);
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        MapperHelper mapperHelper = new MapperHelper();
        Properties properties = new Properties();
        properties.setProperty("mappers", CommonsMapper.class.getName());
        properties.setProperty("notEmpty", "false");
        properties.setProperty("IDENTITY", "MYSQL");
        mapperHelper.setProperties(properties);
        mapperScannerConfigurer.setMapperHelper(mapperHelper);
        return mapperScannerConfigurer;
    }

}
