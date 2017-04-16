package com.hx.json;

import java.util.Collection;
import java.util.List;

/**
 * NullJSONArray
 *
 * @author Jerry.X.He <970655147@qq.com>
 * @version 1.0
 * @date 4/16/2017 11:16 AM
 */
public class NullJSONArray extends JSONArray {

    /**
     * 单例
     */
    private static class NullJSONArrayHolder {
        private static NullJSONArray INSTANCE = new NullJSONArray();
    }

    private NullJSONArray() {
    }

    /**
     * 获取NullJSONArray的实例
     *
     * @return com.hx.log.json.NullJSONObject
     * @author Jerry.X.He
     * @date 4/16/2017 11:08 AM
     * @since 1.0
     */
    public static NullJSONArray getInstance() {
        return NullJSONArrayHolder.INSTANCE;
    }

    @Override
    public boolean isNull() {
        return true;
    }

    @Override
    public boolean add(Object val) {
        throw new RuntimeException("Unsupported Operation Exception !");
    }

    @Override
    public void add(JSONObject val) {
        throw new RuntimeException("Unsupported Operation Exception !");
    }

    @Override
    public void add(JSONArray val) {
        throw new RuntimeException("Unsupported Operation Exception !");
    }

    @Override
    public void add(String val) {
        throw new RuntimeException("Unsupported Operation Exception !");
    }

    @Override
    public void add(boolean val) {
        throw new RuntimeException("Unsupported Operation Exception !");
    }

    @Override
    public void add(int val) {
        throw new RuntimeException("Unsupported Operation Exception !");
    }

    @Override
    public void add(long val) {
        throw new RuntimeException("Unsupported Operation Exception !");
    }

    @Override
    public void add(float val) {
        throw new RuntimeException("Unsupported Operation Exception !");
    }

    @Override
    public void add(double val) {
        throw new RuntimeException("Unsupported Operation Exception !");
    }

    @Override
    public boolean remove(Object o) {
        throw new RuntimeException("Unsupported Operation Exception !");
    }

    @Override
    public boolean addAll(Collection c) {
        throw new RuntimeException("Unsupported Operation Exception !");
    }

    @Override
    public boolean addAll(int index, Collection c) {
        throw new RuntimeException("Unsupported Operation Exception !");
    }

    @Override
    public boolean removeAll(Collection c) {
        throw new RuntimeException("Unsupported Operation Exception !");
    }

    @Override
    public boolean retainAll(Collection c) {
        throw new RuntimeException("Unsupported Operation Exception !");
    }

    @Override
    public Object set(int index, Object element) {
        throw new RuntimeException("Unsupported Operation Exception !");
    }

    @Override
    public void add(int index, Object element) {
        throw new RuntimeException("Unsupported Operation Exception !");
    }

    @Override
    public List<Object> subList(int fromIndex, int toIndex) {
        throw new RuntimeException("Unsupported Operation Exception !");
    }

    @Override
    public Object remove(int idx) {
        throw new RuntimeException("Unsupported Operation Exception !");
    }

    @Override
    public void clear() {
        throw new RuntimeException("Unsupported Operation Exception !");
    }
}
