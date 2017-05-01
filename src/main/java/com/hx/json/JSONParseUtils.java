package com.hx.json;

import com.hx.json.interf.JSON;
import com.hx.json.interf.JSONConfig;
import com.hx.json.interf.JSONType;
import com.hx.json.interf.JSONValueNodeParser;
import com.hx.json.util.JSONConstants;
import com.hx.log.cache.mem.LFUMCache;
import com.hx.log.interf.Cache;
import com.hx.log.str.WordsSeprator;
import com.hx.log.util.Constants;
import com.hx.log.util.Tools;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * JSONParseUtils
 *
 * @author Jerry.X.He <970655147@qq.com>
 * @version 1.0
 * @date 4/15/2017 5:49 PM
 */
public final class JSONParseUtils {

    /**
     * 解析JSONValue的parser
     */
    private static JSONValueNodeParser VALUE_NODE_PARSER = new SimpleValueNodeParser();

    /**
     * Type的几种实现
     */
    public static final Integer TYPE_CLASS = 0;
    public static final Integer TYPE_PARAMETER_TYPE = TYPE_CLASS + 1;
    public static final Integer TYPE_GENERIC_ARRAY = TYPE_PARAMETER_TYPE + 1;
    public static final Integer TYPE_TYPE_VARIABLE = TYPE_GENERIC_ARRAY + 1;
    public static final Integer TYPE_WILDCARD_TYPE = TYPE_TYPE_VARIABLE + 1;

    /**
     * 缓存的Type -> Type类型 的映射
     */
    private static Cache<Type, Integer> CACHED_TYPE_2_TYPE_IMPL = new LFUMCache<>(Constants.optInt("hxJson.cache.capacity"), false);

    // disable constructor
    private JSONParseUtils() {
        Tools.assert0("can't instantiate !");
    }

    /**
     * 将给定的Object解析为JSON[JSONObject | JSONArray]
     *
     * @param obj    给定的Object
     * @param config jsonConfig
     * @return com.hx.json.interf.JSON
     * @author Jerry.X.He
     * @date 4/29/2017 10:58 AM
     * @since 1.0
     */
    public static JSON parse(Object obj, JSONConfig config) {
        if (obj instanceof String) {
            String json = (String) obj;
            if (json.startsWith(JSONConstants.OBJ_START)) {
                return JSONObject.fromObject(json, config);
            } else if (json.startsWith(JSONConstants.ARR_START)) {
                return JSONArray.fromObject(json, config);
            } else {
                Tools.assert0("illegal format obj : " + json);
            }
        }

        if (obj instanceof Collection) {
            return JSONArray.fromObject(obj, config);
        } else if (obj instanceof Map) {
            return JSONObject.fromObject(obj, config);
        } else if (obj.getClass().isArray()) {
            return JSONArray.fromObject(obj, config);
        } else {
            return JSONObject.fromObject(obj, config);
        }
    }

    /**
     * 将给定的Object转换为JSON
     *
     * @param obj    给定的Object, 可能为JSONObject, JSONArray等等
     * @param config 解析json的config
     * @param type   目标类型
     * @return java.lang.Object
     * @author Jerry.X.He
     * @date 4/29/2017 11:23 AM
     * @since 1.0
     */
    public static JSON fromBean(Object obj, JSONConfig config, Type type) {
        if (obj == null) {
            return JSONNull.getInstance();
        }

        if (obj instanceof JSON) {
            return (JSON) obj;
        }

        Class objClazz = obj.getClass();
        if ((boolean.class == objClazz) || (Boolean.class == objClazz)) {
            return JSONBool.fromObject((boolean) obj);
        } else if (((int.class == objClazz) || (Integer.class == objClazz))
                || ((byte.class == objClazz) || (Byte.class == objClazz))
                || ((short.class == objClazz) || (Short.class == objClazz))
                ) {
            return JSONInt.fromObject((int) obj);
        } else if ((long.class == objClazz) || (Long.class == objClazz)) {
            return JSONLong.fromObject((long) obj);
        } else if ((float.class == objClazz) || (Float.class == objClazz)) {
            return JSONFloat.fromObject((float) obj);
        } else if ((double.class == objClazz) || (Double.class == objClazz)) {
            return JSONDouble.fromObject((double) obj);
        } else if (String.class == objClazz) {
            return JSONStr.fromObject((String) obj);
        }

        Class returnClazz = getClassBoundsType(type);
        if (Collection.class.isAssignableFrom(returnClazz)) {
            return JSONArray.fromObject(obj);
        } else if (Map.class.isAssignableFrom(returnClazz)) {
            return JSONObject.fromObject(obj);
        } else if (returnClazz.isArray()) {
            return fromBeanArray(obj, config, returnClazz);
        } else {
            return JSONParseUtils.parse(obj, config);
        }
    }

    /**
     * 将给定的Object转换为目标类型
     *
     * @param obj    给定的Object, 可能为JSONObject, JSONArray等等
     * @param config 解析json的config
     * @param type   目标类型
     * @return java.lang.Object
     * @author Jerry.X.He
     * @date 4/29/2017 11:23 AM
     * @since 1.0
     */
    public static <T> T toBean(Object obj, JSONConfig config, Type type)
            throws IllegalAccessException, InstantiationException {
        if ((obj == null) || (type == null)) {
            return null;
        }

        Class<T> argClazz = getClassBoundsType(type);
        // this judge[argClazz.xx] does not have cross point with next judge[instanceof xx]
        if ((!isTypeParameterizedOrGenericArray(type)) && (argClazz.isInstance(obj))) {
            return argClazz.cast(obj);
        } else if (JSONObject.class == argClazz) {
            return argClazz.cast(JSONObject.fromObject(obj));
        } else if (JSONArray.class == argClazz) {
            return argClazz.cast(JSONArray.fromObject(obj));
        } else if (String.class == argClazz) {
            return argClazz.cast(String.valueOf(obj));
        } else if (argClazz.isArray()) {
            return argClazz.cast(toBeanArray(obj, config, argClazz));
        }

        if (obj instanceof JSONObject) {
            JSONObject jObject = (JSONObject) obj;
            if (Map.class.isAssignableFrom(argClazz)) {
                return argClazz.cast(objToMap(jObject, config, type));
            } else {
                return JSONObject.toBean(jObject, argClazz);
            }
        } else if (obj instanceof JSONArray) {
            JSONArray jArray = (JSONArray) obj;
            if (List.class.isAssignableFrom(argClazz)) {
                return argClazz.cast(arrToList(jArray, config, type));
            } else if (Set.class.isAssignableFrom(argClazz)) {
                return argClazz.cast(arrToSet(jArray, config, type));
            }
        }

        Tools.assert0("the type of 'obj' does not compatiable with 'argClazz' !");
        return null;
    }

    /**
     * 获取给定的JSON的字符串表示
     *
     * @param json 给定的JSON
     * @return java.lang.String
     * @author Jerry.X.He
     * @date 4/29/2017 2:09 PM
     * @since 1.0
     */
    public String toString(JSON json) {
        return String.valueOf(json);
    }

    public String toString(JSON json, int identFactor) {
        return (json == null) ? Tools.NULL : json.toString(identFactor);
    }

    /**
     * 规则化给定的JSON, 如果其为null, 将其转换为JSONNull
     *
     * @param ele 给定的JSON
     * @return com.hx.json.interf.JSON
     * @author Jerry.X.He
     * @date 5/1/2017 3:07 AM
     * @since 1.0
     */
    public static JSON normalizeJSON(JSON ele) {
        if ((ele == null) || (ele.isNull())) {
            ele = JSONNull.getInstance();
        }

        return ele;
    }

    /**
     * 配置当前Utils的JSONValueNodeParser
     *
     * @param valueNodeParser 给定的JSONValueNodeParser
     * @return void
     * @author Jerry.X.He
     * @date 4/23/2017 2:55 PM
     * @since 1.0
     */
    public static void setValueNodeParser(JSONValueNodeParser valueNodeParser) {
        VALUE_NODE_PARSER = valueNodeParser;
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
    public static boolean startsWith(String str, Set<String> prefixes) {
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
    public static boolean endsWith(String str, Set<String> suffixes) {
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
     * 移除key旁边的分隔符
     *
     * @param str      给定的字符串
     * @param prefixes 前缀集合
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
     * 从当前Seprator中提取下一个value, 可能是JSONStr, JSONInt, JSONBool, JSONObject, JSONArray
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
            Tools.append(sb, JSONConstants.STR_SEP02 + entry.getKey() + JSONConstants.STR_SEP02 + JSONConstants.KV_SEP);
            if (JSONType.OBJECT == value.type()) {
                Tools.append(sb, Tools.EMPTY_STR);
                toString((JSONObject) value.value(), sb);
            } else if (JSONType.ARRAY == value.type()) {
                Tools.append(sb, Tools.EMPTY_STR);
                toString((JSONArray) value.value(), sb);
            } else if ((JSONType.OBJ == value.type()) || (JSONType.STR == value.type())) {
                appendForObjOrStr(value, sb, 0);
            } else if (JSONType.NULL == value.type()) {
                Tools.append(sb, value.toString(0));
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
            } else if (JSONType.NULL == value.type()) {
                Tools.append(sb, value.toString(0));
            } else {
                Tools.append(sb, value.toString(0));
            }
            Tools.append(sb, JSONConstants.TO_STRING_ELE_SEP);
        }

        Tools.removeLastSep(sb, JSONConstants.TO_STRING_ELE_SEP);
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
            Tools.append(sb, JSONConstants.STR_SEP02 + entry.getKey() + JSONConstants.STR_SEP02 +
                    JSONConstants.ONE_BACKSPACE + JSONConstants.KV_SEP + JSONConstants.ONE_BACKSPACE);
            if (JSONType.OBJECT == value.type()) {
                Tools.appendCRLF(sb, Tools.EMPTY_STR);
                toString((JSONObject) value.value(), indentFactor, depth + 1, sb);
            } else if (JSONType.ARRAY == value.type()) {
                Tools.appendCRLF(sb, Tools.EMPTY_STR);
                toString((JSONArray) value.value(), indentFactor, depth + 1, sb);
            } else if ((JSONType.OBJ == value.type()) || (JSONType.STR == value.type())) {
                appendForObjOrStr(value, sb, identCnt);
            } else if (JSONType.NULL == value.type()) {
                Tools.append(sb, value.toString(indentFactor));
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
            } else if (JSONType.NULL == value.type()) {
                Tools.append(sb, value.toString(indentFactor));
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
     * value为JSONObj 或者JSONStr, 将其格式化输出到sb中
     *
     * @param value        需要输出到sb中的JSON
     * @param sb           保存结果的sb
     * @param indentFactor 缩进
     * @return void
     * @author Jerry.X.He
     * @date 4/23/2017 2:37 PM
     * @since 1.0
     */
    private static void appendForObjOrStr(JSON value, StringBuilder sb, int indentFactor) {
        Tools.append(sb, JSONConstants.STR_SEP02 + value.toString(indentFactor) + JSONConstants.STR_SEP02);
    }

    /**
     * 将给定的数组转换为JSONArray
     *
     * @param obj      给定的对象
     * @param config   解析json的config
     * @param argClazz 目标类型
     * @return java.lang.Object
     * @author Jerry.X.He
     * @date 4/29/2017 12:35 PM
     * @since 1.0
     */
    private static JSON fromBeanArray(Object obj, JSONConfig config, Class argClazz) {
        if(argClazz.isAssignableFrom(obj.getClass()) ) {
            return array2JSONArray(obj, config, argClazz);
        }

        return JSONNull.getInstance();
    }

    /**
     * 将给定的Object转换为目标类型的数组
     *
     * @param obj      给定的对象
     * @param config   解析json的config
     * @param argClazz 目标类型
     * @return java.lang.Object
     * @author Jerry.X.He
     * @date 4/29/2017 12:35 PM
     * @since 1.0
     */
    private static Object toBeanArray(Object obj, JSONConfig config, Class argClazz)
            throws IllegalAccessException, InstantiationException {
        if (argClazz.isArray()) {
            if (obj instanceof JSONArray) {
                JSONArray arr = (JSONArray) obj;
                return jsonArray2TypedArray(arr, config, argClazz);
            } else if (obj instanceof Collection) {
                Collection attrColl = (Collection) obj;
                return collection2TypedArray(attrColl, config, argClazz);
            }
        }

        return null;
    }

    /**
     * 将给定的JSONObject转换为给定的Map
     *
     * @param obj  给定的JSONObject
     * @param type 需要转换的类型
     * @return java.lang.Object
     * @author Jerry.X.He
     * @date 4/29/2017 4:13 PM
     * @since 1.0
     */
    private static Map objToMap(JSONObject obj, JSONConfig config, Type type)
            throws IllegalAccessException, InstantiationException {
        Class argClazz = getClassBoundsType(type);

        int modifier = argClazz.getModifiers();
        Map result = null;
        if (Modifier.isAbstract(modifier) || Modifier.isInterface(modifier)) {
            result = new LinkedHashMap<>();
        }
        if (result == null) {
            result = (Map) argClazz.newInstance();
        }

        putObjToMap(obj, config, result, type);
        return result;
    }

    /**
     * 将给定的JSONArray转换为给定的List
     *
     * @param arr  给定的JSONOArray
     * @param type 需要转换的类型
     * @return java.lang.Object
     * @author Jerry.X.He
     * @date 4/29/2017 4:13 PM
     * @since 1.0
     */
    private static List arrToList(JSONArray arr, JSONConfig config, Type type)
            throws IllegalAccessException, InstantiationException {
        Class argClazz = getClassBoundsType(type);

        List result = null;
        int modifier = argClazz.getModifiers();
        if (Modifier.isAbstract(modifier) || Modifier.isInterface(modifier)) {
            result = new ArrayList<>();
        }
        if (result == null) {
            result = (List) argClazz.newInstance();
        }

        putArrayToColl(arr, config, result, type);
        return result;
    }

    /**
     * 将给定的JSONArray转换为给定的Set
     *
     * @param arr  给定的JSONOArray
     * @param type 需要转换的类型
     * @return java.lang.Object
     * @author Jerry.X.He
     * @date 4/29/2017 4:13 PM
     * @since 1.0
     */
    private static Set arrToSet(JSONArray arr, JSONConfig config, Type type)
            throws IllegalAccessException, InstantiationException {
        Class argClazz = getClassBoundsType(type);

        Set result = null;
        int modifier = argClazz.getModifiers();
        if (Modifier.isAbstract(modifier) || Modifier.isInterface(modifier)) {
            result = new LinkedHashSet<>();
        }
        if (result == null) {
            result = (Set) argClazz.newInstance();
        }

        putArrayToColl(arr, config, result, type);
        return result;
    }

    /**
     * 将给定的JSONArray解析为目标类型[数组]
     *
     * @param arr      给定的JSONArray
     * @param config   解析json的config
     * @param argClazz 目标数组类型
     * @return java.lang.Object
     * @author Jerry.X.He
     * @date 4/29/2017 12:41 PM
     * @since 1.0
     */
    private static Object jsonArray2TypedArray(JSONArray arr, JSONConfig config, Class argClazz)
            throws IllegalAccessException, InstantiationException {
        if ((boolean[].class == argClazz) || (Boolean[].class == argClazz)) {
            if (boolean[].class == argClazz) {
                boolean[] attr = new boolean[arr.size()];
                for (int i = 0, len = arr.size(); i < len; i++) {
                    attr[i] = arr.optBoolean(i);
                }
                return attr;
            } else if (Boolean[].class == argClazz) {
                Boolean[] attr = new Boolean[arr.size()];
                for (int i = 0, len = arr.size(); i < len; i++) {
                    attr[i] = arr.optBoolean(i);
                }
                return attr;
            }
        } else if (((int[].class == argClazz) || (byte[].class == argClazz) || (short[].class == argClazz))
                || ((Integer[].class == argClazz) || (Byte[].class == argClazz) || (Short[].class == argClazz))
                ) {
            if (int[].class == argClazz) {
                int[] attr = new int[arr.size()];
                for (int i = 0, len = arr.size(); i < len; i++) {
                    attr[i] = arr.optInt(i);
                }
                return attr;
            } else if (byte[].class == argClazz) {
                byte[] attr = new byte[arr.size()];
                for (int i = 0, len = arr.size(); i < len; i++) {
                    attr[i] = (byte) arr.optInt(i);
                }
                return attr;
            } else if (short[].class == argClazz) {
                int[] attr = new int[arr.size()];
                for (int i = 0, len = arr.size(); i < len; i++) {
                    attr[i] = (short) arr.optInt(i);
                }
                return attr;
            } else if (Integer[].class == argClazz) {
                Integer[] attr = new Integer[arr.size()];
                for (int i = 0, len = arr.size(); i < len; i++) {
                    attr[i] = arr.optInt(i);
                }
                return attr;
            } else if (Byte[].class == argClazz) {
                Byte[] attr = new Byte[arr.size()];
                for (int i = 0, len = arr.size(); i < len; i++) {
                    attr[i] = (byte) arr.optInt(i);
                }
                return attr;
            } else if (Short[].class == argClazz) {
                Short[] attr = new Short[arr.size()];
                for (int i = 0, len = arr.size(); i < len; i++) {
                    attr[i] = (short) arr.optInt(i);
                }
                return attr;
            }
        } else if ((long[].class == argClazz) || (Long[].class == argClazz)) {
            if (long[].class == argClazz) {
                long[] attr = new long[arr.size()];
                for (int i = 0, len = arr.size(); i < len; i++) {
                    attr[i] = arr.optLong(i);
                }
                return attr;
            } else if (Long[].class == argClazz) {
                Long[] attr = new Long[arr.size()];
                for (int i = 0, len = arr.size(); i < len; i++) {
                    attr[i] = arr.optLong(i);
                }
                return attr;
            }
        } else if ((float[].class == argClazz) || (Float[].class == argClazz)) {
            if (float[].class == argClazz) {
                float[] attr = new float[arr.size()];
                for (int i = 0, len = arr.size(); i < len; i++) {
                    attr[i] = arr.optFloat(i);
                }
                return attr;
            } else if (Float[].class == argClazz) {
                Float[] attr = new Float[arr.size()];
                for (int i = 0, len = arr.size(); i < len; i++) {
                    attr[i] = arr.optFloat(i);
                }
                return attr;
            }
        } else if ((double[].class == argClazz) || (Double[].class == argClazz)) {
            if (double[].class == argClazz) {
                double[] attr = new double[arr.size()];
                for (int i = 0, len = arr.size(); i < len; i++) {
                    attr[i] = arr.optDouble(i);
                }
                return attr;
            } else if (Double[].class == argClazz) {
                Double[] attr = new Double[arr.size()];
                for (int i = 0, len = arr.size(); i < len; i++) {
                    attr[i] = arr.optDouble(i);
                }
                return attr;
            }
        } else {
            Object _attr = argClazz.newInstance();
            Class componentClazz = argClazz.getComponentType();
            Object[] attr = (Object[]) _attr;
            for (int i = 0, len = arr.size(); i < len; i++) {
                attr[i] = toBean(arr.get(i), config, componentClazz);
            }
            return attr;
        }

        return null;
    }

    /**
     * 将给定的集合解析为目标类型[数组]
     *
     * @param obj      给定的数组
     * @param config   解析json的config
     * @param argClazz 目标类型[数组]
     * @return java.lang.Object
     * @author Jerry.X.He
     * @date 4/29/2017 12:40 PM
     * @since 1.0
     */
    private static JSON array2JSONArray(Object obj, JSONConfig config, Class argClazz) {
        JSONArray result = new JSONArray();
        if ((boolean[].class == argClazz) || (Boolean[].class == argClazz)) {
            if (boolean[].class == argClazz) {
                boolean[] arr = (boolean[]) argClazz.cast(obj);
                for (boolean ele : arr) {
                    result.add(ele);
                }
                return result;
            } else if (Boolean[].class == argClazz) {
                Boolean[] arr = (Boolean[]) argClazz.cast(obj);
                for (Boolean ele : arr) {
                    result.add(ele);
                }
                return result;
            }
        } else if (((int[].class == argClazz) || (byte[].class == argClazz) || (short[].class == argClazz))
                || ((Integer[].class == argClazz) || (Byte[].class == argClazz) || (Short[].class == argClazz))
                ) {
            if ((int[].class == argClazz)) {
                int[] arr = (int[]) argClazz.cast(obj);
                for (int ele : arr) {
                    result.add(ele);
                }
                return result;
            } else if ((byte[].class == argClazz)) {
                byte[] arr = (byte[]) argClazz.cast(obj);
                for (byte ele : arr) {
                    result.add(ele);
                }
                return result;
            } else if ((short[].class == argClazz)) {
                short[] arr = (short[]) argClazz.cast(obj);
                for (short ele : arr) {
                    result.add(ele);
                }
                return result;
            } else if ((Integer[].class == argClazz)) {
                Integer[] arr = (Integer[]) argClazz.cast(obj);
                for (Integer ele : arr) {
                    result.add(ele);
                }
                return result;
            } else if ((Byte[].class == argClazz)) {
                Byte[] arr = (Byte[]) argClazz.cast(obj);
                for (Byte ele : arr) {
                    result.add(ele);
                }
                return result;
            } else if ((Short[].class == argClazz)) {
                Short[] arr = (Short[]) argClazz.cast(obj);
                for (Short ele : arr) {
                    result.add(ele);
                }
                return result;
            }
        } else if ((long[].class == argClazz) || (Long[].class == argClazz)) {
            if (long[].class == argClazz) {
                long[] arr = (long[]) argClazz.cast(obj);
                for (long ele : arr) {
                    result.add(ele);
                }
                return result;
            } else if (Long[].class == argClazz) {
                Long[] arr = (Long[]) argClazz.cast(obj);
                for (Long ele : arr) {
                    result.add(ele);
                }
                return result;
            }
        } else if ((float[].class == argClazz) || (Float[].class == argClazz)) {
            if (float[].class == argClazz) {
                float[] arr = (float[]) argClazz.cast(obj);
                for (float ele : arr) {
                    result.add(ele);
                }
                return result;
            } else if (Float[].class == argClazz) {
                Float[] arr = (Float[]) argClazz.cast(obj);
                for (Float ele : arr) {
                    result.add(ele);
                }
                return result;
            }
        } else if ((double[].class == argClazz) || (Double[].class == argClazz)) {
            if (double[].class == argClazz) {
                double[] arr = (double[]) argClazz.cast(obj);
                for (double ele : arr) {
                    result.add(ele);
                }
                return result;
            } else if (Double[].class == argClazz) {
                Double[] arr = (Double[]) argClazz.cast(obj);
                for (Double ele : arr) {
                    result.add(ele);
                }
                return result;
            }
        } else {
            Class componentClazz = argClazz.getComponentType();
            Object[] arr = (Object[]) argClazz.cast(obj);
            for (Object ele : arr) {
                result.add(fromBean(ele, config, componentClazz) );
            }
            return result;
        }

        return null;
    }

    /**
     * 将给定的集合解析为目标类型[数组]
     *
     * @param attrColl 给定的集合
     * @param config   解析json的config
     * @param argClazz 目标类型[数组]
     * @return java.lang.Object
     * @author Jerry.X.He
     * @date 4/29/2017 12:40 PM
     * @since 1.0
     */
    private static Object collection2TypedArray(Collection attrColl, JSONConfig config, Class argClazz)
            throws IllegalAccessException, InstantiationException {
        if ((boolean[].class == argClazz) || (Boolean[].class == argClazz)) {
            if (boolean[].class == argClazz) {
                boolean[] attr = new boolean[attrColl.size()];
                int idx = 0;
                for (Object ele : attrColl) {
                    attr[idx++] = Tools.equalsIgnoreCase(Tools.TRUE, String.valueOf(ele));
                }
                return attr;
            } else if (Boolean[].class == argClazz) {
                Boolean[] attr = new Boolean[attrColl.size()];
                int idx = 0;
                for (Object ele : attrColl) {
                    attr[idx++] = Tools.equalsIgnoreCase(Tools.TRUE, String.valueOf(ele));
                }
                return attr;
            }
        } else if (((int[].class == argClazz) || (byte[].class == argClazz) || (short[].class == argClazz))
                || ((Integer[].class == argClazz) || (Byte[].class == argClazz) || (Short[].class == argClazz))
                ) {
            if ((int[].class == argClazz)) {
                int[] attr = new int[attrColl.size()];
                int idx = 0;
                for (Object ele : attrColl) {
                    attr[idx++] = Integer.valueOf(String.valueOf(ele));
                }
                return attr;
            } else if ((byte[].class == argClazz)) {
                byte[] attr = new byte[attrColl.size()];
                int idx = 0;
                for (Object ele : attrColl) {
                    attr[idx++] = Byte.valueOf(String.valueOf(ele));
                }
                return attr;
            } else if ((short[].class == argClazz)) {
                short[] attr = new short[attrColl.size()];
                int idx = 0;
                for (Object ele : attrColl) {
                    attr[idx++] = Short.valueOf(String.valueOf(ele));
                }
                return attr;
            } else if ((Integer[].class == argClazz)) {
                Integer[] attr = new Integer[attrColl.size()];
                int idx = 0;
                for (Object ele : attrColl) {
                    attr[idx++] = Integer.valueOf(String.valueOf(ele));
                }
                return attr;
            } else if ((Byte[].class == argClazz)) {
                Byte[] attr = new Byte[attrColl.size()];
                int idx = 0;
                for (Object ele : attrColl) {
                    attr[idx++] = Byte.valueOf(String.valueOf(ele));
                }
                return attr;
            } else if ((Short[].class == argClazz)) {
                Short[] attr = new Short[attrColl.size()];
                int idx = 0;
                for (Object ele : attrColl) {
                    attr[idx++] = Short.valueOf(String.valueOf(ele));
                }
                return attr;
            }
        } else if ((long[].class == argClazz) || (Long[].class == argClazz)) {
            if (long[].class == argClazz) {
                long[] attr = new long[attrColl.size()];
                int idx = 0;
                for (Object ele : attrColl) {
                    attr[idx++] = Long.valueOf(String.valueOf(ele));
                }
                return attr;
            } else if (Long[].class == argClazz) {
                Long[] attr = new Long[attrColl.size()];
                int idx = 0;
                for (Object ele : attrColl) {
                    attr[idx++] = Long.valueOf(String.valueOf(ele));
                }
                return attr;
            }
        } else if ((float[].class == argClazz) || (Float[].class == argClazz)) {
            if (float[].class == argClazz) {
                float[] attr = new float[attrColl.size()];
                int idx = 0;
                for (Object ele : attrColl) {
                    attr[idx++] = Float.valueOf(String.valueOf(ele));
                }
                return attr;
            } else if (Float[].class == argClazz) {
                Float[] attr = new Float[attrColl.size()];
                int idx = 0;
                for (Object ele : attrColl) {
                    attr[idx++] = Float.valueOf(String.valueOf(ele));
                }
                return attr;
            }
        } else if ((double[].class == argClazz) || (Double[].class == argClazz)) {
            if (double[].class == argClazz) {
                double[] attr = new double[attrColl.size()];
                int idx = 0;
                for (Object ele : attrColl) {
                    attr[idx++] = Double.valueOf(String.valueOf(ele));
                }
                return attr;
            } else if (Double[].class == argClazz) {
                Double[] attr = new Double[attrColl.size()];
                int idx = 0;
                for (Object ele : attrColl) {
                    attr[idx++] = Double.valueOf(String.valueOf(ele));
                }
                return attr;
            }
        } else {
            Object _attr = argClazz.newInstance();
            Class componentClazz = argClazz.getComponentType();
            Object[] attr = (Object[]) _attr;
            int idx = 0;
            for (Object ele : attrColl) {
                attr[idx++] = toBean(ele, config, componentClazz);
            }
            return attr;
        }

        return null;
    }

    /**
     * 将给定的JSONArray中的元素添加到result中
     *
     * @param arr    给定的JSONArray
     * @param config 解析json的config
     * @param result 需要添加的目标元素集合
     * @param type   集合元素的类型
     * @return void
     * @author Jerry.X.He
     * @date 4/30/2017 10:30 AM
     * @since 1.0
     */
    private static void putArrayToColl(JSONArray arr, JSONConfig config, Collection result, Type type)
            throws IllegalAccessException, InstantiationException {
        // field specified type parameters
        if (type instanceof ParameterizedType) {
            Type typeParam = ((ParameterizedType) type).getActualTypeArguments()[0];
            for (int i = 0, len = arr.size(); i < len; i++) {
                result.add(toBean(arr.get(i), config, typeParam));
            }
        } else {
            result.addAll(arr);
        }
    }

    /**
     * 将给定的JSONObject中的元素添加到result中
     *
     * @param obj    给定的JSONObject
     * @param config 解析json的config
     * @param result 需要添加的目标元素集合
     * @param type   集合元素的类型
     * @return void
     * @author Jerry.X.He
     * @date 4/30/2017 10:30 AM
     * @since 1.0
     */
    private static void putObjToMap(JSONObject obj, JSONConfig config, Map result, Type type)
            throws IllegalAccessException, InstantiationException {
        // field specified type parameters
        if (type instanceof ParameterizedType) {
            Type keyTypeParam = ((ParameterizedType) type).getActualTypeArguments()[0];
            Type valueTypeParam = ((ParameterizedType) type).getActualTypeArguments()[1];
            for (Map.Entry<String, Object> entry : obj.entrySet()) {
                result.put(toBean(entry.getKey(), config, keyTypeParam), toBean(entry.getValue(), config, valueTypeParam));
            }
        } else {
            result.putAll(obj);
        }
    }

    /**
     * 获取给定的Type对应的rawType
     * 注 : 这里没有处理TypeVariable, WildcardType [因为这里的type是字段相关的type]
     *
     * @param type 给定的Type
     * @return java.lang.Class<T>
     * @author Jerry.X.He
     * @date 4/30/2017 10:08 AM
     * @since 1.0
     */
    private static <T> Class<T> getClassBoundsType(Type type) {
        Integer typeCode = CACHED_TYPE_2_TYPE_IMPL.get(type);
        if (typeCode != null) {
            Class<T> argClazz = null;
            if (TYPE_PARAMETER_TYPE.equals(typeCode)) {
                argClazz = (Class<T>) ((ParameterizedType) type).getRawType();
            } else if (TYPE_GENERIC_ARRAY.equals(typeCode)) {
                argClazz = (Class<T>) ((GenericArrayType) type).getGenericComponentType();
            } else if (TYPE_CLASS.equals(typeCode)) {
                argClazz = (Class<T>) type;
            }

            return argClazz;
        }

        Class<T> argClazz = null;
        if (type instanceof ParameterizedType) {
            argClazz = (Class<T>) ((ParameterizedType) type).getRawType();
            CACHED_TYPE_2_TYPE_IMPL.put(type, TYPE_PARAMETER_TYPE);
        } else if (type instanceof GenericArrayType) {
            argClazz = (Class<T>) ((GenericArrayType) type).getGenericComponentType();
            CACHED_TYPE_2_TYPE_IMPL.put(type, TYPE_GENERIC_ARRAY);
        } else {
            argClazz = (Class<T>) type;
            CACHED_TYPE_2_TYPE_IMPL.put(type, TYPE_CLASS);
        }

        return argClazz;
    }

    /**
     * 判断给定的Type是否是ParameterizedType 或者GenericArrayType 的实例
     *
     * @param type 给定的type
     * @return boolean
     * @author Jerry.X.He
     * @date 4/30/2017 10:38 AM
     * @since 1.0
     */
    private static boolean isTypeParameterizedOrGenericArray(Type type) {
        Integer typeCode = CACHED_TYPE_2_TYPE_IMPL.get(type);
        if (typeCode != null) {
            return (TYPE_PARAMETER_TYPE.equals(typeCode)) || (TYPE_GENERIC_ARRAY.equals(typeCode));
        }

        return (type instanceof ParameterizedType) || (type instanceof GenericArrayType);
    }


}
