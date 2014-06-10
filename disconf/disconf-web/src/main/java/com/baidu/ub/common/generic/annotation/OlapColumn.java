package com.baidu.ub.common.generic.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Olap表字段与业务使用的字段映射
 * 
 * @author wangchongjie
 * @fileName OlapColumn.java
 * @dateTime 2014-1-3 下午2:37:25
 */
@Retention(RetentionPolicy.RUNTIME) 
public @interface OlapColumn {
	
	/**
	 * olap 中使用的列名
	 * @return
	 * 2014-1-3 下午2:39:50 created by wangchongjie
	 */
	String value();
}
