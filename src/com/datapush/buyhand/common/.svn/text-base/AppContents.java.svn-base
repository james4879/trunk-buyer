package com.datapush.buyhand.common;

import com.datapush.buyhand.data.UserInfo;

import android.content.Context;
import android.content.SharedPreferences;



public class AppContents {

    // 用户信息
    private static UserInfo mUserInfo;

    public static void init(Context context) {
        mUserInfo = new UserInfo(context);
        Settings.DELAY_SHUTTER_TIME = getDelayShutterTime(context);
    }

    public static UserInfo getUserInfo() {
        return mUserInfo;
    }

    public static int getDelayShutterTime(Context context) {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt("delaytime", 0);
    }
}
