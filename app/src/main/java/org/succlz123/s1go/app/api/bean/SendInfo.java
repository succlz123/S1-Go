package org.succlz123.s1go.app.api.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by succlz123 on 2015/4/20.
 */
public class SendInfo {

    /**
     * Version : 1
     * Charset : UTF-8
     * Variables : {"cookiepre":"B7Y9_2132_","auth":null,"saltkey":"Nw939kF3","member_uid":"0","member_username":"","groupid":"7","formhash":"137ce51a","ismoderator":"0","readaccess":"1","tid":"1343263","pid":"0"}
     * Message : {"messageval":"replyperm_login_nopermission//1","messagestr":"抱歉，您尚未登录，没有权限在该版块回帖"}
     */

    @SerializedName("Version")
    public String Version;
    @SerializedName("Charset")
    public String Charset;
    @SerializedName("Variables")
    public VariablesBean Variables;
    @SerializedName("Message")
    public SendInfo.Message Message;

    public static class VariablesBean {
        /**
         * cookiepre : B7Y9_2132_
         * auth : null
         * saltkey : Nw939kF3
         * member_uid : 0
         * member_username :
         * groupid : 7
         * formhash : 137ce51a
         * ismoderator : 0
         * readaccess : 1
         * tid : 1343263
         * pid : 0
         */

        @SerializedName("cookiepre")
        public String cookiepre;
        @SerializedName("auth")
        public Object auth;
        @SerializedName("saltkey")
        public String saltkey;
        @SerializedName("member_uid")
        public String memberUid;
        @SerializedName("member_username")
        public String memberUsername;
        @SerializedName("groupid")
        public String groupid;
        @SerializedName("formhash")
        public String formhash;
        @SerializedName("ismoderator")
        public String ismoderator;
        @SerializedName("readaccess")
        public String readaccess;
        @SerializedName("tid")
        public String tid;
        @SerializedName("pid")
        public String pid;
    }

    public static class Message {
        /**
         * messageval : replyperm_login_nopermission//1
         * messagestr : 抱歉，您尚未登录，没有权限在该版块回帖
         */

        @SerializedName("messageval")
        public String messageval;
        @SerializedName("messagestr")
        public String messagestr;
    }
}
