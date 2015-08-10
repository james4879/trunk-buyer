package com.datapush.buyhand.activity;

/**
 * 图片位置记录器
 * 
 * @author yanpf
 *
 */

public class ImageState {
  private float left;
  private float top;
  private float right;
  private float bottom;

  @Override
  public String toString() {
	  
    return "[" + left + "," + top + "," + right + "," + bottom + "] (" + (right - left) + ","
        + (bottom - top) + ")";
  }
  public float getLeft() {
    return left;
  }

  public void setLeft(float left) {
    this.left = left;
  }

  public float getTop() {
    return top;
  }

  public void setTop(float top) {
    this.top = top;
  }

  public float getRight() {
    return right;
  }

  public void setRight(float right) {
    this.right = right;
  }

  public float getBottom() {
    return bottom;
  }

  public void setBottom(float bottom) {
    this.bottom = bottom;
  }

} 
