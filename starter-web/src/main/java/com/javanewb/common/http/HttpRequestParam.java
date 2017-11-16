/**
 *
 */
package com.javanewb.common.http;

import com.javanewb.common.configuration.http.annotations.NotUrlParam;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldFilter;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Title: HttpClientParam
 * </p>
 * <p>
 * Description: com.calm.v5.common.http
 * </p>
 *
 * @author Dean.Hwang
 * date 2016年5月24日
 */
@Data
public class HttpRequestParam {

    /**
     * 请求路径
     */
    protected String url;

    /**
     * 请求方式
     */
    protected RequestMethod requestMethod = RequestMethod.GET;

    /**
     * httpEntity
     */
    protected HttpEntity<Object> httpEntity;

    /**
     * 是否是一次restFull风格的接口请求
     */
    protected boolean restFullStyle;

    /**
     * 字段过滤，用于构建Get请求参数的链接模板
     */
    private static FieldFilter fieldFilter = field -> {
        // 静态成员 或 被标注NotUrlParam的字段不参与构建链接模板
        return !field.isAnnotationPresent(NotUrlParam.class)
                && !Modifier.isStatic(field.getModifiers());
    };

    /**
     * 构造
     *
     * @param content
     */
    public HttpRequestParam(Object content) {
        this.httpEntity = new HttpEntity<>(content);
    }

    /**
     * 构造
     *
     * @param content
     */
    public HttpRequestParam(Object content, HttpHeaders headers) {
        this.httpEntity = new HttpEntity<>(content, headers);

    }

    public boolean isRestFullStyle() {
        return restFullStyle;
    }


    /**
     * @return
     * @author Dean.Hwang
     * date 2016年7月19日 下午1:23:40
     */
    public boolean isValid() {
        return StringUtils.isNotBlank(url) && httpEntity.getBody() != null;
    }

    /**
     * @return
     * @author Dean.Hwang
     * date 2016年7月19日 下午1:24:47
     */
    public boolean isNotValid() {
        return !isValid();
    }

    /**
     * get方式url链接模板拼装
     *
     * @author Dean.Hwang
     * date 2016年8月6日 上午8:08:30
     */
    protected void buildHttpGetUrl() {

        if (requestMethod == RequestMethod.POST) {
            return;
        }

        if (restFullStyle)
            return;

        final List<String> urlTplParamPairs = new ArrayList<>();
        ReflectionUtils.doWithFields(httpEntity.getBody().getClass(), field -> urlTplParamPairs.add(String.format("%s={%s}", field.getName(), field.getName())), fieldFilter);

        // 组装好请求参数模板的链接
        url =
                url + "?" + StringUtils.join(urlTplParamPairs, "&");
    }
}
