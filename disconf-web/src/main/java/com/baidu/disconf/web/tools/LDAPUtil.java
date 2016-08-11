package com.baidu.disconf.web.tools;

import java.util.Properties;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LDAPUtil {

    private static final Log logger = LogFactory.getLog(LDAPUtil.class);

    private enum USER_TYPE {BEST_BG,BEST_BL, HT}

    private enum LDAP_TYPE {BEST, HT}

    public static final String INITIAL_CONTEXT_FACTORY = "com.sun.jndi.ldap.LdapCtxFactory";

    public static final String SECURITY_AUTHENTICATION = "simple";

    public static final String SEARCH_FILTER = "(&(objectClass=user)(sAMAccountName=*))";

    //BEST
    public static final String PROVIDER_URL_BEST = "ldap://800best.net:389";

    public static final String SEARCH_BASE_BEST = "DC=800best,DC=net";

    public static final String USER_DN_BEST = "sm";

    public static final String PASSWORD_BEST = "sm800best";

    public static final String DOMAIN_BEST = "800best";

    //HT
    public static final String PROVIDER_URL_HT = "ldap://hzhtdc1.ht.800best.net:389";

    public static final String SEARCH_BASE_HT = "DC=ht,DC=800best,DC=net";

    public static final String USER_DN_HT = "htjs";

    public static final String PASSWORD_HT = "ht1234js";

    public static final String DOMAIN_HT = "ht.800best.net";

    /**
     * 用户域验证
     *
     * @param userName
     * @param passwd
     * @return
     */
    public static boolean validateByLdap(String userName, String passwd) {
        if (StringUtils.isBlank(passwd)) {
            return false;
        }
        LdapContext ctx = null;
        if(getUserType(userName) == USER_TYPE.HT) {
             ctx = initLdap(userName, passwd, LDAP_TYPE.HT);
        }
        if(getUserType(userName) == USER_TYPE.BEST_BL) {
             ctx = initLdap(userName, passwd, LDAP_TYPE.BEST);
        }
        if(getUserType(userName) == USER_TYPE.BEST_BG) {
             ctx = initLdap(userName, passwd, LDAP_TYPE.BEST);
            if(ctx == null) {
                ctx = initLdap(userName, passwd, LDAP_TYPE.HT);
            }
        }
        return ctx != null;
    }

    /**
     * 检查域用户是否存在
     *
     * @param userName
     * @return
     */
    public static boolean searchByLdap(String userName) {
        if (StringUtils.isBlank(userName)) {
            return false;
        }
        if (getUserType(userName) == USER_TYPE.HT) {
            return searchLdap(userName, LDAP_TYPE.HT);
        }
        if (getUserType(userName) == USER_TYPE.BEST_BL) {
            return searchLdap(userName, LDAP_TYPE.BEST);
        }
        if (getUserType(userName) == USER_TYPE.BEST_BG) {
            if (!searchLdap(userName, LDAP_TYPE.BEST)) {
                return searchLdap(userName, LDAP_TYPE.HT);
            }
            return true;
        }
        return false;
    }

    private static boolean searchLdap(String userName, LDAP_TYPE ldapType) {
        LdapContext ctx = initLdap(getUserDn(ldapType), getPassword(ldapType), ldapType);
        if (ctx != null) {
            SearchControls searchControls = new SearchControls();
            searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            try {
                NamingEnumeration<SearchResult> results = ctx
                        .search(getSearchBase(ldapType), SEARCH_FILTER.replace("*", userName),
                                searchControls);
                if (results.hasMoreElements()) {
                    results.nextElement();
                    if (!results.hasMoreElements()) {
                        return true;
                    }
                }
            } catch (Exception e) {
                logger.info(e);
            }
        }
        return false;
    }

    private static LdapContext initLdap(String userName, String passwd, LDAP_TYPE ldapType) {
        String domain = getDomain(ldapType);
        Properties env = new Properties();
        env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
        env.put(Context.PROVIDER_URL, getProviderUrl(ldapType));
        env.put(Context.SECURITY_AUTHENTICATION, SECURITY_AUTHENTICATION);
        env.put(Context.SECURITY_PRINCIPAL, userName + "@" + domain);
        env.put(Context.SECURITY_CREDENTIALS, passwd);
        try {
            return new InitialLdapContext(env, null);
        } catch (Exception e) {
            logger.info(e);
            return null;
        }
    }

    /**
     * 根据用户名选择员工类型
     *
     * @param userName
     * @return
     */
    public static USER_TYPE getUserType(String userName) {
        if (StringUtils.containsIgnoreCase(userName, "H")) {
            // 百世快递员工
            return USER_TYPE.HT;
        }
        if(StringUtils.containsIgnoreCase(userName, "BG")) {
            return USER_TYPE.BEST_BG;
        }
        else {
            // best员工
            return USER_TYPE.BEST_BL;
        }
    }

    private static String getDomain(LDAP_TYPE ldapType) {
        return ldapType == LDAP_TYPE.HT ? DOMAIN_HT : DOMAIN_BEST;
    }

    private static String getUserDn(LDAP_TYPE ldapType) {
        return ldapType == LDAP_TYPE.HT ? USER_DN_HT : USER_DN_BEST;
    }

    private static String getPassword(LDAP_TYPE ldapType) {
        return ldapType == LDAP_TYPE.HT ? PASSWORD_HT : PASSWORD_BEST;
    }

    private static String getProviderUrl(LDAP_TYPE ldapType) {
        return ldapType == LDAP_TYPE.HT ? PROVIDER_URL_HT : PROVIDER_URL_BEST;
    }

    private static String getSearchBase(LDAP_TYPE ldapType) {
        return ldapType == LDAP_TYPE.HT ? SEARCH_BASE_HT : SEARCH_BASE_BEST;
    }

}
