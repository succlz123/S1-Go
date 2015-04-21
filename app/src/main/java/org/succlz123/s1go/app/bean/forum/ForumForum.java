package org.succlz123.s1go.app.bean.forum;

/**
 * Created by fashi on 2015/4/14.
 */
public class ForumForum {
    private String autoclose;//
    private String fid;//板块id
    private String fup;//
    private String name;//板块名字
    private String password;
    private String posts;//板块总回复数
    private String rules;//板块头封面内容
    private String threads;//板块总主题数

    public String getAutoclose() {
        return autoclose;
    }

    public void setAutoclose(String autoclose) {
        this.autoclose = autoclose;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getFup() {
        return fup;
    }

    public void setFup(String fup) {
        this.fup = fup;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPosts() {
        return posts;
    }

    public void setPosts(String posts) {
        this.posts = posts;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public String getThreads() {
        return threads;
    }

    public void setThreads(String threads) {
        this.threads = threads;
    }
}
