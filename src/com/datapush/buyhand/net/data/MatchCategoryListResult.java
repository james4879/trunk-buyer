package com.datapush.buyhand.net.data;

import java.util.ArrayList;
import java.util.List;

public class MatchCategoryListResult {

	private List<Category> categoryList = new ArrayList<Category>();

	public List<Category> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<Category> categoryList) {
		this.categoryList = categoryList;
	}

}
