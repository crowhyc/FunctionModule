package com.javanewb.common.configuration.mybatis;

/**
 * @author Dean.Hwang
 * date: 2016/10/8 18:38
 */

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * 被继承的Mapper，一般业务Mapper继承它
 * 特别注意，该接口不能被扫描到，否则会出错
 */
public interface CommonsMapper<T> extends Mapper<T>, MySqlMapper<T> {
}