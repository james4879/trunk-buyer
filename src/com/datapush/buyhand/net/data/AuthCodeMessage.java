package com.datapush.buyhand.net.data;
/**
 * 获取验证码 信息
 * @author yb
 *
 */
public class AuthCodeMessage {

	//操作响应编码
	private String code;
	//提示信息
	private String message;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
