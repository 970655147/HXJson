package com.hx.json.test;

import com.hx.json.JSONArray;
import com.hx.json.JSONObject;
import com.hx.json.JSONParseUtils;
import com.hx.log.util.Log;
import org.junit.Before;
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

    @Before
    public void before() {

        Log.infoFatalLogger.logPatternChain = null;

    }

    @Test
    public void nullValue() {

        String objStr = "{'name':'hx', \"age\":22, 'friends' : [1, 2, 4], 'chineseScore' : null, 'matchScore':56.4}";

//        JSONParseUtils.setValueNodeParser(null);
        JSONObject obj = JSONObject.fromObject(objStr);

        info(obj.toString() );
        info(obj.toString(5) );

        String arrStr = "[null, 'null', 124d]";
        JSONArray arr = JSONArray.fromObject(arrStr);
        info(arr.toString() );

    }

    @Test
    public void emptyObjOrArr() {

        String objStr = "{ }";
        JSONObject obj = JSONObject.fromObject(objStr);
        info(obj.toString() );

        String arrStr = "[ ]";
        JSONArray arr = JSONArray.fromObject(arrStr);
        info(arr.toString() );

    }


}
