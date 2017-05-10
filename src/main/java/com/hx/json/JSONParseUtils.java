package com.hx.json;

import com.hx.common.collection.SimpleFixedSizeHashMap;
import com.hx.common.str.WordsSeprator;
import com.hx.common.util.InnerTools;
import com.hx.json.config.interf.JSONConfig;
import com.hx.json.config.interf.JSONKeyNodeParser;
import com.hx.json.config.interf.JSONValueNodeParser;
import com.hx.json.config.simple.SimpleJSONConfig;
import com.hx.json.config.simple.SimpleKeyNodeParser;
import com.hx.json.config.simple.SimpleValueNodeParser;
import com.hx.json.interf.*;
import com.hx.json.util.JSONConstants;

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
     * Type�ļ���ʵ��
     */
    public static final Integer TYPE_CLASS = 0;
    public static final Integer TYPE_PARAMETER_TYPE = TYPE_CLASS + 1;
    public static final Integer TYPE_GENERIC_ARRAY = TYPE_PARAMETER_TYPE + 1;
    public static final Integer TYPE_TYPE_VARIABLE = TYPE_GENERIC_ARRAY + 1;
    public static final Integer TYPE_WILDCARD_TYPE = TYPE_TYPE_VARIABLE + 1;

    /**
     * �����Type -> Type���� ��ӳ��
     */
    private static Map<Type, Integer> CACHED_TYPE_2_TYPE_IMPL = new SimpleFixedSizeHashMap<>(200);

    // disable constructor
    private JSONParseUtils() {
        InnerTools.assert0("can't instantiate !");
    }

    /**
     * ��������Object����ΪJSON[JSONObject | JSONArray]
     *
     * @param obj    ������Object
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
                InnerTools.assert0("illegal format obj : " + json);
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

    public static JSON parse(Object obj) {
        return parse(obj, new SimpleJSONConfig());
    }

    /**
     * ��������Objectת��ΪJSON
     *
     * @param obj    ������Object, ����ΪJSONObject, JSONArray�ȵ�
     * @param config ����json��config
     * @return java.lang.Object
     * @author Jerry.X.He
     * @date 4/29/2017 11:23 AM
     * @since 1.0
     */
    public static JSON fromBean(Object obj, JSONConfig config) {
        if (obj == null) {
            return JSONNull.getInstance();
        }
        if (obj instanceof JSON) {
            return (JSON) obj;
        }

        Class clazz = obj.getClass();
        if ((boolean.class == clazz) || (Boolean.class == clazz)) {
            return JSONBool.fromObject((boolean) obj);
        } else if (((int.class == clazz) || (Integer.class == clazz))
                || ((byte.class == clazz) || (Byte.class == clazz))
                || ((short.class == clazz) || (Short.class == clazz))
                ) {
            return JSONInt.fromObject((int) obj);
        } else if ((long.class == clazz) || (Long.class == clazz)) {
            return JSONLong.fromObject((long) obj);
        } else if ((float.class == clazz) || (Float.class == clazz)) {
            return JSONFloat.fromObject((float) obj);
        } else if ((double.class == clazz) || (Double.class == clazz)) {
            return JSONDouble.fromObject((double) obj);
        } else if (String.class == clazz) {
            return JSONStr.fromObject((String) obj);
        }

        if (Collection.class.isAssignableFrom(clazz)) {
            return JSONArray.fromObject(obj);
        } else if (Map.class.isAssignableFrom(clazz)) {
            return JSONObject.fromObject(obj);
        } else if (clazz.isArray()) {
            return JSONArray.fromArray(obj, config);
        } else {
            return JSONParseUtils.parse(obj, config);
        }
    }

    public static JSON fromBean(Object obj) {
        return fromBean(obj, new SimpleJSONConfig());
    }

    /**
     * ��������Objectת��ΪĿ������
     *
     * @param obj    ������Object, ����ΪJSONObject, JSONArray�ȵ�
     * @param config ����json��config
     * @param type   Ŀ������
     * @return java.lang.Object
     * @author Jerry.X.He
     * @date 4/29/2017 11:23 AM
     * @since 1.0
     */
    public static <T> T toBean(Object obj, Type type, JSONConfig config)
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
            return argClazz.cast(toBeanArray(obj, argClazz, config));
        }

        if (obj instanceof JSONObject) {
            JSONObject jObject = (JSONObject) obj;
            if (Map.class.isAssignableFrom(argClazz)) {
                return argClazz.cast(objToMap(jObject, type, config));
            } else {
                return JSONObject.toBean(jObject, argClazz);
            }
        } else if (obj instanceof JSONArray) {
            JSONArray jArray = (JSONArray) obj;
            if (List.class.isAssignableFrom(argClazz)) {
                return argClazz.cast(arrToList(jArray, type, config));
            } else if (Set.class.isAssignableFrom(argClazz)) {
                return argClazz.cast(arrToSet(jArray, type, config));
            }
        }

        InnerTools.assert0("the type of 'obj' does not compatiable with 'argClazz' !");
        return null;
    }

    public static <T> T toBean(Object obj, Type type)
            throws IllegalAccessException, InstantiationException {
        return toBean(obj, type, new SimpleJSONConfig());
    }

    /**
     * ��ȡ������JSON���ַ�����ʾ
     *
     * @param json ������JSON
     * @return java.lang.String
     * @author Jerry.X.He
     * @date 4/29/2017 2:09 PM
     * @since 1.0
     */
    public String toString(JSON json) {
        return String.valueOf(json);
    }

    public String toString(JSON json, int identFactor) {
        return (json == null) ? InnerTools.NULL : json.toString(identFactor);
    }

    /**
     * ���򻯸�����JSON, �����Ϊnull, ����ת��ΪJSONNull
     *
     * @param ele ������JSON
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
        InnerTools.assert0("key must startsWith : " + seps.toString());
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
        sb.append(JSONConstants.OBJ_START);

        for (Map.Entry<String, JSON> entry : obj.eles.entrySet()) {
            JSON value = entry.getValue();
            sb.append(JSONConstants.STR_SEP02 + entry.getKey() + JSONConstants.STR_SEP02 + JSONConstants.KV_SEP);
            if (JSONType.OBJECT == value.type()) {
                sb.append(InnerTools.EMPTY_STR);
                toString((JSONObject) value.value(), sb);
            } else if (JSONType.ARRAY == value.type()) {
                sb.append(InnerTools.EMPTY_STR);
                toString((JSONArray) value.value(), sb);
            } else if ((JSONType.OBJ == value.type()) || (JSONType.STR == value.type())) {
                appendForObjOrStr(value, sb, 0);
            } else if (JSONType.NULL == value.type()) {
                sb.append(value.toString(0));
            } else {
                sb.append(value.toString(0));
            }
            sb.append(JSONConstants.TO_STRING_ELE_SEP);
        }

        if (obj.size() > 0) {
            InnerTools.removeLastSep(sb, JSONConstants.TO_STRING_ELE_SEP);
        }
        sb.append(JSONConstants.OBJ_END);
    }

    static void toString(JSONArray obj, StringBuilder sb) {
        sb.append(JSONConstants.ARR_START);

        for (JSON value : obj.eles) {
            if (JSONType.OBJECT == value.type()) {
                toString((JSONObject) value.value(), sb);
            } else if (JSONType.ARRAY == value.type()) {
                toString((JSONArray) value.value(), sb);
            } else if ((JSONType.OBJ == value.type()) || (JSONType.STR == value.type())) {
                appendForObjOrStr(value, sb, 0);
            } else if (JSONType.NULL == value.type()) {
                sb.append(value.toString(0));
            } else {
                sb.append(value.toString(0));
            }
            sb.append(JSONConstants.TO_STRING_ELE_SEP);
        }

        if (obj.size() > 0) {
            InnerTools.removeLastSep(sb, JSONConstants.TO_STRING_ELE_SEP);
        }
        sb.append(JSONConstants.ARR_END);
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
        sb.append(JSONConstants.OBJ_START + InnerTools.CRLF);

        for (Map.Entry<String, JSON> entry : obj.eles.entrySet()) {
            JSON value = entry.getValue();
            appendBackspace(sb, identCnt);
            sb.append(JSONConstants.STR_SEP02 + entry.getKey() + JSONConstants.STR_SEP02 +
                    JSONConstants.ONE_BACKSPACE + JSONConstants.KV_SEP + JSONConstants.ONE_BACKSPACE);
            if (JSONType.OBJECT == value.type()) {
                sb.append(InnerTools.CRLF);
                toString((JSONObject) value.value(), indentFactor, depth + 1, sb);
            } else if (JSONType.ARRAY == value.type()) {
                sb.append(InnerTools.CRLF);
                toString((JSONArray) value.value(), indentFactor, depth + 1, sb);
            } else if ((JSONType.OBJ == value.type()) || (JSONType.STR == value.type())) {
                appendForObjOrStr(value, sb, identCnt);
            } else if (JSONType.NULL == value.type()) {
                sb.append(value.toString(indentFactor));
            } else {
                sb.append(value.toString(indentFactor));
            }
            sb.append(JSONConstants.TO_STRING_ELE_SEP + InnerTools.CRLF);
        }

        if (obj.size() > 0) {
            InnerTools.removeLastSep(sb, JSONConstants.TO_STRING_ELE_SEP + InnerTools.CRLF);
        }
        sb.append(InnerTools.CRLF);
        appendBackspace(sb, identCnt - indentFactor);
        sb.append(JSONConstants.OBJ_END);
    }

    static void toString(JSONArray obj, int indentFactor, int depth, StringBuilder sb) {
        int identCnt = indentFactor * depth;
        appendBackspace(sb, identCnt - indentFactor);
        sb.append(JSONConstants.ARR_START + InnerTools.CRLF);

        for (JSON value : obj.eles) {
            if (JSONType.OBJECT == value.type()) {
                toString((JSONObject) value.value(), indentFactor, depth + 1, sb);
            } else if (JSONType.ARRAY == value.type()) {
                toString((JSONArray) value.value(), indentFactor, depth + 1, sb);
            } else if ((JSONType.OBJ == value.type()) || (JSONType.STR == value.type())) {
                appendBackspace(sb, identCnt);
                appendForObjOrStr(value, sb, identCnt);
            } else if (JSONType.NULL == value.type()) {
                sb.append(value.toString(indentFactor));
            } else {
                appendBackspace(sb, identCnt);
                sb.append(value.toString(indentFactor));
            }
            sb.append(JSONConstants.TO_STRING_ELE_SEP + InnerTools.CRLF);
        }

        if (obj.size() > 0) {
            InnerTools.removeLastSep(sb, JSONConstants.TO_STRING_ELE_SEP + InnerTools.CRLF);
        }
        sb.append(InnerTools.CRLF);
        appendBackspace(sb, identCnt - indentFactor);
        sb.append(JSONConstants.ARR_END);
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
        sb.append(JSONConstants.STR_SEP02 + InnerTools.transfer(value.toString(indentFactor), InnerTools.asSet('\\', '"'))
                + JSONConstants.STR_SEP02);
    }

    /**
     * ��������Objectת��ΪĿ�����͵�����
     *
     * @param obj      �����Ķ���
     * @param config   ����json��config
     * @param argClazz Ŀ������
     * @return java.lang.Object
     * @author Jerry.X.He
     * @date 4/29/2017 12:35 PM
     * @since 1.0
     */
    private static Object toBeanArray(Object obj, Class argClazz, JSONConfig config)
            throws IllegalAccessException, InstantiationException {
        if (argClazz.isArray()) {
            if (obj instanceof JSONArray) {
                JSONArray arr = (JSONArray) obj;
                return jsonArray2TypedArray(arr, argClazz, config);
            } else if (obj instanceof Collection) {
                Collection attrColl = (Collection) obj;
                return collection2TypedArray(attrColl, argClazz, config);
            }
        }

        return null;
    }

    /**
     * ��������JSONObjectת��Ϊ������Map
     *
     * @param obj  ������JSONObject
     * @param type ��Ҫת��������
     * @return java.lang.Object
     * @author Jerry.X.He
     * @date 4/29/2017 4:13 PM
     * @since 1.0
     */
    private static Map objToMap(JSONObject obj, Type type, JSONConfig config)
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

        putObjToMap(obj, result, type, config);
        return result;
    }

    /**
     * ��������JSONArrayת��Ϊ������List
     *
     * @param arr  ������JSONOArray
     * @param type ��Ҫת��������
     * @return java.lang.Object
     * @author Jerry.X.He
     * @date 4/29/2017 4:13 PM
     * @since 1.0
     */
    private static List arrToList(JSONArray arr, Type type, JSONConfig config)
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

        putArrayToColl(arr, result, type, config);
        return result;
    }

    /**
     * ��������JSONArrayת��Ϊ������Set
     *
     * @param arr  ������JSONOArray
     * @param type ��Ҫת��������
     * @return java.lang.Object
     * @author Jerry.X.He
     * @date 4/29/2017 4:13 PM
     * @since 1.0
     */
    private static Set arrToSet(JSONArray arr, Type type, JSONConfig config)
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

        putArrayToColl(arr, result, type, config);
        return result;
    }

    /**
     * ��������JSONArray����ΪĿ������[����]
     *
     * @param arr      ������JSONArray
     * @param config   ����json��config
     * @param argClazz Ŀ����������
     * @return java.lang.Object
     * @author Jerry.X.He
     * @date 4/29/2017 12:41 PM
     * @since 1.0
     */
    private static Object jsonArray2TypedArray(JSONArray arr, Class argClazz, JSONConfig config)
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
                attr[i] = toBean(arr.get(i), componentClazz, config);
            }
            return attr;
        }

        return null;
    }

    /**
     * �������ļ��Ͻ���ΪĿ������[����]
     *
     * @param attrColl �����ļ���
     * @param config   ����json��config
     * @param argClazz Ŀ������[����]
     * @return java.lang.Object
     * @author Jerry.X.He
     * @date 4/29/2017 12:40 PM
     * @since 1.0
     */
    private static Object collection2TypedArray(Collection attrColl, Class argClazz, JSONConfig config)
            throws IllegalAccessException, InstantiationException {
        if ((boolean[].class == argClazz) || (Boolean[].class == argClazz)) {
            if (boolean[].class == argClazz) {
                boolean[] attr = new boolean[attrColl.size()];
                int idx = 0;
                for (Object ele : attrColl) {
                    attr[idx++] = InnerTools.equalsIgnoreCase(InnerTools.TRUE, String.valueOf(ele));
                }
                return attr;
            } else if (Boolean[].class == argClazz) {
                Boolean[] attr = new Boolean[attrColl.size()];
                int idx = 0;
                for (Object ele : attrColl) {
                    attr[idx++] = InnerTools.equalsIgnoreCase(InnerTools.TRUE, String.valueOf(ele));
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
                attr[idx++] = toBean(ele, componentClazz, config);
            }
            return attr;
        }

        return null;
    }

    /**
     * ��������JSONArray�е�Ԫ����ӵ�result��
     *
     * @param arr    ������JSONArray
     * @param config ����json��config
     * @param result ��Ҫ��ӵ�Ŀ��Ԫ�ؼ���
     * @param type   ����Ԫ�ص�����
     * @return void
     * @author Jerry.X.He
     * @date 4/30/2017 10:30 AM
     * @since 1.0
     */
    private static void putArrayToColl(JSONArray arr, Collection result, Type type, JSONConfig config)
            throws IllegalAccessException, InstantiationException {
        // field specified type parameters
        if (type instanceof ParameterizedType) {
            Type typeParam = ((ParameterizedType) type).getActualTypeArguments()[0];
            for (int i = 0, len = arr.size(); i < len; i++) {
                result.add(toBean(arr.get(i), typeParam, config));
            }
        } else {
            result.addAll(arr);
        }
    }

    /**
     * ��������JSONObject�е�Ԫ����ӵ�result��
     *
     * @param obj    ������JSONObject
     * @param config ����json��config
     * @param result ��Ҫ��ӵ�Ŀ��Ԫ�ؼ���
     * @param type   ����Ԫ�ص�����
     * @return void
     * @author Jerry.X.He
     * @date 4/30/2017 10:30 AM
     * @since 1.0
     */
    private static void putObjToMap(JSONObject obj, Map result, Type type, JSONConfig config)
            throws IllegalAccessException, InstantiationException {
        // field specified type parameters
        if (type instanceof ParameterizedType) {
            Type keyTypeParam = ((ParameterizedType) type).getActualTypeArguments()[0];
            Type valueTypeParam = ((ParameterizedType) type).getActualTypeArguments()[1];
            for (Map.Entry<String, Object> entry : obj.entrySet()) {
                result.put(toBean(entry.getKey(), keyTypeParam, config), toBean(entry.getValue(), valueTypeParam, config));
            }
        } else {
            result.putAll(obj);
        }
    }

    /**
     * ��ȡ������Type��Ӧ��rawType
     * ע : ����û�д���TypeVariable, WildcardType [��Ϊ�����type���ֶ���ص�type]
     *
     * @param type ������Type
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
     * �жϸ�����Type�Ƿ���ParameterizedType ����GenericArrayType ��ʵ��
     *
     * @param type ������type
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
