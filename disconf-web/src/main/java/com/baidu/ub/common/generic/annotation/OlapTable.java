package com.baidu.ub.common.generic.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Olap表相关信息
 * 
 * @author wangchongjie
 * @fileName OlapTable.java
 * @dateTime 2014-1-4 下午8:15:21
 */
@Retention(RetentionPolicy.RUNTIME) 
public @interface OlapTable {
	
	/**
	 * 表名
	 * @return
	 * 2014-1-4 下午8:21:45 created by wangchongjie
	 */
	String name();
	
	/**
	 * 需聚合的key列
	 * @return
	 * 2014-1-4 下午8:22:29 created by wangchongjie
	 */
	String[] keyVal() default {};
	
	/**
	 * 基础数据列
	 * @return
	 * 2014-1-4 下午8:24:33 created by wangchongjie
	 */
	String[] basicVal() default {};
	
	/**
	 * 扩展列名
	 * @return
	 * 2014-1-4 下午8:42:12 created by wangchongjie
	 */
	String[] extCol() default {};
	
	/**
	 * 扩展列表达式
	 * @return
	 * 2014-1-4 下午8:42:47 created by wangchongjie
	 */
	String[] extExpr() default {};
	
	/**
	 * 过滤表达式
	 * @return
	 * 2014-1-4 下午8:53:29 created by wangchongjie
	 */
	String[] filter() default {};
}
