package com.baidu.disconf.web.service.config.vo;

import java.util.List;

import com.baidu.disconf.web.service.zookeeper.dto.ZkDisconfData.ZkDisconfDataItem;

/**
 * @author knightliao
 */
public class MachineListVo {

    private List<ZkDisconfDataItem> datalist;
    private int errorNum = 0;
    private int machineSize;

    public int getMachineSize() {
        return machineSize;
    }

    public void setMachineSize(int machineSize) {
        this.machineSize = machineSize;
    }

    public List<ZkDisconfDataItem> getDatalist() {
        return datalist;
    }

    public void setDatalist(List<ZkDisconfDataItem> datalist) {
        this.datalist = datalist;
    }

    public int getErrorNum() {
        return errorNum;
    }

    public void setErrorNum(int errorNum) {
        this.errorNum = errorNum;
    }

    @Override
    public String toString() {
        return "MachineListVo [datalist=" + datalist + ", errorNum=" + errorNum + ", machineSize=" + machineSize + "]";
    }

}
