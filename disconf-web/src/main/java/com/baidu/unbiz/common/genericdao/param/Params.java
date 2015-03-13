/**
 * unbiz-genericdao#com.baidu.unbiz.common.genericdao.params.Params.java
 * 下午1:11:15 created by Darwin(Tianxin)
 */
package com.baidu.unbiz.common.genericdao.param;

/**
 * @author Darwin(Tianxin)
 */

public class Params {

    /**
     * 构造between查询的参数
     *
     * @param start
     * @param end
     *
     * @return 上午10:24:53 created by Darwin(Tianxin)
     */
    public static Object between(Object start, Object end) {
        return new BetweenParam(start, end);
    }

    /**
     * 构造大于参数
     *
     * @param value
     *
     * @return 上午10:32:37 created by Darwin(Tianxin)
     */
    public static Object greaterThan(Object value) {
        return new GreaterThanParam(value);
    }

    /**
     * 构造大于等於参数
     *
     * @param value
     *
     * @return 上午10:32:37 created by Darwin(Tianxin)
     */
    public static Object gte(Object value) {
        return new GteParam(value);
    }

    /**
     * 构造小于参数
     *
     * @param value
     *
     * @return 上午10:32:52 created by Darwin(Tianxin)
     */
    public static Object lessThan(Object value) {
        return new LessThanParam(value);
    }

    /**
     * 构造小于等於参数
     *
     * @param value
     *
     * @return 上午10:32:52 created by Darwin(Tianxin)
     */
    public static Object lte(Object value) {
        return new LteParam(value);
    }

    /**
     * 构造表达式参数
     *
     * @return 上午10:33:02 created by Darwin(Tianxin)
     */
    public static Object express() {
        return new ExpressionParam();
    }

    /**
     * 构造反向参数，包括not in !=
     *
     * @param value
     *
     * @return 上午10:33:11 created by Darwin(Tianxin)
     */
    public static Object not(Object value) {
        return new NotParam(value);
    }

    /**
     * 构造加法参数
     *
     * @param value
     *
     * @return
     */
    public static Object incr(Number value) {
        return new IncrParam(value);
    }

    /**
     * 构造like查询的参数
     *
     * @param searchWord
     *
     * @return
     */
    public static Object like(String searchWord) {
        return new LikeParam(searchWord);
    }
}
