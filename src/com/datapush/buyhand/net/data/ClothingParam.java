package com.datapush.buyhand.net.data;

import java.io.File;

/**
 * 添衣 参数
 * @author yb
 *
 */
public class ClothingParam {

	private String userId;//用户ID
	
	private String categoryId;//类目ID
	
	private String clothingId;//宝贝ID
	
	private String seasonId;//季节
	
	private String styleId;//风格
	
	private String brandId;//品牌
	
	private String priceId;//价格
	
	private String description;//描述
	
	private File file;
	
	private String thumbnail;//是否生成缩略图(0需要  1不需要)

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getSeasonId() {
		return seasonId;
	}

	public void setSeasonId(String seasonId) {
		this.seasonId = seasonId;
	}

	public String getStyleId() {
		return styleId;
	}

	public void setStyleId(String styleId) {
		this.styleId = styleId;
	}

	public String getPriceId() {
		return priceId;
	}

	public void setPriceId(String priceId) {
		this.priceId = priceId;
	}

	public String getClothingId() {
		return clothingId;
	}

	public void setClothingId(String clothingId) {
		this.clothingId = clothingId;
	}

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
}
