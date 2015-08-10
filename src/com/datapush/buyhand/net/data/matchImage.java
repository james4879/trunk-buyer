package com.datapush.buyhand.net.data;

public class matchImage {

	private Image matchImage;

	public Image getMatchImage() {
		return matchImage;
	}

	public void setMatchImage(Image matchImage) {
		this.matchImage = matchImage;
	}

	public class Image {

		private String id;
		private String imagePath;
		private String thumbnailPath;
		private String praiseCount;
		private String stepCount;

		public String getImagePath() {
			return imagePath;
		}

		public void setImagePath(String imagePath) {
			this.imagePath = imagePath;
		}

		public String getThumbnailPath() {
			return thumbnailPath;
		}

		public void setThumbnailPath(String thumbnailPath) {
			this.thumbnailPath = thumbnailPath;
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

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}
	}

}
