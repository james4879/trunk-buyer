package com.datapush.buyhand.net.data;

import java.util.ArrayList;
import java.util.List;

public class CommentListResult {

	private int totalPage;
	private int code;
	private List<Comment> commentList = new ArrayList<Comment>();

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public List<Comment> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<Comment> commentList) {
		this.commentList = commentList;
	}

}
