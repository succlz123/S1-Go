package org.succlz123.s1go.app.support.bean.hostthreads;

/**
 * Created by fashi on 2015/4/12.
 */
public class HotThreadsDate {
	private String tid;//帖子的id
	private int fid;//帖子所处板块id
	private String posttableid;
	private String typeid;//帖子标题的分类[]
	private String sortid;
	private String readperm;
	private String price;
	private String author;//帖子作者
	private String authorid;//帖子作者id
	private String subject;//帖子标题
	private String dateline;//发帖时间
	private String lastpost;//回帖时间
	private String lastposter;//最后回帖时间的作者
	private String views;//帖子查看数量
	private String replies;//回帖的数量
	private String displayorder;
	private String highlight;
	private String digest;
	private String rate;
	private String special;
	private String attachment;
	private String moderated;
	private String closed;
	private String stickreply;
	private String recommends;
	private String recommend_add;
	private String recommend_sub;
	private String heats;
	private String status;
	private String isgroup;
	private String favtimes;
	private String sharetimes;
	private String stamp;
	private String icon;
	private String pushedaid;
	private String cover;
	private String replycredit;
	private String relatebytag;
	private String maxposition;//应该是帖子楼数 默认回帖+1
	private String bgcolor;
	private String comments;
	private String lastposterenc;
	private String multipage;
	private int pages;//帖子页数 如果不满页 默认无返回(默认30楼一页)
	private String recommendicon;
	private String newnew;
	private String heatlevel;
	private String moved;
	private String icontid;
	private String folder;
	private String weeknew;
	private String istoday;//帖子是否是今天发的(是今天 默认1)(不是今天 默认0)
	private int dbdateline;
	private int dblastpost;
	private String id;
	private String rushreply;

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public int getFid() {
		return fid;
	}

	public void setFid(int fid) {
		this.fid = fid;
	}

	public String getPosttableid() {
		return posttableid;
	}

	public void setPosttableid(String posttableid) {
		this.posttableid = posttableid;
	}

	public String getTypeid() {
		return typeid;
	}

	public void setTypeid(String typeid) {
		this.typeid = typeid;
	}

	public String getSortid() {
		return sortid;
	}

	public void setSortid(String sortid) {
		this.sortid = sortid;
	}

	public String getReadperm() {
		return readperm;
	}

	public void setReadperm(String readperm) {
		this.readperm = readperm;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
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

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getDateline() {
		return dateline;
	}

	public void setDateline(String dateline) {
		this.dateline = dateline;
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

	public String getViews() {
		return views;
	}

	public void setViews(String views) {
		this.views = views;
	}

	public String getReplies() {
		return replies;
	}

	public void setReplies(String replies) {
		this.replies = replies;
	}

	public String getDisplayorder() {
		return displayorder;
	}

	public void setDisplayorder(String displayorder) {
		this.displayorder = displayorder;
	}

	public String getHighlight() {
		return highlight;
	}

	public void setHighlight(String highlight) {
		this.highlight = highlight;
	}

	public String getDigest() {
		return digest;
	}

	public void setDigest(String digest) {
		this.digest = digest;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public String getSpecial() {
		return special;
	}

	public void setSpecial(String special) {
		this.special = special;
	}

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	public String getModerated() {
		return moderated;
	}

	public void setModerated(String moderated) {
		this.moderated = moderated;
	}

	public String getClosed() {
		return closed;
	}

	public void setClosed(String closed) {
		this.closed = closed;
	}

	public String getStickreply() {
		return stickreply;
	}

	public void setStickreply(String stickreply) {
		this.stickreply = stickreply;
	}

	public String getRecommends() {
		return recommends;
	}

	public void setRecommends(String recommends) {
		this.recommends = recommends;
	}

	public String getRecommend_add() {
		return recommend_add;
	}

	public void setRecommend_add(String recommend_add) {
		this.recommend_add = recommend_add;
	}

	public String getRecommend_sub() {
		return recommend_sub;
	}

	public void setRecommend_sub(String recommend_sub) {
		this.recommend_sub = recommend_sub;
	}

	public String getHeats() {
		return heats;
	}

	public void setHeats(String heats) {
		this.heats = heats;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIsgroup() {
		return isgroup;
	}

	public void setIsgroup(String isgroup) {
		this.isgroup = isgroup;
	}

	public String getFavtimes() {
		return favtimes;
	}

	public void setFavtimes(String favtimes) {
		this.favtimes = favtimes;
	}

	public String getSharetimes() {
		return sharetimes;
	}

	public void setSharetimes(String sharetimes) {
		this.sharetimes = sharetimes;
	}

	public String getStamp() {
		return stamp;
	}

	public void setStamp(String stamp) {
		this.stamp = stamp;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getPushedaid() {
		return pushedaid;
	}

	public void setPushedaid(String pushedaid) {
		this.pushedaid = pushedaid;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getReplycredit() {
		return replycredit;
	}

	public void setReplycredit(String replycredit) {
		this.replycredit = replycredit;
	}

	public String getRelatebytag() {
		return relatebytag;
	}

	public void setRelatebytag(String relatebytag) {
		this.relatebytag = relatebytag;
	}

	public String getMaxposition() {
		return maxposition;
	}

	public void setMaxposition(String maxposition) {
		this.maxposition = maxposition;
	}

	public String getBgcolor() {
		return bgcolor;
	}

	public void setBgcolor(String bgcolor) {
		this.bgcolor = bgcolor;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getLastposterenc() {
		return lastposterenc;
	}

	public void setLastposterenc(String lastposterenc) {
		this.lastposterenc = lastposterenc;
	}

	public String getMultipage() {
		return multipage;
	}

	public void setMultipage(String multipage) {
		this.multipage = multipage;
	}

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

	public String getRecommendicon() {
		return recommendicon;
	}

	public void setRecommendicon(String recommendicon) {
		this.recommendicon = recommendicon;
	}

	public String getNewnew() {
		return newnew;
	}

	public void setNewnew(String newnew) {
		this.newnew = newnew;
	}

	public String getHeatlevel() {
		return heatlevel;
	}

	public void setHeatlevel(String heatlevel) {
		this.heatlevel = heatlevel;
	}

	public String getMoved() {
		return moved;
	}

	public void setMoved(String moved) {
		this.moved = moved;
	}

	public String getIcontid() {
		return icontid;
	}

	public void setIcontid(String icontid) {
		this.icontid = icontid;
	}

	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

	public String getWeeknew() {
		return weeknew;
	}

	public void setWeeknew(String weeknew) {
		this.weeknew = weeknew;
	}

	public String getIstoday() {
		return istoday;
	}

	public void setIstoday(String istoday) {
		this.istoday = istoday;
	}

	public int getDbdateline() {
		return dbdateline;
	}

	public void setDbdateline(int dbdateline) {
		this.dbdateline = dbdateline;
	}

	public int getDblastpost() {
		return dblastpost;
	}

	public void setDblastpost(int dblastpost) {
		this.dblastpost = dblastpost;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRushreply() {
		return rushreply;
	}

	public void setRushreply(String rushreply) {
		this.rushreply = rushreply;
	}
}
