package com.hx.json.config.interf;

/**
 * ����JSONkey��parser
 *
 * @author Jerry.X.He <970655147@qq.com>
 * @version 1.0
 * @date 5/1/2017 6:00 PM
 */
public interface JSONKeyNodeParser {

    /**
     * ��ȡ������clazz��getterMethodName��Ӧ��field��key
     *
     * @param obj              ��ǰ���ڴ���Ķ���
     * @param clazz            seprator
     * @param getterMethodName ������field��getter
     * @param config           the config
     * @return com.hx.log.json.interf.JSON
     * @author Jerry.X.He
     * @date 4/15/2017 5:10 PM
     * @since 1.0
     */
    String getKeyForGetter(Object obj, Class clazz, String getterMethodName, JSONConfig config);

    /**
     * ��ȡ������clazz��setterMethodName��Ӧ��field��key
     *
     * @param obj              ��ǰ���ڴ���Ķ���
     * @param clazz            seprator
     * @param setterMethodName ������field��setter
     * @param config           the config
     * @return com.hx.log.json.interf.JSON
     * @author Jerry.X.He
     * @date 4/15/2017 5:10 PM
     * @since 1.0
     */
    String getKeyForSetter(Object obj, Class clazz, String setterMethodName, JSONConfig config);

}
