package com.hx.json.util;

import com.hx.log.util.Tools;

import java.util.Map;
import java.util.Set;

/**
 * JSON相关的常量配置
 *
 * @author Jerry.X.He <970655147@qq.com>
 * @version 1.0
 * @date 4/15/2017 1:01 PM
 */
public final class JSONConstants {

    // disable constructor
    private JSONConstants() {
        Tools.assert0("can't instantiate !");
    }

    /**
     * JSON中的相关常量
     */
    public static final String OBJ_START = "{";
    public static final String OBJ_END = "}";
    public static final String ARR_START = "[";
    public static final String ARR_END = "]";
    public static final String STR_SEP01 = "'";
    public static final String STR_SEP02 = "\"";
    public static final String KV_SEP = ":";
    public static final String ELE_SEP = ",";

    /**
     * null元素, parse 为一个JSONObj(null)
     */
    public static final String ELE_NULL = "null";

    /**
     * 几种基本类型的后缀, long, float, double
     */
    public static final Set<String> ELE_LONG_SUFFIXES = Tools.asSet("l", "L" );
    public static final Set<String> ELE_FLOAT_SUFFIXES = Tools.asSet("f", "F" );
    public static final Set<String> ELE_DOUBLE_SUFFIXES = Tools.asSet("d", "D" );

    /**
     * 解析字符串的时候需要处理的分隔符
     */
    public static final Set<String> JSON_SEPS = Tools.asSet(OBJ_START, OBJ_END, ARR_START, ARR_END, KV_SEP, ELE_SEP);
    public static final Map<String, String> NEED_TO_ESCAPE = Tools.asMap(new String[]{STR_SEP01, STR_SEP02 }, STR_SEP01, STR_SEP02);
    public static final Set<String> KEY_SEPS = Tools.asSet(STR_SEP01, STR_SEP02);

    /**
     * toString的时候需要的常亮
     */
    public static String TO_STRING_ELE_SEP = ", ";
    public static String ONE_BACKSPACE = " ";
    public static String FOUR_BACKSPACE = "    ";

}
