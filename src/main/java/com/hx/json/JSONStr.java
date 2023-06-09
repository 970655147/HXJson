package com.hx.json;


import com.hx.json.interf.JSON;
import com.hx.json.interf.JSONType;

/**
 * JSON中的字符串元素
 *
 * @author Jerry.X.He <970655147@qq.com>
 * @version 1.0
 * @date 4/15/2017 11:55 AM
 */
public class JSONStr implements JSON {

    /**
     * JSONStr持有的字符串
     */
    private String str;

    private JSONStr(String str) {
        this.str = str;
    }

    @Override
    public JSONType type() {
        return JSONType.STR;
    }

    @Override
    public Object value() {
        return str;
    }

    @Override
    public boolean isArray() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public boolean isNull() {
        return false;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public String toString(int indentFactor) {
        return str;
    }

    @Override
    public String toString() {
        return toString(0);
    }

    /**
     * 根据给定的Object创建一个JSONStr
     *
     * @param obj 给定的Object
     * @return com.hx.log.json.JSONStr
     * @author Jerry.X.He
     * @date 4/15/2017 5:18 PM
     * @since 1.0
     */
    public static JSON fromObject(Object obj) {
        if((obj == null) || (obj == JSONNull.getInstance()) ) {
            return JSONNull.getInstance();
        }

        return new JSONStr(String.valueOf(obj));
    }

}
