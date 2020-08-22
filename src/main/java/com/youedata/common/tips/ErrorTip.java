package com.youedata.common.tips;

import com.youedata.common.exception.ExceptionEnum;

/**
 * 返回给前台的错误提示
 *
 */
public class ErrorTip extends Tip {

    public ErrorTip(int code, String message) {
        super();
        this.code = code;
        this.message = message;
    }

    public ErrorTip(){
        super();
        this.code = 201;
        this.message = "操作失败！";
    }

    public static <T> Tip<T> error() {
        Tip<T> tip = new Tip<>();
        tip.setCode(ExceptionEnum.ERROR.getCode());
        tip.setMessage(ExceptionEnum.ERROR.getMessage());
        return tip;
    }
    public static <T> Tip<T> error(T result) {
        Tip<T> tip = new Tip<>();
        tip.setCode(ExceptionEnum.ERROR.getCode());
        tip.setMessage(ExceptionEnum.ERROR.getMessage());
        tip.result = result;
        return tip;
    }
    /**
     * SQL 执行失败
     * @param result
     * @param <T>
     * @return
     */
    public static <T> Tip<T> sqlError(T result) {
        Tip<T> tip = new Tip<>();
        tip.setCode(ExceptionEnum.SQLERROR.getCode());
        tip.setMessage(ExceptionEnum.SQLERROR.getMessage());
        tip.result = result;
        return tip;
    }
}
