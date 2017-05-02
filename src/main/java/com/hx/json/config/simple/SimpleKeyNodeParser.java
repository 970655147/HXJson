package com.hx.json.config.simple;

import com.hx.common.util.InnerTools;
import com.hx.json.JSONParseUtils;
import com.hx.json.config.interf.JSONConfig;
import com.hx.json.config.interf.JSONKeyNodeParser;
import com.hx.json.util.JSONConstants;

/**
 * SimpleKeyNodeParser
 *
 * @author Jerry.X.He <970655147@qq.com>
 * @version 1.0
 * @date 5/1/2017 6:03 PM
 */
public class SimpleKeyNodeParser implements JSONKeyNodeParser {

    @Override
    public String getKeyForGetter(Class clazz, String getterMethodName, JSONConfig config) {
        String fieldName = JSONParseUtils.trimIfStartsWith(getterMethodName, JSONConstants.BEAN_GETTER_PREFIXES);
        return InnerTools.lowerCaseFirstChar(fieldName);
    }

    @Override
    public String getKeyForSetter(Class clazz, String setterMethodName, JSONConfig config) {
        String fieldName = JSONParseUtils.trimIfStartsWith(setterMethodName, JSONConstants.BEAN_SETTER_PREFIXES);
        return InnerTools.lowerCaseFirstChar(fieldName);
    }
}
