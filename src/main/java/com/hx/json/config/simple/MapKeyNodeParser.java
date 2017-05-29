package com.hx.json.config.simple;

import com.hx.common.util.InnerTools;
import com.hx.json.JSONParseUtils;
import com.hx.json.config.interf.JSONConfig;
import com.hx.json.config.interf.JSONKeyNodeParser;
import com.hx.json.util.JSONConstants;

import java.util.Map;

/**
 * 根据指定的 map 获取给定的字段的 to, from 映射的字段
 *
 * @author Jerry.X.He <970655147@qq.com>
 * @version 1.0
 * @date 5/12/2017 10:43 PM
 */
public class MapKeyNodeParser implements JSONKeyNodeParser {

    /**
     * 给定的映射[toString[field -> targetKey], fromObject[targetKey -> field]]
     */
    private Map<String, String> map;

    /**
     * 初始化
     *
     * @param map 给定的映射
     * @since 1.0
     */
    public MapKeyNodeParser(Map<String, String> map) {
        InnerTools.assert0(map != null, "'map' can't be null !");
        this.map = map;
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
        String result = map.get(fieldName);
        if (result == null) {
            return fieldName;
        }

        return result;
    }

}
