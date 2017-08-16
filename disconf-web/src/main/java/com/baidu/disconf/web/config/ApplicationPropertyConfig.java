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

    private String emailProtocol;
    private String emailEncoding;


    private boolean emailMonitorOn = false;

    //
    // consistency
    //

    private boolean checkConsistencyOn = false;

    //
    // domain
    //
    private String domain;

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

    public String getEmailProtocol() {
        return emailProtocol;
    }

    public void setEmailProtocol(String emailProtocol) {
        this.emailProtocol = emailProtocol;
    }

    public String getEmailEncoding() {
        return emailEncoding;
    }

    public void setEmailEncoding(String emailEncoding) {
        this.emailEncoding = emailEncoding;
    }

    @Override
    public String toString() {
        return "ApplicationPropertyConfig{" +
                "emailHost='" + emailHost + '\'' +
                ", emailPassword='" + emailPassword + '\'' +
                ", emailUser='" + emailUser + '\'' +
                ", emailPort='" + emailPort + '\'' +
                ", fromEmail='" + fromEmail + '\'' +
                ", emailReceiver='" + emailReceiver + '\'' +
                ", emailProtocol='" + emailProtocol + '\'' +
                ", emailEncoding='" + emailEncoding + '\'' +
                ", emailMonitorOn=" + emailMonitorOn +
                ", checkConsistencyOn=" + checkConsistencyOn +
                ", domain='" + domain + '\'' +
                '}';
    }
}
