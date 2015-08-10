package com.datapush.buyhand.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daoshun.lib.communication.http.HttpAccessor;
import com.daoshun.lib.communication.http.JSONAccessor;
import com.daoshun.lib.util.DensityUtils;
import com.daoshun.lib.view.OnSingleClickListener;
import com.datapush.buyhand.R;
import com.datapush.buyhand.common.Settings;
import com.datapush.buyhand.net.data.Catalog;
import com.datapush.buyhand.net.data.CatalogListParam;
import com.datapush.buyhand.net.data.CatalogListResult;
import com.datapush.buyhand.net.data.CatalogPostParam;

public class TripItemActivity extends BaseActivity{
	private ImageView mBack;
	private TextView mTitle;
	private GridView mGridView;
	private List<Catalog> tripDetailList = new ArrayList<Catalog>();
	private CatalogAdapter mCatalogAdapter;
	private Dialog mAddDialog;
	private int ckflg = -1;
	private String mCategoryId="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tripitem_layout);
		findView();
		initView();
		mCategoryId = getIntent().getStringExtra("id");
		new CatalogListTask().execute(getIntent().getStringExtra("id"));
	}
	
	public void findView(){
		mBack = (ImageView) findViewById(R.id.back);
		mTitle = (TextView) findViewById(R.id.title);
		mGridView = (GridView) findViewById(R.id.gridview);
	}
	public void initView(){
		mBack.setOnClickListener(new OnSingleClickListener() {
			
			@Override
			public void doOnClick(View v) {
				finish();
			}
		});
		mTitle.setText(getIntent().getStringExtra("name"));
		mCatalogAdapter = new CatalogAdapter();
		mGridView.setAdapter(mCatalogAdapter);
		
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if(tripDetailList.get(arg2).getId().equals("0")){
					Eject();
				}else{
					return;
				}
				
			}
		});
	}
	
	public class CatalogAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return tripDetailList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return arg0;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			HolerView holerView = null;
			if(arg1 == null){
				holerView = new HolerView();
				arg1 = LayoutInflater.from(TripItemActivity.this).inflate(R.layout.catalog_ietm, arg2, false);
				holerView.title = (TextView) arg1.findViewById(R.id.title);
				holerView.info = (TextView) arg1.findViewById(R.id.info);
				holerView.yue = (TextView) arg1.findViewById(R.id.yue);
				holerView.ri = (TextView) arg1.findViewById(R.id.ri);
				holerView.tiqian = (TextView) arg1.findViewById(R.id.tiqian);
				holerView.rl = (RelativeLayout) arg1.findViewById(R.id.rl);
				holerView.rl1 = (RelativeLayout) arg1.findViewById(R.id.rl1);
				holerView.rl2 = (RelativeLayout) arg1.findViewById(R.id.rl2);
				
				arg1.setTag(holerView);
			}else{
				holerView = (HolerView) arg1.getTag();
			}
			int w = (Settings.DISPLAY_WIDTH -DensityUtils.dp2px(TripItemActivity.this, 16))/2;
			RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holerView.rl.getLayoutParams();
			lp.width = w;
			lp.height = w;
			holerView.rl.setLayoutParams(lp);
			if(tripDetailList.get(arg0).getId().equals("0")){
				holerView.rl2.setVisibility(View.VISIBLE);
				holerView.rl1.setVisibility(View.GONE);
			}else{
				holerView.rl1.setVisibility(View.VISIBLE);
				holerView.rl2.setVisibility(View.GONE);
				holerView.title.setText(tripDetailList.get(arg0).getPlanTitle());
				holerView.info.setText(tripDetailList.get(arg0).getContent());
				holerView.yue.setText(tripDetailList.get(arg0).getPlanTime().split("-")[0]);
				holerView.ri.setText(tripDetailList.get(arg0).getPlanTime().split("-")[1]);
				if(tripDetailList.get(arg0).getNotifyFlag() ==0){
					holerView.tiqian.setText("提前一天");
				}else{
					holerView.tiqian.setText("提前三天");
				}
			}
			return arg1;
		}
		class HolerView{
			TextView title,info,yue,ri,juli,tiqian;
			RelativeLayout rl,rl1,rl2;
		}
		
	}
	
	//行程小目录
	private class CatalogListTask extends AsyncTask<String, Void, CatalogListResult> {
		
		
		private ProgressDialog mProgressDialog;

		@Override
		protected void onPreExecute() {

			mProgressDialog = new ProgressDialog(TripItemActivity.this);
			mProgressDialog.setTitle(R.string.app_name);
			mProgressDialog.setMessage(getString(R.string.post));
			mProgressDialog.setCanceledOnTouchOutside(false);
			mProgressDialog.show();
		}

		@Override
		protected CatalogListResult doInBackground(String... arg0) {

			JSONAccessor accessor = new JSONAccessor(TripItemActivity.this,HttpAccessor.METHOD_GET);
			accessor.enableJsonLog(true);
			CatalogListParam param = new CatalogListParam();
			param.setCategoryId(arg0[0]);
			return accessor.execute(Settings.CARALOGLIST, param, CatalogListResult.class);
		}

		@Override
		protected void onPostExecute(CatalogListResult result) {
			mProgressDialog.dismiss();
			if (result != null) {
				if (result.getCode() == 0) {

					if(result.getTripDetailList() != null){
						tripDetailList.clear();
					tripDetailList.addAll(result.getTripDetailList());
					}
					mCatalogAdapter.notifyDataSetChanged();
				}

			} else {
				Toast.makeText(TripItemActivity.this,getString(R.string.net_error), Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(result);
		}

	}
	
	
	//弹出添加
		public void Eject(){
			
			if(mAddDialog != null){
				mAddDialog = null;
			}
			View view = LayoutInflater.from(TripItemActivity.this).inflate(R.layout.trip_item_bottom_dialog, null);
			mAddDialog = new Dialog(TripItemActivity.this, R.style.dialog);
			mAddDialog.setContentView(view);
			mAddDialog.show();
			
			Window w = mAddDialog.getWindow();
			WindowManager.LayoutParams lp = w.getAttributes();
			lp.gravity = Gravity.BOTTOM;
			lp.y = -200;
			w.setAttributes(lp);
			
			ImageView post = (ImageView) view.findViewById(R.id.post);
			ImageView delete = (ImageView) view.findViewById(R.id.delete);
			final EditText edit = (EditText) view.findViewById(R.id.edit); 
			final EditText info = (EditText) view.findViewById(R.id.info); 
			final EditText yue = (EditText) view.findViewById(R.id.yue); 
			final EditText ri = (EditText) view.findViewById(R.id.ri); 
			final CheckBox one = (CheckBox) view.findViewById(R.id.one);
			final CheckBox three = (CheckBox) view.findViewById(R.id.three);
			
			one.setOnClickListener(new OnSingleClickListener() {
				
				@Override
				public void doOnClick(View v) {
					if(!v.isClickable()){
						ckflg = -1;
					}else{
						ckflg = 0;
						three.setChecked(false);
					}
					
				}
			});
			three.setOnClickListener(new OnSingleClickListener() {
				
				@Override
				public void doOnClick(View v) {
					if(!v.isClickable()){
						ckflg = -1;
					}else{
						ckflg = 1;
						one.setChecked(false);
					}
					
				}
			});
			
			post.setOnClickListener(new OnSingleClickListener() {
				
				@Override
				public void doOnClick(View v) {
					if(check(edit.getText().toString(), info.getText().toString(), yue.getText().toString(), ri.getText().toString())){
						mAddDialog.dismiss();
						new AddCatalogTask().execute(edit.getText().toString(), info.getText().toString(), yue.getText().toString(), ri.getText().toString());
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
		//添加一个新的闹钟
		private class AddCatalogTask extends AsyncTask<String, Void, CatalogListResult> {
			
			
			private ProgressDialog mProgressDialog;

			@Override
			protected void onPreExecute() {

				mProgressDialog = new ProgressDialog(TripItemActivity.this);
				mProgressDialog.setTitle(R.string.app_name);
				mProgressDialog.setMessage(getString(R.string.post));
				mProgressDialog.setCanceledOnTouchOutside(false);
				mProgressDialog.show();
			}

			@Override
			protected CatalogListResult doInBackground(String... arg0) {

				JSONAccessor accessor = new JSONAccessor(TripItemActivity.this,HttpAccessor.METHOD_GET);
				accessor.enableJsonLog(true);
				CatalogPostParam param = new CatalogPostParam();
				param.setCategoryId(mCategoryId);
				param.setPlanTitle(arg0[0]);
				param.setContent(arg0[1]);
				param.setPlanTime(arg0[2]+"-"+arg0[3]);
				param.setNotifyFlag(ckflg);
				return accessor.execute(Settings.ADDCATALOG, param, CatalogListResult.class);
			}

			@Override
			protected void onPostExecute(CatalogListResult result) {
				mProgressDialog.dismiss();
				if (result != null) {
					if (result.getCode() == 0) {

						if(result.getTripDetailList() != null){
							tripDetailList.clear();
						tripDetailList.addAll(result.getTripDetailList());
						}
						mCatalogAdapter.notifyDataSetChanged();
					}

				} else {
					Toast.makeText(TripItemActivity.this,getString(R.string.net_error), Toast.LENGTH_SHORT).show();
				}
				super.onPostExecute(result);
			}

		}
		// 判断传入的参数
		public boolean check(String title,String info,String yue,String ri){
			
			if(title.equals("")){
				Toast.makeText(TripItemActivity.this,"主题不能为空", Toast.LENGTH_SHORT).show();
				return false;
			}
			if(info.equals("")){
				Toast.makeText(TripItemActivity.this,"内容不能为空", Toast.LENGTH_SHORT).show();
				return false;
			}
			if(yue.equals("") || ri.equals("")){
				Toast.makeText(TripItemActivity.this,"时间不能为空", Toast.LENGTH_SHORT).show();
				return false;
			}
			if(Integer.parseInt(yue)<0 || Integer.parseInt(yue) >12){
				Toast.makeText(TripItemActivity.this,"月输入格式错误", Toast.LENGTH_SHORT).show();
				return false;
			}
			if(Integer.parseInt(yue)<0 || Integer.parseInt(yue) >31){
				Toast.makeText(TripItemActivity.this,"日输入格式错误", Toast.LENGTH_SHORT).show();
				return false;
			}
			if(ckflg == -1){
				Toast.makeText(TripItemActivity.this,"请选择提醒时间", Toast.LENGTH_SHORT).show();
				return false;
			}
			return true;
		}
		
}
