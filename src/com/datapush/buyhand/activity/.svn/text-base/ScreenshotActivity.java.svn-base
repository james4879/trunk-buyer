package com.datapush.buyhand.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.daoshun.lib.communication.http.HttpAccessor;
import com.daoshun.lib.communication.http.JSONAccessor;
import com.daoshun.lib.util.BitmapUtils;
import com.daoshun.lib.util.DensityUtils;
import com.daoshun.lib.view.OnSingleClickListener;
import com.datapush.buyhand.R;
import com.datapush.buyhand.common.AppContents;
import com.datapush.buyhand.common.Settings;
import com.datapush.buyhand.net.data.AddCategoryResult;
import com.datapush.buyhand.net.data.BaseResult;
import com.datapush.buyhand.net.data.Category;
import com.datapush.buyhand.net.data.LoginParam;
import com.datapush.buyhand.net.data.MatchCategoryListParam;
import com.datapush.buyhand.net.data.MatchCategoryListResult;
import com.datapush.buyhand.net.data.NewMatchParam;
import com.datapush.buyhand.net.data.UploadFileParam;
import com.datapush.buyhand.net.data.UploadFileResult;

public class ScreenshotActivity extends BaseActivity{
	
	private Button mPost;
	private ImageView mBack;
	private ImageView mImg;
	private LinearLayout mSetectFrameLayout,mNoSetectFrameLayout;
	private List<Category> category = new ArrayList<Category>();
	private ImageView mAdd;
	private Dialog mAddDialog;
	private String matchCategoryIds = "";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screenshot_layout);
		findView();
		initView();
		new CategoryTask().execute();
	}

	
	public void findView(){
		mPost = (Button) findViewById(R.id.post);
		mBack = (ImageView) findViewById(R.id.back);
		mImg = (ImageView) findViewById(R.id.img);
		mSetectFrameLayout = (LinearLayout) findViewById(R.id.select);
		mNoSetectFrameLayout = (LinearLayout) findViewById(R.id.noselect);
		mAdd = (ImageView) findViewById(R.id.add);
	}
	
	public void initView(){
		
		mBack.setOnClickListener(new OnSingleClickListener() {
			
			@Override
			public void doOnClick(View v) {
				finish();
			}
		});
		
		mAdd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Eject();
			}
		});
		mPost.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				forStr();
				if(matchCategoryIds.length() >0){
				new UploadFileTask().execute(getIntent().getStringExtra("str"));
				}else{
					Toast.makeText(ScreenshotActivity.this, "请选择类目!", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		int w = Settings.DISPLAY_WIDTH;
        int h = Settings.DISPLAY_HEIGHT - (((Settings.DISPLAY_WIDTH - DensityUtils.dp2px(ScreenshotActivity.this, 90)) / 5)*3/2+
        		Settings.STATUS_BAR_HEIGHT+DensityUtils.dp2px(ScreenshotActivity.this, 101));
		LinearLayout.LayoutParams pl = (LinearLayout.LayoutParams) mImg.getLayoutParams();
		pl.width =w;
		pl.height = h;
		mImg.setLayoutParams(pl);
		mImg.setImageBitmap(BitmapUtils.getBitmapFromFile(new File(getIntent().getStringExtra("str")), w, h));
		
	}
	
	/**
	 * 搭配目录
	 * 
	 */
	private class CategoryTask extends AsyncTask<LoginParam, Void, MatchCategoryListResult> {
		
		@Override
		protected MatchCategoryListResult doInBackground(LoginParam... params) {
			
			JSONAccessor mAccessor = new JSONAccessor(ScreenshotActivity.this, HttpAccessor.METHOD_POST);
			MatchCategoryListParam param = new MatchCategoryListParam();
			param.setUserId(AppContents.getUserInfo().getId());
			return mAccessor.execute(Settings.SCREENSHOT, param, MatchCategoryListResult.class);
		}
		
		@Override
		protected void onPostExecute(MatchCategoryListResult result) {
			if (result != null && result.getCategoryList() != null) {
				category.addAll(result.getCategoryList());
				setCategory();
			} else {
				Toast.makeText(ScreenshotActivity.this, getString(R.string.net_error), Toast.LENGTH_SHORT).show();
			}

		}

	}
	//设置类目数据
	public void setCategory(){
		mSetectFrameLayout.removeAllViewsInLayout();
		mNoSetectFrameLayout.removeAllViewsInLayout();
		for(final Category data : category){
			TextView tv = new TextView(ScreenshotActivity.this);
			tv.setText(data.getCategoryName());
			tv.setBackgroundResource(R.drawable.post_bg);
			tv.setTextColor(Color.WHITE);
			tv.setGravity( Gravity.CENTER);
			LinearLayout.LayoutParams pl = new LayoutParams((Settings.DISPLAY_WIDTH - DensityUtils.dp2px(ScreenshotActivity.this, 10))/5, 
					DensityUtils.dp2px(ScreenshotActivity.this, 30));
			pl.leftMargin = DensityUtils.dp2px(ScreenshotActivity.this, 2);
			
			tv.setOnClickListener(new OnSingleClickListener() {
				
				@Override
				public void doOnClick(View v) {
					if(data.getState() == 0){
						 data.setState(1);
					}else{
						data.setState(0);
					}
					setCategory();
				}
			});
			
			if(data.getState() == 1){
				mSetectFrameLayout.addView(tv, pl);
			}else{
				mNoSetectFrameLayout.addView(tv, pl);
			}
			if(mSetectFrameLayout.getChildCount() >0){
				mSetectFrameLayout.setVisibility(View.VISIBLE);
			}else{
				mSetectFrameLayout.setVisibility(View.GONE);
			}
			if(mNoSetectFrameLayout.getChildCount() >0){
				mNoSetectFrameLayout.setVisibility(View.VISIBLE);
			}else{
				mNoSetectFrameLayout.setVisibility(View.GONE);
			}
		}
	}
	
	
	//弹出添加
	public void Eject(){
		
		if(mAddDialog != null){
			mAddDialog = null;
		}
		View view = LayoutInflater.from(ScreenshotActivity.this).inflate(R.layout.screenshot_dialog, null);
		mAddDialog = new Dialog(ScreenshotActivity.this, R.style.dialog);
		mAddDialog.setContentView(view);
		mAddDialog.show();
		
		Window w = mAddDialog.getWindow();
		WindowManager.LayoutParams lp = w.getAttributes();
		lp.gravity = Gravity.BOTTOM;
		lp.y = -200;
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
				Toast.makeText(ScreenshotActivity.this,getString(R.string.biaoti), Toast.LENGTH_SHORT).show();
				}else{
					mAddDialog.dismiss();
					new AddTask().execute(edit.getText().toString());
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
	
	private class AddTask extends AsyncTask<String , Void, AddCategoryResult> {
		
		String name = "";
		
		@Override
		protected AddCategoryResult doInBackground(String ... params) {
			
			JSONAccessor mAccessor = new JSONAccessor(ScreenshotActivity.this, HttpAccessor.METHOD_POST);
			MatchCategoryListParam param = new MatchCategoryListParam();
			param.setUserId(AppContents.getUserInfo().getId());
			param.setName(params[0]);
			name = params[0];
			return mAccessor.execute(Settings.ADDCATEGORY, param, AddCategoryResult.class);
		}
		
		@Override
		protected void onPostExecute(AddCategoryResult result) {
			if (result != null && result.getCode() == 0) {
				Category cg = new Category();
				cg.setCategoryName(name);
				cg.setId(result.getCategoryId());
				category.add(cg);
				setCategory();
			} else {
				Toast.makeText(ScreenshotActivity.this, getString(R.string.net_error), Toast.LENGTH_SHORT).show();
			}

		}

	}
	
	public class UploadFileTask extends AsyncTask<String, Void, BaseResult> {
		
		private ProgressDialog mProgressDialog;
		
		@Override
		protected void onPreExecute() {
			
				mProgressDialog = new ProgressDialog(ScreenshotActivity.this);
				mProgressDialog.setTitle(R.string.app_name);
				mProgressDialog.setMessage(getString(R.string.tijiao));
				mProgressDialog.setCanceledOnTouchOutside(false);
				mProgressDialog.show();
		}
	    
	    @Override
	    protected BaseResult doInBackground(String... params) {
	    	
	    	JSONAccessor mAccessor = new JSONAccessor(ScreenshotActivity.this, HttpAccessor.METHOD_POST_MULTIPART);
	        File file = new File(params[0]);

	        if (!file.exists() || !file.isFile())
	            return null;
	        NewMatchParam param = new NewMatchParam();
	        param.setFile(file);
	        param.setMatchCategoryIds(matchCategoryIds);
	        param.setThumbnail("2");
	        param.setUserId(AppContents.getUserInfo().getId());
	        return mAccessor.execute(Settings.FILE_URL, param, BaseResult.class);
	    }
	    
		@Override
		protected void onPostExecute(BaseResult result) {
			mProgressDialog.dismiss();
			if (result != null && result.getCode() == 0) {
				finish();
			} else {
				Toast.makeText(ScreenshotActivity.this, getString(R.string.net_error), Toast.LENGTH_SHORT).show();
			}

		}
	}
	public void forStr(){
		for (int i = 0; i < category.size(); i++) {
			if(category.get(i).getState() ==1){
				matchCategoryIds =  matchCategoryIds+category.get(i).getId()+",";
			}
		}
		if(matchCategoryIds.length() >0){
		matchCategoryIds = matchCategoryIds.substring(0,matchCategoryIds.length()-1);
		}
	}
	
	
}
