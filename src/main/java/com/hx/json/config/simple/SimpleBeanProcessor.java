package com.hx.json.config.simple;

import com.hx.json.JSONObject;
import com.hx.json.config.interf.JSONBeanProcessor;
import com.hx.json.config.interf.JSONConfig;

/**
 * SimpleBeanProcessor
 *
 * @author Jerry.X.He <970655147@qq.com>
 * @version 1.0
 * @date 5/29/2017 11:23 AM
 */
public class SimpleBeanProcessor implements JSONBeanProcessor {

    /**
     * ����
     */
    public static final SimpleBeanProcessor INSTANCE = new SimpleBeanProcessor();

    /**
     * ��ȡ��������
     *
     * @return com.hx.json.config.simple.SimpleBeanProcessor
     * @author Jerry.X.He
     * @date 5/29/2017 11:40 AM
     * @since 1.0
     */
    public static SimpleBeanProcessor getInstance() {
        return INSTANCE;
    }

    private SimpleBeanProcessor() {
    }

    @Override
    public <T> void beforeToBean(JSONObject obj, JSONConfig config, T receiver) {

    }

    @Override
    public void afterFromBean(Object obj, JSONConfig config, JSONObject result) {

    }
}
