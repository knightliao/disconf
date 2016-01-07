package com.baidu.disconf.core.common.restful.core;

/**
 * 一个可重试可执行方法
 *
 * @author liaoqiqi
 * @version 2014-6-10
 */
public interface UnreliableInterface {

    Object call() throws Exception;

}
