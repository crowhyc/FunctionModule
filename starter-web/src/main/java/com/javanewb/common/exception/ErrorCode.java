package com.javanewb.common.exception;

import com.javanewb.common.filter.LoggerMDCFilter;
import lombok.Data;
import lombok.ToString;
import org.slf4j.MDC;

import java.io.Serializable;

/**
 * <p>
 * Description: com.javanewb.common.exceptions
 * </p>
 * <p>
 * </p>
 *
 * @author Dean.Hwang
 * date 17/5/22
 */
@Data
@ToString
public class ErrorCode implements Serializable {
    private int code;
    private String msg;
    private String mdcTarget;
    private Long timestamp;

    static final int SYSTEM_ERROR = 1000;
    static final int INTERNAL_ERROR_CODE = SYSTEM_ERROR + 1;
    static final int NET_ERROR_CODE = SYSTEM_ERROR + 2;
    static final int PARAM_ERROR_CODE = SYSTEM_ERROR + 3;
    static final int BRAND_ID_INVALID_CODE = SYSTEM_ERROR + 4;
    static final int SHOP_ID_INVALID_CODE = SYSTEM_ERROR + 5;
    static final int NEED_SWITCH_TO_SHOP = SYSTEM_ERROR + 6;


    public ErrorCode(int code, String msg) {
        this.mdcTarget = MDC.get(LoggerMDCFilter.IDENTIFIER);
        this.timestamp = System.currentTimeMillis();
        this.code = code;
        this.msg = msg;
    }

    public static ErrorCode getInternalError(Exception e) {
        long start = Long.parseLong(MDC.get("TIME"));
        return new ErrorCode(INTERNAL_ERROR_CODE, "服务器发生异常 cost time " + (System.currentTimeMillis() - start) + " : " + e.getMessage());
    }

    public static ErrorCode getInternalError() {
        long start = Long.parseLong(MDC.get("TIME"));
        return new ErrorCode(INTERNAL_ERROR_CODE, "服务器发生异常 cost time " + (System.currentTimeMillis() - start));
    }

    public static ErrorCode getNetError() {
        return new ErrorCode(NET_ERROR_CODE, "网络错误");
    }

    public static ErrorCode getParamError() {
        return new ErrorCode(PARAM_ERROR_CODE, "参数错误");
    }

    public static ErrorCode getBrandIdInvalid() {
        return new ErrorCode(BRAND_ID_INVALID_CODE, "需要合法的BrandId");
    }

    public static ErrorCode getShopIdInvalid() {
        return new ErrorCode(SHOP_ID_INVALID_CODE, "需要合法的ShopId");
    }

    public static ErrorCode getNeedSwitchShop() {
        return new ErrorCode(NEED_SWITCH_TO_SHOP, "请切换至门店后进行操作");
    }
}