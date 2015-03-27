package com.baidu.disconf.client.test.model;

import org.springframework.stereotype.Service;

import com.baidu.disconf.client.common.annotations.DisconfFile;

/**
 * 空的分布式配置文件,用途有两种:<br/>
 * 1. 对配置文件里的内容不感兴趣，只是单纯的下载<br/>
 * 2. 当配置文件更新时，可以自动下载到本地
 */
@Service
@DisconfFile(filename = "empty.properties")
public class EmptyConf {

}
