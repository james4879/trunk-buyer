package com.datapush.buyhand.net.data;

import java.util.List;

public class FashionListResult {

	private List<Fashion> modelList;

	private int totalPage;

	public List<Fashion> getModelList() {
		return modelList;
	}

	public void setModelList(List<Fashion> modelList) {
		this.modelList = modelList;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

}
