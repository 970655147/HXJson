package com.hx.json;

import com.hx.json.*;
import com.hx.json.interf.JSON;
import com.hx.json.interf.JSONConfig;
import com.hx.json.interf.JSONType;
import com.hx.json.util.JSONConstants;
import com.hx.log.str.WordsSeprator;
import com.hx.log.util.Tools;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * JSONParseUtils
 *
 * @author Jerry.X.He <970655147@qq.com>
 * @version 1.0
 * @date 4/15/2017 5:49 PM
 */
final class JSONParseUtils {

    // disable constructor
    private JSONParseUtils() {
        Tools.assert0("can't instantiate !");
    }

    /**
     * 从当前Seprator中提取下一个value, 可能是JSONStr, JSONInt, JSONBool, JSONObject, JSONArray
     *
     * @param sep seprator
     * @param key the key
     * @return com.hx.log.json.interf.JSON
     * @author Jerry.X.He
     * @date 4/15/2017 5:10 PM
     * @since 1.0
     */
    static JSON getNextValue(WordsSeprator sep, String key, JSONConfig config) {
        Tools.assert0(sep.hasNext(), "expect an value for key : " + key);
        String next = sep.seek().trim();
        if (JSONConstants.OBJ_START.equals(next)) {
            return JSONObject.fromString(sep, config, false);
        } else if (JSONConstants.ARR_START.equals(next)) {
            return JSONArray.fromString(sep, config, false);
        } else if (next.startsWith(JSONConstants.STR_SEP01) || next.startsWith(JSONConstants.STR_SEP02)) {
            sep.next();
            return JSONStr.fromObject(trimForSurroundSep(next, JSONConstants.KEY_SEPS));
            // take null check before 'l', "L"
        } else if(Tools.equalsIgnoreCase(JSONConstants.ELE_NULL, next)){
            sep.next();
            return JSONObj.JSON_OBJ_NULL;
        } else if (Tools.equalsIgnoreCase(Tools.TRUE, next) || Tools.equalsIgnoreCase(Tools.FALSE, next)) {
            sep.next();
            return JSONBool.fromObject(Tools.equalsIgnoreCase(Tools.TRUE, next));
        } else if (endsWith(next, JSONConstants.ELE_LONG_SUFFIXES)) {
            try {
                long longVal = Long.parseLong(next);
                sep.next();
                return JSONLong.fromObject(longVal);
            } catch (Exception e) {
                // ignore
            }
        } else if (endsWith(next, JSONConstants.ELE_FLOAT_SUFFIXES) ||
                // if text with '.', default choose it as float
                (next.contains(".")) && (!endsWith(next, JSONConstants.ELE_DOUBLE_SUFFIXES))) {
            try {
                float floatVal = Float.parseFloat(next);
                sep.next();
                return JSONFloat.fromObject(floatVal);
            } catch (Exception e) {
                // ignore
            }
        } else if (endsWith(next, JSONConstants.ELE_DOUBLE_SUFFIXES)) {
            try {
                double doubleVal = Double.parseDouble(next);
                sep.next();
                return JSONDouble.fromObject(doubleVal);
            } catch (Exception e) {
                // ignore
            }
        } else {
            try {
                int intVal = Integer.parseInt(next);
                sep.next();
                return JSONInt.fromObject(intVal);
            } catch (Exception e) {
                // ignore
            }
        }

        Tools.assert0("bad format value for key : " + key);
        return null;
    }

    /**
     * 判断给定的字符串是否 匹配给定的后缀列表中某一个后缀
     *
     * @param str      给定的字符串
     * @param prefixes 给定的备选后缀列表
     * @return boolean
     * @author Jerry.X.He
     * @date 4/15/2017 6:17 PM
     * @since 1.0
     */
    static boolean startsWith(String str, Set<String> prefixes) {
        for (String suffix : prefixes) {
            if (str.startsWith(suffix)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 判断给定的字符串是否 匹配给定的后缀列表中某一个后缀
     *
     * @param str      给定的字符串
     * @param suffixes 给定的备选后缀列表
     * @return boolean
     * @author Jerry.X.He
     * @date 4/15/2017 6:17 PM
     * @since 1.0
     */
    static boolean endsWith(String str, Set<String> suffixes) {
        for (String suffix : suffixes) {
            if (str.endsWith(suffix)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 移除key旁边的分隔符
     *
     * @param keyWithSep 旁边包含了分隔符的key
     * @param seps       keyWithSep的分隔符备选列表
     * @return java.lang.String
     * @author Jerry.X.He
     * @date 4/15/2017 5:06 PM
     * @since 1.0
     */
    static String trimForSurroundSep(String keyWithSep, Collection<String> seps) {
        for (String sep : seps) {
            if (keyWithSep.startsWith(sep)) {
                return keyWithSep.substring(sep.length(), keyWithSep.length() - sep.length());
            }
        }
        Tools.assert0("key must startsWith : " + seps.toString());
        return keyWithSep;
    }

    /**
     * 移除key旁边的分隔符
     *
     * @param str      给定的字符串
     * @param prefixes 前缀集合
     * @return java.lang.String
     * @author Jerry.X.He
     * @date 4/15/2017 5:06 PM
     * @since 1.0
     */
    static String trimIfStartsWith(String str, Set<String> prefixes) {
        for (String suffix : prefixes) {
            if (str.startsWith(suffix)) {
                return str.substring(suffix.length());
            }
        }

        return str;
    }

    /**
     * 将给定的JSONObject输出到sb中[尽量压缩]
     *
     * @param obj 给定的JSONObject
     * @param sb  输出的字符串
     * @return void
     * @author Jerry.X.He
     * @date 4/15/2017 10:39 PM
     * @since 1.0
     */
    static void toString(JSONObject obj, StringBuilder sb) {
        Tools.append(sb, JSONConstants.OBJ_START);

        for (Map.Entry<String, JSON> entry : obj.eles.entrySet()) {
            JSON value = entry.getValue();
            Tools.append(sb, "\"" + entry.getKey() + "\":");
            if (JSONType.OBJECT == value.type()) {
                Tools.append(sb, "");
                toString((JSONObject) value.value(), sb);
            } else if (JSONType.ARRAY == value.type()) {
                Tools.append(sb, "");
                toString((JSONArray) value.value(), sb);
            } else if ((JSONType.OBJ == value.type()) || (JSONType.STR == value.type())) {
                // for null, output null directly
                if(JSONType.OBJ == value.type() && (JSONObj.JSON_OBJ_NULL == value) ) {
                    Tools.append(sb, value.toString(0) );
                } else {
                    Tools.append(sb, "\"" + value.toString(0) + "\"");
                }
            } else {
                Tools.append(sb, value.toString(0));
            }
            Tools.append(sb, ", ");
        }

        Tools.removeLastSep(sb, ", ");
        Tools.append(sb, JSONConstants.OBJ_END);
    }

    static void toString(JSONArray obj, StringBuilder sb) {
        Tools.append(sb, JSONConstants.ARR_START);

        for (JSON value : obj.eles) {
            if (JSONType.OBJECT == value.type()) {
                toString((JSONObject) value.value(), sb);
            } else if (JSONType.ARRAY == value.type()) {
                toString((JSONArray) value.value(), sb);
            } else if ((JSONType.OBJ == value.type()) || (JSONType.STR == value.type())) {
                Tools.append(sb, "\"" + value.toString(0) + "\"");
            } else {
                Tools.append(sb, value.toString(0));
            }
            Tools.append(sb, ", ");
        }

        Tools.removeLastSep(sb, ", ");
        Tools.append(sb, JSONConstants.ARR_END);
    }

    /**
     * 将给定的JSONObject格式化输出到sb中
     *
     * @param obj          给定的JSONObject
     * @param indentFactor 缩进
     * @param sb           输出的字符串
     * @return void
     * @author Jerry.X.He
     * @date 4/15/2017 10:39 PM
     * @since 1.0
     */
    static void toString(JSONObject obj, int indentFactor, int depth, StringBuilder sb) {
        int identCnt = indentFactor * depth;
        appendBackspace(sb, identCnt - indentFactor);
        Tools.appendCRLF(sb, JSONConstants.OBJ_START);

        for (Map.Entry<String, JSON> entry : obj.eles.entrySet()) {
            JSON value = entry.getValue();
            appendBackspace(sb, identCnt);
            Tools.append(sb, "\"" + entry.getKey() + "\" : ");
            if (JSONType.OBJECT == value.type()) {
                Tools.appendCRLF(sb, "");
                toString((JSONObject) value.value(), indentFactor, depth + 1, sb);
            } else if (JSONType.ARRAY == value.type()) {
                Tools.appendCRLF(sb, "");
                toString((JSONArray) value.value(), indentFactor, depth + 1, sb);
            } else if ((JSONType.OBJ == value.type()) || (JSONType.STR == value.type())) {
                Tools.append(sb, "\"" + value.toString(indentFactor) + "\"");
            } else {
                Tools.append(sb, value.toString(indentFactor));
            }
            Tools.appendCRLF(sb, ", ");
        }

        Tools.removeLastSep(sb, ", \r\n");
        Tools.appendCRLF(sb, "");
        appendBackspace(sb, identCnt - indentFactor);
        Tools.append(sb, JSONConstants.OBJ_END);
    }

    static void toString(JSONArray obj, int indentFactor, int depth, StringBuilder sb) {
        int identCnt = indentFactor * depth;
        appendBackspace(sb, identCnt - indentFactor);
        Tools.appendCRLF(sb, JSONConstants.ARR_START);

        for (JSON value : obj.eles) {
            if (JSONType.OBJECT == value.type()) {
                toString((JSONObject) value.value(), indentFactor, depth + 1, sb);
            } else if (JSONType.ARRAY == value.type()) {
                toString((JSONArray) value.value(), indentFactor, depth + 1, sb);
            } else if ((JSONType.OBJ == value.type()) || (JSONType.STR == value.type())) {
                appendBackspace(sb, identCnt);
                Tools.append(sb, "\"" + value.toString(indentFactor) + "\"");
            } else {
                appendBackspace(sb, identCnt);
                Tools.append(sb, value.toString(indentFactor));
            }
            Tools.appendCRLF(sb, ", ");
        }

        Tools.removeLastSep(sb, ", \r\n");
        Tools.appendCRLF(sb, "");
        appendBackspace(sb, identCnt - indentFactor);
        Tools.append(sb, JSONConstants.ARR_END);
    }

    /**
     * 向给定的sb填充identFactor个空白字符
     *
     * @param sb          给定的字符串
     * @param identFactor 需要填充的空白字符的个数
     * @return void
     * @author Jerry.X.He
     * @date 4/15/2017 10:41 PM
     * @since 1.0
     */
    private static void appendBackspace(StringBuilder sb, int identFactor) {
        for (int i = 0; i < identFactor; i++) {
            sb.append(" ");
        }
    }

}
