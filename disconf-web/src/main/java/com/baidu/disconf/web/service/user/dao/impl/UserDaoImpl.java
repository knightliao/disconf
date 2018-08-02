package com.baidu.disconf.web.service.user.dao.impl;

import com.baidu.disconf.web.service.user.bo.User;
import com.baidu.disconf.web.service.user.dao.UserDao;
import com.baidu.disconf.web.service.user.form.UserModifyForm;
import com.baidu.disconf.web.utils.SHAUtil;
import com.baidu.dsp.common.dao.AbstractDao;
import com.baidu.dsp.common.dao.Columns;
import com.baidu.dsp.common.form.RequestListBase;
import com.baidu.dsp.common.utils.DaoUtils;
import com.baidu.ub.common.db.DaoPage;
import com.baidu.ub.common.db.DaoPageResult;
import com.baidu.unbiz.common.genericdao.operator.Match;
import com.baidu.unbiz.common.genericdao.operator.Modify;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liaoqiqi
 * @version 2013-11-28
 */
@Repository
public class UserDaoImpl extends AbstractDao<Long, User> implements UserDao {

    /**
     * 执行SQL
     */
    public void executeSql(String sql) {

        executeSQL(sql, null);
    }

    /**
     */
    @Override
    public User getUserByName(String name) {

        return findOne(match(Columns.NAME, name));
    }
    @Override
    public DaoPageResult<User> getUserList(RequestListBase.Page page) {

        DaoPage daoPage = DaoUtils.daoPageAdapter(page);
        List<Match> matchs = new ArrayList<Match>();


        return page2(matchs, daoPage);
    }
    @Override
    public void updateByUserForm(UserModifyForm userModifyForm) {
        List<Modify> modifyList = new ArrayList<Modify>();
        modifyList.add(modify(Columns.OWNAPPS, userModifyForm.getOwnApps()));
        modifyList.add(modify(Columns.ROLE_ID, userModifyForm.getRoleId()));
        update(modifyList, match(Columns.USER_ID, userModifyForm.getUserId()));
    }
    @Override
    public void updateUserProfile(User user){
        List<Modify> modifyList = new ArrayList<Modify>();
        modifyList.add(modify(Columns.PASSWORD, user.getPassword()));
        update(modifyList, match(Columns.USER_ID, user.getId()));
    }

}