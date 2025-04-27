package com.java110.dto.notice;

import java.io.Serializable;

public class NoticeWechatDto implements Serializable {

    private String communityId;
    private String openId;
    private String title;

    private String notifyUserName;

    private String url;

    public NoticeWechatDto() {
    }

    /**
     * 通知实体
     * "流程名称", "发起时间", "发起人"
     *
     * @param communityId    小区
     * @param openId        通知人
     * @param title          流程名称
     * @param notifyUserName 发起人
     */
    public NoticeWechatDto(String communityId, String openId, String title, String notifyUserName) {
        this.communityId = communityId;
        this.openId = openId;
        this.title = title;
        this.notifyUserName = notifyUserName;
    }

    /**
     * 通知实体
     * "流程名称", "发起时间", "发起人"
     *
     * @param communityId    小区
     * @param openId        通知人
     * @param title          流程名称
     * @param notifyUserName 发起人
     * @param url            打开地址
     */
    public NoticeWechatDto(String communityId, String openId, String title, String notifyUserName, String url) {
        this.communityId = communityId;
        this.openId = openId;
        this.title = title;
        this.notifyUserName = notifyUserName;
        this.url = url;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotifyUserName() {
        return notifyUserName;
    }

    public void setNotifyUserName(String notifyUserName) {
        this.notifyUserName = notifyUserName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
