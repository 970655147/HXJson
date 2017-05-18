package com.hx.json.test;

import com.hx.json.JSONArray;
import com.hx.json.JSONObj;
import com.hx.json.JSONObject;
import com.hx.json.JSONParseUtils;
import org.junit.Test;

import java.util.Map;

/**
 * Test07FlatternMap
 *
 * @author Jerry.X.He <970655147@qq.com>
 * @version 1.0
 * @date 5/18/2017 7:49 PM
 */
public class Test07FlatternMap {

    @Test
    public void test01FlatternMap() {

        JSONObject obj = new JSONObject().element("name", "hx").element("age", 12)
                .element("friends", new JSONArray()
                        .element(new JSONObject().element("name", "documents").element("age", 15)))
                .element("live", new JSONObject().element("name", "live").element("attr", "engineer"))
                ;

        Map<String, String> result = JSONParseUtils.flatternMap(obj);
        System.out.println(JSONObject.fromObject(result).toString() );


    }

}
