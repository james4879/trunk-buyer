package com.datapush.buyhand.activity;
import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.daoshun.lib.util.ImageLoader.OnLoadListener;
import com.daoshun.lib.view.OnSingleClickListener;
import com.datapush.buyhand.R;
import com.datapush.buyhand.common.AppContents;
import com.datapush.buyhand.common.Settings;
import com.datapush.buyhand.net.data.Choose;
import com.datapush.buyhand.util.BuyerShare;
import com.datapush.buyhand.util.CacheImageLoader;

public class HelpMeChooseActivity extends BaseActivity{
	
	private Dialog mPohotoDg;
	private ImageView mBack,mPost;
	private ImageView mImageView1,mImageView2,mImageView3,mImageView4,mImageView5,mImageView6;
	private LinearLayout mLayoutTop,mLayoutBottom;
	private EditText mTile,mInfo;
	private List<ImageView> mListView = new ArrayList<ImageView>();
	private int mState;
	private List<Choose> mChooseList = new ArrayList<Choose>();
	private static int FROM = 123;
	private CacheImageLoader mCacheImageLoader;//图片下载共通
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.helpmechoose_layout);
		mCacheImageLoader = new CacheImageLoader(HelpMeChooseActivity.this);
		findView();
		initView();
	}
	public void findView(){
		mBack = (ImageView) findViewById(R.id.back);
		mPost = (ImageView) findViewById(R.id.post);
		mLayoutTop = (LinearLayout) findViewById(R.id.top);
		mLayoutBottom = (LinearLayout) findViewById(R.id.bottom);
		mTile = (EditText) findViewById(R.id.title);
		mInfo = (EditText) findViewById(R.id.info);
		mImageView1 = (ImageView) findViewById(R.id.img1);
		mImageView2 = (ImageView) findViewById(R.id.img2);
		mImageView3 = (ImageView) findViewById(R.id.img3);
		mImageView4 = (ImageView) findViewById(R.id.img4);
		mImageView5 = (ImageView) findViewById(R.id.img5);
		mImageView6 = (ImageView) findViewById(R.id.img6);
		
		mListView.add(mImageView1);
		mListView.add(mImageView2);
		mListView.add(mImageView3);
		mListView.add(mImageView4);
		mListView.add(mImageView5);
		mListView.add(mImageView6);
		
	}
	
	public void initView(){
		
		int h = Settings.DISPLAY_WIDTH*5/12;
		mLayoutTop.getLayoutParams().height = h;
		mLayoutBottom.getLayoutParams().height = h;
		
		mPost.setOnClickListener(new OnSingleClickListener() {
			
			@Override
			public void doOnClick(View v) {
				if(check()){
					BuyerShare bs = new BuyerShare(HelpMeChooseActivity.this, mTile.getText().toString(), AppContents.getUserInfo().getId(),
							getStrId(), mInfo.getText().toString(), new BuyerShare.OnCallBack() {
								
								@Override
								public void callback() {
									finish();
								}
							});
					bs.share();
				}
			}
		});
		
		mBack.setOnClickListener(new OnSingleClickListener() {
			
			@Override
			public void doOnClick(View v) {
			    finish();
			}
		});
		for (int i = 0; i < mListView.size(); i++) {
			Choose co = new Choose();
			mChooseList.add(co);
			co = null;
			final int index = i;
			mListView.get(i).setOnClickListener(new OnSingleClickListener() {
				
				@Override
				public void doOnClick(View v) {
					mState = index;
					getDialog();
				}
			});
		}
	}
	
	//选择图片dialog
		public void getDialog(){
			
			if(mPohotoDg != null){
				mPohotoDg = null;
			}
			View view = LayoutInflater.from(HelpMeChooseActivity.this).inflate(R.layout.wenwein_dialog, null);
			mPohotoDg = new Dialog(HelpMeChooseActivity.this, R.style.dialog);
			mPohotoDg.setContentView(view);
			mPohotoDg.show();
			
			Window w = mPohotoDg.getWindow();
			w.setWindowAnimations(R.style.dialog_anim_style_v13);
			WindowManager.LayoutParams lp = w.getAttributes();
			lp.width = Settings.DISPLAY_WIDTH/2;
			w.setAttributes(lp);
			
			RelativeLayout yigui = (RelativeLayout) view.findViewById(R.id.tianyi);
			RelativeLayout dapei = (RelativeLayout) view.findViewById(R.id.dapei);
			RelativeLayout shoucang = (RelativeLayout) view.findViewById(R.id.shoucang);
			
			//我的衣柜
			yigui.setOnClickListener(new OnSingleClickListener() {
				
				@Override
				public void doOnClick(View v) {
					mPohotoDg.dismiss();
					Intent intent = new Intent(HelpMeChooseActivity.this, AddClothingActivity.class);
					intent.putExtra("from", 1);
					startActivityForResult(intent, FROM);
				}
			});
			//我的搭配
			dapei.setOnClickListener(new OnSingleClickListener() {
				
				@Override
				public void doOnClick(View v) {
					mPohotoDg.dismiss();
					Intent intent = new Intent(HelpMeChooseActivity.this, MyMatchActivity.class);
					intent.putExtra("from", 1);
					startActivityForResult(intent, FROM);
				}
			});
			//我的收藏
			shoucang.setOnClickListener(new OnSingleClickListener() {
				
				@Override
				public void doOnClick(View v) {
					mPohotoDg.dismiss();
					Intent intent = new Intent(HelpMeChooseActivity.this, MyCollectionActivity.class);
					intent.putExtra("from", 1);
					startActivityForResult(intent, FROM);
				}
			});
		}
		
		@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			super.onActivityResult(requestCode, resultCode, data);

			if (FROM == requestCode && RESULT_OK == resultCode) {

					mChooseList.get(mState).setImagepath(data.getStringExtra("imagePath"));
					mChooseList.get(mState).setId(data.getStringExtra("id"));
					setImage();
				}
			
		}
		public void setImage(){
			for (int i = 0; i < mListView.size(); i++) {
				if(mChooseList.get(i).getImagepath().length() > 0){
					mCacheImageLoader.loadImage(mChooseList.get(i).getImagepath(), mListView.get(i), new OnLoadListener() {
						
						@Override
						public void onLoad(Bitmap bitmap, View targetView) {
							if(bitmap != null){
								((ImageView) targetView).setImageBitmap(bitmap);
	
							}
						}
					});
				}
				
		}
	}
		
		public boolean check(){
			
			if(mTile.getText().toString().equals("")){
				Toast.makeText(HelpMeChooseActivity.this, "标题不能为空!", Toast.LENGTH_SHORT).show();
				return false;
			}
			if(mInfo.getText().toString().equals("")){
				Toast.makeText(HelpMeChooseActivity.this, "内容不能为空!", Toast.LENGTH_SHORT).show();
				return false;
			}
			if(checkNumber() < 2){
				Toast.makeText(HelpMeChooseActivity.this, "图片不能少于两张!", Toast.LENGTH_SHORT).show();
				return false;
			}
			
			return true;
		}
		
		public int checkNumber(){
			int number = 0;
			for (int i = 0; i < mChooseList.size(); i++) {
				
				if(mChooseList.get(i).getImagepath().length() > 0){
					number += 1;
				}
			}
			return number;
		}
		
		public String getStrId(){
			String str = "";
			for (int i = 0; i < mChooseList.size(); i++) {
				
				if(mChooseList.get(i).getId().length() >0){
					str = str+mChooseList.get(i).getId() + ",";
				}
			}
			return str.substring(0, str.length()-1);
		}

}
