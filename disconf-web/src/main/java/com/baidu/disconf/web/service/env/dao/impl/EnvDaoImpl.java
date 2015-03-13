package com.baidu.disconf.web.service.env.dao.impl;

import org.springframework.stereotype.Service;

import com.baidu.disconf.web.service.env.bo.Env;
import com.baidu.disconf.web.service.env.dao.EnvDao;
import com.baidu.dsp.common.dao.AbstractDao;
import com.baidu.dsp.common.dao.Columns;
import com.baidu.unbiz.common.genericdao.operator.Match;

/**
 * @author liaoqiqi
 * @version 2014-6-16
 */
@Service
public class EnvDaoImpl extends AbstractDao<Long, Env> implements EnvDao {

    @Override
    public Env getByName(String name) {

        return findOne(new Match(Columns.NAME, name));
    }

}
