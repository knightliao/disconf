package com.baidu.disconf.web.tools;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.baidu.disconf.web.service.sign.utils.SignUtils;
import com.baidu.disconf.web.service.user.bo.User;
import com.baidu.disconf.web.service.user.dao.UserDao;
import com.baidu.ub.common.log.AopLogFactory;
import com.github.knightliao.apollo.utils.common.RandomUtil;

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

        /**
         * 生成指定用户
         */
        createSpecifyUser("dancai", "Asd123");
    }

    /**
     * 
     * @param userName
     * @param password
     */
    private static void createSpecifyUser(String userName, String password) {

        User user = new User();

        user.setName(userName);

        user.setPassword(SignUtils.createPassword(password));
        // token
        user.setToken(SignUtils.createToken(userName));

        LOG.info(user.toString() + "\t" + password);
        userDao.create(user);

        List<User> userList = new ArrayList<User>();
        userList.add(user);

        printUserList(userList);
    }

    private static String getUserName(Long i) {

        return "testUser" + i;
    }

    /**
     * 生成内测用户
     */
    private static void createTestUser() {

        int num = 5;

        for (Long i = 0L; i < num + 1; ++i) {

            String userName = getUserName(i);
            LOG.info("delete " + userName);
            userDao.delete(i);
        }

        List<User> userList = new ArrayList<User>();
        for (Long i = 1L; i < num + 1; ++i) {

            User user = new User();

            user.setId(i);

            user.setName(getUserName(i));

            int random = RandomUtil.random(0, 10000);
            String password = "MhxzKhl" + String.valueOf(random);
            user.setPassword(SignUtils.createPassword(password));
            // token
            user.setToken(SignUtils.createToken(user.getName()));

            LOG.info(user.toString() + "\t" + password);
            userDao.create(user);
            userList.add(user);
        }

        printUserList(userList);
    }

    /**
     * 
     * @param userList
     */
    private static void printUserList(List<User> userList) {

        //
        //
        //
        for (User user : userList) {

            System.out
                    .format("INSERT INTO `user` (`user_id`, `name`, `password`, `token`) VALUES (%d, '%s', '%s', '%s');\n",
                            user.getId(), user.getName(), user.getPassword(),
                            user.getToken());
        }
    }
}
