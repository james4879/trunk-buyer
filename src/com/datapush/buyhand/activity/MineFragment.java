package com.datapush.buyhand.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.daoshun.lib.communication.http.HttpAccessor;
import com.daoshun.lib.communication.http.JSONAccessor;
import com.daoshun.lib.util.DensityUtils;
import com.daoshun.lib.view.OnSingleClickListener;
import com.datapush.buyhand.R;
import com.datapush.buyhand.code.NewCaptureActivity;
import com.datapush.buyhand.common.AppContents;
import com.datapush.buyhand.common.Settings;
import com.datapush.buyhand.net.data.SizeParam;
import com.datapush.buyhand.net.data.SizeResult;
import com.datapush.buyhand.net.data.WeatherParam;
import com.datapush.buyhand.net.data.WeatherResult;

/**
 * 我的
 * 
 * @author yanpf
 *
 */

public class MineFragment extends BaseFragment{
	
private MenuActivity mMenuActivity;
private RelativeLayout mMineTrip;//我的行程
private RelativeLayout mMineCollocation;//我的搭配
private RelativeLayout mMineCollection;//我的收藏
private RelativeLayout mMineWardrobe;//我的衣柜
private ImageButton mMineSettings;//我的设置
private Dialog mSettingDg;
private ImageButton mAddClothing;//添衣

private TextView mAddress,mTime,mChuanyi;

public LocationClient mLocationClient = null;
public BDLocationListener myListener = new MyLocationListener();
private TextView mDapeiTime,mDapeiSize,mYiTiSize,mBabySize,mYiGuiSize;

	
	@Override
	public void onAttach(Activity activity) {
		mMenuActivity = (MenuActivity) getActivity();
		super.onAttach(activity);
	}

	@Override
	public View onInitView(LayoutInflater inflater, ViewGroup container) {
		View view = inflater.inflate(R.layout.settings_layout, container, false);
		mMineTrip = (RelativeLayout) view.findViewById(R.id.mine_trip);
		mMineCollocation = (RelativeLayout) view.findViewById(R.id.mine_collocation);
		mMineCollection = (RelativeLayout) view.findViewById(R.id.mine_collection);
		mMineWardrobe = (RelativeLayout) view.findViewById(R.id.mine_wardrobe);
		mMineSettings = (ImageButton) view.findViewById(R.id.settings);
		mAddClothing = (ImageButton) view.findViewById(R.id.clothes);
		mAddress = (TextView) view.findViewById(R.id.dingwei);
		mTime = (TextView) view.findViewById(R.id.time);
		mChuanyi = (TextView) view.findViewById(R.id.chuanyi);
		mDapeiTime = (TextView) view.findViewById(R.id.dapeitime);
		mDapeiSize = (TextView) view.findViewById(R.id.dapeisize);
		mYiTiSize = (TextView) view.findViewById(R.id.yitisize);
		mBabySize = (TextView) view.findViewById(R.id.baobeisize);
		mYiGuiSize = (TextView) view.findViewById(R.id.yiguisize);
		ViewSize();
		initView();
		return view;
	}
	/**
	 * 动态设置控件大小
	 */
	public void ViewSize(){
		
		int w =  (Settings.DISPLAY_WIDTH-DensityUtils.dp2px(mMenuActivity, 12))/2;
		int h = (Settings.DISPLAY_HEIGHT-Settings.STATUS_BAR_HEIGHT-DensityUtils.dp2px(mMenuActivity, 109))/2;
		
		RelativeLayout.LayoutParams param0 = (RelativeLayout.LayoutParams)  mMineTrip.getLayoutParams();
		param0.width = w;
		param0.height = h;
		
		RelativeLayout.LayoutParams param1 = (RelativeLayout.LayoutParams)  mMineCollocation.getLayoutParams();
		param1.width = w;
		param1.height = h;
		
		RelativeLayout.LayoutParams param2 = (RelativeLayout.LayoutParams)  mMineCollection.getLayoutParams();
		param2.width = w;
		param2.height = h;
		
		RelativeLayout.LayoutParams param3 = (RelativeLayout.LayoutParams)  mMineWardrobe.getLayoutParams();
		param3.width = w;
		param3.height = h;
		
		mMineTrip.setLayoutParams(param0);
		mMineCollocation.setLayoutParams(param1);
		mMineCollection.setLayoutParams(param2);
		mMineWardrobe.setLayoutParams(param3);
		
	}

	public void initView(){
		
	    mLocationClient = new LocationClient(mMenuActivity);     //声明LocationClient类
	    mLocationClient.registerLocationListener( myListener );    //注册监听函数
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setScanSpan(500); // 设置定位模式，小于1秒则一次定位;大于等于1秒则定时定位
        option.setAddrType("all");
        mLocationClient.setLocOption(option);
        mLocationClient.start();
		
		mMineSettings.setOnClickListener(new OnSingleClickListener() {
			
			@Override
			public void doOnClick(View v) {
				 Eject();
			}
		});
		
		mMineTrip.setOnClickListener(new OnSingleClickListener() {
			
			@Override
			public void doOnClick(View v) {
				if(isLogin()){
				Intent intent = new Intent(mMenuActivity,TripActivity.class);
				startActivity(intent);
				}
			}
		});
		mMineWardrobe.setOnClickListener(new OnSingleClickListener() {
			
			@Override
			public void doOnClick(View v) {
				if(isLogin()){	
				Intent intent = new Intent(mMenuActivity,MyWardrobeActivity.class);
				startActivity(intent);
				}
			}
		});
		
		mAddClothing.setOnClickListener(new OnSingleClickListener() {
			
			@Override
			public void doOnClick(View v) {
				if(isLogin()){
				Intent intent = new Intent(mMenuActivity, AddClothingActivity.class);
				startActivity(intent);
				}
			}
		});
		mMineCollocation.setOnClickListener(new OnSingleClickListener() {
			
			@Override
			public void doOnClick(View v) {
				if(isLogin()){
				Intent intent = new Intent(mMenuActivity, MyMatchActivity.class);
				startActivity(intent);
				}
			}
		});
		mMineCollection.setOnClickListener(new OnSingleClickListener() {
			
			@Override
			public void doOnClick(View v) {
				if(isLogin()){
				Intent intent = new Intent(mMenuActivity, MyCollectionActivity.class);
				startActivity(intent);
				}
			}
		});
		
		
	}
	
	//弹出设置
	public void Eject(){
		
		if(mSettingDg != null){
			mSettingDg = null;
		}
		View view = LayoutInflater.from(mMenuActivity).inflate(R.layout.setting_layout, null);
		mSettingDg = new Dialog(mMenuActivity, R.style.dialog);
		mSettingDg.setContentView(view);
		mSettingDg.show();
		
		Window w = mSettingDg.getWindow();
		w.setWindowAnimations(R.style.dialog_anim_style_v14);
		WindowManager.LayoutParams lp = w.getAttributes();
		lp.gravity = Gravity.TOP;
		lp.x = (Settings.DISPLAY_WIDTH/2-Settings.DISPLAY_WIDTH)/2;
		lp.y = 0;
		lp.width = Settings.DISPLAY_WIDTH/2;
		w.setAttributes(lp);
		/**个人资料**/
		RelativeLayout personal = (RelativeLayout) view.findViewById(R.id.personal);
		personal.setOnClickListener(new OnSingleClickListener() {
			
			@Override
			public void doOnClick(View v) {
				if(isLogin()){
				mSettingDg.dismiss();
				Intent intent = new Intent(mMenuActivity,PersonalActivity.class);
				startActivity(intent);
				}
			}
		});
		RelativeLayout sweep = (RelativeLayout) view.findViewById(R.id.saoyisao);
		sweep.setOnClickListener(new OnSingleClickListener() {
			
			@Override
			public void doOnClick(View v) {
				if(isLogin()){
				mSettingDg.dismiss();
				Intent intent = new Intent(mMenuActivity,NewCaptureActivity.class);
				startActivity(intent);
				}
			}
		});
	}
	
	public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location != null){
				new WeatherTask().execute("101280101");
			}
		    mLocationClient.stop();
		}
	    public void onReceivePoi(BDLocation poiLocation) {}
	}
	
	
	private class WeatherTask extends AsyncTask<String, Void, WeatherResult> {
		
		
		@Override
		protected WeatherResult doInBackground(String... params) {
			
			JSONAccessor mAccessor = new JSONAccessor(mMenuActivity, HttpAccessor.METHOD_POST);
			WeatherParam param = new WeatherParam();
			param.setCityCode(params[0]);
			return mAccessor.execute("http://weather.51wnl.com/weatherinfo/GetMoreWeather", param, WeatherResult.class);
		}
		
		@Override
		protected void onPostExecute(WeatherResult result) {
			if (result != null ) {
				mChuanyi.setText(result.getWeatherinfo().getIndex_d());
				mTime.setText(result.getWeatherinfo().getDate_y()+" "+result.getWeatherinfo().getWeek());
				mAddress.setText(result.getWeatherinfo().getCity()+"/"+result.getWeatherinfo().getTemp1());
			} else {
//				Toast.makeText(mMenuActivity, getString(R.string.net_error), Toast.LENGTH_SHORT).show();
			}
		}

	}
	
	private class PersonalTask extends AsyncTask<String, Void, SizeResult> {
		
		
		@Override
		protected SizeResult doInBackground(String... params) {
			
			JSONAccessor mAccessor = new JSONAccessor(mMenuActivity, HttpAccessor.METHOD_POST);
			SizeParam param = new SizeParam();
			param.setUserId(AppContents.getUserInfo().getId());
			return mAccessor.execute(Settings.PERSONAL, param, SizeResult.class);
		}
		
		@Override
		protected void onPostExecute(SizeResult result) {
			if (result != null ) {
				if(result.getPersonal().getMatch().getNewestTime() != null){
					mDapeiTime.setText(result.getPersonal().getMatch().getNewestTime());
				}else{
					mDapeiTime.setText("暂无更新");
				}
				mDapeiSize.setText(result.getPersonal().getMatch().getMatchCount() + "套创作");
				mYiTiSize.setText("议题收藏" + result.getPersonal().getCollect().getTopicCount());
				mBabySize.setText("宝贝收藏" + result.getPersonal().getCollect().getGoodsCount());
				mYiGuiSize.setText("上传共"+result.getPersonal().getChest().getChestClothingCount()+"件");
				

			} else {
				Toast.makeText(mMenuActivity, getString(R.string.net_error), Toast.LENGTH_SHORT).show();
			}
		}

	}
	
	@Override
		public void onResume() {
			super.onResume();
			if(AppContents.getUserInfo().getId() != null && AppContents.getUserInfo().getId().length() > 0){
				new PersonalTask().execute();
			}
		}
	
	

}
