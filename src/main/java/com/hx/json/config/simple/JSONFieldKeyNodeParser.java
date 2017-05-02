package com.hx.json.config.simple;

import com.hx.common.util.InnerTools;
import com.hx.json.JSONParseUtils;
import com.hx.json.config.interf.JSONConfig;
import com.hx.json.interf.JSONField;
import com.hx.json.config.interf.JSONKeyNodeParser;
import com.hx.json.util.JSONConstants;
import java.lang.reflect.Field;

/**
 * JSONFieldKeyNodeParser
 *
 * @author Jerry.X.He <970655147@qq.com>
 * @version 1.0
 * @date 5/1/2017 6:10 PM
 */
public class JSONFieldKeyNodeParser implements JSONKeyNodeParser {

    @Override
    public String getKeyForGetter(Class clazz, String getterMethodName, JSONConfig config) {
        String fieldName = JSONParseUtils.trimIfStartsWith(getterMethodName, JSONConstants.BEAN_GETTER_PREFIXES);
        fieldName = InnerTools.lowerCaseFirstChar(fieldName);

        return getKeyForField(clazz, fieldName, config);
    }

    @Override
    public String getKeyForSetter(Class clazz, String setterMethodName, JSONConfig config) {
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
                return fieldAnno.value();
            }
        } catch (Exception e) {
            // ignore
        }

        return fieldName;
    }
}
