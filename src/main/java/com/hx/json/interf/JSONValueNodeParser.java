package com.hx.json.interf;

import com.hx.log.str.WordsSeprator;

/**
 * JSONValueNodeParser : 解析一个JSON节点的值, JSONObject中的value, 或者JSONArray中的value
 *
 * @author Jerry.X.He <970655147@qq.com>
 * @version 1.0
 * @date 4/23/2017 2:47 PM
 */
public interface JSONValueNodeParser {

    /**
     * 从当前Seprator中提取下一个value, 可能是JSONStr, JSONInt, JSONBool, JSONObject, JSONArray, 或者格式不合法抛出异常
     *
     * @param sep    seprator
     * @param key    the key
     * @param config the config
     * @return com.hx.log.json.interf.JSON
     * @author Jerry.X.He
     * @date 4/15/2017 5:10 PM
     * @since 1.0
     */
    JSON parse(WordsSeprator sep, String key, JSONConfig config);

}
