package org.succlz123.s1go.app.support.bean.login;

/**
 * Created by fashi on 2015/4/18.
 */
public class LoginVariables {
    private String auth;
    private String cookiepre;
    private String formhash;
    private String groupid;//用户组
    private String member_uid;//用户id
    private String member_username;//用户名
    private String readaccess;//阅读权
    private String saltkey;
    private String password;

//    private static LoginVariables instance;
//
//    public static LoginVariables getInstance() {
//        if (instance == null) {
//            instance = new LoginVariables();
//        }
//        return instance;
//    }
//
//    private LoginVariables() {
//
//    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getCookiepre() {
        return cookiepre;
    }

    public void setCookiepre(String cookiepre) {
        this.cookiepre = cookiepre;
    }

    public String getFormhash() {
        return formhash;
    }

    public void setFormhash(String formhash) {
        this.formhash = formhash;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getMember_uid() {
        return member_uid;
    }

    public void setMember_uid(String member_uid) {
        this.member_uid = member_uid;
    }

    public String getMember_username() {
        return member_username;
    }

    public void setMember_username(String member_username) {
        this.member_username = member_username;
    }

    public String getReadaccess() {
        return readaccess;
    }

    public void setReadaccess(String readaccess) {
        this.readaccess = readaccess;
    }

    public String getSaltkey() {
        return saltkey;
    }

    public void setSaltkey(String saltkey) {
        this.saltkey = saltkey;
    }
}
