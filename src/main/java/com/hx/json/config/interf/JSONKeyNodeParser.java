package com.hx.json.config.interf;

/**
 * 解析JSONkey的parser
 *
 * @author Jerry.X.He <970655147@qq.com>
 * @version 1.0
 * @date 5/1/2017 6:00 PM
 */
public interface JSONKeyNodeParser {

    /**
     * 获取给定的clazz的getterMethodName对应的field的key
     *
     * @param obj              当前正在处理的对象
     * @param clazz            seprator
     * @param getterMethodName 给定的field的getter
     * @param config           the config
     * @return com.hx.log.json.interf.JSON
     * @author Jerry.X.He
     * @date 4/15/2017 5:10 PM
     * @since 1.0
     */
    String getKeyForGetter(Object obj, Class clazz, String getterMethodName, JSONConfig config);

    /**
     * 获取给定的clazz的setterMethodName对应的field的key
     *
     * @param obj              当前正在处理的对象
     * @param clazz            seprator
     * @param setterMethodName 给定的field的setter
     * @param config           the config
     * @return com.hx.log.json.interf.JSON
     * @author Jerry.X.He
     * @date 4/15/2017 5:10 PM
     * @since 1.0
     */
    String getKeyForSetter(Object obj, Class clazz, String setterMethodName, JSONConfig config);

}
