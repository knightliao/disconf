package com.baidu.disconf.web.config;

import org.springframework.stereotype.Service;

/**
 * @author knightliao
 */
@Service
public class ApplicationPropertyConfig {

    //
    // email setting
    //

    private String emailHost;

    private String emailPassword;

    private String emailUser;

    private String emailPort;

    private String fromEmail;

    private String emailReceiver;

    private boolean emailMonitorOn = false;

    //
    // consistency
    //

    private boolean checkConsistencyOn = false;

    //
    // domain
    //
    private String domain;
    /*公用基础配置对应的app name，配置中心中仅允许创建唯一此名称的app，用于管理各个不同app全部共用的配置 */
    private String common_base_app;

    public String getCommon_base_app() {
		return common_base_app;
	}

	public void setCommon_base_app(String common_base_app) {
		this.common_base_app = common_base_app;
	}

	public String getEmailHost() {
        return emailHost;
    }

    public void setEmailHost(String emailHost) {
        this.emailHost = emailHost;
    }

    public String getEmailPassword() {
        return emailPassword;
    }

    public void setEmailPassword(String emailPassword) {
        this.emailPassword = emailPassword;
    }

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    public String getEmailPort() {
        return emailPort;
    }

    public void setEmailPort(String emailPort) {
        this.emailPort = emailPort;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public boolean isEmailMonitorOn() {
        return emailMonitorOn;
    }

    public void setEmailMonitorOn(boolean emailMonitorOn) {
        this.emailMonitorOn = emailMonitorOn;
    }

    public boolean isCheckConsistencyOn() {
        return checkConsistencyOn;
    }

    public void setCheckConsistencyOn(boolean checkConsistencyOn) {
        this.checkConsistencyOn = checkConsistencyOn;
    }

    public String getEmailReceiver() {
        return emailReceiver;
    }

    public void setEmailReceiver(String emailReceiver) {
        this.emailReceiver = emailReceiver;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    @Override
    public String toString() {
        return "ApplicationPropertyConfig [emailHost=" + emailHost + ", emailPassword=" + emailPassword +
                   ", emailUser=" + emailUser + ", emailPort=" + emailPort + ", fromEmail=" + fromEmail +
                   ", emailReceiver=" + emailReceiver + ", emailMonitorOn=" + emailMonitorOn + ", checkConsistencyOn=" +
                   checkConsistencyOn + "]";
    }

}
