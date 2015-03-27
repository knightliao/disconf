package com.baidu.disconf.client.store.inner;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by knightliao on 15/3/20.
 * <p/>
 * reloadable file store
 */
public class DisconfCenterReloadableFileStore {

    protected static final Logger LOGGER = LoggerFactory.getLogger(DisconfCenterReloadableFileStore.class);

    private DisconfCenterReloadableFileStore() {

    }

    /**
     * 类级的内部类，也就是静态的成员式内部类，该内部类的实例与外部类的实例 没有绑定关系，而且只有被调用到时才会装载，从而实现了延迟加载。
     */
    private static class SingletonHolder {
        /**
         * 静态初始化器，由JVM来保证线程安全
         */
        private static DisconfCenterReloadableFileStore instance = new DisconfCenterReloadableFileStore();
    }

    public static DisconfCenterReloadableFileStore getInstance() {
        return SingletonHolder.instance;
    }

    private Set<String> reloadableFileSet = new HashSet<String>();

    public Set<String> get() {
        return reloadableFileSet;
    }

    public void add(String fileName) {
        this.reloadableFileSet.add(fileName);
    }
}
