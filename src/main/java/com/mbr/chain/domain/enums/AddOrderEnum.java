package com.mbr.chain.domain.enums;

public enum  AddOrderEnum {

    ORDER_EXIST("400","订单已经存在"),
    ORDER_SUCCESS("200","成功"),
    ORDER_EXCEPTION("500","异常"),
    ORDER_ID_IS_NULL("401","订单ID不能为空");


    private String code;
    private String message;

    AddOrderEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
