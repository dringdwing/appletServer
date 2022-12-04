package com.vector.server.util;

import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: 封装响应对象
 * @Title: R
 * @Package com.vector.server.util
 * @Author 芝士汉堡
 * @Date 2022/12/4 6:10
 */
public class R extends HashMap<String, Object> {
    public R() {
        put("code", HttpStatus.SC_OK);
        put("msg", "success");
    }

    /**
     * @param key
     * @param value
     * @return
     * @description: 封装put可以链式调用
     */
    public R put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    /**
     * 声明工厂方法，调用时可以直接R.ok()，不用new
     */

    public static R ok() {
        return new R();
    }

    /**
     * @param msg
     * @return
     * @description: 重载ok方法，可以传入数据
     */
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

    public static R error() {
        return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "未知异常，请联系管理员");
    }


    public static R error(String msg) {
        return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, msg);
    }


    public static R error(int code, String msg) {
        return new R().put("code", code).put("msg", msg);
    }


}
