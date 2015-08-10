package com.datapush.buyhand.net.data;

public class BuyerTopicImageForm {
	private String id;
	private String imagePath;
	private String praiseCount;
	private String stepCount;
	private int praiseFlag;// 赞 0没选 1已选
	private int stepFlag;// 踩 0没选 1已选
	private String imageType;
	private String collectCount;

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getPraiseCount() {
		return praiseCount;
	}

	public void setPraiseCount(String praiseCount) {
		this.praiseCount = praiseCount;
	}

	public String getStepCount() {
		return stepCount;
	}

	public void setStepCount(String stepCount) {
		this.stepCount = stepCount;
	}

	public int getPraiseFlag() {
		return praiseFlag;
	}

	public void setPraiseFlag(int praiseFlag) {
		this.praiseFlag = praiseFlag;
	}

	public int getStepFlag() {
		return stepFlag;
	}

	public void setStepFlag(int stepFlag) {
		this.stepFlag = stepFlag;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getImageType() {
		return imageType;
	}

	public void setImageType(String imageType) {
		this.imageType = imageType;
	}

	public String getCollectCount() {
		return collectCount;
	}

	public void setCollectCount(String collectCount) {
		this.collectCount = collectCount;
	}

}
