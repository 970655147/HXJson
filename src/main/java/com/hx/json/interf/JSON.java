package com.hx.json.interf;

/**
 * JSON
 *
 * @author Jerry.X.He <970655147@qq.com>
 * @version 1.0
 * @date 4/15/2017 11:45 AM
 */
public interface JSON {

    /**
     * 获取当前JSON的type
     *
     * @return true if currentStartIdx JSON is instance of JSONArray
     * @author Jerry.X.He
     * @date 4/15/2017 11:46 AM
     * @since 1.0
     */
    JSONType type();

    /**
     * 获取当前JSON包装的Object
     *
     * @return the object that current JSON wrapped
     * @author Jerry.X.He
     * @date 4/15/2017 11:46 AM
     * @since 1.0
     */
    Object value();

    /**
     * 获取当前JSON是否是数组
     *
     * @return true if there are no element in currentStartIdx JSON
     * @author Jerry.X.He
     * @date 4/15/2017 11:46 AM
     * @since 1.0
     */
    boolean isArray();

    /**
     * 获取当前JSON是否为空
     *
     * @return true if there are no element in currentStartIdx JSON
     * @author Jerry.X.He
     * @date 4/15/2017 11:46 AM
     * @since 1.0
     */
    boolean isEmpty();

    /**
     * 获取当前JSON是否为NULL[不合法的解析出现的空对象]
     *
     * @return true if there are no element in currentStartIdx JSON
     * @author Jerry.X.He
     * @date 4/15/2017 11:46 AM
     * @since 1.0
     */
    boolean isNull();

    /**
     * 获取当前JSON中元素的数量
     *
     * @return the element num of current JSON
     * @author Jerry.X.He
     * @date 4/15/2017 11:46 AM
     * @since 1.0
     */
    int size();

    /**
     * Make a prettyprinted JSON text.
     * <p>
     * Warning: This method assumes that the data structure is acyclical.
     *
     * @param indentFactor The number of spaces to add to each level of
     *        indentation.
     * @return a printable, displayable, portable, transmittable representation
     *         of the object, beginning with <code>{</code>&nbsp;<small>(left
     *         brace)</small> and ending with <code>}</code>&nbsp;<small>(right
     *         brace)</small>.
     * @author Jerry.X.He
     * @date 4/15/2017 11:46 AM
     * @since 1.0
     */
    String toString( int indentFactor );

}
