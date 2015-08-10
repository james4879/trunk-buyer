package com.datapush.buyhand.net.data;
/**
 * 添衣 返回结果
 * @author yb
 *
 */
public class AddClothingResult {

	private String goodsId;//宝贝ID
	
	private String code;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
}
