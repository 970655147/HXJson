package com.hx.json.config.interf;

/**
 * ����JSON��ʱ����������
 *
 * @author Jerry.X.He <970655147@qq.com>
 * @version 1.0
 * @date 4/15/2017 12:02 PM
 */
public interface JSONConfig {

    /**
     * ��ȡ����json��Ҫʹ�õ�KeyNodeParser [����key]
     *
     * @author Jerry.X.He
     * @date 5/7/2017 8:08 PM
     * @since 1.0
     */
    JSONKeyNodeParser keyNodeParser();

    /**
     * ��ȡ����json��Ҫʹ�õ�ValueNodeParser [����value]
     *
     * @author Jerry.X.He
     * @date 5/7/2017 8:08 PM
     * @since 1.0
     */
    JSONValueNodeParser valueNodeParser();

    /**
     * ��ȡ toBean, fromBean ֮ǰ֮��, ��Ҫ��Ŀ�� JSONObject Ԥ�����processor
     *
     * @return JSONBeanProcessor
     * @author Jerry.X.He
     * @date 5/29/2017 11:21 AM
     * @since 1.0
     */
    JSONBeanProcessor beanProcessor();

}
