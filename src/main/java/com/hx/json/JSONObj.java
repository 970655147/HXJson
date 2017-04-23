package com.hx.json;

import com.hx.json.interf.JSON;
import com.hx.json.interf.JSONType;

/**
 * 持有一个Object的JSON
 *
 * @author Jerry.X.He <970655147@qq.com>
 * @version 1.0
 * @date 4/15/2017 5:21 PM
 */
class JSONObj implements JSON {

    /**
     * JSONObj(null)
     */
    public static final JSONObj JSON_OBJ_NULL = JSONObj.fromObject(null);

    /**
     * 当前JSON持有的Object
     */
    private Object obj;

    JSONObj(Object obj) {
        this.obj = obj;
    }

    @Override
    public JSONType type() {
        return JSONType.OBJ;
    }

    @Override
    public Object value() {
        return obj;
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
        return String.valueOf(obj);
    }

    /**
     * 根据给定的Object创建一个JSONObj
     *
     * @param obj 给定的Object
     * @return com.hx.log.json.JSONStr
     * @author Jerry.X.He
     * @date 4/15/2017 5:18 PM
     * @since 1.0
     */
    static JSONObj fromObject(Object obj) {
        return new JSONObj(obj);
    }

}
