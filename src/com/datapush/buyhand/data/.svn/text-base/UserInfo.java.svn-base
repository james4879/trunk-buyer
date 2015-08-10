package com.datapush.buyhand.data;

import com.datapush.buyhand.common.Constants;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class UserInfo {

    private String userCode = "";//手机号(用户名)
    private String id = "";//用户ID
    private String headPic = "";//头像地址
    private String password = "";//密码
    private String userName = "";//昵称
    private String gender;//性别
    private String summary;//个人说明
    private String birthday;//出生日期
    private String userStyle;
    private Context context;
    public UserInfo(Context context) {
        this.context = context;

        SharedPreferences settings =
                context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        id = settings.getString("id", "");
        userCode = settings.getString("userCode", "");
        headPic = settings.getString("portrait", "");
        password = settings.getString("password", "");
        headPic = settings.getString("portrait", "");
        userName = settings.getString("userName", "");
        gender = settings.getString("gender", "");
        summary = settings.getString("summary", "");
        birthday = settings.getString("birthday", "");
        userStyle = settings.getString("userStyle", "");
    }

    public void saveUserInfo(UserInfo userinfo) {
        setPassword(userinfo.getPassword());
        setUserCode(userinfo.getUserCode());
        setHeadPic(userinfo.getHeadPic());
        setId(userinfo.getId());
        setUserName(userinfo.getUserName());
        setGender(userinfo.getGender());
        setSummary(userinfo.getSummary());
        setBirthday(userinfo.getBirthday());
        setUserStyle(userinfo.getUserStyle());

        SharedPreferences preferences =
                context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putString("password", getPassword());
        editor.putString("userCode", getUserCode());
        editor.putString("portrait", getHeadPic());
        editor.putString("id", getId());
        editor.putString("userName", getUserName());
        editor.putString("gender", getGender());
        editor.putString("summary", getSummary());
        editor.putString("birthday", getBirthday());
        editor.putString("userStyle", getUserStyle());
        
        editor.commit();
    }

    public void logOut() {
        SharedPreferences preferences =
                context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.remove("password");
        editor.remove("userCode");
        editor.remove("portrait");
        editor.remove("sessionid");
        editor.remove("id");
        editor.remove("gender");
        editor.remove("summary");
        editor.remove("birthday");
        editor.remove("userStyle");
        editor.commit();
        userCode = "";
        password = "";
        headPic = "";
        id = "0";
        userName = "";
        gender = "";
        summary = "";
        birthday = "";
        userStyle = "";
    }

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getHeadPic() {
		return headPic;
	}

	public void setHeadPic(String headPic) {
		this.headPic = headPic;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getUserStyle() {
		return userStyle;
	}

	public void setUserStyle(String userStyle) {
		this.userStyle = userStyle;
	}
    
    
}
