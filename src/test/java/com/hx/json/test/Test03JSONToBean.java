package com.hx.json.test;

import com.hx.common.util.InnerTools;
import com.hx.json.JSONArray;
import com.hx.json.JSONObject;
import com.hx.json.JSONParseUtils;
import com.hx.json.config.simple.SimpleJSONConfig;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

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

    }

    @Test
    public void toBean() {

        JSONObject obj = JSONObject.fromObject("{\"name\":\"hx\", \"age\":33, 'friends':['123', '222'], 'self':{'name':'hxSelf', 'friends':['111'] }, 'scores':[1, 2, 4], 'others' : [{'name':'hxSelf01', 'friends':['111'] }, {'name':'hxSelf02', 'friends':['111'] }] }");

        User user = JSONObject.toBean(obj, User.class);
        JSONParseUtils.parse("{}", null);
        InnerTools.log(user.toString() );

    }

    @Test
    public void fromBean() throws Exception {

        JSONArray arr = JSONArray.fromObject(new int[]{1, 2, 4} );
//        JSONArray arr = JSONArray.fromObject(new Integer[]{1, 2, 4} );
        InnerTools.log(arr.toString() );

        JSONObject obj = JSONParseUtils.toBean("{'name':'hx', 'age':43}", JSONObject.class, new SimpleJSONConfig());
        InnerTools.log(obj.toString() );

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

    @Test
    public void testGetFieldGeneraicTypeParam() throws Exception {

        Class clazz = User.class;
        Field friends = clazz.getDeclaredField("friends");

        InnerTools.log(friends.getType() );
        Type type = friends.getGenericType() ;
        // 如果friends为rawType, 则type为Class
        if(type instanceof ParameterizedType) {
            ParameterizedType paramType = (ParameterizedType) type;
            InnerTools.log(paramType.getActualTypeArguments() );
        }

        InnerTools.log(type);


    }

    /**
     * test bean
     *
     * @author Jerry.X.He <970655147@qq.com>
     * @version 1.0
     * @date 4/16/2017 11:50 AM
     */
    public static class User {
        String name;
//        LinkedList<String> friends;
        List<String> friends;
        List<User> others;
//        List friends;
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
        public List<User> getOthers() {
            return others;
        }
        public void setOthers(List<User> others) {
            this.others = others;
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
                    .element("self", self).element("scores", JSONArray.fromObject(scores)).element("others", others)
                    .toString();
        }
    }

}
