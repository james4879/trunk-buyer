package com.datapush.buyhand.net.data;

public class Comment {

	private String id;

	private String userName;

	private String title;// 昵称

	private String content;// 标题

	private String publishedTime;// 发布时间

	private String longPublishedTime;

	private String headPic;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

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

	public String getPublishedTime() {
		return publishedTime;
	}

	public void setPublishedTime(String publishedTime) {
		this.publishedTime = publishedTime;
	}

	public String getLongPublishedTime() {
		return longPublishedTime;
	}

	public void setLongPublishedTime(String longPublishedTime) {
		this.longPublishedTime = longPublishedTime;
	}

	public String getHeadPic() {
		return headPic;
	}

	public void setHeadPic(String headPic) {
		this.headPic = headPic;
	}

}
