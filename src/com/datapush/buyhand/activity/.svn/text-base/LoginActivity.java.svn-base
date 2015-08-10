package com.datapush.buyhand.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.daoshun.lib.communication.http.HttpAccessor;
import com.daoshun.lib.communication.http.JSONAccessor;
import com.daoshun.lib.view.OnSingleClickListener;
import com.datapush.buyhand.R;
import com.datapush.buyhand.common.AppContents;
import com.datapush.buyhand.common.Settings;
import com.datapush.buyhand.net.data.LoginParam;
import com.datapush.buyhand.net.data.LoginResult;

/**
 * 登入
 * 
 * @author yanpf
 *
 */

public class LoginActivity extends BaseActivity{
	
	private RelativeLayout mPost;//登入按钮
	private ImageView mRegister;//注册
	private EditText mUserName,mPassWord;//用户名，密码
	public static LoginActivity COMMON_CONTEXT;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);
		findView();
		initView();
	}
	
	public void findView (){
		 mRegister = (ImageView) findViewById(R.id.register);
		 mPost = (RelativeLayout) findViewById(R.id.post);
		 mUserName = (EditText) findViewById(R.id.name);
		 mPassWord = (EditText) findViewById(R.id.password);
	}
	
	public void initView(){
		
		COMMON_CONTEXT = this;
		
		mPost.setOnClickListener(new OnSingleClickListener() {
			
			@Override
			public void doOnClick(View v) {
				if(cheke())
				new LoginTask().execute();
			}
		});
		
		mRegister.setOnClickListener(new OnSingleClickListener() {
			
			@Override
			public void doOnClick(View v) {
				Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
				startActivity(intent);
			}
		});
	}
	/**
	 * 登录线程
	 * 
	 */
	private class LoginTask extends AsyncTask<LoginParam, Void, LoginResult> {
		
		private ProgressDialog mProgressDialog;
		
		@Override
		protected void onPreExecute() {
			
				mProgressDialog = new ProgressDialog(LoginActivity.this);
				mProgressDialog.setTitle(R.string.app_name);
				mProgressDialog.setMessage(getString(R.string.dengluz));
				mProgressDialog.setCanceledOnTouchOutside(false);
				mProgressDialog.show();
		}
		
		@Override
		protected LoginResult doInBackground(LoginParam... params) {
			
			JSONAccessor mAccessor = new JSONAccessor(LoginActivity.this, HttpAccessor.METHOD_POST);
			LoginParam param = new LoginParam();
			param.setUsername(mUserName.getText().toString());
			param.setPassword(mPassWord.getText().toString());
			return mAccessor.execute(Settings.LOGIN, param, LoginResult.class);
		}
		
		@Override
		protected void onPostExecute(LoginResult result) {
			mProgressDialog.dismiss();
			if (result != null) {
				if (result.getUserinfo() != null && result.getLoginStatus() == 0) {
					finish();
					AppContents.getUserInfo().saveUserInfo(result.getUserinfo());
					Toast.makeText(LoginActivity.this, getString(R.string.chenggong), Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(LoginActivity.this, getString(R.string.shibai), Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(LoginActivity.this, getString(R.string.net_error), Toast.LENGTH_SHORT).show();
			}

		}

	}
	
	public boolean cheke(){
		
		if(mUserName.getText().toString().equals("") || mPassWord.getText().toString().equals("")){
			Toast.makeText(LoginActivity.this, getString(R.string.check), Toast.LENGTH_SHORT).show();
		}else{
			return true;
		}
		return false;
	}
}
