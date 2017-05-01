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
     * ��ǰJSON���е�val
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
     * ���ݸ�����ֵ����һ��JSONFloat
     *
     * @param val ������ֵ
     * @return com.hx.log.json.JSONStr
     * @author Jerry.X.He
     * @date 4/15/2017 5:18 PM
     * @since 1.0
     */
    static JSON fromObject(float val) {
        return new JSONFloat(val);
    }

}
