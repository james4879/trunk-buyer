package com.datapush.buyhand.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daoshun.lib.communication.http.HttpAccessor;
import com.daoshun.lib.communication.http.JSONAccessor;
import com.daoshun.lib.listview.PullToRefreshBase;
import com.daoshun.lib.listview.PullToRefreshBase.Mode;
import com.daoshun.lib.listview.PullToRefreshListView;
import com.daoshun.lib.util.ImageLoader.OnLoadListener;
import com.daoshun.lib.view.OnSingleClickListener;
import com.datapush.buyhand.R;
import com.datapush.buyhand.common.Settings;
import com.datapush.buyhand.net.data.Fabric;
import com.datapush.buyhand.net.data.FabricListParam;
import com.datapush.buyhand.net.data.FabricListResult;
import com.datapush.buyhand.util.BitmapUtil;
import com.datapush.buyhand.util.CacheImageLoader;

/**
 * 晒物
 * 
 * @author yanpf
 *
 */

public class SelectionFragment extends BaseFragment {

	private MenuActivity mMenuActivity;
	private Button HotButton, NewestButton, BabyButton;// 热门，最新，宝贝
	private ImageButton AskButton;// 问问
	private int StateIndex = -1;
	private FabricAdapter mFabricAdapter;
	private PullToRefreshListView mFabricList;
	private int pageNo =1;//页数
	private int type = 0;//类型
	private String latestime="";//时间
	private String CommentCount = "";//评论数
	private List<Fabric> modelList = new ArrayList<Fabric>();
	private FabricListTask mFabricListTask;
	private CacheImageLoader mCacheImageLoader;//图片下载共通
	

	@Override
	public void onAttach(Activity activity) {
		mMenuActivity = (MenuActivity) getActivity();
		super.onAttach(activity);
	}

	@Override
	public View onInitView(LayoutInflater inflater, ViewGroup container) {
		View view = inflater.inflate(R.layout.selection_layout, container,false);
		HotButton = (Button) view.findViewById(R.id.hot);
		NewestButton = (Button) view.findViewById(R.id.newest);
		BabyButton = (Button) view.findViewById(R.id.baby);
		AskButton = (ImageButton) view.findViewById(R.id.ask);
		mFabricList = (PullToRefreshListView) view.findViewById(R.id.list);
		mCacheImageLoader = new CacheImageLoader(mMenuActivity);
		initView();
		return view;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void initView() {
		
		AskButton.setOnClickListener(new OnSingleClickListener() {
			
			@Override
			public void doOnClick(View v) {
				if(isLogin()){
				Intent intent = new Intent(mMenuActivity,HelpMeChooseActivity.class);
				startActivity(intent);
				}
			}
		});

		setState(R.id.hot);
		HotButton.setOnClickListener(new OnSingleClickListener() {

			@Override
			public void doOnClick(View v) {
				setState(v.getId());
			}
		});
		NewestButton.setOnClickListener(new OnSingleClickListener() {

			@Override
			public void doOnClick(View v) {
				setState(v.getId());
			}
		});
		BabyButton.setOnClickListener(new OnSingleClickListener() {

			@Override
			public void doOnClick(View v) {
				setState(v.getId());
			}
		});
		mFabricAdapter = new FabricAdapter();
		mFabricList.setAdapter(mFabricAdapter);
		mFabricList.setMode(Mode.BOTH);
		
		mFabricList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				String label = DateUtils.formatDateTime(mMenuActivity, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				mFabricListTask = new FabricListTask(true);
				mFabricListTask.execute();
				
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				String label = DateUtils.formatDateTime(mMenuActivity, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				mFabricListTask = new FabricListTask(false);
				mFabricListTask.execute();
			}
		});
		mFabricList.setRefreshing(true);
		
		mFabricList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(mMenuActivity,FabricDetailActivity.class);
				intent.putExtra("id", modelList.get(position-1).getId());
				startActivity(intent);
			}
		});
		
	}

	// 改变选择状态
	@SuppressLint("ResourceAsColor")
	public void setState(int resid) {
		if (resid == StateIndex) {
			return;
		} else {
			StateIndex = resid;
		}
		HotButton.setTextColor(Color.parseColor("#ffffff"));
		NewestButton.setTextColor(Color.parseColor("#ffffff"));
		BabyButton.setTextColor(Color.parseColor("#ffffff"));
		switch (resid) {
		case R.id.hot:
			type = 0;
			HotButton.setTextColor(Color.parseColor("#CD3700"));
			if(mFabricListTask == null){
				mFabricList.setRefreshing(true);
			}else{
				mFabricListTask.stop();
				mFabricList.setRefreshing(true);
			}
			break;
		case R.id.newest:
			type = 1;
			NewestButton.setTextColor(Color.parseColor("#CD3700"));
			if(mFabricListTask == null){
				mFabricList.setRefreshing(true);
			}else{
				mFabricListTask.stop();
				mFabricList.setRefreshing(true);
			}
			break;
		case R.id.baby:
			type = 2;
			BabyButton.setTextColor(Color.parseColor("#CD3700"));
			if(mFabricListTask == null){
				mFabricList.setRefreshing(true);
			}else{
				mFabricListTask.stop();
				mFabricList.setRefreshing(true);
			}
			break;
		}
	}
	/**
	 * 晒物列表适配器
	 */
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
				convertView = getLayoutInflater().from(mMenuActivity).inflate(R.layout.fabric_item, parent, false);
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
		
		JSONAccessor accessor;
		boolean bl;
		public FabricListTask(boolean bl) {
			if(bl){
				this.bl = bl;
				latestime = "";
				pageNo =1;
				CommentCount = "";
			}
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if(bl){
				mFabricList.setMode(Mode.BOTH);
			}
		}

		@Override
		protected FabricListResult doInBackground(String... arg0) {
			
			accessor = new JSONAccessor(mMenuActivity, HttpAccessor.METHOD_GET);
			FabricListParam param = new FabricListParam();
			param.setLatestime(latestime);
			param.setPageNo(pageNo);
			param.setType(type);
			if(type == 0){
				param.setCommentCount(CommentCount);
			}
			return accessor.execute(Settings.TOPICLIST,param , FabricListResult.class);
		}
		
		@Override
		protected void onPostExecute(FabricListResult result) {
			
			mFabricList.onRefreshComplete();
			
			if(result != null && result.getTopicList() != null){
				if(bl){
				modelList.clear();
				}
				modelList.addAll(result.getTopicList());
				if(pageNo >= result.getTotalPage()){
					mFabricList.setMode(Mode.PULL_FROM_START);
				}else{
					pageNo +=1;
					latestime = modelList.get(0).getLongPublishedTime();
					CommentCount =  modelList.get(0).getCommentCount();
				}
				mFabricAdapter.notifyDataSetChanged();
			} else {
				Toast.makeText(mMenuActivity, getString(R.string.net_error), Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(result);
			mFabricListTask = null;
		}

		public void stop() {
			if (accessor != null) {
				accessor.stop();
			}
		}
	}
	
}
