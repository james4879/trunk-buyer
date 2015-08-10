package com.datapush.buyhand.activity;

import java.util.ArrayList;
import java.util.List;




import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daoshun.lib.communication.http.HttpAccessor;
import com.daoshun.lib.communication.http.JSONAccessor;
import com.daoshun.lib.listview.PullToRefreshBase;
import com.daoshun.lib.listview.PullToRefreshBase.Mode;
import com.daoshun.lib.listview.PullToRefreshListView;
import com.daoshun.lib.util.DensityUtils;
import com.daoshun.lib.util.ImageLoader.OnLoadListener;
import com.daoshun.lib.view.OnSingleClickListener;
import com.datapush.buyhand.R;
import com.datapush.buyhand.common.AppContents;
import com.datapush.buyhand.common.Constants;
import com.datapush.buyhand.common.Settings;
import com.datapush.buyhand.net.data.BaseResult;
import com.datapush.buyhand.net.data.DataResult;
import com.datapush.buyhand.net.data.Fashion;
import com.datapush.buyhand.net.data.FashionListParam;
import com.datapush.buyhand.net.data.FashionListResult;
import com.datapush.buyhand.net.data.GoodCollectionParam;
import com.datapush.buyhand.net.data.Item;
import com.datapush.buyhand.util.BitmapUtil;
import com.datapush.buyhand.util.CacheImageLoader;
import com.datapush.buyhand.view.TextViewFrameLayout;
import com.google.gson.Gson;

/**
 * T台
 * 
 * @author yanpf
 *
 */
public class TplatformFragment extends BaseFragment{
	private TplalformtTask mTplalformtTask;
	private SharedPreferences mSharedPreferences;
	private DataResult data;
	private MenuActivity mMenuActivity;
	private List<Fashion> FashionList =  new ArrayList<Fashion>();//列表数据
	private PullToRefreshListView mTplatformListView;//列表View
	private TplatformAdapter mTplatformAdapter;
	
	private String mLatestime;//接口时间
	private int mPageNo; //当前页数
	private String mCategoryId = "";// 类目ID
	private String mBodyStyleId = "";// 身型ID
	private String mStyleId = "";// 风格ID
	private String mColorStyleId = "";// 色系ID
	private String mPriceId = "";// 价格区间ID
	private String mBrandId = "";// 品牌ID
	
	private CacheImageLoader mCacheImageLoader;//图片下载共通
	
	private Button mCategory,mStyle,mPrice,mBrand;//类目，风格，价格，品牌
	private int SideIndex = 0;//顶部位置控制指向
	
	private LinearLayout mSideAll;//全部隐藏类目
	private ListView mSideListView;//侧边选项ListView
	private SideAdapter mSideAdapter;//侧边栏容器
	private int Index = -1;//item选择指引
	private List<Item> mSideListData = new ArrayList<Item>();//显示数据
	
	private LinearLayout mSideRight;//色系和身型大块
	private TextViewFrameLayout mColor,mBody;//色系和身型小块
	
	private List<Item> mColorListData = new ArrayList<Item>();//色系数据
	private List<Item> mBodyListData = new ArrayList<Item>();//身型数据
	@Override
	public void onAttach(Activity activity) {
		mMenuActivity = (MenuActivity) getActivity();
		super.onAttach(activity);
	}

	@Override
	public View onInitView(LayoutInflater inflater, ViewGroup container) {
		View view = inflater.inflate(R.layout.buyhand_layout, container, false);
		mTplatformListView = (PullToRefreshListView) view.findViewById(R.id.list);
		mCacheImageLoader = new CacheImageLoader(mMenuActivity);
		mSideListView = (ListView) view.findViewById(R.id.side_list);
		mCategory = (Button) view.findViewById(R.id.category);
		mStyle = (Button) view.findViewById(R.id.style);
		mPrice = (Button) view.findViewById(R.id.price);
		mBrand = (Button) view.findViewById(R.id.brand);
		mSideAll = (LinearLayout) view.findViewById(R.id.side_all);
		mSideRight = (LinearLayout) view.findViewById(R.id.right_side);
		mColor = (TextViewFrameLayout) view.findViewById(R.id.frame_color);
		mBody = (TextViewFrameLayout) view.findViewById(R.id.frame_body);
		initView();
		return view;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void initView(){
		mSharedPreferences = mMenuActivity.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
		String json;
		json = mSharedPreferences.getString("category", "");
		Gson gson = new Gson();
		data = gson.fromJson(json, DataResult.class);
		
		mCategory.setOnClickListener(new OnSingleClickListener() {
			
			@Override
			public void doOnClick(View v) {
				if(TopSwitch(v.getId())){
					mSideAll.setVisibility(View.VISIBLE);
				}else{
					mSideAll.setVisibility(View.GONE);
					SideIndex = 0;
				}

			}
		});
		
		mStyle.setOnClickListener(new OnSingleClickListener() {
			
			@Override
			public void doOnClick(View v) {
				if(TopSwitch(v.getId())){
					mSideAll.setVisibility(View.VISIBLE);
			}else{
				mSideAll.setVisibility(View.GONE);
				SideIndex = 0;
			}

			}
		});
		
		mPrice.setOnClickListener(new OnSingleClickListener() {
			
			@Override
			public void doOnClick(View v) {
				if(TopSwitch(v.getId())){
					mSideAll.setVisibility(View.VISIBLE);
			}else{
				mSideAll.setVisibility(View.GONE);
				SideIndex = 0;
			}

			}
		});
		
		mBrand.setOnClickListener(new OnSingleClickListener() {
			
			@Override
			public void doOnClick(View v) {
				if(TopSwitch(v.getId())){
					mSideAll.setVisibility(View.VISIBLE);
			}else{
				mSideAll.setVisibility(View.GONE);
				SideIndex = 0;
			}

			}
		});
		
		
		ListView list = mTplatformListView.getRefreshableView();
		list.setDividerHeight(DensityUtils.dp2px(mMenuActivity, 3));
		mTplatformAdapter = new TplatformAdapter();
		mTplatformListView.setAdapter(mTplatformAdapter);
		
		mTplatformListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				String label = DateUtils.formatDateTime(mMenuActivity, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				mTplalformtTask = new TplalformtTask(true);
				mTplalformtTask.execute();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				String label = DateUtils.formatDateTime(mMenuActivity, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				mTplalformtTask = new TplalformtTask(false);
				mTplalformtTask.execute();
			}
		});
		mSideAdapter = new SideAdapter();
		mSideListView.setAdapter(mSideAdapter);
		
		mSideListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
					if(arg2 == Index){
						return;
					}else{
						if(mSideListData.get(arg2).getId().equals("100")){
						mSideRight.setVisibility(View.VISIBLE);
						}else{
							mSideRight.setVisibility(View.GONE);
							UpdateAsk(mSideListData.get(arg2).getId());
							mSideAll.setVisibility(View.GONE);
							SideIndex = 0;
						}
						Index = arg2;
					}
			}
		});
	}
	
	
	private class TplalformtTask extends AsyncTask<String, Void, FashionListResult>{
		JSONAccessor accessor;
		boolean bl;
		public TplalformtTask(boolean bl) {
			if(bl){
			mLatestime = "";
			mPageNo = 1;
			FashionList.clear();
			this.bl = bl;
			}
		}

		@Override
		protected void onPreExecute() {
			if (bl) {
				mTplatformListView.setMode(Mode.BOTH);
			}
			super.onPreExecute();
		}
		
		@Override
		protected FashionListResult doInBackground(String... arg0) {
			
			accessor = new JSONAccessor(mMenuActivity, HttpAccessor.METHOD_GET);
			FashionListParam param = new FashionListParam();
			param.setLatestime(mLatestime);
			param.setPageNo(mPageNo);
			param.setBrandId(mBrandId);
			param.setCategoryId(mCategoryId);
			param.setBodyStyleId(mBodyStyleId);
			param.setColorStyleId(mColorStyleId);
			param.setStyleId(mStyleId);
			param.setPriceId(mPriceId);
			if(AppContents.getUserInfo().getId() != null && AppContents.getUserInfo().getId().length() > 0 ){
				param.setUserId(AppContents.getUserInfo().getId());
			}
			return accessor.execute(Settings.FASHIONLIST,
					param , FashionListResult.class);
		}
		
		@Override
		protected void onPostExecute(FashionListResult result) {
			
			mTplatformListView.onRefreshComplete();
			
			if(result != null && result.getModelList() != null){
				FashionList.addAll(result.getModelList());
				if(mPageNo >= result.getTotalPage()){
					mTplatformListView.setMode(Mode.PULL_FROM_START);
				}else{
					mLatestime =FashionList.get(0).getGoodsCrawlingTime();
					mPageNo += 1;
				}
				mTplatformAdapter.notifyDataSetChanged();
			} else {
				Toast.makeText(mMenuActivity, getString(R.string.net_error), Toast.LENGTH_SHORT).show();
			}
			mTplalformtTask = null;
			super.onPostExecute(result);
		}
		public void stop() {
			if (accessor != null) {
				accessor.stop();
			}
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

		@SuppressLint("NewApi")
		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			HolerView holerView = null;
			final int index = arg0;
			if(arg1 == null){
				holerView = new HolerView();
				arg1 = LayoutInflater.from(mMenuActivity).inflate(R.layout.tplatform_item, arg2, false);
				holerView.img = (ImageView) arg1.findViewById(R.id.img);
				holerView.delete = (ImageView) arg1.findViewById(R.id.delete);
				holerView.love = (ImageView) arg1.findViewById(R.id.love);
				holerView.title = (TextView) arg1.findViewById(R.id.title);
				holerView.price = (TextView) arg1.findViewById(R.id.price);
				holerView.time = (TextView) arg1.findViewById(R.id.time);
				holerView.dapei = (RatingBar) arg1.findViewById(R.id.dapei);
				holerView.liuxing = (RatingBar) arg1.findViewById(R.id.liuxing);
				holerView.lovenumber = (TextView) arg1.findViewById(R.id.lovenumber);
				arg1.setTag(holerView);
			}else{
				holerView = (HolerView) arg1.getTag();
			}
			
			holerView.dapei.setProgress(FashionList.get(arg0).getMatchLevel());
			holerView.liuxing.setProgress(FashionList.get(arg0).getStarLevel());
			
			if(FashionList.get(arg0).getName() !=null ){
			holerView.title.setText(FashionList.get(arg0).getName());
			}
			if(FashionList.get(arg0).getGoodsUploadTime()!=null ){
				holerView.time.setText(FashionList.get(arg0).getGoodsUploadTime());
			}
			holerView.lovenumber.setText(FashionList.get(arg0).getCollectCount()+"");
			
			if(FashionList.get(arg0).getCollectFlag() == 0){
				holerView.love.setBackgroundResource(R.drawable.love_a);
			}else{
				holerView.love.setBackgroundResource(R.drawable.love);
			}
			holerView.love.setOnClickListener(new OnSingleClickListener() {
				
				@Override
				public void doOnClick(View v) {
					if(isLogin()){
						new GoodCollectionTask(index, FashionList.get(index).getId(), FashionList.get(index).getCollectFlag(), FashionList.get(index).getCollectCount()).execute();
					}
				}
			});
			
			if(FashionList.get(arg0).getImagePath() != null && !(FashionList.get(arg0).getImagePath().equals("")))	{
				
				holerView.img.setImageBitmap(BitmapFactory.decodeStream(getResources().openRawResource(R.drawable.pohoto_bg)));
				mCacheImageLoader.loadImage(FashionList.get(arg0).getImagePath(), holerView.img, new OnLoadListener() {
					
					@Override
					public void onLoad(Bitmap bitmap, View targetView) {
						if(bitmap != null){
							((ImageView) targetView).setImageBitmap(BitmapUtil.zoomBitmap(bitmap, 
									Settings.DISPLAY_WIDTH-DensityUtils.dp2px(mMenuActivity, 20)));
						}
					}
				});
			}
			
			return arg1;
		}
		class HolerView{
			ImageView img,delete,love;
			TextView title,price,time,lovenumber;
			RatingBar dapei , liuxing;
			
		}
	}

	
	public class SideAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return mSideListData.size();
		}

		@Override
		public Object getItem(int arg0) {
			return mSideListData.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@SuppressLint("ResourceAsColor")
		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			HolerView holerView = null;
			if(arg1 == null){
				holerView = new HolerView();
				arg1 = LayoutInflater.from(mMenuActivity).inflate(R.layout.commodity_item, arg2, false);
				holerView.table = (TextView) arg1.findViewById(R.id.table);
				arg1.setTag(holerView);
			}else{
				holerView = (HolerView) arg1.getTag();
			}
			RelativeLayout.LayoutParams param = (RelativeLayout.LayoutParams) holerView.table.getLayoutParams();
			param.height =  mSideListView.getHeight()/9;
//			param.width =  (Settings.DISPLAY_WIDTH-DensityUtils.dp2px(mMenuActivity, 3))/4;
			param.width = mSideListView.getWidth();
			holerView.table.setLayoutParams(param);
			holerView.table.setText(mSideListData.get(arg0).getName());
			return arg1;
		}
		class HolerView{
			TextView table;
		}
	}
	//类目数据
	public void getCategorydata(){
		mSideListData.clear();
		mSideListData.addAll(data.getData().getCategoryList());
	}
	
	//风格数据
	public void getStyledata(){
		mSideListData.clear();
		mSideListData.addAll(data.getData().getStyleList());
	}
	
	//价格数据
	public void getBranddata(){
		mSideListData.clear();
		mSideListData.addAll(data.getData().getBrandList());
	}
	
	//品牌数据
	public void getPricedata(){
		mSideListData.clear();
		mSideListData.addAll(data.getData().getPriceList());
	}
	
	//顶部栏切换
	@SuppressWarnings("deprecation")
	public boolean TopSwitch(int id){
		
		mSideRight.setVisibility(View.GONE);
		mSideListView.setLayoutParams(new LayoutParams(Settings.DISPLAY_WIDTH/4, LayoutParams.FILL_PARENT));//设置listView大小
		mColor.setWidth(Settings.DISPLAY_WIDTH*3/4);
		mBody.setWidth(Settings.DISPLAY_WIDTH*3/4);
		AddColorOrBody();
		Index = -1;
		mCategory.setSelected(false);
		mStyle.setSelected(false);
		mPrice.setSelected(false);
		mBrand.setSelected(false);
		
		if(SideIndex == id){
			return false;
		}else{
			SideIndex = id;
		}
		switch (id) {
		case R.id.category:
			mCategory.setSelected(true);
			getCategorydata();
			break;
		case R.id.style:
			mStyle.setSelected(true);
			getStyledata();
			break;
		case R.id.price:
			mPrice.setSelected(true);
			getPricedata();
			break;
		case R.id.brand:
			mBrand.setSelected(true);
			getBranddata();
			break;
		}
		mSideAdapter.notifyDataSetChanged();
		return true;
	}
	
	
	//色系和身型动态加载
	public void AddColorOrBody(){
		GetColorOrBodyData();
		mColor.removeAllViewsInLayout();
		for(int i = 0 ;i<mColorListData.size();i++){
			final int index = i;
				TextView tv = new TextView(mMenuActivity);
				tv.setText(mColorListData.get(i).getName()+"色");
				tv.setBackgroundResource(R.drawable.tp_mm);
				tv.setTextColor(Color.parseColor("#ffff00"));
				tv.setGravity(Gravity.CENTER);
				mColor.addView(tv);
				tv.setOnClickListener(new OnSingleClickListener() {
					
					@Override
					public void doOnClick(View v) {
						mColorStyleId = mColorListData.get(index).getId();
						mSideAll.setVisibility(View.GONE);
						SideIndex = 0;
						if(mTplalformtTask == null){
							mTplalformtTask = new TplalformtTask(true);
							mTplalformtTask.execute();
						}else{
							mTplalformtTask.stop();
							mTplalformtTask = null;
							mTplalformtTask = new TplalformtTask(true);
							mTplalformtTask.execute();
						}
					}
				});
		}
		mBody.removeAllViewsInLayout();
		for(int i = 0 ;i<mBodyListData.size();i++){
			final int index = i;
			TextView tv = new TextView(mMenuActivity);
			tv.setText(mBodyListData.get(i).getName());
			tv.setBackgroundResource(R.drawable.tp_mm);
			tv.setTextColor(Color.parseColor("#ffff00"));
			tv.setGravity(Gravity.CENTER);
			mBody.addView(tv);
			tv.setOnClickListener(new OnSingleClickListener() {
				
				@Override
				public void doOnClick(View v) {
					mBodyStyleId = mBodyListData.get(index).getId();
					mSideAll.setVisibility(View.GONE);
					SideIndex = 0;
					if(mTplalformtTask == null){
						mTplalformtTask = new TplalformtTask(true);
						mTplalformtTask.execute();
					}else{
						mTplalformtTask.stop();
						mTplalformtTask = null;
						mTplalformtTask = new TplalformtTask(true);
						mTplalformtTask.execute();
					}
				}
			});
	}
		
		
	}
	//色系和身型的数据
	public void GetColorOrBodyData(){
		
		mColorListData.clear();
		mColorListData.addAll(data.getData().getColorList());
		
		mBodyListData.clear();
		mBodyListData.addAll(data.getData().getBodyList());
	}
	public void UpdateAsk(String id){
		switch (SideIndex) {
		case R.id.category:
			mCategoryId = id;
			break;
		case R.id.style:
			mStyleId = id;
			break;
		case R.id.price:
			mPriceId = id;
			break;
		case R.id.brand:
			mBrandId = id;
			break;
		}
		if(mTplalformtTask == null){
			mTplalformtTask = new TplalformtTask(true);
			mTplalformtTask.execute();
		}else{
			mTplalformtTask.stop();
			mTplalformtTask = null;
			mTplalformtTask = new TplalformtTask(true);
			mTplalformtTask.execute();
		}
	}
	/**
	 * 
	 * @author Fei
	 *
	 */
	
	private class GoodCollectionTask extends AsyncTask<String, Void, BaseResult> {
		
		int index,collectFlag,collectCount;
		String goodId;
		
		protected GoodCollectionTask(int index,String goodId,int collectFlag,int collectCount) {
			this.index = index;
			this.collectFlag = collectFlag;
			this.goodId = goodId;
			this.collectCount = collectCount;
		}
		
		@Override
		protected BaseResult doInBackground(String... params) {
			
			JSONAccessor mAccessor = new JSONAccessor(mMenuActivity, HttpAccessor.METHOD_POST);
			GoodCollectionParam param = new GoodCollectionParam();
			param.setUserId(AppContents.getUserInfo().getId());
			param.setCollectFlag(collectFlag);
			param.setGoodsId(goodId);
			return mAccessor.execute(Settings.GOOD_COLLECTION, param,BaseResult.class);
		}
		
		@Override
		protected void onPostExecute(BaseResult result) {
			if (result != null) {
				if(collectFlag == 0){
					FashionList.get(index).setCollectFlag(1);
					FashionList.get(index).setCollectCount(collectCount+1);
				}else{
					FashionList.get(index).setCollectFlag(0);
					FashionList.get(index).setCollectCount(collectCount-1);
				}
				mTplatformAdapter.notifyDataSetChanged();
			} else {
//				Toast.makeText(LoadingActivity.this, getString(R.string.net_error), Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	@Override
	public void onResume() {
		mTplatformListView.setRefreshing(true);
		mCategoryId = "";// 类目ID
		mBodyStyleId = "";// 身型ID
		mStyleId = "";// 风格ID
		mColorStyleId = "";// 色系ID
		mPriceId = "";// 价格区间ID
		mBrandId = "";// 品牌ID
		super.onResume();
	}
}
