package com.baidu.disconf2.inter;

/**
 * 配置更新 时 用户的回调函数来调用
 * 
 * @author liaoqiqi
 * @version 2014-5-20
 */
public interface IDisconfUpdate {

    public void reload() throws Exception;
}
