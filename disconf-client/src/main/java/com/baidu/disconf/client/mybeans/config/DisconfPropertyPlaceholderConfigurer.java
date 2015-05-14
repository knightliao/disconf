package com.baidu.disconf.client.mybeans.config;

import java.io.IOException;
import java.util.Properties;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * Created by knightliao on 15/3/13.
 * <p/>
 * <p/>
 * 扩展配置类 实现 基于XML配置 Disconf
 * </p>
 */
public class DisconfPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

    /**
     * Load properties into the given instance.
     *
     * @throws IOException
     */
    @Override
    protected void loadProperties(Properties props) throws IOException {
        super.loadProperties(props);

    }
}
