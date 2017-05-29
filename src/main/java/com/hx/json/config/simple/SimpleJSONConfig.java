package com.hx.json.config.simple;

import com.hx.common.util.InnerTools;
import com.hx.json.config.interf.JSONBeanProcessor;
import com.hx.json.config.interf.JSONConfig;
import com.hx.json.config.interf.JSONKeyNodeParser;
import com.hx.json.config.interf.JSONValueNodeParser;

/**
 * JSONConfig的简单实现
 *
 * @author Jerry.X.He <970655147@qq.com>
 * @version 1.0
 * @date 4/15/2017 12:03 PM
 */
public class SimpleJSONConfig implements JSONConfig {

    /**
     * keyNodeParser
     */
    private JSONKeyNodeParser keyNodeParser;
    /**
     * valueNodeParser
     */
    private JSONValueNodeParser valueNodeParser;
    /**
     * beanProcessor
     */
    private JSONBeanProcessor beanProcessor;

    /**
     * 初始化
     *
     * @param keyNodeParser keyNodeParser
     * @param valueNodeParser valueNodeParser
     * @since 1.0
     */
    public SimpleJSONConfig(JSONKeyNodeParser keyNodeParser, JSONValueNodeParser valueNodeParser,
                            JSONBeanProcessor beanProcessor) {
        InnerTools.assert0(keyNodeParser != null, "'keyNodeParser' can't be null !");
        InnerTools.assert0(valueNodeParser != null, "'valueNodeParser' can't be null !");
        InnerTools.assert0(beanProcessor != null, "'beanProcessor' can't be null !");
        this.keyNodeParser = keyNodeParser;
        this.valueNodeParser = valueNodeParser;
        this.beanProcessor = beanProcessor;
    }

    public SimpleJSONConfig() {
        this(new SimpleKeyNodeParser(), new SimpleValueNodeParser(), SimpleBeanProcessor.getInstance() );
    }

    @Override
    public JSONKeyNodeParser keyNodeParser() {
        return keyNodeParser;
    }

    @Override
    public JSONValueNodeParser valueNodeParser() {
        return valueNodeParser;
    }

    @Override
    public JSONBeanProcessor beanProcessor() {
        return beanProcessor;
    }
}
