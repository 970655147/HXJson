package com.hx.json.test;

import com.hx.common.util.InnerTools;
import com.hx.json.JSONObject;
import com.hx.json.config.interf.JSONConfig;
import com.hx.json.config.simple.JSONFieldKeyNodeParser;
import com.hx.json.config.simple.MapKeyNodeParser;
import com.hx.json.config.simple.SimpleJSONConfig;
import com.hx.json.config.simple.SimpleValueNodeParser;
import org.junit.Test;

/**
 * Test06MapKeyParser
 *
 * @author Jerry.X.He <970655147@qq.com>
 * @version 1.0
 * @date 5/12/2017 10:47 PM
 */
public class Test06MapKeyParser {

    @Test
    public void test01ForMapKeyParser() {
        //        String json = "{'name':'hx', 'age':'12' }";
        Test05JSONFieldValueParser.User json = new Test05JSONFieldValueParser.User("hx", 12);

        JSONConfig config = new SimpleJSONConfig(new MapKeyNodeParser(
                InnerTools.asMap(new String[]{"name" }, "_x_name")
        ), new SimpleValueNodeParser());

        JSONObject obj = JSONObject.fromObject(json, config);
        InnerTools.log(obj.toString() );

        Test05JSONFieldValueParser.User newUser = JSONObject.toBean(obj, Test05JSONFieldValueParser.User.class, config);
        InnerTools.log(newUser);
    }


}
