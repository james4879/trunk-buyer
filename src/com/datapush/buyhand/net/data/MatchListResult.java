package com.datapush.buyhand.net.data;

import java.util.ArrayList;
import java.util.List;

public class MatchListResult {

	private int code;
	private List<matchImage> matchList = new ArrayList<matchImage>();

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public List<matchImage> getMatchList() {
		return matchList;
	}

	public void setMatchList(List<matchImage> matchList) {
		this.matchList = matchList;
	}

}
