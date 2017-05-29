package com.hx.json.config.interf;

import com.hx.json.JSONObject;

/**
 * 处理 JSON 与 Bean 交互的时候相关业务
 *
 * @author Jerry.X.He <970655147@qq.com>
 * @version 1.0
 * @date 5/29/2017 11:15 AM
 */
public interface JSONBeanProcessor {

    /**
     * 处理 toBean 之前的相关业务
     *
     * @param obj      obj
     * @param config   config
     * @param receiver receiver
     * @author Jerry.X.He
     * @date 5/29/2017 11:16 AM
     * @since 1.0
     */
    <T> void beforeToBean(JSONObject obj, JSONConfig config, T receiver);

    /**
     * 处理 fromBean 之后的相关业务
     *
     * @param obj    obj
     * @param config config
     * @param result result
     * @author Jerry.X.He
     * @date 5/29/2017 11:16 AM
     * @since 1.0
     */
    void afterFromBean(Object obj, JSONConfig config, JSONObject result);

}
