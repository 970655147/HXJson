package com.hx.json;

import com.hx.json.interf.JSON;
import com.hx.json.interf.JSONConfig;
import com.hx.json.interf.JSONValueNodeParser;
import com.hx.json.util.JSONConstants;
import com.hx.log.str.WordsSeprator;
import com.hx.log.util.Tools;

/**
 * SimpleValueNodeParser
 *
 * @author Jerry.X.He <970655147@qq.com>
 * @version 1.0
 * @date 4/23/2017 2:49 PM
 */
public class SimpleValueNodeParser implements JSONValueNodeParser {

    @Override
    public JSON parse(WordsSeprator sep, String key, JSONConfig config) {
        Tools.assert0(sep.hasNext(), "expect an value for key : " + key);
        String next = sep.seek().trim();
        if (JSONConstants.OBJ_START.equals(next)) {
            return JSONObject.fromString(sep, config, false);
        } else if (JSONConstants.ARR_START.equals(next)) {
            return JSONArray.fromString(sep, config, false);
        } else if (next.startsWith(JSONConstants.STR_SEP01) || next.startsWith(JSONConstants.STR_SEP02)) {
            sep.next();
            return JSONStr.fromObject(JSONParseUtils.trimForSurroundSep(next, JSONConstants.KEY_SEPS));
            // take null check before 'l', "L"
        } else if (Tools.equalsIgnoreCase(JSONConstants.ELE_NULL, next)) {
            sep.next();
            return JSONObj.JSON_OBJ_NULL;
        } else if (Tools.equalsIgnoreCase(Tools.TRUE, next) || Tools.equalsIgnoreCase(Tools.FALSE, next)) {
            sep.next();
            return JSONBool.fromObject(Tools.equalsIgnoreCase(Tools.TRUE, next));
        } else if (JSONParseUtils.endsWith(next, JSONConstants.ELE_LONG_SUFFIXES)) {
            try {
                long longVal = Long.parseLong(next);
                sep.next();
                return JSONLong.fromObject(longVal);
            } catch (Exception e) {
                // ignore
            }
        } else if (JSONParseUtils.endsWith(next, JSONConstants.ELE_FLOAT_SUFFIXES) ||
                // if text with '.', default choose it as float
                (next.contains(".")) && (!JSONParseUtils.endsWith(next, JSONConstants.ELE_DOUBLE_SUFFIXES))) {
            try {
                float floatVal = Float.parseFloat(next);
                sep.next();
                return JSONFloat.fromObject(floatVal);
            } catch (Exception e) {
                // ignore
            }
        } else if (JSONParseUtils.endsWith(next, JSONConstants.ELE_DOUBLE_SUFFIXES)) {
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

}
