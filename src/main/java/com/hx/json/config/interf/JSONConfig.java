package com.hx.json.config.interf;

/**
 * 解析JSON的时候的相关配置
 *
 * @author Jerry.X.He <970655147@qq.com>
 * @version 1.0
 * @date 4/15/2017 12:02 PM
 */
public interface JSONConfig {

    /**
     * 获取解析json需要使用的KeyNodeParser [解析key]
     *
     * @author Jerry.X.He
     * @date 5/7/2017 8:08 PM
     * @since 1.0
     */
    JSONKeyNodeParser keyNodeParser();

    /**
     * 获取解析json需要使用的ValueNodeParser [解析value]
     *
     * @author Jerry.X.He
     * @date 5/7/2017 8:08 PM
     * @since 1.0
     */
    JSONValueNodeParser valueNodeParser();

}
