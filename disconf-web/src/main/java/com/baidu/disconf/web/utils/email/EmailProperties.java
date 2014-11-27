package com.baidu.disconf.web.utils.email;

import org.springframework.stereotype.Service;

/**
 * 邮箱的设置
 * 
 * @author knightliao
 * 
 */
@Service
public class EmailProperties {

    private String host;

    private String password;

    private String user;

    private String port;

    private String fromEmail;

    private String receiver;

    private boolean monitorOn;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Integer getPortInteger() {

        try {
            return Integer.parseInt(port);
        } catch (Exception e) {
            return 25;
        }
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public boolean isMonitorOn() {
        return monitorOn;
    }

    public void setMonitorOn(boolean monitorOn) {
        this.monitorOn = monitorOn;
    }

    @Override
    public String toString() {
        return "EmailProperties [host=" + host + ", password=" + password + ", user=" + user + ", port=" + port
                + ", fromEmail=" + fromEmail + ", receiver=" + receiver + ", monitorOn=" + monitorOn + "]";
    }

}
