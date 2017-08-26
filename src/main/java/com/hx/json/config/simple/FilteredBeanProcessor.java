package com.hx.json.config.simple;

import com.hx.common.util.InnerTools;
import com.hx.json.JSONArray;
import com.hx.json.JSONObject;
import com.hx.json.config.interf.JSONBeanProcessor;
import com.hx.json.config.interf.JSONConfig;

import java.util.Set;

/**
 * SimpleBeanProcessor
 *
 * @author Jerry.X.He <970655147@qq.com>
 * @version 1.0
 * @date 5/29/2017 11:23 AM
 */
public class FilteredBeanProcessor implements JSONBeanProcessor {

    /**
     * �Ƿ���Ҫ�� beforeToBean ��ʱ�� ����
     */
    private boolean filterBeforeToBean;
    /**
     * �Ƿ���Ҫ�� afterFromBean ��ʱ�� ����
     */
    private boolean filterAfterFromBean;
    /**
     * ��Ҫ���˵�����
     */
    private Set<String> filter;

    /**
     * ��ʼ��
     *
     * @param filterBeforeToBean  filterBeforeToBean
     * @param filterAfterFromBean filterAfterFromBean
     * @param filter              filter
     * @since 1.0
     */
    public FilteredBeanProcessor(boolean filterBeforeToBean, boolean filterAfterFromBean, Set<String> filter) {
        this.filterBeforeToBean = filterBeforeToBean;
        this.filterAfterFromBean = filterAfterFromBean;
        this.filter = filter;
    }

    @Override
    public <T> void beforeToBean(JSONObject obj, JSONConfig config, T receiver) {
        if ((!filterBeforeToBean) || (InnerTools.isEmpty(filter))) {
            return;
        }
        filter(obj, filter);
    }

    @Override
    public void afterFromBean(Object obj, JSONConfig config, JSONObject result) {
        if ((!filterAfterFromBean) || (InnerTools.isEmpty(filter))) {
            return;
        }
        filter(result, filter);
    }

    /**
     * ���˵� obj �� filter Ҳ���ڵ���Ŀ
     *
     * @param obj    obj
     * @param filter filter
     * @return void
     * @author Jerry.X.He
     * @date 5/29/2017 11:37 AM
     * @since 1.0
     */
    private void filter(JSONObject obj, Set<String> filter) {
        if (obj.size() < (filter.size() << 10)) {
            JSONArray names = obj.names();
            for (Object _key : names) {
                String key = (String) _key;
                if (filter.contains(key)) {
                    obj.remove(key);
                }
            }
        } else {
            for (String needToBeFilter : filter) {
                if (obj.containsKey(needToBeFilter)) {
                    obj.remove(needToBeFilter);
                }
            }
        }
    }

}
