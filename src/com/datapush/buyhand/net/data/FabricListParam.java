package com.datapush.buyhand.net.data;

public class FabricListParam {

	private int type;

	private int pageNo;

	private String latestime;

	private String commentCount;

	private String userId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public String getLatestime() {
		return latestime;
	}

	public void setLatestime(String latestime) {
		this.latestime = latestime;
	}

	public String getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(String commentCount) {
		this.commentCount = commentCount;
	}

}
