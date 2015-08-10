package com.datapush.buyhand.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.daoshun.lib.communication.http.HttpAccessor;
import com.daoshun.lib.communication.http.JSONAccessor;
import com.daoshun.lib.listview.PullToRefreshBase;
import com.daoshun.lib.listview.PullToRefreshListView;
import com.daoshun.lib.listview.PullToRefreshBase.Mode;
import com.daoshun.lib.util.DensityUtils;
import com.daoshun.lib.util.ImageLoader.OnLoadListener;
import com.daoshun.lib.view.OnSingleClickListener;
import com.datapush.buyhand.R;
import com.datapush.buyhand.common.AppContents;
import com.datapush.buyhand.common.Settings;
import com.datapush.buyhand.net.data.Category;
import com.datapush.buyhand.net.data.LoginParam;
import com.datapush.buyhand.net.data.MatchCategoryListParam;
import com.datapush.buyhand.net.data.MatchCategoryListResult;
import com.datapush.buyhand.net.data.MatchListParam;
import com.datapush.buyhand.net.data.MatchListResult;
import com.datapush.buyhand.net.data.matchImage;
import com.datapush.buyhand.util.BuyerShare;
import com.datapush.buyhand.util.CacheImageLoader;

/**
 * 
 * 我的搭配
 * 
 * @author yanpf
 *
 */
public class MyMatchActivity extends BaseActivity {
	
	private LinearLayout mLinearLayout;
	private PullToRefreshListView mPullToRefreshListView;
	private Button mAdd;
	private ImageView mBack;
	private List<Category> category = new ArrayList<Category>();
	private int selectId;
	private List<matchImage> matchList = new ArrayList<matchImage>();
	private MatchAdapter mMatchAdapter;
	private CacheImageLoader mCacheImageLoader;//图片下载共通
	private int from;
	private Dialog mShareDg,mShareEditDg;
	private int mShareIndex;
	private String imageId = "";
	private String imagePath = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mymatch_layout);
		findView();
		 initView();
		mCacheImageLoader = new CacheImageLoader(MyMatchActivity.this);
		new CategoryTask().execute();
	}
	
	public void findView(){
		from = getIntent().getIntExtra("from", 0);
		mLinearLayout = (LinearLayout) findViewById(R.id.three_ll);
		mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.list);
		mAdd = (Button) findViewById(R.id.newmatch);
		mBack = (ImageView) findViewById(R.id.back);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes"})
	public void initView(){
		
		mAdd.setOnClickListener(new OnSingleClickListener() {
			
			@Override
			public void doOnClick(View v) {
				Intent intent = new Intent(MyMatchActivity.this,CombinationActivity.class);
				startActivity(intent);
			}
		});
		mBack.setOnClickListener(new OnSingleClickListener() {
			@Override
			public void doOnClick(View v) {
				finish();
			}
		});
		ListView mRefreshListView = mPullToRefreshListView.getRefreshableView();
		mRefreshListView.setDividerHeight(DensityUtils.dp2px(MyMatchActivity.this, 1));
		mMatchAdapter = new MatchAdapter();
		mPullToRefreshListView.setAdapter(mMatchAdapter);
		
		mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				String label = DateUtils.formatDateTime(MyMatchActivity.this, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				new MatchTask().execute();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
			}
		});
	}
	
	
	/**
	 * 搭配目录
	 * 
	 */
	private class CategoryTask extends AsyncTask<LoginParam, Void, MatchCategoryListResult> {
		
		@Override
		protected MatchCategoryListResult doInBackground(LoginParam... params) {
			
			JSONAccessor mAccessor = new JSONAccessor(MyMatchActivity.this, HttpAccessor.METHOD_POST);
			MatchCategoryListParam param = new MatchCategoryListParam();
			param.setUserId(AppContents.getUserInfo().getId());
			return mAccessor.execute(Settings.SCREENSHOT, param, MatchCategoryListResult.class);
		}
		
		@Override
		protected void onPostExecute(MatchCategoryListResult result) {
			if (result != null && result.getCategoryList() != null) {
				category.addAll(result.getCategoryList());
				setSelect();
				mPullToRefreshListView.setRefreshing(true);
			} else {
				Toast.makeText(MyMatchActivity.this, getString(R.string.net_error), Toast.LENGTH_SHORT).show();
			}

		}

	}
	
	public void setSelect(){
		
		int childWidth = (Settings.DISPLAY_WIDTH - DensityUtils.dp2px(MyMatchActivity.this, 3)) / 4;
		int childHeight = DensityUtils.dp2px(MyMatchActivity.this, 37);
		
		for (int i = 0; i < category.size(); i++) {
		final int index = i;
			TextView tv = new TextView(MyMatchActivity.this);
			LayoutParams lp = new LayoutParams(childWidth, childHeight);
			lp.leftMargin = 0;
			lp.gravity = Gravity.CENTER;
			tv.setGravity(Gravity.CENTER);
			tv.setTextColor(Color.WHITE);
			tv.setText(category.get(i).getCategoryName());
			mLinearLayout.addView(tv, lp);
			if(i == selectId){
				tv.setBackgroundResource(R.drawable.add_cl_category_choosed);
			}else{
				tv.setBackgroundResource(R.drawable.add_cl_category);
			}
			tv.setOnClickListener(new OnSingleClickListener() {
				
				@Override
				public void doOnClick(View v) {
					if(index == selectId){
						return;
					}
					mLinearLayout.getChildAt(selectId * 2).setBackgroundResource(R.drawable.add_cl_category);
					mLinearLayout.getChildAt(index * 2).setBackgroundResource(R.drawable.add_cl_category_choosed);
					selectId = index;
					mPullToRefreshListView.setRefreshing(true);
				}
			});
			
			if(i == category.size() - 1){
				return;
			}
			ImageView iv = new ImageView(MyMatchActivity.this);
			lp = new LayoutParams(DensityUtils.dp2px(MyMatchActivity.this,1), DensityUtils.dp2px(MyMatchActivity.this, 33));
			lp.gravity = Gravity.CENTER_VERTICAL;
			iv.setBackgroundResource(R.drawable.tp_line_bg);
			mLinearLayout.addView(iv, lp);
			
		}
	}
	
	/**
	 * 获取我的搭配列表数据类
	 */
	private class MatchTask extends AsyncTask<LoginParam, Void, MatchListResult> {
		
		@Override
		protected void onPreExecute() {
			mPullToRefreshListView.setMode(Mode.BOTH);
			super.onPreExecute();
		}
		
		@Override
		protected MatchListResult doInBackground(LoginParam... params) {
			
			JSONAccessor mAccessor = new JSONAccessor(MyMatchActivity.this, HttpAccessor.METHOD_POST);
			MatchListParam param = new MatchListParam();
			param.setUserId(AppContents.getUserInfo().getId());
			param.setCategoryId(category.get(selectId).getId());
			return mAccessor.execute(Settings.MATCHLIST, param, MatchListResult.class);
		}
		
		@Override
		protected void onPostExecute(MatchListResult result) {
			mPullToRefreshListView.onRefreshComplete();
			mPullToRefreshListView.setMode(Mode.PULL_FROM_START);
			if (result != null && result.getMatchList() != null) {
				matchList.clear();
				matchList.addAll(result.getMatchList());
				mMatchAdapter.notifyDataSetChanged();
			} else {
				Toast.makeText(MyMatchActivity.this, getString(R.string.net_error), Toast.LENGTH_SHORT).show();
			}

		}

	}
	//搭配列表是适配器
	public class  MatchAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			
			if(matchList.size() == 0){
				return 0 ;
			}
			else if(matchList.size()%2 == 0){
				return matchList.size()/2;
			}
			return matchList.size()/2 + 1;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final int index = position;
			HolerView holer = null;
			if(convertView == null){
				holer = new HolerView();
				convertView = LayoutInflater.from(MyMatchActivity.this).inflate(R.layout.match_item, parent,false);
				holer.zan = (TextView) convertView.findViewById(R.id.zan);
				holer.cai = (TextView) convertView.findViewById(R.id.cai);
				holer.img = (ImageView) convertView.findViewById(R.id.img);
				holer.share = (ImageView) convertView.findViewById(R.id.share);
				holer.zan1 = (TextView) convertView.findViewById(R.id.zan1);
				holer.cai1 = (TextView) convertView.findViewById(R.id.cai1);
				holer.img1 = (ImageView) convertView.findViewById(R.id.img1);
				holer.share1 = (ImageView) convertView.findViewById(R.id.share1);
				holer.line = (ImageView) convertView.findViewById(R.id.line);
				holer.ly = (LinearLayout) convertView.findViewById(R.id.lytwo);
				convertView.setTag(holer);
			}else{
				holer = (HolerView) convertView.getTag();
			}
			int w = (Settings.DISPLAY_HEIGHT -(DensityUtils.dp2px(MyMatchActivity.this, 78+85)+Settings.STATUS_BAR_HEIGHT))/2;
			LinearLayout.LayoutParams lpimg = (LinearLayout.LayoutParams) holer.img.getLayoutParams();
			lpimg.height = w;
			holer.img.setLayoutParams(lpimg);
			
			LinearLayout.LayoutParams lpimg1 = (LinearLayout.LayoutParams) holer.img1.getLayoutParams();
			lpimg1.height = w;
			holer.img1.setLayoutParams(lpimg1);
			
			LinearLayout.LayoutParams line = (LinearLayout.LayoutParams) holer.line.getLayoutParams();
			line.height = w+DensityUtils.dp2px(MyMatchActivity.this, 38);
			holer.line.setLayoutParams(line);
			
			holer.zan.setText(matchList.get(position*2).getMatchImage().getPraiseCount());
			holer.cai.setText(matchList.get(position*2).getMatchImage().getStepCount());
			holer.img.setImageBitmap(BitmapFactory.decodeStream(getResources().openRawResource(R.drawable.pohoto_bg)));
			if(matchList.get(position*2).getMatchImage().getThumbnailPath() != null && matchList.get(position*2).getMatchImage().getThumbnailPath().length() >0){
				
				mCacheImageLoader.loadImage(matchList.get(position*2).getMatchImage().getThumbnailPath(), holer.img, new OnLoadListener() {

					@Override
					public void onLoad(Bitmap bitmap, View targetView) {
						if (bitmap != null) {
							((ImageView) targetView).setImageBitmap(bitmap);
						}
					}
				});
			}
			holer.img.setOnClickListener(new OnSingleClickListener() {
				
				@Override
				public void doOnClick(View v) {
					if(from == 1){
					Intent intent = new Intent();
					intent.setClass(MyMatchActivity.this, HelpMeChooseActivity.class);
					intent.putExtra("imagePath", matchList.get(index*2).getMatchImage().getThumbnailPath());
					intent.putExtra("id", matchList.get(index*2).getMatchImage().getId());
					setResult(RESULT_OK, intent);
					finish();	
					}
				}
			});
			holer.share.setOnClickListener(new OnSingleClickListener() {
				
				@Override
				public void doOnClick(View v) {
					if(from != 1){
					getDialog();
					imageId = matchList.get(index*2).getMatchImage().getId();
					imagePath = matchList.get(index*2).getMatchImage().getImagePath();
					}
				}
			});
			
			holer.share1.setOnClickListener(new OnSingleClickListener() {
				
				@Override
				public void doOnClick(View v) {
					if(from != 1){
					getDialog();
					imageId = matchList.get(index*2+1).getMatchImage().getId();
					imagePath = matchList.get(index*2+1).getMatchImage().getImagePath();
					}
				}
			});
			
			if(matchList.size() > position*2 +1){
			holer.ly.setVisibility(View.VISIBLE);
			holer.line.setVisibility(View.VISIBLE);
			holer.zan1.setText(matchList.get(position*2+1).getMatchImage().getPraiseCount());
			holer.cai1.setText(matchList.get(position*2+1).getMatchImage().getStepCount());
			holer.img1.setImageBitmap(BitmapFactory.decodeStream(getResources().openRawResource(R.drawable.pohoto_bg)));
			if(matchList.get(position*2+1).getMatchImage().getThumbnailPath() != null && matchList.get(position*2+1).getMatchImage().getThumbnailPath().length() >0){
				
				mCacheImageLoader.loadImage(matchList.get(position*2+1).getMatchImage().getThumbnailPath(), holer.img1, new OnLoadListener() {

					@Override
					public void onLoad(Bitmap bitmap, View targetView) {
						if (bitmap != null) {
							((ImageView) targetView).setImageBitmap(bitmap);
						}
					}
				});
			}
			
			holer.img1.setOnClickListener(new OnSingleClickListener() {
				
				@Override
				public void doOnClick(View v) {
					
					if(from == 1){
					Intent intent = new Intent();
					intent.setClass(MyMatchActivity.this, HelpMeChooseActivity.class);
					intent.putExtra("imagePath", matchList.get(index*2+1).getMatchImage().getThumbnailPath());
					intent.putExtra("id", matchList.get(index*2+1).getMatchImage().getId());
					setResult(RESULT_OK, intent);
					finish();				
					}
				}
			});
		}else{
			holer.ly.setVisibility(View.INVISIBLE);
			holer.line.setVisibility(View.INVISIBLE);
		}
			
			
			return convertView;
		}
		class HolerView{
			TextView zan,cai,zan1,cai1;
			ImageView img,share,img1,share1,line;
			LinearLayout ly;
		}
	}
	
	public void getDialog(){
		
		if(mShareDg != null){
			mShareDg = null;
		}
		View view = LayoutInflater.from(MyMatchActivity.this).inflate(R.layout.share_dialog, null);
		mShareDg = new Dialog(MyMatchActivity.this, R.style.dialog);
		mShareDg.setContentView(view);
		mShareDg.show();
		
		Window w = mShareDg.getWindow();
		w.setWindowAnimations(R.style.dialog_anim_style_v13);
		WindowManager.LayoutParams lp = w.getAttributes();
		w.setAttributes(lp);
		
		LinearLayout tengxun = (LinearLayout) view.findViewById(R.id.tengxun);
		LinearLayout sina = (LinearLayout) view.findViewById(R.id.sina);
		LinearLayout weixin = (LinearLayout) view.findViewById(R.id.weixin);
		LinearLayout pengyou = (LinearLayout) view.findViewById(R.id.pengyou);
		LinearLayout shaiwu = (LinearLayout) view.findViewById(R.id.shaiwu);
		
		tengxun.setOnClickListener(new OnSingleClickListener() {
			
			@Override
			public void doOnClick(View v) {
				mShareDg.dismiss();
				mShareIndex = 4;
				getShareDialog();
				
			}
		});
		sina.setOnClickListener(new OnSingleClickListener() {
			
			@Override
			public void doOnClick(View v) {
				mShareDg.dismiss();
				mShareIndex = 1;
				getShareDialog();
				
			}
		});
		weixin.setOnClickListener(new OnSingleClickListener() {
			
			@Override
			public void doOnClick(View v) {
				mShareDg.dismiss();
				mShareIndex = 2;
				getShareDialog();
				
			}
		});
		pengyou.setOnClickListener(new OnSingleClickListener() {
			
			@Override
			public void doOnClick(View v) {
				mShareDg.dismiss();
				mShareIndex = 3;
				getShareDialog();
				
			}
		});
		shaiwu.setOnClickListener(new OnSingleClickListener() {
			
			@Override
			public void doOnClick(View v) {
				mShareDg.dismiss();
				mShareIndex = 5;
				getShareDialog();
			}
		});
	}
	
	//选择图片dialog
		public void getShareDialog(){
			
			if(mShareEditDg != null){
				mShareEditDg = null;
			}
			View view = LayoutInflater.from(MyMatchActivity.this).inflate(R.layout.shareedit_dialog, null);
			mShareEditDg = new Dialog(MyMatchActivity.this, R.style.dialog);
			mShareEditDg.setContentView(view);
			mShareEditDg.show();
			
			Window w = mShareEditDg.getWindow();
			WindowManager.LayoutParams lp = w.getAttributes();
			lp.gravity = Gravity.BOTTOM;
			w.setAttributes(lp);
			
			ImageView delete = (ImageView) view.findViewById(R.id.delete);
			ImageView post = (ImageView) view.findViewById(R.id.post);
			final EditText title = (EditText) view.findViewById(R.id.title);
			final EditText info = (EditText) view.findViewById(R.id.info);
			
			delete.setOnClickListener(new OnSingleClickListener() {
				
				@Override
				public void doOnClick(View v) {
					mShareEditDg.dismiss();
				}
			});
			
			post.setOnClickListener(new OnSingleClickListener() {
				
				@Override
				public void doOnClick(View v) {
					mShareEditDg.dismiss();
					if(title.getText().toString().length() >0 && info.getText().toString().length() > 0){
						if(mShareIndex == 5){
							BuyerShare bs = new BuyerShare(MyMatchActivity.this, title.getText().toString(), AppContents.getUserInfo().getId(),
									imageId, info.getText().toString(), new BuyerShare.OnCallBack() {
										
										@Override
										public void callback() {
											finish();
										}
									});
							bs.share();
						}else{
							BuyerShare bs = new BuyerShare(MyMatchActivity.this, info.getText().toString(), imagePath, mShareIndex);
							bs.share();
						}
					}
				}
			});
			
			
		}
	
}
