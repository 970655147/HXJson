package com.hx.json;

import com.hx.json.interf.JSONConfig;
import com.hx.json.interf.JSONField;
import com.hx.json.interf.JSONKeyNodeParser;
import com.hx.log.util.Constants;
import com.hx.log.util.Tools;

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
        String fieldName = JSONParseUtils.trimIfStartsWith(getterMethodName, Constants.BEAN_GETTER_PREFIXES);
        fieldName = Tools.lowerCaseFirstChar(fieldName);

        return getKeyForField(clazz, fieldName, config);
    }

    @Override
    public String getKeyForSetter(Class clazz, String setterMethodName, JSONConfig config) {
        String fieldName = JSONParseUtils.trimIfStartsWith(setterMethodName, Constants.BEAN_SETTER_PREFIXES);
        fieldName = Tools.lowerCaseFirstChar(fieldName);

        return getKeyForField(clazz, fieldName, config);
    }

    /**
     * ��ȡclass��fieldName��Ӧ���ֶ���JSON�����е�key
     *
     * @param clazz     ������className
     * @param fieldName �������ֶ�����
     * @param config    ����JSON��config
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