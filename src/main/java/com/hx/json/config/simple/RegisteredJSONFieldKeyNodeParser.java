package com.hx.json.config.simple;

import com.hx.common.util.InnerTools;
import com.hx.json.config.interf.JSONConfig;
import com.hx.json.config.interf.JSONKeyNodeParser;

import java.util.HashMap;
import java.util.Map;

/**
 * JSONFieldKeyNodeParser
 *
 * @author Jerry.X.He <970655147@qq.com>
 * @version 1.0
 * @date 5/1/2017 6:10 PM
 */
public class RegisteredJSONFieldKeyNodeParser implements JSONKeyNodeParser {

    /**
     * 注册的 class -> keyNodeParser 的映射
     */
    private Map<Class, JSONFieldKeyNodeParser> keyNodeParserMap;
    /**
     * 默认的处理 业务的 keyNodeParser
     */
    private JSONFieldKeyNodeParser defaultKeyNodeParser = JSONFieldKeyNodeParser.of();

    /**
     * 获取 JSONKeyFieldNodeParser 的接口
     *
     * @param sz 可能注册的元素的数量
     * @return com.hx.json.config.simple.JSONFieldKeyNodeParser
     * @author Jerry.X.He
     * @date 5/29/2017 9:31 AM
     * @since 1.0
     */
    public static RegisteredJSONFieldKeyNodeParser of(int sz) {
        return new RegisteredJSONFieldKeyNodeParser(sz);
    }

    public static RegisteredJSONFieldKeyNodeParser of() {
        return of(10);
    }

    /**
     * 初始化
     *
     * @since 1.0
     */
    public RegisteredJSONFieldKeyNodeParser(int sz) {
        this.keyNodeParserMap = new HashMap<>(sz + (sz >> 1));
    }

    public RegisteredJSONFieldKeyNodeParser register(Class clazz, JSONFieldKeyNodeParser keyNodeParser) {
        InnerTools.assert0(clazz != null, "'clazz' can't be null !");
        InnerTools.assert0(keyNodeParser != null, "'keyNodeParser' can't be null !");
        keyNodeParserMap.put(clazz, keyNodeParser);
        return this;
    }

    @Override
    public String getKeyForGetter(Object obj, Class clazz, String getterMethodName, JSONConfig config) {
        JSONFieldKeyNodeParser keyNodeParser = keyNodeParserMap.get(clazz);
        if (keyNodeParser != null) {
            return keyNodeParser.getKeyForGetter(obj, clazz, getterMethodName, config);
        }

        return defaultKeyNodeParser.getKeyForGetter(obj, clazz, getterMethodName, config);
    }

    @Override
    public String getKeyForSetter(Object obj, Class clazz, String setterMethodName, JSONConfig config) {
        JSONFieldKeyNodeParser keyNodeParser = keyNodeParserMap.get(clazz);
        if (keyNodeParser != null) {
            return keyNodeParser.getKeyForSetter(obj, clazz, setterMethodName, config);
        }

        return defaultKeyNodeParser.getKeyForSetter(obj, clazz, setterMethodName, config);
    }

}
