package com.baidu.ub.common.generic.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Olap非主表相关信息
 * 
 * @author wangchongjie
 * @fileName OlapTablePlus.java
 * @dateTime 2014-1-16 上午10:27:52
 */
@Retention(RetentionPolicy.RUNTIME) 
public @interface OlapTablePlus {
	
	/**
	 * Olap非主表元信息
	 * @return
	 * 2014-1-16 上午10:48:59 created by wangchongjie
	 */
	OlapTable[] value();
}
