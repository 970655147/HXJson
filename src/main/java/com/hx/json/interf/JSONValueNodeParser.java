package com.hx.json.interf;

import com.hx.log.str.WordsSeprator;

/**
 * JSONValueNodeParser : ����һ��JSON�ڵ��ֵ, JSONObject�е�value, ����JSONArray�е�value
 *
 * @author Jerry.X.He <970655147@qq.com>
 * @version 1.0
 * @date 4/23/2017 2:47 PM
 */
public interface JSONValueNodeParser {

    /**
     * �ӵ�ǰSeprator����ȡ��һ��value, ������JSONStr, JSONInt, JSONBool, JSONObject, JSONArray, ���߸�ʽ���Ϸ��׳��쳣
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
