package com.datapush.buyhand.net.data;

public class ClothingDetailMessage {

	private String id;

	private String name;

	private String imagePath;
	
	private String thumbnailPath;

	private String clothingUploadTime;

	private String imageId;

	public String getThumbnailPath() {
		return thumbnailPath;
	}

	public void setThumbnailPath(String thumbnailPath) {
		this.thumbnailPath = thumbnailPath;
	}

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getClothingUploadTime() {
		return clothingUploadTime;
	}

	public void setClothingUploadTime(String clothingUploadTime) {
		this.clothingUploadTime = clothingUploadTime;
	}
}
