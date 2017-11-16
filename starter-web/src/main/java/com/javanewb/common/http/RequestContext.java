package com.javanewb.common.http;


import com.javanewb.common.exception.BusinessException;
import com.javanewb.common.exception.ErrorCode;

import javax.servlet.http.HttpServletRequest;

/**
 * 工具类，可在controller，service及同线程下，随时获取当前请求相关信息。
 * 注意：不能再新起线程，及发布的service服务中使用RequestContext
 */
public class RequestContext {
    private RequestContext() {
    }

    private static final ThreadLocal<HttpServletRequest> requests = new ThreadLocal<>();
    public static final String SESSION_USER_ID = "session_user_id";
    public static final String SESSION_USER_NAME = "session_user_name";
    public static final String SESSION_BRAND_ID = "session_brand_id";
    public static final String SESSION_BRAND_NAME = "session_brand_name";
    public static final String SESSION_COMMERCIAL_ID = "session_commercial_id";
    public static final String SESSION_COMMERCIAL_NAME = "session_commercial_name";
    public static final String SESSION_USER_ICON = "session_user_icon";
    public static final String SESSION_ROLE_NAME = "session_role_name";
    public static final String SESSION_ROLE_CODE = "session_role_code";

    public static final Long SYS_ADMIN_ID = 99999999L;
    public static final String SYS_ADMIN_NAME = "admin";

    public static void setRequest(HttpServletRequest request) {
        requests.set(request);
    }

    static void remove() {
        requests.remove();
    }

    /**
     * 获取当前登录人id
     *
     * @return
     */
    public static Long getCurrentUserId() {
        Long userId = (Long) getSessionAttribute(SESSION_USER_ID);
        return userId == null ? SYS_ADMIN_ID : userId;
    }

    /**
     * 获取session brandId
     *
     * @return
     */
    public static long getBrandId() {
        return (long) getSessionAttribute(SESSION_BRAND_ID);
    }


    /**
     * 获取 session brand Name
     *
     * @return
     */
    public static String getBrandName() {
        return (String) getSessionAttribute(SESSION_BRAND_NAME);
    }

    /**
     * 获取 session commercial Name
     *
     * @return
     */
    public static String getCommercialName() {
        return (String) getSessionAttribute(SESSION_COMMERCIAL_NAME);
    }


    /**
     * 获取当前登录人所在商户id
     *
     * @return
     */
    public static Long getCurrentCommercialId() {
        Long shopId =
                (Long) getSessionAttribute(SESSION_COMMERCIAL_ID);
        if (shopId == null || shopId < 1L) {
            throw new BusinessException(ErrorCode.getNeedSwitchShop());
        }
        return shopId;
    }

    /**
     * 获取当前登录人所在品牌id
     *
     * @return
     */
    public static Long getCurrentBrandId() {
        return (Long) getSessionAttribute(SESSION_BRAND_ID);
    }

    public static Object getSessionAttribute(String key) {
        HttpServletRequest req = requests.get();
        return req == null ? null : req.getSession(false).getAttribute(key);
    }

    public static HttpServletRequest getRequest() {
        return requests.get();
    }

    public static boolean checkIsLoginAsCommercial() {
        return getCurrentCommercialId() != null;
    }


    public static String getCurrentUserName() {
        String userName = (String) getSessionAttribute(SESSION_USER_NAME);
        return userName == null ? SYS_ADMIN_NAME : userName;
    }


}
