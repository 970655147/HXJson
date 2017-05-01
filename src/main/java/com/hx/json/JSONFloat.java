package com.hx.json;


import com.hx.json.interf.JSON;
import com.hx.json.interf.JSONType;

/**
 * JSONFloat
 *
 * @author Jerry.X.He <970655147@qq.com>
 * @version 1.0
 * @date 4/15/2017 6:08 PM
 */
class JSONFloat implements JSON {

    /**
     * 当前JSON持有的val
     */
    private float val;

    private JSONFloat(float val) {
        this.val = val;
    }

    @Override
    public JSONType type() {
        return JSONType.FLOAT;
    }

    @Override
    public Object value() {
        return Float.valueOf(val);
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

    /**
     * 根据给定的值创建一个JSONFloat
     *
     * @param val 给定的值
     * @return com.hx.log.json.JSONStr
     * @author Jerry.X.He
     * @date 4/15/2017 5:18 PM
     * @since 1.0
     */
    static JSON fromObject(float val) {
        return new JSONFloat(val);
    }

}
