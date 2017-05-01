package com.hx.json.test;

import com.hx.json.config.simple.JSONFieldKeyNodeParser;
import com.hx.json.JSONObject;
import com.hx.json.JSONParseUtils;
import com.hx.json.interf.JSONField;
import com.hx.log.util.Log;
import org.junit.Before;
import org.junit.Test;

import static com.hx.log.util.Log.info;

/**
 * Test05JSONFieldValueParser
 *
 * @author Jerry.X.He <970655147@qq.com>
 * @version 1.0
 * @date 5/1/2017 6:14 PM
 */
public class Test05JSONFieldValueParser {


    @Before
    public void before() {
        Log.infoFatalLogger.logPatternChain = null;
    }

    @Test
    public void testForKeyNodeParser() {

//        String json = "{'name':'hx', 'age':'12' }";
        User json = new User("hx", 12);

        JSONParseUtils.setKeyNodeParser(new JSONFieldKeyNodeParser());

        JSONObject obj = JSONObject.fromObject(json);
        info(obj.toString() );

        User newUser = JSONObject.toBean(obj, User.class);
        info(newUser);

    }

    /**
     * test bean
     *
     * @author Jerry.X.He <970655147@qq.com>
     * @version 1.0
     * @date 4/16/2017 11:50 AM
     */
    public static class User {
        @JSONField("_name")
        String name;
        @JSONField("_age")
        int age;
        public User() {
        }
        public User(String name, int age) {
            this.name = name;
            this.age = age;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public int getAge() {
            return age;
        }
        public void setAge(int age) {
            this.age = age;
        }
        @Override
        public String toString() {
            return new JSONObject().element("name", name).element("age", age).toString();
        }
    }


}
