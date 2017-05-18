package com.hx.json;

import com.hx.common.util.InnerTools;
import com.hx.json.interf.JSON;
import com.hx.json.interf.JSONType;

/**
 * represent null
 *
 * @author Jerry.X.He <970655147@qq.com>
 * @version 1.0
 * @date 5/1/2017 1:04 AM
 */
public class JSONNull implements JSON {

    /**
     * JSONObj(null)
     */
    public static final JSONNull INSTANCE = new JSONNull();

    private JSONNull() {

    }

    @Override
    public JSONType type() {
        return JSONType.NULL;
    }

    @Override
    public Object value() {
        return null;
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
        return true;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public String toString(int indentFactor) {
        return InnerTools.NULL;
    }

    @Override
    public String toString() {
        return toString(0);
    }

    /**
     * 根据给定的Object创建一个JSONNull
     *
     * @param obj 给定的Object
     * @return com.hx.log.json.JSONStr
     * @author Jerry.X.He
     * @date 4/15/2017 5:18 PM
     * @since 1.0
     */
    public static JSONNull fromObject(Object obj) {
        return getInstance();
    }

    /**
     * 获取JSONNull的实例
     *
     * @return com.hx.json.JSONNull
     * @author Jerry.X.He
     * @date 5/1/2017 1:08 AM
     * @since 1.0
     */
    public static JSONNull getInstance() {
        return INSTANCE;
    }

}
