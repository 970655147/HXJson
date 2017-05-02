package com.hx.json.test;

import com.hx.common.util.InnerTools;
import com.hx.json.JSONArray;
import com.hx.json.JSONObject;
import com.hx.json.util.JSONConstants;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

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

    }

    @Test
    public void parseArray() {
        String objStr = "{\"configs\":             [\n" +
                "                {\"$ref\": \"config\"},\n" +
                "                {\"$ref\": \"config\"}\n" +
                "            ]}";

        JSONObject obj = JSONObject.fromObject(objStr);
        InnerTools.log(obj.toString() );
    }

    @Test
    public void castNull() {

        InnerTools.log((Object) null);

    }

    @Test
    public void addNull() {

        JSONArray arr = new JSONArray();

        arr.add((JSONObject) null);
        InnerTools.log(arr.toString());

    }

    @Test
    public void parseArray02() {

//        List<String> ls = Tools.asList("123", "34", "df");
        List<Test01JSONTests.User> ls = InnerTools.asList(new Test01JSONTests.User("123", 11),
                new Test01JSONTests.User("11", 12), new Test01JSONTests.User("22", 22));
        JSONArray arr = JSONArray.fromObject(ls);

        InnerTools.log(arr.toString());

    }

    @Test
    public void printOBJ_ALREADY_EXISTS() {

        InnerTools.log(JSONConstants.BEAN_SETTER_PREFIXES);

    }

}
