package org.succlz123.s1go.app.bean.hostpost;

import java.util.List;

/**
 * Created by fashi on 2015/4/12.
 */
public class HotPostVariables {
    private String cookiepre;
    private String auth;
    private String saltkey;
    private int member_uid;//用户的id
    private String member_username;//用户的名字
    private int groupid;//用户所处的组
    private String formhash;
    private String ismoderator;
    private String readaccess;//用户的阅读权分数
    private List<HotPostDate> data;

    public String getCookiepre() {
        return cookiepre;
    }

    public void setCookiepre(String cookiepre) {
        this.cookiepre = cookiepre;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getSaltkey() {
        return saltkey;
    }

    public void setSaltkey(String saltkey) {
        this.saltkey = saltkey;
    }

    public int getMember_uid() {
        return member_uid;
    }

    public void setMember_uid(int member_uid) {
        this.member_uid = member_uid;
    }

    public String getMember_username() {
        return member_username;
    }

    public void setMember_username(String member_username) {
        this.member_username = member_username;
    }

    public int getGroupid() {
        return groupid;
    }

    public void setGroupid(int groupid) {
        this.groupid = groupid;
    }

    public String getFormhash() {
        return formhash;
    }

    public void setFormhash(String formhash) {
        this.formhash = formhash;
    }

    public String getIsmoderator() {
        return ismoderator;
    }

    public void setIsmoderator(String ismoderator) {
        this.ismoderator = ismoderator;
    }

    public String getReadaccess() {
        return readaccess;
    }

    public void setReadaccess(String readaccess) {
        this.readaccess = readaccess;
    }

    public List<HotPostDate> getHotPostDateList() {
        return data;
    }

    public void setHotPostDateList(List<HotPostDate> hotPostDateList) {
        this.data = hotPostDateList;
    }
}
