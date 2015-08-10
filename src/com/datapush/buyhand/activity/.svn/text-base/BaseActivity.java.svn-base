package com.datapush.buyhand.activity;

import com.datapush.buyhand.BuyHandAppApplication;
import com.datapush.buyhand.R;
import com.datapush.buyhand.common.AppContents;
import com.datapush.buyhand.common.Constants;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.widget.Toast;
/**
 * 父类
 * 
 * 所有的Acivity继承该类
 * 
 * @author yanpf
 *
 */
public class BaseActivity extends FragmentActivity {
	
    // 应用共通
    public static BuyHandAppApplication mApplication;
    // Fragment管理
    protected FragmentManager mFragmentManager;
    @SuppressWarnings("unused")
	private SharedPreferences mSharedPreferences;
	
	@SuppressWarnings("static-access")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        mApplication = (BuyHandAppApplication) getApplication();
        mFragmentManager = getSupportFragmentManager();
        mApplication.mActivityList.add(this);
        mSharedPreferences = getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
		
	}
	
	  // 释放内存
    @SuppressWarnings("static-access")
	@Override
    protected void onDestroy() {
        super.onDestroy();
        // 删除activity
        mApplication.mActivityList.remove(this);
    }

    // 返回activity的页面操作
    @Override
    protected void onResume() {
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_MEDIA_MOUNTED);
        intentFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
        intentFilter.addAction(Intent.ACTION_MEDIA_EJECT);
        intentFilter.addAction(Intent.ACTION_MEDIA_REMOVED);
        intentFilter.addAction(Intent.ACTION_MEDIA_SHARED);
        intentFilter.addAction(Intent.ACTION_MEDIA_BAD_REMOVAL);
        intentFilter.addDataScheme("file");
        super.onResume();
    }
    
    // 键盘点击事件处理
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
        case KeyEvent.KEYCODE_SEARCH:
            return false;
        }

        return super.onKeyDown(keyCode, event);
    }
	/**
	 * 判断是否登入
	 * 
	 * @return 
	 */
	public boolean isLogin(){
		if(AppContents.getUserInfo().getUserCode() !=null && AppContents.getUserInfo().getUserCode().length() >0){
			return true;
		}else{
			Toast.makeText(BaseActivity.this, getString(R.string.not_login_msg), Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(BaseActivity.this,LoginActivity.class);
			startActivity(intent);
		}
		return false;
	}
}
