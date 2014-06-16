package com.baidu.dsp.common.dao;

import java.io.Serializable;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.baidu.ub.common.generic.bo.BaseObject;
import com.baidu.ub.common.generic.dao.impl.GenericDao;

/**
 * 
 * @author Darwin(Tianxin)
 */
public abstract class AbstractDao<KEY extends Serializable, ENTITY extends BaseObject<KEY>>
        extends GenericDao<KEY, ENTITY> {

    @Override
    @Resource(name = "onedbJdbcTemplate")
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        super.setJdbcTemplate(jdbcTemplate);
    }

    /**
     * 该类是否有dspId一列
     */
    public AbstractDao() {
        super();
    }
}
