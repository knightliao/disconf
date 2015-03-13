package com.baidu.ub.common.dbmanage.router;

public interface Router {

    /**
     * locate target virtualdatasource by routing rule
     *
     * @param userid
     * @param readMaster
     *
     * @return
     */
    String getTargetDataSourceKey(int userid, boolean readMaster);

}
