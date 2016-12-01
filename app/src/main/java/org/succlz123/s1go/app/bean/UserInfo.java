package org.succlz123.s1go.app.bean;

/**
 * Created by succlz123 on 16/4/24.
 */
public class UserInfo {

    /**
     * Version : 1
     * Charset : UTF-8
     * Variables : {"cookiepre":"B7Y9_2132_","auth":"86b8Rw5N8/Kf2aqDqQTVnqKlKP3e8788mPo2bRa4DaNx6m4TdxR7/vW/GC6d7NbA+66OTDWN1zqnbe0S3gEQj3tsjRY","saltkey":"ZmMZPYvM","member_uid":"182765","member_username":"sunjacker","groupid":"32","formhash":"fe1bb629","ismoderator":null,"readaccess":"80"}
     * Message : {"messageval":"location_login_succeed_mobile","messagestr":"欢迎您回来，sunjacker。点击进入登录前页面"}
     */

    public String Version;
    public String Charset;
    /**
     * cookiepre : B7Y9_2132_
     * auth : 86b8Rw5N8/Kf2aqDqQTVnqKlKP3e8788mPo2bRa4DaNx6m4TdxR7/vW/GC6d7NbA+66OTDWN1zqnbe0S3gEQj3tsjRY
     * saltkey : ZmMZPYvM
     * member_uid : 182765
     * member_username : sunjacker
     * groupid : 32
     * formhash : fe1bb629
     * ismoderator : null
     * readaccess : 80
     */

    public UserInfo.Variables Variables;
    /**
     * messageval : location_login_succeed_mobile
     * messagestr : 欢迎您回来，sunjacker。点击进入登录前页面
     */

    public UserInfo.Message Message;

    public static class Variables {
        public String cookiepre;
        public String auth;
        public String saltkey;
        public String member_uid;//用户id
        public String member_username;//用户名
        public String password;
        public String groupid;//用户组
        public String formhash;
        public Object ismoderator;
        public String readaccess;//阅读权
    }

    public static class Message {
        public String messageval;
        public String messagestr;
    }
}
