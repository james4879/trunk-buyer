package com.datapush.buyhand.net.data;

public class Fashion {
	private String id;
	private int brandId;
	private String brandName;
	private String description;
	private String goodsTypeName;
	private String imagePath;
	private String goodsCrawlingTime;
	private String goodsUploadTime;
	private String latestime;
	private String price;
	private String imageId;
	private int collectFlag;
	private int collectCount;
	private String name;
	private int starLevel;
	private int matchLevel;

	public int getStarLevel() {
		return starLevel;
	}

	public void setStarLevel(int starLevel) {
		this.starLevel = starLevel;
	}

	public int getMatchLevel() {
		return matchLevel;
	}

	public void setMatchLevel(int matchLevel) {
		this.matchLevel = matchLevel;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getCollectCount() {
		return collectCount;
	}

	public void setCollectCount(int collectCount) {
		this.collectCount = collectCount;
	}

	public int getCollectFlag() {
		return collectFlag;
	}

	public void setCollectFlag(int collectFlag) {
		this.collectFlag = collectFlag;
	}

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public String getGoodsCrawlingTime() {
		return goodsCrawlingTime;
	}

	public void setGoodsCrawlingTime(String goodsCrawlingTime) {
		this.goodsCrawlingTime = goodsCrawlingTime;
	}

	public int getBrandId() {
		return brandId;
	}

	public void setBrandId(int brandId) {
		this.brandId = brandId;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getGoodsTypeName() {
		return goodsTypeName;
	}

	public void setGoodsTypeName(String goodsTypeName) {
		this.goodsTypeName = goodsTypeName;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getLatestime() {
		return latestime;
	}

	public void setLatestime(String latestime) {
		this.latestime = latestime;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getGoodsUploadTime() {
		return goodsUploadTime;
	}

	public void setGoodsUploadTime(String goodsUploadTime) {
		this.goodsUploadTime = goodsUploadTime;
	}

}
