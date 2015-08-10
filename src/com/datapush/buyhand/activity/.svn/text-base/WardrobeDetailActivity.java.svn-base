package com.datapush.buyhand.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daoshun.lib.communication.http.HttpAccessor;
import com.daoshun.lib.communication.http.JSONAccessor;
import com.daoshun.lib.util.DensityUtils;
import com.daoshun.lib.util.ImageLoader.OnLoadListener;
import com.daoshun.lib.view.OnSingleClickListener;
import com.datapush.buyhand.R;
import com.datapush.buyhand.common.AppContents;
import com.datapush.buyhand.common.Settings;
import com.datapush.buyhand.net.data.LoginParam;
import com.datapush.buyhand.net.data.LoginResult;
import com.datapush.buyhand.net.data.Wardrobe;
import com.datapush.buyhand.net.data.WardrobeDetailParam;
import com.datapush.buyhand.net.data.WardrobeDetailResult;
import com.datapush.buyhand.util.CacheImageLoader;

/**
 * 衣柜详细
 * 
 * @author Fei
 *
 */

public class WardrobeDetailActivity extends BaseActivity{
	
	private ImageView mBack;
	private GridView mGridView;
	private TextView mTitle;
	private List<Wardrobe> clothingList = new ArrayList<Wardrobe>();
	private WardrobeAdapter mWardrobeAdapter;
	private CacheImageLoader mCacheImageLoader;//图片下载共通
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wardrobedetail_layout);
		findView();
		initView();
		new WardrobeListTask().execute();
	}

	
	public void findView (){
		mCacheImageLoader = new CacheImageLoader(WardrobeDetailActivity.this);
		mBack = (ImageView) findViewById(R.id.back);
		mGridView = (GridView) findViewById(R.id.gridview);
		mTitle = (TextView) findViewById(R.id.title);
	}
	
	public void initView(){
		mTitle.setText(getIntent().getStringExtra("name"));
		mBack.setOnClickListener(new OnSingleClickListener() {
			
			@Override
			public void doOnClick(View v) {
				finish();
			}
		});
		
		mWardrobeAdapter = new WardrobeAdapter();
		mGridView.setAdapter(mWardrobeAdapter);
	}
	
	public class WardrobeAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return clothingList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return arg0;
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			HolerView holerView = null;
			if(arg1 == null){
				holerView = new HolerView();
				arg1 = LayoutInflater.from(WardrobeDetailActivity.this).inflate(R.layout.classification_item, arg2, false);
				holerView.table = (TextView) arg1.findViewById(R.id.table);
				holerView.img = (ImageView) arg1.findViewById(R.id.img);
				arg1.setTag(holerView);
			}else{
				holerView = (HolerView) arg1.getTag();
			}
			LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) holerView.img.getLayoutParams();
			int w = (Settings.DISPLAY_WIDTH - DensityUtils.dp2px(WardrobeDetailActivity.this, 0))/3;
			param.width = w;
			param.height = w*3/2;
			holerView.img.setLayoutParams(param);
			
			holerView.table.setText(clothingList.get(arg0).getClothingUploadTime());
			
			if(clothingList.get(arg0).getImagePath() != null && clothingList.get(arg0).getImagePath().length() >0){
				mCacheImageLoader.loadImage(clothingList.get(arg0).getImagePath(), holerView.img, new OnLoadListener() {
					
					@Override
					public void onLoad(Bitmap bitmap, View targetView) {
						if(bitmap != null){
							((ImageView) targetView).setImageBitmap(bitmap); 
						}
					}
				});
			}

			return arg1;
		}
		class HolerView{
			TextView table;
			ImageView img;
		}
	}
	
	//添加行程表
	private class WardrobeListTask extends AsyncTask<String, Void, WardrobeDetailResult> {
		
		@Override
		protected WardrobeDetailResult doInBackground(String... arg0) {

			JSONAccessor accessor = new JSONAccessor(WardrobeDetailActivity.this,HttpAccessor.METHOD_GET);
			accessor.enableJsonLog(true);
			WardrobeDetailParam param = new WardrobeDetailParam();
			param.setChestId(getIntent().getStringExtra("id"));
			return accessor.execute(Settings.WARDROBELIST, param, WardrobeDetailResult.class);
		}

		@Override
		protected void onPostExecute(WardrobeDetailResult result) {
			if (result != null) {
				if (result.getCode() == 0) {
					clothingList.addAll(result.getClothingList());
					mWardrobeAdapter.notifyDataSetChanged();
				}

			} else {
				Toast.makeText(WardrobeDetailActivity.this,getString(R.string.net_error), Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(result);
		}

	}

}
