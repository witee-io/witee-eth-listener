package com.mbr.chain.domain.enums;

public enum MessageCallBackStatusEnum {

    SUCCESS("200","成功"),
    FIAL("500","失败");

    private String code;
    private String message;

    MessageCallBackStatusEnum(String code, String message) {
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
