package com.baidu.disconf.web.tools;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf.web.service.role.bo.RoleEnum;
import com.baidu.disconf.web.service.sign.utils.SignUtils;
import com.baidu.disconf.web.service.user.bo.User;
import com.baidu.disconf.web.service.user.dao.UserDao;
import com.github.knightliao.apollo.utils.common.RandomUtil;

/**
 * @author knightliao
 */
public class UserCreateCommon {

    protected static final Logger LOG = LoggerFactory.getLogger(UserCreateCommon.class);

    /**
     * @param userName
     * @param password
     */
    public static void generateCreateSpecifyUserSQL(UserDao userDao, String userName, String password,
                                                    RoleEnum roleEnum, String ownAppIds) {

        User user = new User();

        user.setName(userName);

        user.setPassword(SignUtils.createPassword(password));
        // token
        user.setToken(SignUtils.createToken(userName));

        // set appids
        user.setOwnApps(ownAppIds);

        // role
        user.setRoleId(roleEnum.getValue());

        System.out.println("/* " + userName + "\t" + password + "*/");
        // userDao.create(user);

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
    public static void generateCreateTestUserSQL(UserDao userDao) {

        System.out.println("\n");

        int num = 5;

        List<User> userList = new ArrayList<User>();
        for (Long i = 1L; i < num + 1; ++i) {

            User user = new User();

            user.setId(i);

            user.setName(getUserName(i));

            user.setOwnApps("2");

            user.setRoleId(RoleEnum.NORMAL.getValue());

            int random = RandomUtil.random(0, 10000);
            String password = "MhxzKhl" + String.valueOf(random);
            user.setPassword(SignUtils.createPassword(password));
            // token
            user.setToken(SignUtils.createToken(user.getName()));

            System.out.println("/* userid" + user.getId() + "\t" + password + "*/");
            // userDao.create(user);
            userList.add(user);
        }

        printUserList(userList);
    }

    /**
     * @param userList
     */
    private static void printUserList(List<User> userList) {

        //
        //
        //

        for (User user : userList) {

            if (user.getId() != null) {
                System.out.format("DELETE FROM `user` where user_id=%d;\n", user.getId());
            }
            System.out
                    .format("INSERT INTO `user` (`user_id`, `name`, `password`, `token`, `ownapps`,`role_id`) VALUES "
                                    + "(%d,"
                                    +
                                    " '%s', " +
                                    "'%s', '%s','%s', '%d');\n", user.getId(), user.getName(), user.getPassword(),
                            user.getToken(), user.getOwnApps(), user.getRoleId());
        }
        System.out.println("\n");
    }
}
