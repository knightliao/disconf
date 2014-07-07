package com.baidu.disconf2.web.tools;

import org.slf4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.baidu.disconf2.web.service.sign.utils.SignUtils;
import com.baidu.disconf2.web.service.user.bo.User;
import com.baidu.disconf2.web.service.user.dao.UserDao;
import com.baidu.ub.common.log.AopLogFactory;
import com.baidu.ub.common.utils.RandomUtil;

/**
 * 
 * 生成测试用户工具
 * 
 * @author liaoqiqi
 * @version 2014-2-8
 */
public class UserCreateTools {

    private static UserDao userDao;

    protected final static Logger LOG = AopLogFactory
            .getLogger(UserCreateTools.class);

    /**
     * @param args
     */
    public static void main(String[] args) {

        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext();
        ctx.getEnvironment().setActiveProfiles("production");
        ctx.setConfigLocation("applicationContext.xml");
        ctx.refresh();

        userDao = (UserDao) ctx.getBean("userDaoImpl");

        /**
         * 生成测试用户
         */
        createTestUser();
    }

    private static String getUserName(int i) {

        return "testUser" + i;
    }

    /**
     * 生成内测用户
     */
    private static void createTestUser() {

        int num = 5;

        for (int i = 0; i < num; ++i) {

            String userName = getUserName(i);
            LOG.info("delete " + userName);
            userDao.delete(i);
        }

        for (int i = 1; i < num + 1; ++i) {

            User user = new User();

            user.setId(i);

            user.setName(getUserName(i));

            int random = RandomUtil.random(0, 10000);
            String password = "MhxzKhl" + String.valueOf(random);
            user.setPassword(SignUtils.createPassword(password));
            // token
            user.setToken(SignUtils.createToken(user.getName()));

            LOG.info("create " + i + "\t" + user.toString() + "\t" + password);
            userDao.create(user);
        }
    }
}
