package com.gitee.jsbd.proxypool.common;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回数据
 */
public class R extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    public R() {
        put("code", CodeEnum.SUCCESS_CODE.getCode());
        put("msg", CodeEnum.SUCCESS_CODE.getMsg());
        put("data", null);
    }

    public R(CodeEnum codeEnum) {
        put("code", codeEnum.getCode());
        put("msg", codeEnum.getMsg());
        put("data", null);
    }

    public static R error() {
        return error(CodeEnum.SERVER_INTERNAL_ERROR.getCode(), CodeEnum.SERVER_INTERNAL_ERROR.getMsg());
    }

    public static R error(String msg) {
        return error(CodeEnum.SERVER_INTERNAL_ERROR.getCode(), msg);
    }

    public static R error(int code, String msg) {
        R r = new R();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }

    public static R error(CodeEnum codeEnum) {
        R r = new R(codeEnum);
        return r;
    }

    public static R ok(String msg) {
        R r = new R();
        r.put("msg", msg);
        return r;
    }

    public static R ok(Map<String, Object> map) {
        R r = new R();
        r.putAll(map);
        return r;
    }

    public static R ok() {
        return new R();
    }

    public static R ok(CodeEnum codeEnum) {
        return new R(codeEnum);
    }

    public R data(Object data) {
        super.put("data", data);
        return this;
    }

    public R put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
