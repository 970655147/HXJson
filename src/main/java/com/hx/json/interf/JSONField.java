package com.hx.json.interf;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * JSONField
 *
 * @author Jerry.X.He <970655147@qq.com>
 * @version 1.0
 * @date 5/1/2017 6:10 PM
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JSONField {

    /**
     * 获取当前字段转换为JSON之后的key
     *
     * @return
     * @author Jerry.X.He
     * @date 5/1/2017 6:11 PM
     * @since 1.0
     */
    String value();

}
