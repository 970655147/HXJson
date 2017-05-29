package com.hx.json.config.simple;

import com.hx.common.interf.cache.Cache;
import com.hx.common.util.InnerTools;
import com.hx.json.JSONParseUtils;
import com.hx.json.config.interf.JSONConfig;
import com.hx.json.config.interf.JSONKeyNodeParser;
import com.hx.json.interf.JSONField;
import com.hx.json.util.JSONConstants;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * JSONFieldKeyNodeParser
 *
 * @author Jerry.X.He <970655147@qq.com>
 * @version 1.0
 * @date 5/1/2017 6:10 PM
 */
public class JSONFieldKeyNodeParser implements JSONKeyNodeParser {

    private static Map<Integer, JSONFieldKeyNodeParser> cache = new HashMap<>();

    /**
     * 默认的索引
     */
    public static int DEFAULT_IDX = 0;

    /**
     * 拿给定的JSONField的key的索引
     */
    private int idx;

    /**
     * 获取 JSONKeyFieldNodeParser 的接口
     *
     * @param idx idx
     * @return com.hx.json.config.simple.JSONFieldKeyNodeParser
     * @author Jerry.X.He
     * @date 5/29/2017 9:31 AM
     * @since 1.0
     */
    public static JSONFieldKeyNodeParser of(int idx) {
        JSONFieldKeyNodeParser result = cache.get(idx);
        if(result != null) {
            return result;
        }

        result = new JSONFieldKeyNodeParser(idx);
        cache.put(idx, result);
        return result;
    }

    public static JSONFieldKeyNodeParser of() {
        return of(0);
    }

    /**
     * 初始化
     *
     * @param idx idx
     * @since 1.0
     */
    private JSONFieldKeyNodeParser(int idx) {
        this.idx = idx;
    }

    @Override
    public String getKeyForGetter(Object obj, Class clazz, String getterMethodName, JSONConfig config) {
        String fieldName = JSONParseUtils.trimIfStartsWith(getterMethodName, JSONConstants.BEAN_GETTER_PREFIXES);
        fieldName = InnerTools.lowerCaseFirstChar(fieldName);

        return getKeyForField(clazz, fieldName, config);
    }

    @Override
    public String getKeyForSetter(Object obj, Class clazz, String setterMethodName, JSONConfig config) {
        String fieldName = JSONParseUtils.trimIfStartsWith(setterMethodName, JSONConstants.BEAN_SETTER_PREFIXES);
        fieldName = InnerTools.lowerCaseFirstChar(fieldName);

        return getKeyForField(clazz, fieldName, config);
    }

    /**
     * 获取class中fieldName对应的字段在JSON解析中的key
     *
     * @param clazz     给定的className
     * @param fieldName 给定的字段名称
     * @param config    解析JSON的config
     * @return java.lang.String
     * @author Jerry.X.He
     * @date 5/1/2017 6:24 PM
     * @since 1.0
     */
    private String getKeyForField(Class clazz, String fieldName, JSONConfig config) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            JSONField fieldAnno = field.getAnnotation(JSONField.class);
            if (fieldAnno != null) {
                String[] keys = fieldAnno.value();
                return (idx < keys.length) ? keys[idx] : (DEFAULT_IDX < keys.length ? keys[DEFAULT_IDX] : keys[0]);
            }
        } catch (Exception e) {
            // ignore
        }

        return fieldName;
    }
}
