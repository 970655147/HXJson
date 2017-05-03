package com.hx.json;

import com.hx.common.str.WordsSeprator;
import com.hx.common.util.InnerTools;
import com.hx.json.config.simple.SimpleJSONConfig;
import com.hx.json.interf.JSON;
import com.hx.json.config.interf.JSONConfig;
import com.hx.json.interf.JSONType;
import com.hx.json.util.JSONConstants;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.*;

/**
 * JSONObject
 *
 * @author Jerry.X.He <970655147@qq.com>
 * @version 1.0
 * @date 4/15/2017 11:50 AM
 */
public class JSONObject implements JSON, Map {

    /**
     * 一个表示空的JSONObject的实例
     */
    public static final JSONObject NULL_JSON_OBJECT = NullJSONObject.getInstance();

    /**
     * 添加元素的时候, 如果元素存在是否覆盖
     */
    public static final boolean PUT_FORCE = true;

    /**
     * 存放各个元素
     */
    Map<String, JSON> eles;

    public JSONObject() {
        eles = new LinkedHashMap<>();
    }

    /**
     * 将给定的Object解析为一个JSONObject
     *
     * @param obj    给定的Object
     * @param config 解析JSONObject的时候的配置
     * @return com.hx.log.json.JSONObject
     * @author Jerry.X.He
     * @date 4/15/2017 12:03 PM
     * @since 1.0
     */
    public static JSONObject fromObject(Object obj, JSONConfig config) {
        if (obj == null) {
            return NULL_JSON_OBJECT;
        }

        if (obj instanceof String) {
            return fromString((String) obj, config);
        } else if (obj instanceof JSONObject) {
            return fromObject((JSONObject) obj, config);
        } else if (obj instanceof Map) {
            return fromMap((Map) obj, config);
        } else {
            return fromBean(obj, config);
        }
    }

    /**
     * 将给定的JSONObject转换为一个实体
     *
     * @param obj    给定的JSONObject
     * @param config 转换的所需的JSONConfig
     * @param clazz  给定的实体的Class
     * @return T
     * @author Jerry.X.He
     * @date 4/16/2017 12:10 PM
     * @since 1.0
     */
    public static <T> T toBean(JSONObject obj, JSONConfig config, Class<T> clazz) {
        return toBean0(obj, config, clazz);
    }

    public static <T> T toBean(JSONObject obj, Class<T> clazz) {
        return toBean(obj, new SimpleJSONConfig(), clazz);
    }

    public static JSONObject fromObject(Object obj) {
        return fromObject(obj, new SimpleJSONConfig());
    }

    @Override
    public JSONType type() {
        return JSONType.OBJECT;
    }

    @Override
    public Object value() {
        return this;
    }

    @Override
    public boolean isArray() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
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
        StringBuilder sb = new StringBuilder();
        JSONParseUtils.toString(this, sb);
        return sb.toString();
    }

    @Override
    public String toString(int indentFactor) {
        StringBuilder sb = new StringBuilder();
        JSONParseUtils.toString(this, indentFactor, 1, sb);
        return sb.toString();
    }

    /**
     * 想当前JSONObject中刚添加一个kv pair
     * 如果元素已经存在, 并且force为false, 则不添加该元素
     *
     * @param key   给定的key
     * @param val   给定的value
     * @param force 是否覆盖已有的元素
     * @return com.hx.log.json.JSONObject
     * @author Jerry.X.He
     * @date 4/15/2017 6:29 PM
     * @since 1.0
     */
    public JSONObject element(String key, Object val, boolean force) {
        put(key, val, force);
        return this;
    }

    public JSONObject element(String key, Object val) {
        return element(key, val, PUT_FORCE);
    }

    public JSONObject element(String key, JSONObject val, boolean force) {
        put(key, val, force);
        return this;
    }

    public JSONObject element(String key, JSONObject val) {
        return element(key, val, PUT_FORCE);
    }

    public JSONObject element(String key, JSONArray val, boolean force) {
        put(key, val, force);
        return this;
    }

    public JSONObject element(String key, JSONArray val) {
        return element(key, val, PUT_FORCE);
    }

    public JSONObject element(String key, String val, boolean force) {
        put(key, val, force);
        return this;
    }

    public JSONObject element(String key, String val) {
        return element(key, val, PUT_FORCE);
    }

    public JSONObject element(String key, boolean val, boolean force) {
        put(key, val, force);
        return this;
    }

    public JSONObject element(String key, boolean val) {
        return element(key, val, PUT_FORCE);
    }

    public JSONObject element(String key, int val, boolean force) {
        put(key, val, force);
        return this;
    }

    public JSONObject element(String key, int val) {
        return element(key, val, PUT_FORCE);
    }

    public JSONObject element(String key, long val, boolean force) {
        put(key, val, force);
        return this;
    }

    public JSONObject element(String key, long val) {
        return element(key, val, PUT_FORCE);
    }


    public JSONObject element(String key, float val, boolean force) {
        put(key, val, force);
        return this;
    }

    public JSONObject element(String key, float val) {
        return element(key, val, PUT_FORCE);
    }

    public JSONObject element(String key, double val, boolean force) {
        put(key, val, force);
        return this;
    }

    public JSONObject element(String key, double val) {
        return element(key, val, PUT_FORCE);
    }

    public Object put(String key, Object val, boolean force) {
        Object result = null;
        if (!(eles.containsKey(key) && !force)) {
            result = put(key, JSONObj.fromObject(val));
        }

        return result;
    }

    public Object put(String key, Object val) {
        return put(key, val, PUT_FORCE);
    }

    public Object put(String key, JSONObject val, boolean force) {
        Object result = null;
        if (!(eles.containsKey(key) && !force)) {
            result = put(key, (JSON) val);
        }

        return result;
    }

    public Object put(String key, JSONObject val) {
        return element(key, val, PUT_FORCE);
    }

    public Object put(String key, JSONArray val, boolean force) {
        Object result = null;
        if (!(eles.containsKey(key) && !force)) {
            result = put(key, (JSON) val);
        }

        return result;
    }

    public Object put(String key, JSONArray val) {
        return element(key, val, PUT_FORCE);
    }

    public Object put(String key, String val, boolean force) {
        Object result = null;
        if (!(eles.containsKey(key) && !force)) {
            result = put(key, JSONStr.fromObject(val));
        }

        return result;
    }

    public Object put(String key, String val) {
        return put(key, val, PUT_FORCE);
    }

    public Object put(String key, boolean val, boolean force) {
        Object result = null;
        if (!(eles.containsKey(key) && !force)) {
            result = put(key, JSONBool.fromObject(val));
        }

        return result;
    }

    public Object put(String key, boolean val) {
        return put(key, val, PUT_FORCE);
    }

    public Object put(String key, int val, boolean force) {
        Object result = null;
        if (!(eles.containsKey(key) && !force)) {
            result = put(key, JSONInt.fromObject(val));
        }

        return result;
    }

    public Object put(String key, int val) {
        return put(key, val, PUT_FORCE);
    }

    public Object put(String key, long val, boolean force) {
        Object result = null;
        if (!(eles.containsKey(key) && !force)) {
            result = put(key, JSONLong.fromObject(val));
        }

        return result;
    }

    public Object put(String key, long val) {
        return put(key, val, PUT_FORCE);
    }


    public Object put(String key, float val, boolean force) {
        Object result = null;
        if (!(eles.containsKey(key) && !force)) {
            result = put(key, JSONFloat.fromObject(val));
        }

        return result;
    }

    public Object put(String key, float val) {
        return put(key, val, PUT_FORCE);
    }

    public Object put(String key, double val, boolean force) {
        Object result = null;
        if (!(eles.containsKey(key) && !force)) {
            result = put(key, JSONDouble.fromObject(val));
        }

        return result;
    }

    public Object put(String key, double val) {
        return put(key, val, PUT_FORCE);
    }

    /**
     * 获取key对应的Object
     * 如果不存在, 或者类型不匹配, 抛出异常
     *
     * @param key 给定的key
     * @return java.lang.Object
     * @author Jerry.X.He
     * @date 4/15/2017 7:06 PM
     * @since 1.0
     */
    public Object get(String key) {
        JSON val = eles.get(key);
        if (val == null) {
            InnerTools.assert0("the key : " + key + " do not exists !");
        }

        return val.value();
    }

    public JSONObject getJSONObject(String key) {
        JSON val = eles.get(key);
        if (val == null || ((JSONType.NULL != val.type()) && (JSONType.OBJECT != val.type()))) {
            InnerTools.assert0("the key : " + key + " do not exists or it does not an JSONObject !");
        }

        return (JSONObject) val.value();
    }

    public JSONArray getJSONArray(String key) {
        JSON val = eles.get(key);
        if (val == null || ((JSONType.NULL != val.type()) && (JSONType.ARRAY != val.type()))) {
            InnerTools.assert0("the key : " + key + " do not exists or it does not an JSONArray !");
        }

        return (JSONArray) val.value();
    }

    public String getString(String key) {
        JSON val = eles.get(key);
        if (val == null) {
            InnerTools.assert0("the key : " + key + " do not exists !");
        }

        return String.valueOf(val.value());
    }

    public boolean getBoolean(String key) {
        JSON val = eles.get(key);
        if (val == null || (JSONType.BOOL != val.type())) {
            InnerTools.assert0("the key : " + key + " do not exists or it does not an boolean !");
        }

        return (Boolean) val.value();
    }

    public int getInt(String key) {
        JSON val = eles.get(key);
        if (val == null || (JSONType.INT != val.type())) {
            InnerTools.assert0("the key : " + key + " do not exists or it does not an int !");
        }

        return (Integer) val.value();
    }

    public long getLong(String key) {
        JSON val = eles.get(key);
        if (val == null || (JSONType.LONG != val.type())) {
            InnerTools.assert0("the key : " + key + " do not exists or it does not an long !");
        }

        return (Long) val.value();
    }

    public float getFloat(String key) {
        JSON val = eles.get(key);
        if (val == null || (JSONType.FLOAT != val.type())) {
            InnerTools.assert0("the key : " + key + " do not exists or it does not an float !");
        }

        return (Float) val.value();
    }

    public double getDouble(String key) {
        JSON val = eles.get(key);
        if (val == null || (JSONType.DOUBLE != val.type())) {
            InnerTools.assert0("the key : " + key + " do not exists or it does not an double !");
        }

        return (Double) val.value();
    }

    /**
     * 获取key对应的Object
     * 如果不存在, 或者类型不匹配, 返回默认结果
     *
     * @param key 给定的key
     * @return java.lang.Object
     * @author Jerry.X.He
     * @date 4/15/2017 7:06 PM
     * @since 1.0
     */
    public Object opt(String key) {
        return opt(key, null);
    }

    public JSONObject optJSONObject(String key) {
        return optJSONObject(key, null);
    }

    public JSONArray optJSONArray(String key) {
        return optJSONArray(key, null);
    }

    public String optString(String key) {
        return optString(key, null);
    }

    public boolean optBoolean(String key) {
        return optBoolean(key, false);
    }

    public int optInt(String key) {
        return optInt(key, 0);
    }

    public long optLong(String key) {
        return optLong(key, 0L);
    }

    public float optFloat(String key) {
        return optFloat(key, 0F);
    }

    public double optDouble(String key) {
        return optDouble(key, 0D);
    }

    public Object opt(String key, Object defaultValue) {
        JSON val = eles.get(key);
        if (val == null) {
            return defaultValue;
        }

        return val.value();
    }

    public JSONObject optJSONObject(String key, JSONObject defaultValue) {
        JSON val = eles.get(key);
        if (val == null || ((JSONType.NULL != val.type()) && (JSONType.OBJECT != val.type()))) {
            return defaultValue;
        }

        return (JSONObject) val.value();
    }

    public JSONArray optJSONArray(String key, JSONArray defaultValue) {
        JSON val = eles.get(key);
        if (val == null || ((JSONType.NULL != val.type()) && (JSONType.ARRAY != val.type()))) {
            return defaultValue;
        }

        return (JSONArray) val.value();
    }

    public String optString(String key, String defaultValue) {
        JSON val = eles.get(key);
        if (val == null) {
            return defaultValue;
        }

        return String.valueOf(val.value());
    }

    public boolean optBoolean(String key, boolean defaultValue) {
        JSON val = eles.get(key);
        if (val == null || (JSONType.BOOL != val.type())) {
            return defaultValue;
        }

        return (Boolean) val.value();
    }

    public int optInt(String key, int defaultValue) {
        JSON val = eles.get(key);
        if (val == null || (JSONType.INT != val.type())) {
            return defaultValue;
        }

        return (Integer) val.value();
    }

    public long optLong(String key, long defaultValue) {
        JSON val = eles.get(key);
        if (val == null || (JSONType.LONG != val.type())) {
            return defaultValue;
        }

        return (Long) val.value();
    }

    public float optFloat(String key, float defaultValue) {
        JSON val = eles.get(key);
        if (val == null || (JSONType.FLOAT != val.type())) {
            return defaultValue;
        }

        return (Float) val.value();
    }

    public double optDouble(String key, double defaultValue) {
        JSON val = eles.get(key);
        if (val == null || (JSONType.DOUBLE != val.type())) {
            return defaultValue;
        }

        return (Double) val.value();
    }

    /**
     * 获取当前JSONObject的所有的key的集合的iterator
     *
     * @return com.hx.log.json.interf.JSON
     * @author Jerry.X.He
     * @date 4/15/2017 6:44 PM
     * @since 1.0
     */
    public Iterator<String> keys() {
        return keySet().iterator();
    }

    /**
     * 获取当前JSONObject的所有的key的集合
     *
     * @return com.hx.log.json.interf.JSON
     * @author Jerry.X.He
     * @date 4/15/2017 6:44 PM
     * @since 1.0
     */
    public Set<String> keySet() {
        return eles.keySet();
    }

    /**
     * 获取当前JSONObject的所有的key的集合, 存放于结果的JSONArray中
     *
     * @return com.hx.json.JSONArray
     * @author Jerry.X.He
     * @date 4/16/2017 1:51 PM
     * @since 1.0
     */
    public JSONArray names() {
        JSONArray result = new JSONArray();
        result.addAll(eles.keySet());
        return result;
    }

    @Override
    public boolean containsKey(Object key) {
        return eles.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        for (Entry<String, JSON> entry : eles.entrySet()) {
            if (Objects.equals(entry.getValue().value(), value)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Object get(Object key) {
        return opt(String.valueOf(key));
    }

    @Override
    public Object remove(Object key) {
        return eles.remove(key);
    }

    @Override
    public Object put(Object key, Object value) {
        return put(String.valueOf(key), value);
    }

    @Override
    public void putAll(Map m) {
        for (Object key : m.keySet()) {
            put(String.valueOf(key), m.get(key));
        }
    }

    @Override
    public Collection<Object> values() {
        Collection<Object> result = new ArrayList<>(size());
        for (Entry<String, JSON> entry : eles.entrySet()) {
            result.add(entry.getValue().value());
        }
        return result;
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        Set<Entry<String, Object>> result = new LinkedHashSet<>(size());
        for (Entry<String, JSON> entry : eles.entrySet()) {
            result.add(new MapEentry<>(entry.getKey(), entry.getValue().value()));
        }
        return result;
    }

    /**
     * 移除当前JSONObject中key对应的条目
     *
     * @param key 给定的key
     * @return com.hx.log.json.interf.JSON
     * @author Jerry.X.He
     * @date 4/15/2017 6:44 PM
     * @since 1.0
     */
    public Object remove(String key) {
        JSON removed = eles.remove(key);
        if (removed == null) {
            return null;
        }

        return removed.value();
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
    private static class MapEentry<K, V> implements Map.Entry<K, V> {
        /**
         * key & value
         */
        private K key;
        private V value;

        public MapEentry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            throw new RuntimeException("Unsupported Operation Exception !");
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
    public static JSONObject fromString(WordsSeprator sep, JSONConfig config, boolean checkEnd) {
        if(sep == null) {
            return NULL_JSON_OBJECT;
        }
        InnerTools.assert0(JSONConstants.OBJ_START.equals(sep.next()), "expect a : " + JSONConstants.OBJ_START + " ! around : " + sep.currentAndRest());
        JSONObject result = new JSONObject();

        if (!JSONConstants.OBJ_END.equals(sep.seek())) {
            while (sep.hasNext()) {
                String nextKey = sep.next().trim();
                InnerTools.assert0(
                        (nextKey.startsWith(JSONConstants.STR_SEP01) && nextKey.endsWith(JSONConstants.STR_SEP01))
                                ||
                                (nextKey.startsWith(JSONConstants.STR_SEP02) && nextKey.endsWith(JSONConstants.STR_SEP02)),
                        "bad key format around : " + sep.currentAndRest()
                );
                InnerTools.assert0(JSONConstants.KV_SEP.equals(sep.next()), "expect a : " + JSONConstants.KV_SEP + " ! around : " + sep.currentAndRest());
                nextKey = JSONParseUtils.trimForSurroundSep(nextKey, JSONConstants.KEY_SEPS);
                JSON nextValue = JSONParseUtils.getNextValue(sep, nextKey, config);
                result.eles.put(nextKey, nextValue);

                if (JSONConstants.OBJ_END.equals(sep.seek())) {
                    break;
                }
                InnerTools.assert0(JSONConstants.ELE_SEP.equals(sep.next()), "expect a : " + JSONConstants.ELE_SEP + " ! around : " + sep.currentAndRest());
            }
        }

        // skip '}'
        sep.next();
        if (checkEnd) {
            InnerTools.assert0(InnerTools.isEmpty(sep.next()), "expect nothing after '}' !");
        }
        return result;
    }

    public static JSONObject fromString(String str, JSONConfig config) {
        if(str == null) {
            return NULL_JSON_OBJECT;
        }

        WordsSeprator sep = new WordsSeprator(str, JSONConstants.JSON_SEPS, JSONConstants.NEED_TO_ESCAPE, true, false);
        return fromString(sep, config, true);
    }

    /**
     * 从给定的JSONObject中解析一个JSONObject [copy]
     *
     * @param obj    给定的JSONObject
     * @param config 解析json的config
     * @return com.hx.json.JSONObject
     * @author Jerry.X.He
     * @date 4/30/2017 1:22 PM
     * @since 1.0
     */
    public static JSONObject fromObject(JSONObject obj, JSONConfig config) {
        if(obj == null) {
            return NULL_JSON_OBJECT;
        }

        JSONObject result = new JSONObject();
        for (Entry<String, JSON> entry : obj.eles.entrySet()) {
            String key = entry.getKey();
            switch (entry.getValue().type()) {
                case BOOL:
                    result.put(entry.getKey(), obj.optBoolean(key));
                    break;
                case INT:
                    result.put(entry.getKey(), obj.optInt(key));
                    break;
                case LONG:
                    result.put(entry.getKey(), obj.optLong(key));
                    break;
                case FLOAT:
                    result.put(entry.getKey(), obj.optFloat(key));
                    break;
                case DOUBLE:
                    result.put(entry.getKey(), obj.optDouble(key));
                    break;
                case STR:
                    result.put(entry.getKey(), obj.optString(key));
                    break;
                case OBJ:
                    result.put(entry.getKey(), obj.opt(key));
                    break;
                case NULL:
                    result.put(entry.getKey(), JSONNull.getInstance());
                    break;
                case OBJECT:
                    result.put(entry.getKey(), JSONObject.fromObject(obj.optJSONObject(key), config));
                    break;
                case ARRAY:
                    result.put(entry.getKey(), JSONArray.fromObject(obj.optJSONArray(key), config));
                    break;
            }
        }

        return result;
    }

    /**
     * 从给定的Map中解析一个JSONObject
     *
     * @param map    给定的map
     * @param config 解析json的config
     * @return com.hx.json.JSONObject
     * @author Jerry.X.He
     * @date 4/30/2017 1:22 PM
     * @since 1.0
     */
    public static JSONObject fromMap(Map map, JSONConfig config) {
        if(map == null) {
            return NULL_JSON_OBJECT;
        }

        JSONObject result = new JSONObject();
        for (Object key : map.keySet()) {
            result.put(String.valueOf(key), JSONParseUtils.fromBean(map.get(key), config));
        }
        return result;
    }

    /**
     * 从给定的Object中解析JSONObject
     *
     * @param obj    给定的object
     * @param config 解析object的配置
     * @return com.hx.log.json.JSONObject
     * @author Jerry.X.He
     * @date 4/16/2017 11:49 AM
     * @since 1.0
     */
    public static JSONObject fromBean(Object obj, JSONConfig config) {
        if(obj == null) {
            return NULL_JSON_OBJECT;
        }
        Class clazz = obj.getClass();
        int clazzModifier = clazz.getModifiers();
        if ((!Modifier.isPublic(clazzModifier)) || (Modifier.isAbstract(clazzModifier))) {
            return NULL_JSON_OBJECT;
        }

        JSONObject result = new JSONObject();
        Method[] methods = clazz.getDeclaredMethods();
        try {
            for (Method method : methods) {
                String methodName = method.getName();
                int modifier = method.getModifiers();

                if (JSONParseUtils.startsWith(methodName, JSONConstants.BEAN_GETTER_PREFIXES)
                        && (Modifier.isPublic(modifier) && (!Modifier.isStatic(modifier)) )
                        && (method.getParameterTypes().length == 0)) {
                    Object invokeResult = method.invoke(obj);
                    String key = JSONParseUtils.getKeyForGetter(clazz, methodName, config);
                    if(invokeResult == null) {
                        result.put(key, JSONNull.getInstance());
                        continue ;
                    }

                    Class resultClazz = invokeResult.getClass();
                    if ((boolean.class == resultClazz) || (Boolean.class == resultClazz)) {
                        result.put(key, Boolean.valueOf(invokeResult.toString()).booleanValue());
                    } else if (((int.class == resultClazz) || (Integer.class == resultClazz))
                            || ((byte.class == resultClazz) || (Byte.class == resultClazz))
                            || ((short.class == resultClazz) || (Short.class == resultClazz))
                            ) {
                        result.put(key, Integer.valueOf(invokeResult.toString()).intValue());
                    } else if ((long.class == resultClazz) || (Long.class == resultClazz)) {
                        result.put(key, Long.valueOf(invokeResult.toString()).longValue());
                    } else if ((float.class == resultClazz) || (Float.class == resultClazz)) {
                        result.put(key, Float.valueOf(invokeResult.toString()).floatValue());
                    } else if ((double.class == resultClazz) || (Double.class == resultClazz)) {
                        result.put(key, Double.valueOf(invokeResult.toString()).doubleValue());
                    } else if (String.class == resultClazz) {
                        result.put(key, String.valueOf(invokeResult));
                    } else {
                        result.put(key, JSONParseUtils.fromBean(invokeResult, config));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return NULL_JSON_OBJECT;
        }

        return result;
    }

    /**
     * 向当前JSONObject中增加一个kv pair
     *
     * @param key   给定的key
     * @param value 给定的JSON
     * @return java.lang.Object
     * @author Jerry.X.He
     * @date 5/1/2017 2:04 AM
     * @since 1.0
     */
    protected Object put(String key, JSON value) {
        value = JSONParseUtils.normalizeJSON(value);
        Object result = eles.put(key, value);
        return result;
    }

    /**
     * 将给定的JSONObject转换为一个实体
     *
     * @param obj    给定的JSONObject
     * @param config 转换的所需的JSONConfig
     * @param clazz  给定的实体的Class
     * @return T
     * @author Jerry.X.He
     * @date 4/16/2017 12:10 PM
     * @since 1.0
     */
    static <T> T toBean0(JSONObject obj, JSONConfig config, Class<T> clazz) {
        if ((obj == null) || (clazz == null)) {
            return null;
        }
        int clazzModifier = clazz.getModifiers();
        if ((!Modifier.isPublic(clazzModifier)) || (Modifier.isAbstract(clazzModifier))) {
            return null;
        }

        Method[] methods = clazz.getDeclaredMethods();
        try {
            T result = clazz.newInstance();
            for (Method method : methods) {
                String methodName = method.getName();
                int modifier = method.getModifiers();

                if (JSONParseUtils.startsWith(methodName, JSONConstants.BEAN_SETTER_PREFIXES)
                        && (Modifier.isPublic(modifier) && (!Modifier.isStatic(modifier) && (!Modifier.isAbstract(modifier))))
                        && (method.getParameterTypes().length == 1)) {
                    String key = JSONParseUtils.getKeyForSetter(clazz, methodName, config);

                    Class argClazz = method.getParameterTypes()[0];
                    if ((boolean.class == argClazz) || (Boolean.class == argClazz)) {
                        method.invoke(result, obj.optBoolean(key));
                    } else if (((int.class == argClazz) || (Integer.class == argClazz))
                            || ((byte.class == argClazz) || (Byte.class == argClazz))
                            || ((short.class == argClazz) || (Short.class == argClazz))
                            ) {
                        method.invoke(result, obj.optInt(key));
                    } else if ((long.class == argClazz) || (Long.class == argClazz)) {
                        method.invoke(result, obj.optLong(key));
                    } else if ((float.class == argClazz) || (Float.class == argClazz)) {
                        method.invoke(result, obj.optFloat(key));
                    } else if ((double.class == argClazz) || (Double.class == argClazz)) {
                        method.invoke(result, obj.optDouble(key));
                    } else if (String.class == argClazz) {
                        method.invoke(result, obj.optString(key));
                    } else {
                        if (obj.containsKey(key)) {
                            Type paramType = method.getGenericParameterTypes()[0];
                            Object valueObj = JSONParseUtils.toBean(obj.opt(key), config, paramType);
                            method.invoke(result, argClazz.cast(valueObj));
                        }
                    }
                }
            }

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
