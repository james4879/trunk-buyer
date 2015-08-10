package com.datapush.buyhand.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

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
import com.datapush.buyhand.net.data.Fabric;
import com.datapush.buyhand.net.data.FabricListParam;
import com.datapush.buyhand.net.data.FabricListResult;
import com.datapush.buyhand.net.data.Fashion;
import com.datapush.buyhand.net.data.FashionListParam;
import com.datapush.buyhand.net.data.FashionListResult;
import com.datapush.buyhand.util.BitmapUtil;
import com.datapush.buyhand.util.CacheImageLoader;
import com.datapush.buyhand.view.ViewPagerAdapter;

/**
 * 
 * 我的收藏
 * @author yanpf
 *
 */
public class MyCollectionActivity extends BaseActivity{
	private ImageView mBack;
	private ViewPager mViewPager;
	private List<View> mViewPagerList= new ArrayList<View>();
	private ViewPagerAdapter mViewPagerAdapter;
	private LinearLayout mIssueLayout,mBabyLayout;
	private int State = -1;
	private int [] residList = new int[] {R.id.yiti,R.id.baobei};
	private CacheImageLoader mCacheImageLoader;//图片下载共通
	private PullToRefreshListView IssueListView,BabyListView;
	
	private List<Fabric> modelList = new ArrayList<Fabric>();//议题数据
	private int IssuepageNo =1;//议题页数
	private FabricAdapter mFabricAdapter;
	
	private List<Fashion> FashionList =  new ArrayList<Fashion>();//列表数据
	private int BabyPageNo; //当前页数
	private TplatformAdapter mTplatformAdapter;
	private int from;
	private LinearLayout mLyy;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mycollection_layout);
		mCacheImageLoader = new CacheImageLoader(MyCollectionActivity.this);
		findView();
		initView();
		if(from == 1){
		 mViewPager.setCurrentItem(1, false);
		}
	}
	
	public void findView(){
		from = getIntent().getIntExtra("from", 0);
		mViewPager = (ViewPager) findViewById(R.id.viewpage);
		mIssueLayout = (LinearLayout) findViewById(R.id.yiti);
		mBabyLayout = (LinearLayout) findViewById(R.id.baobei);
		mBack = (ImageView) findViewById(R.id.back);
		mLyy = (LinearLayout) findViewById(R.id.lyy);
        if(from == 1){
        	mLyy.setVisibility(View.GONE);
        }
	}
	public void initView(){
		setState(R.id.yiti);
		mIssueLayout.setOnClickListener(new OnSingleClickListener() {
			
			@Override
			public void doOnClick(View v) {
				setState(v.getId());
				  mViewPager.setCurrentItem(0, false);
			}
		});
		
		mBack.setOnClickListener(new OnSingleClickListener() {
			
			@Override
			public void doOnClick(View v) {
				finish();
			}
		});
		
		mBabyLayout.setOnClickListener(new OnSingleClickListener() {
			
			@Override
			public void doOnClick(View v) {
				setState(v.getId());
				  mViewPager.setCurrentItem(1, false);
			}
		});
		addView();
		mViewPagerAdapter = new ViewPagerAdapter(mViewPagerList);
		mViewPager.setAdapter(mViewPagerAdapter);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				setState(residList[arg0]);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
		
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void  addView(){
		
        FrameLayout IssueView =(FrameLayout) LayoutInflater.from(MyCollectionActivity.this).inflate(R.layout.viewpager_item, null, false);
        
        IssueListView = (PullToRefreshListView) IssueView.findViewById(R.id.list);
        mFabricAdapter = new FabricAdapter();
        IssueListView.setAdapter(mFabricAdapter);
        IssueListView.setMode(Mode.BOTH);
        
        IssueListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				String label = DateUtils.formatDateTime(MyCollectionActivity.this, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				new FabricListTask(true).execute();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				String label = DateUtils.formatDateTime(MyCollectionActivity.this, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				new FabricListTask(false).execute();
			}
		});
        if(from != 1){
        IssueListView.setRefreshing(true);
        }
        
        IssueListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(MyCollectionActivity.this,FabricDetailActivity.class);
				intent.putExtra("id", modelList.get(position-1).getId());
				startActivity(intent);
			}
		});
        
        FrameLayout babyView =(FrameLayout) LayoutInflater.from(MyCollectionActivity.this).inflate(R.layout.viewpager_item, null, false);
        BabyListView = (PullToRefreshListView) babyView.findViewById(R.id.list);
        mTplatformAdapter = new TplatformAdapter();
        BabyListView.setAdapter(mTplatformAdapter);
        BabyListView.setMode(Mode.BOTH);
        
        
        BabyListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				if(from == 1){
					Intent intent = new Intent();
					intent.setClass(MyCollectionActivity.this, HelpMeChooseActivity.class);
					intent.putExtra("imagePath",FashionList.get(position-1).getImagePath() );
					intent.putExtra("id", FashionList.get(position-1).getImageId());
					setResult(RESULT_OK, intent);
					finish();	
				}
			}
		});
        
        BabyListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				String label = DateUtils.formatDateTime(MyCollectionActivity.this, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				new TplalformtTask(true).execute();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				String label = DateUtils.formatDateTime(MyCollectionActivity.this, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				new TplalformtTask(false).execute();
			}
		});
        BabyListView.setRefreshing(true);
        
        
        if(from != 1){
        mViewPagerList.add(IssueView);
        }
        mViewPagerList.add(babyView);
	}
	
	public void setState(int resid){
		if(resid == State){
			return;
		}else{
			State = resid;
		}
		mIssueLayout.setBackgroundResource(R.drawable.mycollection_top_n);
		mBabyLayout.setBackgroundResource(R.drawable.mycollection_top_n);
		switch (resid) {
		case R.id.yiti:
			mIssueLayout.setBackgroundResource(R.drawable.mycollection_top_p);
			break;
		case R.id.baobei:
			mBabyLayout.setBackgroundResource(R.drawable.mycollection_top_p);
			break;
		}
	}
	
	public class FabricAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return modelList.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@SuppressWarnings("static-access")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			HolerView holer = null;
			if(convertView == null){
				holer = new HolerView();
				convertView = getLayoutInflater().from(MyCollectionActivity.this).inflate(R.layout.fabric_item, parent, false);
				holer.head = (ImageView) convertView.findViewById(R.id.head);
				holer.name = (TextView) convertView.findViewById(R.id.name);
				holer.title = (TextView) convertView.findViewById(R.id.title);
				holer.titme = (TextView) convertView.findViewById(R.id.titme);
				holer.comment = (TextView) convertView.findViewById(R.id.comment);
				convertView.setTag(holer);
			}else{
				holer = (HolerView) convertView.getTag();
			}
			holer.head.setImageBitmap(BitmapUtil.getRoundBitmap(BitmapFactory.decodeStream(getResources().openRawResource(R.drawable.head_img_bg))));
			if(modelList.get(position).getHeadPic() != null && modelList.get(position).getHeadPic().length() >0){
				mCacheImageLoader.loadImage(modelList.get(position).getHeadPic(), holer.head, new OnLoadListener() {
					
					@Override
					public void onLoad(Bitmap bitmap, View targetView) {
						if(bitmap != null){
							((ImageView) targetView).setImageBitmap(BitmapUtil.getRoundBitmap(bitmap)); 
						}
					}
				});
			}
			holer.name.setText(modelList.get(position).getUserName());
			holer.title.setText(modelList.get(position).getTitle());
			holer.comment.setText(modelList.get(position).getCommentCount());
			holer.titme.setText(modelList.get(position).getPublishedTime());
			return convertView;
		}

		class HolerView {
			
			ImageView head;
			TextView name, title, titme,comment;
		}

	}
private class FabricListTask extends AsyncTask<String, Void, FabricListResult>{
		
		boolean bl;
		public FabricListTask(boolean bl) {
			if(bl){
				this.bl = bl;
				IssuepageNo =1;
			}
		}
		
		@Override
		protected FabricListResult doInBackground(String... arg0) {
			
			JSONAccessor accessor = new JSONAccessor(MyCollectionActivity.this, HttpAccessor.METHOD_GET);
			FabricListParam param = new FabricListParam();
			param.setPageNo(IssuepageNo);
			param.setUserId(AppContents.getUserInfo().getId());
			return accessor.execute(Settings.MYCOLLECT,param , FabricListResult.class);
		}
		
		@Override
		protected void onPostExecute(FabricListResult result) {
			
			IssueListView.onRefreshComplete();
			
			if(result != null && result.getTopicList() != null){
				if(bl){
				modelList.clear();
				}
				modelList.addAll(result.getTopicList());
				if(IssuepageNo >= result.getTotalPage()){
					IssueListView.setMode(Mode.PULL_FROM_START);
				}else{
					IssuepageNo +=1;
				}
				mFabricAdapter.notifyDataSetChanged();
			} else {
				Toast.makeText(MyCollectionActivity.this, getString(R.string.net_error), Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(result);
		}
	}

public class TplatformAdapter extends BaseAdapter{

	@Override
	public int getCount() {
		return FashionList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return FashionList.get(arg0);
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
			arg1 = LayoutInflater.from(MyCollectionActivity.this).inflate(R.layout.tplatform_item, arg2, false);
			holerView.img = (ImageView) arg1.findViewById(R.id.img);
			holerView.delete = (ImageView) arg1.findViewById(R.id.delete);
			holerView.love = (ImageView) arg1.findViewById(R.id.love);
			holerView.title = (TextView) arg1.findViewById(R.id.title);
			holerView.price = (TextView) arg1.findViewById(R.id.price);
			holerView.time = (TextView) arg1.findViewById(R.id.time);
			holerView.lovenumber = (TextView) arg1.findViewById(R.id.lovenumber);
			arg1.setTag(holerView);
		}else{
			holerView = (HolerView) arg1.getTag();
		}
		
		if(FashionList.get(arg0).getName() !=null ){
		holerView.title.setText(FashionList.get(arg0).getName());
		}
		if(FashionList.get(arg0).getGoodsUploadTime()!=null ){
			holerView.time.setText(FashionList.get(arg0).getGoodsUploadTime());
		}
		holerView.lovenumber.setText(FashionList.get(arg0).getCollectCount()+"");
		if(FashionList.get(arg0).getImagePath() != null && !(FashionList.get(arg0).getImagePath().equals("")))	{
			
			holerView.img.setImageBitmap(BitmapFactory.decodeStream(getResources().openRawResource(R.drawable.pohoto_bg)));
			mCacheImageLoader.loadImage(FashionList.get(arg0).getImagePath(), holerView.img, new OnLoadListener() {
				
				@Override
				public void onLoad(Bitmap bitmap, View targetView) {
					if(bitmap != null){
						((ImageView) targetView).setImageBitmap(BitmapUtil.zoomBitmap(bitmap, 
								Settings.DISPLAY_WIDTH-DensityUtils.dp2px(MyCollectionActivity.this, 20))); 
					}
				}
			});
		}
		
		return arg1;
	}
	class HolerView{
		ImageView img,delete,love;
		TextView title,price,time,lovenumber;
		
	}
}

private class TplalformtTask extends AsyncTask<String, Void, FashionListResult>{
	
	public TplalformtTask(boolean bl) {
		if(bl){
		BabyPageNo = 1;
		FashionList.clear();
		}
	}

	@Override
	protected FashionListResult doInBackground(String... arg0) {
		
		JSONAccessor accessor = new JSONAccessor(MyCollectionActivity.this, HttpAccessor.METHOD_GET);
		FashionListParam param = new FashionListParam();
		param.setPageNo(BabyPageNo);
		param.setUserId(AppContents.getUserInfo().getId());
		return accessor.execute(Settings.MYCOECTGOODS,param , FashionListResult.class);
	}
	
	@Override
	protected void onPostExecute(FashionListResult result) {
		
		BabyListView.onRefreshComplete();
		
		if(result != null){
			if( result.getModelList() != null){
			FashionList.addAll(result.getModelList());
			}
			if(BabyPageNo >= result.getTotalPage()){
				BabyListView.setMode(Mode.PULL_FROM_START);
			}else{
				BabyPageNo += 1;
			}
			mTplatformAdapter.notifyDataSetChanged();
		} else {
			Toast.makeText(MyCollectionActivity.this, getString(R.string.net_error), Toast.LENGTH_SHORT).show();
		}
		super.onPostExecute(result);
	}

	
}


	

}
