package com.hx.json.config.interf;

import com.hx.json.JSONObject;

/**
 * ���� JSON �� Bean ������ʱ�����ҵ��
 *
 * @author Jerry.X.He <970655147@qq.com>
 * @version 1.0
 * @date 5/29/2017 11:15 AM
 */
public interface JSONBeanProcessor {

    /**
     * ���� toBean ֮ǰ�����ҵ��
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
     * ���� fromBean ֮������ҵ��
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
