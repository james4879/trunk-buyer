package com.datapush.buyhand.activity;

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daoshun.lib.communication.http.HttpAccessor;
import com.daoshun.lib.communication.http.JSONAccessor;
import com.datapush.buyhand.R;
import com.datapush.buyhand.common.AppContents;
import com.datapush.buyhand.common.Settings;
import com.datapush.buyhand.net.data.AuthCodeMessage;
import com.datapush.buyhand.net.data.AuthCodeParam;
import com.datapush.buyhand.net.data.LoginResult;
import com.datapush.buyhand.net.data.RegisterMessage;
import com.datapush.buyhand.net.data.RegisterParam;

/**
 * 用户注册界面
 * 
 * @author yb
 * 
 */
public class RegisterActivity extends BaseActivity {

	private String phoneNum;

	private String password;

	private String verifyPassword;

	private EditText phoneNum_Ed;

	private EditText password_Ed;

	private EditText verifyPassword_Ed;
	// 验证码
	private EditText authCode_Ed;
	// 用户输入的验证码
	private String authCode;
	// 获取验证码
	private RelativeLayout getCode;
	// "获取验证码"
	private ImageView getCodeIv;
	// 存放返回的验证码
	private TextView authCode_Tv;
	// 存放的验证码
	private String tvAuthCode;
	// 提交
	private RelativeLayout commit;
	// 计时View
	private TextView clockTv;
	// 用户昵称
	private EditText userName_ed;
	// 用户输入的昵称
	private String userName;

	private ProgressDialog pd;

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				getCodeIv.setVisibility(View.GONE);
				clockTv.setVisibility(View.VISIBLE);
				clockTv.setText(count + getString(R.string.obtain_again));
				getCode.setEnabled(false);
				break;
			case 1:
				getCodeIv.setVisibility(View.VISIBLE);
				clockTv.setVisibility(View.GONE);
				getCode.setEnabled(true);
				count = 25;
			case 3:
				commit.setEnabled(true);
			default:
				break;
			}
		};
	};

	int count = 25;

	Timer clockTimer = new Timer();

	TimerTask clockTask;

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_layout);
		init();
	}

	public void init() {
		phoneNum_Ed = (EditText) findViewById(R.id.phone_code);
		password_Ed = (EditText) findViewById(R.id.password_code);
		verifyPassword_Ed = (EditText) findViewById(R.id.verify_password_code);
		authCode_Ed = (EditText) findViewById(R.id.auth_code);
		userName_ed = (EditText) findViewById(R.id.user_name_ed);
		getCode = (RelativeLayout) findViewById(R.id.rl_get_auth_code);
		getCodeIv = (ImageView) findViewById(R.id.get_code);
		authCode_Tv = (TextView) findViewById(R.id.tv_auth_code);
		commit = (RelativeLayout) findViewById(R.id.commit_msg);
		clockTv = (TextView) findViewById(R.id.clock_tv);
	}

	public void clickMe(View view) {
		switch (view.getId()) {
		case R.id.rl_get_auth_code:
			obtainInput();
			if (!verifyPhoneNum()) {
				return;
			}
			// 获取验证码
			authCode_Tv.setText("");// 先清空
			commit.setEnabled(false);
			startClock();
			AuthCodeTask authTask = new AuthCodeTask();
			authTask.execute(Settings.GET_AUTH_CODE);
			break;
		case R.id.commit_msg:
			obtainInput();
			if (!verifyPhoneNum()) {
				return;
			}
			if(!verifyUserName()){
				return;
			}
			if (!verifyPassword()) {
				return;
			}
			if (!verifyAuthCode()) {
				return;
			}
			if (!authVerifyPassword()) {
				return;
			}
			// 提交注册
			RegisterTask registerTask = new RegisterTask();
			registerTask.execute(Settings.REGISTER);
		default:
			break;
		}
	}

	public void startClock() {
		clockTask = new TimerTask() {

			@Override
			public void run() {
				Message msg = handler.obtainMessage();
				if (count <= 0) {
					msg.what = 1;
					msg.sendToTarget();
					clockTask.cancel();
				} else {
					msg.what = 0;
					msg.sendToTarget();
				}
				count--;
			}
		};
		clockTimer.schedule(clockTask, 0, 1000);
	}

	public void obtainInput() {
		phoneNum = phoneNum_Ed.getText().toString();
		password = password_Ed.getText().toString();
		// 提交时,获取用户输入的密码
		verifyPassword = verifyPassword_Ed.getText().toString();
		authCode = authCode_Ed.getText().toString();
		userName = userName_ed.getText().toString();
	}

	public boolean verifyPhoneNum() {
		if (phoneNum == null || "".equals(phoneNum)) {
			Toast.makeText(this, getString(R.string.fillin_phone_num),
					Toast.LENGTH_SHORT).show();
			return false;
		}
		if (!authPhoneNum()) {
			Toast.makeText(this, getString(R.string.illegal_phone_num),
					Toast.LENGTH_SHORT).show();
			phoneNum_Ed.setText("");
			return false;
		}
		return true;
	}

	public boolean verifyUserName() {
		if (TextUtils.isEmpty(userName)) {
			Toast.makeText(this, getString(R.string.user_name_msg),
					Toast.LENGTH_SHORT).show();
			return false;
		}
		if (!authUserName()) {
			Toast.makeText(this, getString(R.string.user_name_format),
					Toast.LENGTH_SHORT).show();
			userName_ed.setText("");
			return false;
		}
		return true;
	}

	public boolean verifyPassword() {
		// 验证密码
		if (password == null || "".equals(password)) {
			Toast.makeText(this, getString(R.string.password_message),
					Toast.LENGTH_SHORT).show();
			return false;
		}
		if (!authPassword()) {
			Toast.makeText(this, getString(R.string.password_digit_msg),
					Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	public boolean verifyAuthCode() {
		tvAuthCode = authCode_Tv.getText().toString();
		// Toast.makeText(this, "verifyAuthCode.tvAuthCode="+tvAuthCode,
		// Toast.LENGTH_SHORT).show();
		if (tvAuthCode == null || "".equals(tvAuthCode)) {
			return false;
		} else {
			if (!tvAuthCode.equals(authCode)) {
				Toast.makeText(this, getString(R.string.auth_code_error),
						Toast.LENGTH_SHORT).show();
				return false;
			}
		}
		return true;
	}

	/**
	 * 验证手机格式
	 * 
	 */
	public boolean authPhoneNum() {
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(phoneNum);
		return m.matches();
	}

	/**
	 * 验证用户昵称
	 * 
	 * @return
	 */
	public boolean authUserName() {
		Pattern p = Pattern.compile("^\\w{1,}");
		Matcher m = p.matcher(userName);
		return m.matches();
	}

	/**
	 * 验证密码格式
	 * 
	 * @return
	 */
	public boolean authPassword() {
		Pattern p = Pattern.compile("^[a-zA-Z0-9]{5,15}$");
		Matcher m = p.matcher(password);
		return m.matches();
	}

	/**
	 * 验证两次输入密码是否一致
	 * 
	 * @return
	 */
	public boolean authVerifyPassword() {
		if (verifyPassword != null && !"".equals(verifyPassword)) {
			if (verifyPassword.equals(password)) {
				return true;
			}
		}
		Toast.makeText(this, getString(R.string.password_inconsistent),
				Toast.LENGTH_SHORT).show();
		return false;
	}

	/**
	 * 注册
	 * 
	 * @author Administrator
	 * 
	 */
	private class RegisterTask extends AsyncTask<String, Void, LoginResult> {

		@Override
		protected void onPreExecute() {
			showProgressDialog();
			super.onPreExecute();
		}

		@Override
		protected LoginResult doInBackground(String... url) {
			JSONAccessor ac = new JSONAccessor(RegisterActivity.this,
					HttpAccessor.METHOD_POST);
			ac.enableJsonLog(true);
			RegisterParam param = new RegisterParam();
			param.setPhoneNum(phoneNum);
			param.setPassword(password);
			param.setUserName(userName);
			if (getPhoneImei() != null && !"".equals(getPhoneImei())) {
				param.setIms(getPhoneImei());
			}
			return ac.execute(url[0], param, LoginResult.class);
		}

		@Override
		protected void onPostExecute(LoginResult result) {
			if (result != null) {
				if (result.getLoginStatus() == 0 && result.getUserinfo() != null) {
					pd.dismiss();
					Toast.makeText(RegisterActivity.this,
							getString(R.string.register_success),
							Toast.LENGTH_SHORT).show();
					AppContents.getUserInfo().saveUserInfo(result.getUserinfo());
					LoginActivity.COMMON_CONTEXT.finish();
					finish();
				} else {
					pd.dismiss();
					Toast.makeText(RegisterActivity.this,
							getString(R.string.register_failed),
							Toast.LENGTH_SHORT).show();
				}
			}
			super.onPostExecute(result);
		}

		private void showProgressDialog() {
			pd = new ProgressDialog(RegisterActivity.this);
			pd.setTitle(getString(R.string.register_pd_title));
			pd.setMessage(getString(R.string.register_pd_msg));
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.setCancelable(true);
			pd.show();
		}

		private String getPhoneImei() {
			String imei = null;
			TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			imei = manager.getDeviceId();
			return imei;
		}

	}

	/**
	 * 获取验证码
	 * 
	 */
	private class AuthCodeTask extends AsyncTask<String, Void, AuthCodeMessage> {

		@Override
		protected AuthCodeMessage doInBackground(String... url) {

			JSONAccessor ac = new JSONAccessor(RegisterActivity.this,
					HttpAccessor.METHOD_GET);
			ac.enableJsonLog(true);
			AuthCodeParam param = new AuthCodeParam();
			param.setPhoneNum(phoneNum);
			return ac.execute(url[0], param, AuthCodeMessage.class);
		}

		@Override
		protected void onPostExecute(AuthCodeMessage result) {
			if (result != null && "SUCCESS".equals(result.getMessage())) {
				authCode_Tv.setText(result.getCode());
				Message msg = handler.obtainMessage();
				msg.what = 3;
				msg.sendToTarget();
			}
			super.onPostExecute(result);
		}

	}

}
