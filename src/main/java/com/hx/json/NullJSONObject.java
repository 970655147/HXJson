package com.hx.json;

/**
 * NullJSONObject
 *
 * @author Jerry.X.He <970655147@qq.com>
 * @version 1.0
 * @date 4/16/2017 11:06 AM
 */
public class NullJSONObject extends JSONObject {

    /**
     * 单例
     */
    private static class NullJSONObjectHolder {
        private static NullJSONObject INSTANCE = new NullJSONObject();
    }

    private NullJSONObject() {
    }

    /**
     * 获取NullJSONObject的实例
     *
     * @return com.hx.log.json.NullJSONObject
     * @author Jerry.X.He
     * @date 4/16/2017 11:08 AM
     * @since 1.0
     */
    public static NullJSONObject getInstance() {
        return NullJSONObjectHolder.INSTANCE;
    }

    @Override
    public boolean isNull() {
        return true;
    }

    @Override
    public Object put(String key, Object val, boolean force) {
        throw new RuntimeException("Unsupported Operation Exception !");
    }

    @Override
    public Object put(String key, JSONObject val, boolean force) {
        throw new RuntimeException("Unsupported Operation Exception !");
    }

    @Override
    public Object put(String key, JSONArray val, boolean force) {
        throw new RuntimeException("Unsupported Operation Exception !");
    }

    @Override
    public Object put(String key, String val, boolean force) {
        throw new RuntimeException("Unsupported Operation Exception !");
    }

    @Override
    public Object put(String key, boolean val, boolean force) {
        throw new RuntimeException("Unsupported Operation Exception !");
    }

    @Override
    public Object put(String key, int val, boolean force) {
        throw new RuntimeException("Unsupported Operation Exception !");
    }

    @Override
    public Object put(String key, long val, boolean force) {
        throw new RuntimeException("Unsupported Operation Exception !");
    }

    @Override
    public Object put(String key, float val, boolean force) {
        throw new RuntimeException("Unsupported Operation Exception !");
    }

    @Override
    public Object put(String key, double val, boolean force) {
        throw new RuntimeException("Unsupported Operation Exception !");
    }

    @Override
    public Object remove(Object key) {
        throw new RuntimeException("Unsupported Operation Exception !");
    }

    @Override
    public Object remove(String key) {
        throw new RuntimeException("Unsupported Operation Exception !");
    }

    @Override
    public void clear() {
        throw new RuntimeException("Unsupported Operation Exception !");
    }

}
