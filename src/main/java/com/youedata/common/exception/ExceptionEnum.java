package com.youedata.common.exception;


public enum ExceptionEnum {

    SUCCESS(200,"操作成功!"),
    ERROR(201,"操作失败!"),
    SQLERROR(500,"SQL执行异常！"),
    ;

    ExceptionEnum(int code, String message) {
        this.friendlyCode = code;
        this.friendlyMsg = message;
    }


    private int friendlyCode;

    private String friendlyMsg;

    public int getCode() {
        return friendlyCode;
    }

    public void setCode(int code) {
        this.friendlyCode = code;
    }

    public String getMessage() {
        return friendlyMsg;
    }

    public void setMessage(String message) {
        this.friendlyMsg = message;
    }

}
