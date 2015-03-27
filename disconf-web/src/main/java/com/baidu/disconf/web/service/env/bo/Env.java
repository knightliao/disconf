package com.baidu.disconf.web.service.env.bo;

import com.baidu.dsp.common.dao.Columns;
import com.baidu.dsp.common.dao.DB;
import com.baidu.unbiz.common.genericdao.annotation.Column;
import com.baidu.unbiz.common.genericdao.annotation.Table;
import com.github.knightliao.apollo.db.bo.BaseObject;

/**
 * @author liaoqiqi
 * @version 2014-6-16
 */
@Table(db = DB.DB_NAME, name = "env", keyColumn = Columns.ENV_ID)
public class Env extends BaseObject<Long> {

    /**
     *
     */
    private static final long serialVersionUID = -665241738023640732L;

    /**
     *
     */
    @Column(value = Columns.NAME)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Env [name=" + name + "]";
    }

}
