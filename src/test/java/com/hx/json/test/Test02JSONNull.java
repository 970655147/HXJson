package com.hx.json.test;

import com.hx.json.JSONObject;
import com.hx.json.JSONParseUtils;
import com.hx.log.util.Log;
import org.junit.Test;

import static com.hx.log.util.Log.info;

/**
 * Test02JSONNull
 *
 * @author Jerry.X.He <970655147@qq.com>
 * @version 1.0
 * @date 4/23/2017 11:31 AM
 */
public class Test02JSONNull {

    @Test
    public void JSONObjectFromObject() {

        Log.infoFatalLogger.logPatternChain = null;

        String json = "{'name':'hx', \"age\":22, 'friends' : [1, 2, 4], 'chineseScore' : null, 'matchScore':56.4}";

//        JSONParseUtils.setValueNodeParser(null);
        JSONObject obj = JSONObject.fromObject(json);

        info(obj.toString() );
        info(obj.toString(5) );


    }

}
