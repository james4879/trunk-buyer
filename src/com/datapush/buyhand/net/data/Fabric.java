package com.datapush.buyhand.net.data;

public class Fabric {

	private String id;

	private String userName;

	private String title;// 昵称

	private String content;// 标题

	private String praiseCount;// 点赞数

	private String publishedTime;// 发布时间

	private String longPublishedTime;

	private String commentCount;// 评论数

	private String stepCount;// 踩数

	private String headPic;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPraiseCount() {
		return praiseCount;
	}

	public void setPraiseCount(String praiseCount) {
		this.praiseCount = praiseCount;
	}

	public String getPublishedTime() {
		return publishedTime;
	}

	public void setPublishedTime(String publishedTime) {
		this.publishedTime = publishedTime;
	}

	public String getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(String commentCount) {
		this.commentCount = commentCount;
	}

	public String getStepCount() {
		return stepCount;
	}

	public void setStepCount(String stepCount) {
		this.stepCount = stepCount;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getHeadPic() {
		return headPic;
	}

	public void setHeadPic(String headPic) {
		this.headPic = headPic;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getLongPublishedTime() {
		return longPublishedTime;
	}

	public void setLongPublishedTime(String longPublishedTime) {
		this.longPublishedTime = longPublishedTime;
	}

}
