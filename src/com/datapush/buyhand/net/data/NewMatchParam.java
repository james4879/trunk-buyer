package com.datapush.buyhand.net.data;

import java.io.File;

public class NewMatchParam {

	private String matchCategoryIds;
	private String thumbnail;
	private String userId;
	private File file;

	public String getMatchCategoryIds() {
		return matchCategoryIds;
	}

	public void setMatchCategoryIds(String matchCategoryIds) {
		this.matchCategoryIds = matchCategoryIds;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
}
