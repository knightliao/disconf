package com.baidu.disconf.web.service.user.vo;

import java.util.List;

public class UserListVo {

    private Long id;
    private String name;
    private String role;
    private Integer roleId;
    private List<String> ownAppList;
    private List<Long> ownAppIdList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<String> getOwnAppList() {
        return ownAppList;
    }

    public void setOwnAppList(List<String> ownAppList) {
        this.ownAppList = ownAppList;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public List<Long> getOwnAppIdList() {
        return ownAppIdList;
    }

    public void setOwnAppIdList(List<Long> ownAppIdList) {
        this.ownAppIdList = ownAppIdList;
    }
}
