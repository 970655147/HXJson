package com.hx.json;

import com.hx.json.interf.JSON;
import com.hx.json.interf.JSONType;

/**
 * JSONBool
 *
 * @author Jerry.X.He <970655147@qq.com>
 * @version 1.0
 * @date 4/15/2017 11:59 AM
 */
public class JSONBool implements JSON {

    /**
     * 当前JSON持有的val
     */
    private boolean val;

    private JSONBool(boolean val) {
        this.val = val;
    }

    @Override
    public JSONType type() {
        return JSONType.BOOL;
    }

    @Override
    public Object value() {
        return Boolean.valueOf(val);
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
        return String.valueOf(val);
    }

    @Override
    public String toString() {
        return toString(0);
    }

    /**
     * 根据给定的值创建一个JSONBool
     *
     * @param bool 给定的值
     * @return com.hx.log.json.JSONStr
     * @author Jerry.X.He
     * @date 4/15/2017 5:18 PM
     * @since 1.0
     */
    public static JSON fromObject(boolean bool) {
        return new JSONBool(bool);
    }

}
