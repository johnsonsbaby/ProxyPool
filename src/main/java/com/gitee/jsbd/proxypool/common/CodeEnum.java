package com.gitee.jsbd.proxypool.common;

public enum CodeEnum {
    //请求接口相关状态吗
    SUCCESS_CODE(200, "成功"),
    SERVER_INTERNAL_ERROR(500, "系统繁忙，请稍后再试"),
    SAVE_PROXY_ERROR(1000, "保存代理失败"),
    NOT_FOUND(1001, "没有找到相关数据"),
    PARAMS_ERROR(1002, "参数异常"),
    PROXY_EXISTS(1003, "代理已经存在");

    private int code;
    private String msg;

    CodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
