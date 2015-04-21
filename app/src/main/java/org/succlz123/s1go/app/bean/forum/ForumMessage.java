package org.succlz123.s1go.app.bean.forum;

/**
 * Created by fashi on 2015/4/15.
 */
public class ForumMessage {
    private String messagestr;//抱歉，您尚未登录，没有权限访问该版块
    private String messageval;//viewperm_login_nopermission//1

    public String getMessageval() {
        return messageval;
    }

    public void setMessageval(String messageval) {
        this.messageval = messageval;
    }

    public String getMessagestr() {

        return messagestr;
    }

    public void setMessagestr(String messagestr) {
        this.messagestr = messagestr;
    }
}
