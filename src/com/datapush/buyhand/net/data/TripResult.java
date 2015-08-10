package com.datapush.buyhand.net.data;

import java.util.ArrayList;
import java.util.List;

public class TripResult {

	private int code;
	private List<Trip> tripList = new ArrayList<Trip>();

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public List<Trip> getTripList() {
		return tripList;
	}

	public void setTripList(List<Trip> tripList) {
		this.tripList = tripList;
	}

}
