package com.hx.json.test;

import com.hx.json.JSONArray;
import com.hx.json.JSONObject;
import com.hx.log.util.Constants;
import com.hx.log.util.Log;
import org.junit.Before;
import org.junit.Test;

import static com.hx.log.util.Log.info;

/**
 * Test04ParseArray
 *
 * @author Jerry.X.He <970655147@qq.com>
 * @version 1.0
 * @date 4/30/2017 2:01 PM
 */
public class Test04ParseArray {

    @Before
    public void before() {
        Log.infoFatalLogger.logPatternChain = null;
    }

    @Test
    public void parseArray() {
        String objStr = "{\"configs\":             [\n" +
                "                {\"$ref\": \"config\"},\n" +
                "                {\"$ref\": \"config\"}\n" +
                "            ]}";

        JSONObject obj = JSONObject.fromObject(objStr);
        info(obj.toString() );
    }

    @Test
    public void castNull() {

        info((Object) null);

    }

    @Test
    public void addNull() {

        JSONArray arr = new JSONArray();

        arr.add((JSONObject) null);
        info(arr.toString());

    }

    @Test
    public void printOBJ_ALREADY_EXISTS() {

        info(Constants.OBJECT_ALREADY_EXISTS);

    }

}
