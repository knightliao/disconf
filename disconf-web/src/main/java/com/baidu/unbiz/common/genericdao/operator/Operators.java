/**
 * unbiz-genericdao#com.baidu.unbiz.common.genericdao.operator.Operators.java
 * 下午1:11:06 created by Darwin(Tianxin)
 */
package com.baidu.unbiz.common.genericdao.operator;

import java.util.List;

import com.baidu.unbiz.common.genericdao.param.IncrParam;

/**
 * @author Darwin(Tianxin)
 */

public class Operators {

    public static Order order(final String column, final boolean asc) {
        return new Order(column, asc);
    }

    public static Match match(final String column, final Object value) {
        return new Match(column, value);
    }

    public static Modify modify(String column, Object value) {
        return new Modify(column, value);
    }

    public static void analysisModify(List<Modify> modifies, StringBuilder sb, List<Object> params) {
        for (Modify modify : modifies) {

            String column = modify.getColumn();
            Object value = modify.getValue();
            if (value instanceof IncrParam) {
                sb.append(column).append("=(").append(column).append("+?),");
                params.add(((IncrParam) value).getValue());
            } else {
                sb.append(column).append("=?,");
                params.add(value);
            }
        }
        sb.deleteCharAt(sb.length() - 1);
    }

}
