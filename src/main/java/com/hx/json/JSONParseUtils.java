package com.hx.json;

import com.hx.json.*;
import com.hx.json.interf.JSON;
import com.hx.json.interf.JSONConfig;
import com.hx.json.interf.JSONType;
import com.hx.json.interf.JSONValueNodeParser;
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
public final class JSONParseUtils {

    /**
     * ����JSONValue��parser
     */
    private static JSONValueNodeParser VALUE_NODE_PARSER = new SimpleValueNodeParser();

    // disable constructor
    private JSONParseUtils() {
        Tools.assert0("can't instantiate !");
    }

    /**
     * �ӵ�ǰSeprator����ȡ��һ��value, ������JSONStr, JSONInt, JSONBool, JSONObject, JSONArray
     *
     * @param sep    seprator
     * @param key    the key
     * @param config the config
     * @return com.hx.log.json.interf.JSON
     * @author Jerry.X.He
     * @date 4/15/2017 5:10 PM
     * @since 1.0
     */
    static JSON getNextValue(WordsSeprator sep, String key, JSONConfig config) {
        Tools.assert0(VALUE_NODE_PARSER != null, "'VALUE_NODE_PARSER' can't be null !");
        return VALUE_NODE_PARSER.parse(sep, key, config);
    }

    /**
     * ���õ�ǰUtils��JSONValueNodeParser
     *
     * @param valueNodeParser ������JSONValueNodeParser
     * @return void
     * @author Jerry.X.He
     * @date 4/23/2017 2:55 PM
     * @since 1.0
     */
    public static void setValueNodeParser(JSONValueNodeParser valueNodeParser) {
        VALUE_NODE_PARSER = valueNodeParser;
    }

    /**
     * �жϸ������ַ����Ƿ� ƥ������ĺ�׺�б���ĳһ����׺
     *
     * @param str      �������ַ���
     * @param prefixes �����ı�ѡ��׺�б�
     * @return boolean
     * @author Jerry.X.He
     * @date 4/15/2017 6:17 PM
     * @since 1.0
     */
    public static boolean startsWith(String str, Set<String> prefixes) {
        for (String suffix : prefixes) {
            if (str.startsWith(suffix)) {
                return true;
            }
        }

        return false;
    }

    /**
     * �жϸ������ַ����Ƿ� ƥ������ĺ�׺�б���ĳһ����׺
     *
     * @param str      �������ַ���
     * @param suffixes �����ı�ѡ��׺�б�
     * @return boolean
     * @author Jerry.X.He
     * @date 4/15/2017 6:17 PM
     * @since 1.0
     */
    public static boolean endsWith(String str, Set<String> suffixes) {
        for (String suffix : suffixes) {
            if (str.endsWith(suffix)) {
                return true;
            }
        }

        return false;
    }

    /**
     * �Ƴ�key�Աߵķָ���
     *
     * @param keyWithSep �Ա߰����˷ָ�����key
     * @param seps       keyWithSep�ķָ�����ѡ�б�
     * @return java.lang.String
     * @author Jerry.X.He
     * @date 4/15/2017 5:06 PM
     * @since 1.0
     */
    public static String trimForSurroundSep(String keyWithSep, Collection<String> seps) {
        for (String sep : seps) {
            if (keyWithSep.startsWith(sep)) {
                return keyWithSep.substring(sep.length(), keyWithSep.length() - sep.length());
            }
        }
        Tools.assert0("key must startsWith : " + seps.toString());
        return keyWithSep;
    }

    /**
     * �Ƴ�key�Աߵķָ���
     *
     * @param str      �������ַ���
     * @param prefixes ǰ׺����
     * @return java.lang.String
     * @author Jerry.X.He
     * @date 4/15/2017 5:06 PM
     * @since 1.0
     */
    public static String trimIfStartsWith(String str, Set<String> prefixes) {
        for (String suffix : prefixes) {
            if (str.startsWith(suffix)) {
                return str.substring(suffix.length());
            }
        }

        return str;
    }

    /**
     * ��������JSONObject�����sb��[����ѹ��]
     *
     * @param obj ������JSONObject
     * @param sb  ������ַ���
     * @return void
     * @author Jerry.X.He
     * @date 4/15/2017 10:39 PM
     * @since 1.0
     */
    static void toString(JSONObject obj, StringBuilder sb) {
        Tools.append(sb, JSONConstants.OBJ_START);

        for (Map.Entry<String, JSON> entry : obj.eles.entrySet()) {
            JSON value = entry.getValue();
            Tools.append(sb, JSONConstants.STR_SEP02 + entry.getKey() + JSONConstants.STR_SEP02 + JSONConstants.KV_SEP);
            if (JSONType.OBJECT == value.type()) {
                Tools.append(sb, Tools.EMPTY_STR);
                toString((JSONObject) value.value(), sb);
            } else if (JSONType.ARRAY == value.type()) {
                Tools.append(sb, Tools.EMPTY_STR);
                toString((JSONArray) value.value(), sb);
            } else if ((JSONType.OBJ == value.type()) || (JSONType.STR == value.type())) {
                appendForObjOrStr(value, sb, 0);
            } else {
                Tools.append(sb, value.toString(0));
            }
            Tools.append(sb, JSONConstants.TO_STRING_ELE_SEP);
        }

        Tools.removeLastSep(sb, JSONConstants.TO_STRING_ELE_SEP);
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
                appendForObjOrStr(value, sb, 0);
            } else {
                Tools.append(sb, value.toString(0));
            }
            Tools.append(sb, JSONConstants.TO_STRING_ELE_SEP);
        }

        Tools.removeLastSep(sb, JSONConstants.TO_STRING_ELE_SEP);
        Tools.append(sb, JSONConstants.ARR_END);
    }

    /**
     * ��������JSONObject��ʽ�������sb��
     *
     * @param obj          ������JSONObject
     * @param indentFactor ����
     * @param sb           ������ַ���
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
            Tools.append(sb, JSONConstants.STR_SEP02 + entry.getKey() + JSONConstants.STR_SEP02 + JSONConstants.ONE_BACKSPACE + JSONConstants.KV_SEP + JSONConstants.ONE_BACKSPACE);
            if (JSONType.OBJECT == value.type()) {
                Tools.appendCRLF(sb, Tools.EMPTY_STR);
                toString((JSONObject) value.value(), indentFactor, depth + 1, sb);
            } else if (JSONType.ARRAY == value.type()) {
                Tools.appendCRLF(sb, Tools.EMPTY_STR);
                toString((JSONArray) value.value(), indentFactor, depth + 1, sb);
            } else if ((JSONType.OBJ == value.type()) || (JSONType.STR == value.type())) {
                appendForObjOrStr(value, sb, identCnt);
            } else {
                Tools.append(sb, value.toString(indentFactor));
            }
            Tools.appendCRLF(sb, JSONConstants.TO_STRING_ELE_SEP);
        }

        Tools.removeLastSep(sb, JSONConstants.TO_STRING_ELE_SEP + Tools.CRLF);
        Tools.appendCRLF(sb, Tools.EMPTY_STR);
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
                appendForObjOrStr(value, sb, identCnt);
            } else {
                appendBackspace(sb, identCnt);
                Tools.append(sb, value.toString(indentFactor));
            }
            Tools.appendCRLF(sb, JSONConstants.TO_STRING_ELE_SEP);
        }

        Tools.removeLastSep(sb, JSONConstants.TO_STRING_ELE_SEP + Tools.CRLF);
        Tools.appendCRLF(sb, Tools.EMPTY_STR);
        appendBackspace(sb, identCnt - indentFactor);
        Tools.append(sb, JSONConstants.ARR_END);
    }

    /**
     * �������sb���identFactor���հ��ַ�
     *
     * @param sb          �������ַ���
     * @param identFactor ��Ҫ���Ŀհ��ַ��ĸ���
     * @return void
     * @author Jerry.X.He
     * @date 4/15/2017 10:41 PM
     * @since 1.0
     */
    private static void appendBackspace(StringBuilder sb, int identFactor) {
        int fourCnt = identFactor >> 2;
        int remainCnt = identFactor & 0b11;
        for (int i = 0; i < fourCnt; i++) {
            sb.append(JSONConstants.FOUR_BACKSPACE);
        }
        for (int i = 0; i < remainCnt; i++) {
            sb.append(JSONConstants.ONE_BACKSPACE);
        }
    }

    /**
     * valueΪJSONObj ����JSONStr, �����ʽ�������sb��
     *
     * @param value        ��Ҫ�����sb�е�JSON
     * @param sb           ��������sb
     * @param indentFactor ����
     * @return void
     * @author Jerry.X.He
     * @date 4/23/2017 2:37 PM
     * @since 1.0
     */
    private static void appendForObjOrStr(JSON value, StringBuilder sb, int indentFactor) {
        // for null, output null directly
        if (JSONType.OBJ == value.type() && (JSONObj.JSON_OBJ_NULL == value)) {
            Tools.append(sb, value.toString(0));
        } else {
            Tools.append(sb, JSONConstants.STR_SEP02 + value.toString(indentFactor) + JSONConstants.STR_SEP02);
        }
    }

}
