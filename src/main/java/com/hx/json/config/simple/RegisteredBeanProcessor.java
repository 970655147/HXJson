package com.hx.json.config.simple;

import com.hx.common.util.InnerTools;
import com.hx.json.JSONObject;
import com.hx.json.config.interf.JSONBeanProcessor;
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
public class RegisteredBeanProcessor implements JSONBeanProcessor {

    /**
     * ע��� class -> keyNodeParser ��ӳ��
     */
    private Map<Class, JSONBeanProcessor> beanProcessorMap;
    /**
     * Ĭ�ϵĴ��� ҵ��� keyNodeParser
     */
    private JSONBeanProcessor defaultBeanProcessor = SimpleBeanProcessor.getInstance();

    /**
     * ��ȡ JSONKeyFieldNodeParser �Ľӿ�
     *
     * @param sz ����ע���Ԫ�ص�����
     * @return com.hx.json.config.simple.JSONFieldKeyNodeParser
     * @author Jerry.X.He
     * @date 5/29/2017 9:31 AM
     * @since 1.0
     */
    public static RegisteredBeanProcessor of(int sz) {
        return new RegisteredBeanProcessor(sz);
    }

    public static RegisteredBeanProcessor of() {
        return of(10);
    }

    /**
     * ��ʼ��
     *
     * @since 1.0
     */
    public RegisteredBeanProcessor(int sz) {
        this.beanProcessorMap = new HashMap<>(sz + (sz >> 1));
    }

    public RegisteredBeanProcessor register(Class clazz, JSONBeanProcessor beanProcessor) {
        InnerTools.assert0(clazz != null, "'clazz' can't be null !");
        InnerTools.assert0(beanProcessor != null, "'beanProcessor' can't be null !");
        beanProcessorMap.put(clazz, beanProcessor);
        return this;
    }

    @Override
    public <T> void beforeToBean(JSONObject obj, JSONConfig config, T receiver) {
        JSONBeanProcessor beanProcessor = beanProcessorMap.get(obj.getClass());
        if (beanProcessor != null) {
            beanProcessor.beforeToBean(obj, config, receiver);
            return ;
        }

        defaultBeanProcessor.beforeToBean(obj, config, receiver);
    }

    @Override
    public void afterFromBean(Object obj, JSONConfig config, JSONObject result) {
        JSONBeanProcessor beanProcessor = beanProcessorMap.get(obj.getClass());
        if (beanProcessor != null) {
            beanProcessor.afterFromBean(obj, config, result);
            return ;
        }

        defaultBeanProcessor.afterFromBean(obj, config, result);
    }


}
