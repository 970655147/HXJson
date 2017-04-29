package com.hx.json;

import com.hx.json.interf.JSON;
import com.hx.json.interf.JSONConfig;
import com.hx.json.interf.JSONType;
import com.hx.json.util.JSONConstants;
import com.hx.log.str.WordsSeprator;
import com.hx.log.util.Tools;

import java.util.*;

/**
 * JSONObject
 *
 * @author Jerry.X.He <970655147@qq.com>
 * @version 1.0
 * @date 4/15/2017 11:50 AM
 */
public class JSONArray implements JSON, List, RandomAccess {

    /**
     * 一个表示空的JSONArray的实例
     */
    public static final JSONArray NULL_JSON_ARRAY = NullJSONArray.getInstance();

    /**
     * 存储当前Array的所有元素
     */
    List<JSON> eles;

    public JSONArray() {
        eles = new ArrayList<>();
    }

    /**
     * 将给定的Object解析为一个JSONArray
     *
     * @param obj    给定的Object
     * @param config 解析JSONObject的时候的配置
     * @return com.hx.log.json.JSONObject
     * @author Jerry.X.He
     * @date 4/15/2017 12:03 PM
     * @since 1.0
     */
    public static JSONArray fromObject(Object obj, JSONConfig config) {
        if (obj == null) {
            return NULL_JSON_ARRAY;
        }

        if (obj instanceof String) {
            return fromString((String) obj, config);
        } else if (obj instanceof Collection) {
            return fromCollection((Collection) obj, config);
        } else if (obj.getClass().isArray()) {
            return fromArray(obj, config, obj.getClass());
        } else {
            return NULL_JSON_ARRAY;
        }
    }

    public static JSONArray fromObject(Object obj) {
        return fromObject(obj, new SimpleJSONConfig());
    }

    @Override
    public JSONType type() {
        return JSONType.ARRAY;
    }

    @Override
    public Object value() {
        return this;
    }

    @Override
    public boolean isArray() {
        return true;
    }

    @Override
    public boolean isEmpty() {
        return eles.isEmpty();
    }

    @Override
    public boolean isNull() {
        return false;
    }

    @Override
    public int size() {
        return eles.size();
    }

    @Override
    public String toString() {
        int sz = size();
        StringBuilder sb = new StringBuilder(sz > 0 ? (sz * String.valueOf(optString(0)).length()) : 2);
        JSONParseUtils.toString(this, sb);
        return sb.toString();
    }

    @Override
    public String toString(int indentFactor) {
        StringBuilder sb = new StringBuilder(size() * String.valueOf(optString(0)).length());
        JSONParseUtils.toString(this, indentFactor, 1, sb);
        return sb.toString();
    }

    /**
     * 想当前JSONArray中刚添加一个元素
     *
     * @param val 给定的value
     * @return com.hx.log.json.JSONObject
     * @author Jerry.X.He
     * @date 4/15/2017 6:29 PM
     * @since 1.0
     */
    public JSONArray element(Object val) {
        add(val);
        return this;
    }

    public JSONArray element(JSONObject val) {
        add(val);
        return this;
    }

    public JSONArray element(JSONArray val) {
        add(val);
        return this;
    }

    public JSONArray element(String val) {
        add(val);
        return this;
    }

    public JSONArray element(boolean val) {
        add(val);
        return this;
    }

    public JSONArray element(int val) {
        add(val);
        return this;
    }

    public JSONArray element(long val) {
        add(val);
        return this;
    }

    public JSONArray element(float val) {
        add(val);
        return this;
    }

    public JSONArray element(double val) {
        add(val);
        return this;
    }

    @Override
    public boolean add(Object val) {
        eles.add(JSONObj.fromObject(val));
        return true;
    }

    public void add(JSONObject val) {
        eles.add(val);
    }

    public void add(JSONArray val) {
        eles.add(val);
    }

    public void add(String val) {
        eles.add(JSONStr.fromObject(val));
    }

    public void add(boolean val) {
        eles.add(JSONBool.fromObject(val));
    }

    public void add(int val) {
        eles.add(JSONInt.fromObject(val));
    }

    public void add(long val) {
        eles.add(JSONLong.fromObject(val));
    }

    public void add(float val) {
        eles.add(JSONFloat.fromObject(val));
    }

    public void add(double val) {
        eles.add(JSONDouble.fromObject(val));
    }

    /**
     * 获取key对应的Object
     * 如果不存在, 或者类型不匹配, 抛出异常
     *
     * @param idx 给定的索引
     * @return java.lang.Object
     * @author Jerry.X.He
     * @date 4/15/2017 7:06 PM
     * @since 1.0
     */
    public Object get(int idx) {
        JSON val = eles.get(idx);
        if (val == null) {
            Tools.assert0("the element of [" + idx + "] do not exists !");
        }

        return val.value();
    }

    public JSONObject getJSONObject(int idx) {
        JSON val = eles.get(idx);
        if (val == null || (val.type() != JSONType.OBJECT)) {
            Tools.assert0("the element of [" + idx + "] do not exists or it does not an JSONObject !");
        }

        return (JSONObject) val.value();
    }

    public JSONArray getJSONArray(int idx) {
        JSON val = eles.get(idx);
        if (val == null || (val.type() != JSONType.OBJECT)) {
            Tools.assert0("the element of [" + idx + "] do not exists or it does not an JSONArray !");
        }

        return (JSONArray) val.value();
    }


    public String getString(int idx) {
        JSON val = eles.get(idx);
        if (val == null) {
            Tools.assert0("the element of [" + idx + "] do not exists !");
        }

        return String.valueOf(val.value());
    }

    public boolean getBoolean(int idx) {
        JSON val = eles.get(idx);
        if (val == null || (val.type() != JSONType.BOOL)) {
            Tools.assert0("the element of [" + idx + "] do not exists or it does not an boolean !");
        }

        return (Boolean) val.value();
    }

    public int getInt(int idx) {
        JSON val = eles.get(idx);
        if (val == null || (val.type() != JSONType.INT)) {
            Tools.assert0("the element of [" + idx + "] do not exists or it does not an int !");
        }

        return (Integer) val.value();
    }

    public long getLong(int idx) {
        JSON val = eles.get(idx);
        if (val == null || (val.type() != JSONType.LONG)) {
            Tools.assert0("the element of [" + idx + "] do not exists or it does not an long !");
        }

        return (Long) val.value();
    }

    public float getFloat(int idx) {
        JSON val = eles.get(idx);
        if (val == null || (val.type() != JSONType.FLOAT)) {
            Tools.assert0("the element of [" + idx + "] do not exists or it does not an float !");
        }

        return (Float) val.value();
    }

    public double getDouble(int idx) {
        JSON val = eles.get(idx);
        if (val == null || (val.type() != JSONType.DOUBLE)) {
            Tools.assert0("the element of [" + idx + "] do not exists or it does not an double !");
        }

        return (Double) val.value();
    }

    /**
     * 获取key对应的Object
     * 如果不存在, 或者类型不匹配, 返回默认结果
     *
     * @param idx 给定的索引
     * @return java.lang.Object
     * @author Jerry.X.He
     * @date 4/15/2017 7:06 PM
     * @since 1.0
     */
    public Object opt(int idx) {
        return opt(idx, null);
    }

    public JSONObject optJSONObject(int idx) {
        return optJSONObject(idx, null);
    }

    public JSONArray optJSONArray(int idx) {
        return optJSONArray(idx, null);
    }

    public String optString(int idx) {
        return optString(idx, null);
    }

    public boolean optBoolean(int idx) {
        return optBoolean(idx, false);
    }

    public int optInt(int idx) {
        return optInt(idx, 0);
    }

    public long optLong(int idx) {
        return optLong(idx, 0L);
    }

    public float optFloat(int idx) {
        return optFloat(idx, 0F);
    }

    public double optDouble(int idx) {
        return optDouble(idx, 0D);
    }

    public Object opt(int idx, Object defaultValue) {
        JSON val = eles.get(idx);
        if (val == null) {
            return defaultValue;
        }

        return val.value();
    }

    public JSONObject optJSONObject(int idx, JSONObject defaultValue) {
        JSON val = eles.get(idx);
        if (val == null || (val.type() != JSONType.OBJECT)) {
            return defaultValue;
        }

        return (JSONObject) val.value();
    }

    public JSONArray optJSONArray(int idx, JSONArray defaultValue) {
        JSON val = eles.get(idx);
        if (val == null || (val.type() != JSONType.OBJECT)) {
            return defaultValue;
        }

        return (JSONArray) val.value();
    }

    public String optString(int idx, String defaultValue) {
        JSON val = eles.get(idx);
        if (val == null) {
            return defaultValue;
        }

        return String.valueOf(val.value());
    }

    public boolean optBoolean(int idx, boolean defaultValue) {
        JSON val = eles.get(idx);
        if (val == null || (val.type() != JSONType.BOOL)) {
            return defaultValue;
        }

        return (Boolean) val.value();
    }

    public int optInt(int idx, int defaultValue) {
        JSON val = eles.get(idx);
        if (val == null || (val.type() != JSONType.INT)) {
            return defaultValue;
        }

        return (Integer) val.value();
    }

    public long optLong(int idx, long defaultValue) {
        JSON val = eles.get(idx);
        if (val == null || (val.type() != JSONType.LONG)) {
            return defaultValue;
        }

        return (Long) val.value();
    }

    public float optFloat(int idx, float defaultValue) {
        JSON val = eles.get(idx);
        if (val == null || (val.type() != JSONType.FLOAT)) {
            return defaultValue;
        }

        return (Float) val.value();
    }

    public double optDouble(int idx, double defaultValue) {
        JSON val = eles.get(idx);
        if (val == null || (val.type() != JSONType.DOUBLE)) {
            return defaultValue;
        }

        return (Double) val.value();
    }

    @Override
    public boolean contains(Object o) {
        for (Object ele : this) {
            if (Objects.equals(ele, o)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Iterator<Object> iterator() {
        return new InternalIterator(eles.iterator());
    }

    @Override
    public Object[] toArray() {
        Object[] res = new Object[size()];
        int idx = 0;
        for (Object ele : this) {
            res[idx++] = ele;
        }
        return res;
    }

    @Override
    public Object[] toArray(Object[] a) {
        throw new RuntimeException("Unsupported Operation Exception !");
    }

    @Override
    public boolean addAll(Collection c) {
        for (Object ele : c) {
            add(ele);
        }
        return true;
    }

    @Override
    public boolean addAll(int index, Collection c) {
        List<JSON> collected = new ArrayList<>(c.size());
        for (Object ele : c) {
            collected.add(JSONObj.fromObject(ele));
        }
        return eles.addAll(index, collected);
    }

    @Override
    public boolean remove(Object o) {
        Iterator<Object> ite = iterator();
        while (ite.hasNext()) {
            if (Objects.equals(ite.next(), o)) {
                ite.remove();
            }
        }

        return true;
    }

    @Override
    public boolean containsAll(Collection c) {
        throw new RuntimeException("Unsupported Operation Exception !");
    }

    @Override
    public boolean removeAll(Collection c) {
        for (Object ele : c) {
            remove(ele);
        }
        return true;
    }

    @Override
    public boolean retainAll(Collection c) {
        boolean modified = false;
        Iterator<Object> ite = iterator();
        while (ite.hasNext()) {
            if (!c.contains(ite.next())) {
                ite.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public Object set(int index, Object element) {
        return eles.set(index, JSONObj.fromObject(element));
    }

    @Override
    public void add(int index, Object element) {
        eles.add(index, JSONObj.fromObject(element));
    }

    @Override
    public int indexOf(Object o) {
        int idx = 0;
        for (Object ele : this) {
            if (Objects.equals(ele, o)) {
                return idx;
            }
            idx++;
        }

        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    @Override
    public ListIterator<Object> listIterator() {
        return new InternalListIterator(eles.listIterator());
    }

    @Override
    public ListIterator<Object> listIterator(int index) {
        return new InternalListIterator(eles.listIterator(index));
    }

    @Override
    public List<Object> subList(int fromIndex, int toIndex) {
        throw new RuntimeException("Unsupported Operation Exception !");
    }

    /**
     * 移除当前JSONObject中key对应的条目
     *
     * @param idx 给定的索引
     * @return com.hx.log.json.interf.JSON
     * @author Jerry.X.He
     * @date 4/15/2017 6:44 PM
     * @since 1.0
     */
    public Object remove(int idx) {
        return eles.remove(idx).value();
    }

    /**
     * 移除当前JSONObject中所有的条目
     *
     * @return com.hx.log.json.interf.JSON
     * @author Jerry.X.He
     * @date 4/15/2017 6:44 PM
     * @since 1.0
     */
    public void clear() {
        eles.clear();
    }

    // ----------------- 辅助数据结构 -----------------------

    /**
     * Iterator
     *
     * @author Jerry.X.He <970655147@qq.com>
     * @version 1.0
     * @date 4/15/2017 10:30 PM
     */
    private static class InternalIterator implements Iterator<Object> {
        Iterator<JSON> ite;

        public InternalIterator(Iterator<JSON> ite) {
            this.ite = ite;
        }

        @Override
        public boolean hasNext() {
            return ite.hasNext();
        }

        @Override
        public Object next() {
            JSON next = ite.next();
            return (next != null) ? next.value() : null;
        }

        @Override
        public void remove() {
            ite.remove();
        }
    }

    /**
     * ListIterator
     *
     * @author Jerry.X.He <970655147@qq.com>
     * @version 1.0
     * @date 4/15/2017 10:30 PM
     */
    private static class InternalListIterator implements ListIterator<Object> {
        ListIterator<JSON> ite;

        public InternalListIterator(ListIterator<JSON> ite) {
            this.ite = ite;
        }

        @Override
        public boolean hasNext() {
            return ite.hasNext();
        }

        @Override
        public Object next() {
            JSON res = ite.next();
            return (res != null) ? res.value() : null;
        }

        @Override
        public boolean hasPrevious() {
            return ite.hasPrevious();
        }

        @Override
        public Object previous() {
            JSON res = ite.previous();
            return (res != null) ? res.value() : null;
        }

        @Override
        public int nextIndex() {
            return ite.nextIndex();
        }

        @Override
        public int previousIndex() {
            return ite.previousIndex();
        }

        @Override
        public void remove() {
            ite.remove();
        }

        @Override
        public void set(Object o) {
            ite.set(JSONObj.fromObject(o));
        }

        @Override
        public void add(Object o) {
            ite.add(JSONObj.fromObject(o));
        }
    }

    // ----------------- 辅助方法 -----------------------

    /**
     * 解析给定的seprator的剩余的部分, 将其解析为一个JSONObject
     *
     * @param sep 给定的seprator
     * @return com.hx.log.json.JSONObject
     * @author Jerry.X.He
     * @date 4/15/2017 5:32 PM
     * @since 1.0
     */
    static JSONArray fromString(WordsSeprator sep, JSONConfig config, boolean checkEnd) {
        Tools.assert0(JSONConstants.ARR_START.equals(sep.next()), "expect a : " + JSONConstants.ARR_START + " ! around : " + sep.currentAndRest());
        JSONArray result = new JSONArray();

        int idx = 0;
        if (!JSONConstants.ARR_END.equals(sep.seek())) {
            while (sep.hasNext()) {
                JSON nextValue = JSONParseUtils.getNextValue(sep, "[" + idx + "]", config);
                result.eles.add(nextValue);

                if (JSONConstants.ARR_END.equals(sep.seek())) {
                    break;
                }
                Tools.assert0(JSONConstants.ELE_SEP.equals(sep.next()), "expect a : " + JSONConstants.ELE_SEP + " ! around : " + sep.currentAndRest());
                idx++;
            }
        }

        // skip ']'
        sep.next();
        if (checkEnd) {
            Tools.assert0(Tools.isEmpty(sep.next()), "expect nothing after ']' !");
        }
        return result;
    }

    private static JSONArray fromString(String str, JSONConfig config) {
        WordsSeprator sep = new WordsSeprator(str, JSONConstants.JSON_SEPS, JSONConstants.NEED_TO_ESCAPE, true, false);
        return fromString(sep, config, true);
    }

    /**
     * 从给定的Collection中提取JSONArray
     *
     * @param coll   给定的集合
     * @param config 解析json的config
     * @return com.hx.json.JSONArray
     * @author Jerry.X.He
     * @date 4/29/2017 1:54 PM
     * @since 1.0
     */
    private static JSONArray fromCollection(Collection coll, JSONConfig config) {
        JSONArray result = new JSONArray();
        result.addAll(coll);
        return result;
    }

    /**
     * 从给定的数组对象中提取JSONArray
     *
     * @param obj      给定的数组对象
     * @param config   解析json的config
     * @param argClazz 数组类型
     * @return com.hx.json.JSONArray
     * @author Jerry.X.He
     * @date 4/29/2017 1:46 PM
     * @since 1.0
     */
    private static JSONArray fromArray(Object obj, JSONConfig config, Class argClazz) {
        JSONArray result = new JSONArray();
        if ((boolean[].class == argClazz) || (Boolean[].class == argClazz)) {
            if (boolean[].class == argClazz) {
                boolean[] arr = (boolean[]) obj;
                for (boolean ele : arr) {
                    result.add(ele);
                }
            } else if (Boolean[].class == argClazz) {
                Boolean[] arr = (Boolean[]) obj;
                for (Boolean ele : arr) {
                    result.add(ele);
                }
            }
        } else if (((int[].class == argClazz) || (byte[].class == argClazz) || (short[].class == argClazz))
                || ((Integer[].class == argClazz) || (Byte[].class == argClazz) || (Short[].class == argClazz))
                ) {
            if (int[].class == argClazz) {
                int[] arr = (int[]) obj;
                for (int ele : arr) {
                    result.add(ele);
                }
            } else if (byte[].class == argClazz) {
                byte[] arr = (byte[]) obj;
                for (byte ele : arr) {
                    result.add(ele);
                }
            } else if (short[].class == argClazz) {
                short[] arr = (short[]) obj;
                for (short ele : arr) {
                    result.add(ele);
                }
            } else if (Integer[].class == argClazz) {
                Integer[] arr = (Integer[]) obj;
                for (Integer ele : arr) {
                    result.add(ele);
                }
            } else if (Byte[].class == argClazz) {
                Byte[] arr = (Byte[]) obj;
                for (Byte ele : arr) {
                    result.add(ele);
                }
            } else if (Short[].class == argClazz) {
                Short[] arr = (Short[]) obj;
                for (Short ele : arr) {
                    result.add(ele);
                }
            }
        } else if ((long[].class == argClazz) || (Long[].class == argClazz)) {
            if (long[].class == argClazz) {
                long[] arr = (long[]) obj;
                for (long ele : arr) {
                    result.add(ele);
                }
            } else if (Long[].class == argClazz) {
                Long[] arr = (Long[]) obj;
                for (Long ele : arr) {
                    result.add(ele);
                }
            }
        } else if ((float[].class == argClazz) || (Float[].class == argClazz)) {
            if (float[].class == argClazz) {
                float[] arr = (float[]) obj;
                for (float ele : arr) {
                    result.add(ele);
                }
            } else if (Float[].class == argClazz) {
                Float[] arr = (Float[]) obj;
                for (Float ele : arr) {
                    result.add(ele);
                }
            }
        } else if ((double[].class == argClazz) || (Double[].class == argClazz)) {
            if (double[].class == argClazz) {
                double[] arr = (double[]) obj;
                for (double ele : arr) {
                    result.add(ele);
                }
            } else if (Double[].class == argClazz) {
                Double[] arr = (Double[]) obj;
                for (Double ele : arr) {
                    result.add(ele);
                }
            }
        } else {
            Object[] arr = (Object[]) obj;
            for (Object ele : arr) {
                result.add(ele);
            }
        }

        return result;
    }

}
