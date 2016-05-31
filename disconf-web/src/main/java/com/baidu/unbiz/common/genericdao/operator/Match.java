package com.baidu.unbiz.common.genericdao.operator;

/**
 * 一对参数
 *
 * @author Darwin(Tianxin)
 */
public class Match implements Pair {

    /**
     * @param column
     * @param value
     */
    public Match(String column, Object value) {
        super();
        this.column = column;
        this.value = value;
    }

    private String column;
    private Object value;

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}