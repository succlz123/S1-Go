package org.succlz123.s1go.app.bean.threads;

/**
 * Created by fashi on 2015/4/14.
 */
public class ThreadsList {
    String attachment;
    String author;//帖子作者
    String authorid;//帖子作者id
    String dateline;//帖子发布时间
    String dbdateline;
    String dblastpost;
    String digest;
    String lastpost;//最后回复时间
    String lastposter;//最后回复者
    String readperm;
    String replies;//回复数
    String subject;//帖子标题
    String tid;//帖子id
    String views;//查看次数

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

    public String getDblastpost() {
        return dblastpost;
    }

    public void setDblastpost(String dblastpost) {
        this.dblastpost = dblastpost;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getLastpost() {
        return lastpost;
    }

    public void setLastpost(String lastpost) {
        this.lastpost = lastpost;
    }

    public String getLastposter() {
        return lastposter;
    }

    public void setLastposter(String lastposter) {
        this.lastposter = lastposter;
    }

    public String getReadperm() {
        return readperm;
    }

    public void setReadperm(String readperm) {
        this.readperm = readperm;
    }

    public String getReplies() {
        return replies;
    }

    public void setReplies(String replies) {
        this.replies = replies;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }
}
