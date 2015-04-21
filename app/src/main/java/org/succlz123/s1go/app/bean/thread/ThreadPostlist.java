package org.succlz123.s1go.app.bean.thread;

/**
 * Created by fashi on 2015/4/15.
 */
public class ThreadPostlist {
    private String adminid;
    private String anonymous;
    private String attachment;
    private String author;//作者
    private String authorid;//作者id
    private String dateline;//发帖时间
    private String dbdateline;
    private String first;
    private String groupid;
    private String memberstatus;
    private String message;//帖子内容
    private String number;
    private String pid;//应该是回帖的这条的信息id
    private String status;
    private String tid;//帖子id
    private String usernam;//用户名 回帖名 如果账号被删除 默认没有

    public String getAdminid() {
        return adminid;
    }

    public void setAdminid(String adminid) {
        this.adminid = adminid;
    }

    public String getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(String anonymous) {
        this.anonymous = anonymous;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthorid() {
        return authorid;
    }

    public void setAuthorid(String authorid) {
        this.authorid = authorid;
    }

    public String getDateline() {
        return dateline;
    }

    public void setDateline(String dateline) {
        this.dateline = dateline;
    }

    public String getDbdateline() {
        return dbdateline;
    }

    public void setDbdateline(String dbdateline) {
        this.dbdateline = dbdateline;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getMemberstatus() {
        return memberstatus;
    }

    public void setMemberstatus(String memberstatus) {
        this.memberstatus = memberstatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getUsernam() {
        return usernam;
    }

    public void setUsernam(String usernam) {
        this.usernam = usernam;
    }
}
