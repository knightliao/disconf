package com.baidu.disconf.client.usertools.impl;

import java.util.HashMap;
import java.util.Map;

import com.baidu.disconf.client.common.model.DisconfCenterFile;
import com.baidu.disconf.client.common.model.DisconfCenterItem;
import com.baidu.disconf.client.store.DisconfStoreProcessor;
import com.baidu.disconf.client.store.DisconfStoreProcessorFactory;
import com.baidu.disconf.client.usertools.IDisconfDataGetter;

/**
 * Created by knightliao on 16/5/28.
 */
public class DisconfDataGetterDefaultImpl implements IDisconfDataGetter {

    @Override
    public Map<String, Object> getByFile(String fileName) {

        DisconfStoreProcessor disconfStoreProcessor =
                DisconfStoreProcessorFactory.getDisconfStoreFileProcessor();

        DisconfCenterFile disconfCenterFile = (DisconfCenterFile) disconfStoreProcessor.getConfData(fileName);
        if (disconfCenterFile == null) {
            return new HashMap<String, Object>();
        }

        return disconfCenterFile.getKV();
    }

    @Override
    public Object getByFileItem(String fileName, String fileItem) {

        Map<String, Object> map = getByFile(fileName);

        if (map.containsKey(fileItem)) {

            return map.get(fileItem);
        }
        return null;
    }

    @Override
    public Object getByItem(String itemName) {

        DisconfStoreProcessor disconfStoreProcessor =
                DisconfStoreProcessorFactory.getDisconfStoreItemProcessor();

        DisconfCenterItem disconfCenterItem = (DisconfCenterItem) disconfStoreProcessor.getConfData(itemName);

        if (disconfCenterItem == null) {
            return null;
        }

        return disconfCenterItem.getValue();
    }
}
