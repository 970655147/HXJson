package com.hx.json.test;

import com.hx.json.JSONArray;
import com.hx.json.JSONObject;
import com.hx.json.JSONParseUtils;
import com.hx.json.SimpleJSONConfig;
import com.hx.log.util.Log;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.hx.log.util.Log.info;

/**
 * Test03JSONToBean
 *
 * @author Jerry.X.He <970655147@qq.com>
 * @version 1.0
 * @date 4/29/2017 9:48 AM
 */
public class Test03JSONToBean {

    @Before
    public void before() {
        Log.infoFatalLogger.logPatternChain = null;
    }

    @Test
    public void toBean() {

        JSONObject obj = JSONObject.fromObject("{\"name\":\"hx\", \"age\":33, 'friends':['123', '222'], 'self':{'name':'hxSelf', 'friends':['111'] }, 'scores':[1, 2, 4] }");

        User user = JSONObject.toBean(obj, User.class);
        info(user.toString() );

    }

    @Test
    public void fromBean() throws Exception {

        JSONArray arr = JSONArray.fromObject(new int[]{1, 2, 4} );
//        JSONArray arr = JSONArray.fromObject(new Integer[]{1, 2, 4} );
        info(arr.toString() );

        JSONObject obj = JSONParseUtils.toBean("{'name':'hx', 'age':43}", new SimpleJSONConfig(), JSONObject.class);
        info(obj.toString() );

    }

    @Test
    public void testSetIntArr() {
//        setIntArr(new int[]{1, 2 });
//        setIntArr(new byte[]{1, 2 });
//        setByteArr(new int[]{1, 2 });
//        setByteArr(new byte[]{1, 2 });
//        setByteArr(new Byte[]{1, 2 });

//        setIntegerArr(new Object[]{ });
    }
    public void setIntArr(int[] arr) {}
    public void setByteArr(byte[] arr) {}
    public void setIntegerArr(Integer[] arr) {}

    /**
     * test bean
     *
     * @author Jerry.X.He <970655147@qq.com>
     * @version 1.0
     * @date 4/16/2017 11:50 AM
     */
    public static class User {
        String name;
        List<String> friends;
        User self;
        int[] scores;

        public User() {
        }

        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public List<String> getFriends() {
            return friends;
        }
        public void setFriends(List<String> friends) {
            this.friends = friends;
        }
        public User getSelf() {
            return self;
        }
        public void setSelf(User self) {
            this.self = self;
        }
        public int[] getScores() {
            return scores;
        }
        public void setScores(int[] scores) {
            this.scores = scores;
        }

        @Override
        public String toString() {
            return new JSONObject().element("name", name).element("friends", friends)
                    .element("self", self).element("scores", scores)
                    .toString();
        }
    }

}
