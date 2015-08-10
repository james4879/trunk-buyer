package com.datapush.buyhand.net.data;

import java.io.File;

/**
 * 添衣 拍照(增加宝贝 参数)
 * @author yb
 *
 */
public class AddClothingParam {

	private String categoryId;//类目ID
	
	private File file;//图片
	
	public String userId;
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
}
