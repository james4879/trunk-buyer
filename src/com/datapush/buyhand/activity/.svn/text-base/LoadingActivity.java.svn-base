package com.datapush.buyhand.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;
import com.daoshun.lib.communication.http.HttpAccessor;
import com.daoshun.lib.communication.http.JSONAccessor;
import com.datapush.buyhand.R;
import com.datapush.buyhand.common.AppContents;
import com.datapush.buyhand.common.Settings;
import com.datapush.buyhand.net.data.LoginParam;
import com.datapush.buyhand.net.data.LoginResult;


/**
 * 程序加载页面
 * 
 */
public class LoadingActivity extends BaseActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading_layout);
		if(AppContents.getUserInfo().getUserCode() != null && AppContents.getUserInfo().getUserCode().length() >0){
			new LoginTask().execute();
		}else{
			new Thread(new LoadingTask()).start();
		}
		mApplication.UpdateCategory();
	}
	
	/**
	 * 新开加载线程执行自动登录和主画面跳转
	 * 
	 */
	private class LoadingTask implements Runnable {
		
		@Override
		public void run() {
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
			}
			startActivity(new Intent(LoadingActivity.this, MenuActivity.class));
			finish();
		}
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {

	    switch (keyCode) {
	        case KeyEvent.KEYCODE_BACK:
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
	

	/**
	 * 登录线程
	 * 
	 */
	private class LoginTask extends AsyncTask<LoginParam, Void, LoginResult> {
		
		@Override
		protected LoginResult doInBackground(LoginParam... params) {
			
			JSONAccessor mAccessor = new JSONAccessor(LoadingActivity.this, HttpAccessor.METHOD_POST);
			LoginParam param = new LoginParam();
			param.setUsername(AppContents.getUserInfo().getUserCode());
			param.setPassword(AppContents.getUserInfo().getPassword());
			return mAccessor.execute(Settings.LOGIN, param, LoginResult.class);
		}
		
		@Override
		protected void onPostExecute(LoginResult result) {
			if (result != null) {
				if (result.getUserinfo() != null && result.getLoginStatus() == 0) {
					AppContents.getUserInfo().saveUserInfo(result.getUserinfo());
					new Thread(new LoadingTask()).start();
				} else {
//					AppContents.getUserInfo().logOut();
					new Thread(new LoadingTask()).start();
				}
			} else {
//				AppContents.getUserInfo().logOut();
//				Toast.makeText(LoadingActivity.this, getString(R.string.net_error), Toast.LENGTH_SHORT).show();
				new Thread(new LoadingTask()).start();
			}
		}
	}
}
