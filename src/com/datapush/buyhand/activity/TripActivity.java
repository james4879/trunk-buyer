package com.datapush.buyhand.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daoshun.lib.communication.http.HttpAccessor;
import com.daoshun.lib.communication.http.JSONAccessor;
import com.daoshun.lib.view.OnSingleClickListener;
import com.datapush.buyhand.R;
import com.datapush.buyhand.common.AppContents;
import com.datapush.buyhand.common.Settings;
import com.datapush.buyhand.net.data.Trip;
import com.datapush.buyhand.net.data.TripParam;
import com.datapush.buyhand.net.data.TripResult;

public class TripActivity extends BaseActivity{
	
	private ImageView mBack;
	private RelativeLayout rl2,rl3;
	private LinearLayout ly1,ly2,ly3,ly4,ly5,ly6,ly7,ly8,ly9;
	private TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7,tv8,tv9;
	private List<LinearLayout> ListLy = new ArrayList<LinearLayout>();
	private List<TextView> ListTv = new ArrayList<TextView>();
	private Dialog mAddDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trip_layout);
		findView();
		new TripListTask().execute();
	}
	
	public void findView(){
		mBack = (ImageView) findViewById(R.id.back);
		rl2 = (RelativeLayout) findViewById(R.id.rl2);
		rl3 = (RelativeLayout) findViewById(R.id.rl3);
		
		ly1 = (LinearLayout) findViewById(R.id.ly1);
		ly2 = (LinearLayout) findViewById(R.id.ly2);
		ly3 = (LinearLayout) findViewById(R.id.ly3);
		ly4 = (LinearLayout) findViewById(R.id.ly4);
		ly5 = (LinearLayout) findViewById(R.id.ly5);
		ly6 = (LinearLayout) findViewById(R.id.ly6);
		ly7 = (LinearLayout) findViewById(R.id.ly7);
		ly8 = (LinearLayout) findViewById(R.id.ly8);
		ly9 = (LinearLayout) findViewById(R.id.ly9);
		
		ListLy.add(ly1);
		ListLy.add(ly2);
		ListLy.add(ly3);
		ListLy.add(ly4);
		ListLy.add(ly5);
		ListLy.add(ly6);
		ListLy.add(ly7);
		ListLy.add(ly8);
		ListLy.add(ly9);
		
		
		tv1 = (TextView) findViewById(R.id.tv1);
		tv2 = (TextView) findViewById(R.id.tv2);
		tv3 = (TextView) findViewById(R.id.tv3);
		tv4 = (TextView) findViewById(R.id.tv4);
		tv5 = (TextView) findViewById(R.id.tv5);
		tv6 = (TextView) findViewById(R.id.tv6);
		tv7 = (TextView) findViewById(R.id.tv7);
		tv8 = (TextView) findViewById(R.id.tv8);
		tv9 = (TextView) findViewById(R.id.tv9);
		
		ListTv.add(tv1);
		ListTv.add(tv2);
		ListTv.add(tv3);
		ListTv.add(tv4);
		ListTv.add(tv5);
		ListTv.add(tv6);
		ListTv.add(tv7);
		ListTv.add(tv8);
		ListTv.add(tv9);

		mBack.setOnClickListener(new OnSingleClickListener() {
			
			@Override
			public void doOnClick(View v) {
					finish();
			}
		});
		
	}
	
private class TripListTask extends AsyncTask<String, Void, TripResult>{
	
		private ProgressDialog mProgressDialog;

		@Override
		protected void onPreExecute() {

			mProgressDialog = new ProgressDialog(TripActivity.this);
			mProgressDialog.setTitle(R.string.app_name);
			mProgressDialog.setMessage(getString(R.string.post));
			mProgressDialog.setCanceledOnTouchOutside(false);
			mProgressDialog.show();
		}

		@Override
		protected TripResult doInBackground(String... arg0) {
			
			JSONAccessor accessor = new JSONAccessor(TripActivity.this, HttpAccessor.METHOD_GET);
			accessor.enableJsonLog(true);
			TripParam param = new TripParam();
			param.setUserId(AppContents.getUserInfo().getId());
			return accessor.execute(Settings.TRIPLIST,param , TripResult.class);
		}
		
		@Override
		protected void onPostExecute(TripResult result) {
			
			mProgressDialog.dismiss();
			if(result != null){
				if(result.getCode() ==0){
					setTripList(result.getTripList());
				}
				
			} else {
				Toast.makeText(TripActivity.this, getString(R.string.net_error), Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(result);
		}

		
	}

	// 行程动态布局
	public void setTripList(final List<Trip> list) {
			
		if(list.size() >9){
			list.remove(list.size()-1);
		}
		if(list.size() >2){
			rl2.setVisibility(View.VISIBLE);
			if(list.size() >5){
				rl3.setVisibility(View.VISIBLE);
			}else{
				rl3.setVisibility(View.GONE);
			}
		}else{
			rl2.setVisibility(View.GONE);
		}
		for(int i=0;i<list.size();i++){
			
			final int index = i;
			ListLy.get(i).setOnClickListener(new OnSingleClickListener() {
				
				@Override
				public void doOnClick(View v) {
					
					if(!list.get(index).getId().equals("0")){
						//跳转
						Intent intent = new Intent(TripActivity.this,TripItemActivity.class);
						intent.putExtra("name", list.get(index).getName());
						intent.putExtra("id", list.get(index).getId());
						startActivity(intent);
					}else{
						//新增
						Eject();
					}
				}
			});
			
			
			ListLy.get(i).setVisibility(View.VISIBLE);
			if(!list.get(i).getId().equals("0")){
			ListLy.get(i).setSelected(false);
			ListTv.get(i).setVisibility(View.VISIBLE);
			ListTv.get(i).setText(list.get(i).getName());
			}else{
				ListTv.get(i).setVisibility(View.GONE);
				ListLy.get(i).setSelected(true);
			}
		}
		
		for (int i = list.size(); i <ListLy.size() ; i++) {
			
			ListLy.get(i).setVisibility(View.INVISIBLE);
		}
	}
	
	
	//弹出添加
	public void Eject(){
		
		if(mAddDialog != null){
			mAddDialog = null;
		}
		View view = LayoutInflater.from(TripActivity.this).inflate(R.layout.trip_bottom_dialog, null);
		mAddDialog = new Dialog(TripActivity.this, R.style.dialog);
		mAddDialog.setContentView(view);
		mAddDialog.show();
		
		Window w = mAddDialog.getWindow();
		WindowManager.LayoutParams lp = w.getAttributes();
		lp.gravity = Gravity.TOP;
//		lp.width = Settings.DISPLAY_WIDTH-DensityUtils.dp2px(TripActivity.this, 100);
//		lp.height = DensityUtils.dp2px(TripActivity.this, 200);
		w.setAttributes(lp);
		
		ImageView post = (ImageView) view.findViewById(R.id.post);
		ImageView delete = (ImageView) view.findViewById(R.id.delete);
		final EditText edit = (EditText) view.findViewById(R.id.edit); 
		
		post.setOnClickListener(new OnSingleClickListener() {
			
			@Override
			public void doOnClick(View v) {
				if(edit.getText().toString().trim().equals("")){
				Toast.makeText(TripActivity.this,getString(R.string.biaoti), Toast.LENGTH_SHORT).show();
				}else{
					mAddDialog.dismiss();
					new AddTripTask().execute(edit.getText().toString());
				}
			}
		});
		
		delete.setOnClickListener(new OnSingleClickListener() {
			
			@Override
			public void doOnClick(View v) {
				mAddDialog.dismiss();
			}
		});
	}
	
	//添加行程表
	private class AddTripTask extends AsyncTask<String, Void, TripResult> {
		
		
		private ProgressDialog mProgressDialog;

		@Override
		protected void onPreExecute() {

			mProgressDialog = new ProgressDialog(TripActivity.this);
			mProgressDialog.setTitle(R.string.app_name);
			mProgressDialog.setMessage(getString(R.string.tijiao));
			mProgressDialog.setCanceledOnTouchOutside(false);
			mProgressDialog.show();
		}

		@Override
		protected TripResult doInBackground(String... arg0) {

			JSONAccessor accessor = new JSONAccessor(TripActivity.this,HttpAccessor.METHOD_GET);
			accessor.enableJsonLog(true);
			TripParam param = new TripParam();
			param.setCategoryName(arg0[0]);
			param.setUserId(AppContents.getUserInfo().getId());
			return accessor.execute(Settings.ADDTRIP, param, TripResult.class);
		}

		@Override
		protected void onPostExecute(TripResult result) {
			mProgressDialog.dismiss();
			if (result != null) {
				if (result.getCode() == 0) {
					setTripList(result.getTripList());
				}

			} else {
				Toast.makeText(TripActivity.this,getString(R.string.net_error), Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(result);
		}

	}

}
