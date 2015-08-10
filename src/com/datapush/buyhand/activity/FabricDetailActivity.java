package com.datapush.buyhand.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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
import com.datapush.buyhand.common.Settings;
import com.datapush.buyhand.net.data.BaseResult;
import com.datapush.buyhand.net.data.BuyerTopicImageForm;
import com.datapush.buyhand.net.data.CollectParam;
import com.datapush.buyhand.net.data.Comment;
import com.datapush.buyhand.net.data.CommentListParam;
import com.datapush.buyhand.net.data.CommentListResult;
import com.datapush.buyhand.net.data.FabricDetailParam;
import com.datapush.buyhand.net.data.FabricDetailResult;
import com.datapush.buyhand.net.data.PostComemnt;
import com.datapush.buyhand.net.data.ZanCaiParam;
import com.datapush.buyhand.util.BitmapUtil;
import com.datapush.buyhand.util.CacheImageLoader;

/**
 * 晒物详情
 * 
 * @author Fei
 *
 */

public class FabricDetailActivity extends BaseActivity{
	private TextView mLove;
	private RelativeLayout comment;
	private EditText mEditText;
	private Button mPostView;
	private ImageView mHeadView;
	private ImageView mBack;
	private Button mMore;//更多评论
	private TextView mNameText,mTitleText,mInfoText;
	private CacheImageLoader mCacheImageLoader;//图片下载共通
	private LinearLayout mPicturelistLayout;
	private ScrollView mScrollView;
	private List<BuyerTopicImageForm> imageList = new ArrayList<BuyerTopicImageForm>();
	private int mIndexFlg = 0;//控制点的那个图片赞踩
	private List<TextView> mZanListView = new ArrayList<TextView>(); 
	private List<TextView> mCaiListView = new ArrayList<TextView>(); 
	private List<Comment> commentList = new ArrayList<Comment>();
	private FabricAdapter mFabricAdapter;
	private PullToRefreshListView mFabricList;	
	private int pageNo =1;//页数
	private String topicId = "4242";//话题ID
	private String latestime="";//时间
	private int CollectFlag;
	private String CollectCount;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fabricdetail_layout);
		topicId = getIntent().getStringExtra("id");
		mCacheImageLoader = new CacheImageLoader(FabricDetailActivity.this);
		findView();
		initView();
	}

	@Override
	protected void onResume() {
		new FabRicDetailTask().execute();
		super.onResume();
	}
	
	public void findView(){
		mHeadView = (ImageView) findViewById(R.id.head);
		mBack = (ImageView) findViewById(R.id.back);
		mNameText = (TextView) findViewById(R.id.name);
		mTitleText = (TextView) findViewById(R.id.title);
		mInfoText = (TextView) findViewById(R.id.info);
		mPicturelistLayout = (LinearLayout) findViewById(R.id.picturelist);
		mScrollView = (ScrollView) findViewById(R.id.sl);
		mFabricList = (PullToRefreshListView) findViewById(R.id.list);
		mMore = (Button) findViewById(R.id.more);
		comment = (RelativeLayout) findViewById(R.id.comment);
		mEditText = (EditText) findViewById(R.id.edit);
		mPostView = (Button) findViewById(R.id.post);
		 mLove = (TextView) findViewById(R.id.love);
		 
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void initView(){
		mHeadView.setImageBitmap(BitmapUtil.getRoundBitmap(BitmapFactory.decodeStream(getResources().openRawResource(R.drawable.head_img_bg))));
		
		mBack.setOnClickListener(new OnSingleClickListener() {
			
			@Override
			public void doOnClick(View v) {
				finish();
			}
		});
		
		mLove.setOnClickListener(new OnSingleClickListener() {
			
			@Override
			public void doOnClick(View v) {

				if(isLogin()){
					new CollectTask().execute();
				}
				
			}
		});
		
		mMore.setOnClickListener(new OnSingleClickListener() {
			
			@Override
			public void doOnClick(View v) {
				mMore.setVisibility(View.GONE);
				new FabricListTask(false).execute();
			}
		});
		
		/**评论操作**/
		mFabricAdapter = new FabricAdapter();
		mFabricList.setAdapter(mFabricAdapter);
		mFabricList.setMode(Mode.BOTH);
		
		mFabricList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				String label = DateUtils.formatDateTime(FabricDetailActivity.this, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				new FabricListTask(true).execute();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				
			}
		});
		mFabricList.setRefreshing(true);
		
		mPostView.setOnClickListener(new OnSingleClickListener() {
			
			@Override
			public void doOnClick(View v) {
				
			if(isLogin()){
				if(mEditText.getText().toString().length() >0){
					mPostView.setClickable(false);
					new PostCommentTask().execute();
				}else{
					Toast.makeText(FabricDetailActivity.this,getString(R.string.neirong), Toast.LENGTH_SHORT).show();
				}
				}
			}
		});
		
	}
	//动态添加数据
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void UpdateUi(FabricDetailResult result){
		
		mLove.setText(result.getCollectCount());
		
		CollectFlag = result.getCollect().getCollectFlag();
		CollectCount = result.getCollectCount();
		
		if(CollectFlag == 0){
			mLove.setCompoundDrawablesWithIntrinsicBounds(getBaseContext().getResources().getDrawable(R.drawable.love_n), null, null, null);
		}else{
			mLove.setCompoundDrawablesWithIntrinsicBounds(getBaseContext().getResources().getDrawable(R.drawable.love_p), null, null, null);
		}
		
		imageList.clear();
		imageList.addAll(result.getImageList());
	/*加载头像*/	
	if(result.getHeadPic() != null && result.getHeadPic().length() >0){
	mCacheImageLoader.loadImage(result.getHeadPic(), mHeadView, new OnLoadListener() {
			
			@Override
			public void onLoad(Bitmap bitmap, View targetView) {
				if(bitmap != null){
					((ImageView) targetView).setImageBitmap(BitmapUtil.getRoundBitmap(bitmap)); 
				}
			}
		});}
		/* 加载资料 */
		mNameText.setText(result.getUserName());
		mTitleText.setText(result.getTitle());
		mInfoText.setText(result.getContent());

		mZanListView.clear();
		mCaiListView.clear();

		mPicturelistLayout.removeAllViewsInLayout();
		
		
		int w = (Settings.DISPLAY_WIDTH - DensityUtils.dp2px(FabricDetailActivity.this, 1))/2;
		for (int i = 0; i < result.getImageList().size(); i++) {
		final int index	= i;
		View  view = getLayoutInflater().inflate(R.layout.picturelist_item, null);
		ImageView pic = (ImageView) view.findViewById(R.id.pic);
		RelativeLayout rl = (RelativeLayout) view.findViewById(R.id.rl);
		TextView zan = (TextView) view.findViewById(R.id.zan);
		mZanListView.add(zan);
		TextView cai = (TextView) view.findViewById(R.id.cai);
		mCaiListView.add(cai);
		
		LinearLayout.LayoutParams pl = (LinearLayout.LayoutParams) pic.getLayoutParams();
		pl.width = w;
		pl.height = w*3/2;
		pic.setLayoutParams(pl);
		
		mCacheImageLoader.loadImage(result.getImageList().get(i).getImagePath(), pic, new OnLoadListener() {
			
			@Override
			public void onLoad(Bitmap bitmap, View targetView) {
				if(bitmap != null){
					((ImageView) targetView).setImageBitmap(bitmap); 
				}
			}
		});
		
		LinearLayout.LayoutParams pl1 = (LinearLayout.LayoutParams) rl.getLayoutParams();
		pl1.width = w;
		pl1.height = DensityUtils.dp2px(FabricDetailActivity.this, 29);
		rl.setLayoutParams(pl1);
		
		ImageView img = new ImageView(FabricDetailActivity.this);
		LinearLayout.LayoutParams pl2 = new LinearLayout.LayoutParams(DensityUtils.dp2px(FabricDetailActivity.this, 1), w*3/2+DensityUtils.dp2px(FabricDetailActivity.this, 29));
		img.setBackgroundColor(Color.BLACK);
		
		//控制赞和踩
		zan.setText(result.getImageList().get(i).getPraiseCount());
		cai.setText(result.getImageList().get(i).getStepCount());
		if(imageList.get(index).getPraiseFlag() == 0){
			zan.setCompoundDrawablesWithIntrinsicBounds(getBaseContext().getResources().getDrawable(R.drawable.zan_bg_n), null, null, null);
		}else{
			zan.setCompoundDrawablesWithIntrinsicBounds(getBaseContext().getResources().getDrawable(R.drawable.zan_bg_p), null, null, null);
		}
		if(imageList.get(index).getStepFlag() == 0){
			cai.setCompoundDrawablesWithIntrinsicBounds(getBaseContext().getResources().getDrawable(R.drawable.cai_bg_n), null, null, null);
		}else{
			cai.setCompoundDrawablesWithIntrinsicBounds(getBaseContext().getResources().getDrawable(R.drawable.zan_bg_n), null, null, null);
		}
		zan.setOnClickListener(new OnSingleClickListener() {
			
			@Override
			public void doOnClick(View v) {
				if(isLogin()){
					mIndexFlg = index;
					if(imageList.get(index).getStepFlag() == 0){
					if(imageList.get(index).getPraiseFlag() == 0){
						//赞
						new PostTask().execute("0");
					}else{
						//取消赞
						new CancelTask().execute("0");
					}
				}
			}
			}
		});
		cai.setOnClickListener(new OnSingleClickListener() {
			
			@Override
			public void doOnClick(View v) {
				if(isLogin()){
				mIndexFlg = index;
				if(imageList.get(index).getPraiseFlag() == 0){
				if(imageList.get(index).getStepFlag() == 0){
					//踩
					new PostTask().execute("1");
				}else{
					//取消踩
					new CancelTask().execute("1");
				}
				}
			}
			}
		});
		
		
		mPicturelistLayout.addView(view);
		if(i != result.getImageList().size() -1){
		mPicturelistLayout.addView(img,pl2);
		}
		}
		
	}
	/**
	 * 话题详情异步类
	 * @author Fei
	 *
	 */
	private class FabRicDetailTask extends AsyncTask<String, Void, FabricDetailResult> {
		
		
		private ProgressDialog mProgressDialog;

		@Override
		protected void onPreExecute() {

			mProgressDialog = new ProgressDialog(FabricDetailActivity.this);
			mProgressDialog.setTitle(R.string.app_name);
			mProgressDialog.setMessage(getString(R.string.post));
			mProgressDialog.setCanceledOnTouchOutside(false);
			mProgressDialog.show();
		}

		@Override
		protected FabricDetailResult doInBackground(String... arg0) {

			JSONAccessor accessor = new JSONAccessor(FabricDetailActivity.this,HttpAccessor.METHOD_GET);
			accessor.enableJsonLog(true);
			FabricDetailParam param = new FabricDetailParam();
			param.setTopicId(topicId);
			if(AppContents.getUserInfo().getId() != null && AppContents.getUserInfo().getId().length() >0){
			param.setUserId(AppContents.getUserInfo().getId());
			}
			return accessor.execute(Settings.TOPICDETAIL, param, FabricDetailResult.class);
		}

		@Override
		protected void onPostExecute(FabricDetailResult result) {
			mProgressDialog.dismiss();
			if (result != null) {
					mScrollView.setVisibility(View.VISIBLE);
					UpdateUi(result);
			} else {
				Toast.makeText(FabricDetailActivity.this,getString(R.string.net_error), Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(result);
		}

	}
	
	private class PostTask extends AsyncTask<String, Void, BaseResult> {
		
		String flg = "";

		@Override
		protected BaseResult doInBackground(String... arg0) {

			JSONAccessor accessor = new JSONAccessor(FabricDetailActivity.this,HttpAccessor.METHOD_GET);
			accessor.enableJsonLog(true);
			ZanCaiParam param = new ZanCaiParam();
			param.setFlag(arg0[0]);
			flg = arg0[0];
			param.setImageId(imageList.get(mIndexFlg).getId());
			param.setImageType(imageList.get(mIndexFlg).getImageType());
			param.setUserId(AppContents.getUserInfo().getId());
			return accessor.execute(Settings.POST, param, BaseResult.class);
		}

		@Override
		protected void onPostExecute(BaseResult result) {
			if (result != null && result.getCode() == 0) {
				
				if(flg.equals("0")){
				mZanListView.get(mIndexFlg).setCompoundDrawablesWithIntrinsicBounds(getBaseContext().getResources().getDrawable(R.drawable.zan_bg_p), null, null, null);
				imageList.get(mIndexFlg).setPraiseFlag(1);
				String number = imageList.get(mIndexFlg).getPraiseCount();
				imageList.get(mIndexFlg).setPraiseCount(Integer.parseInt(number)+1+"");
				mZanListView.get(mIndexFlg).setText(imageList.get(mIndexFlg).getPraiseCount());
				}else{
					mCaiListView.get(mIndexFlg).setCompoundDrawablesWithIntrinsicBounds(getBaseContext().getResources().getDrawable(R.drawable.cai_bg_p), null, null, null);
					imageList.get(mIndexFlg).setStepFlag(1);
					String number = imageList.get(mIndexFlg).getStepCount();
					imageList.get(mIndexFlg).setStepCount(Integer.parseInt(number)+1+"");
					mCaiListView.get(mIndexFlg).setText(imageList.get(mIndexFlg).getStepCount());
				}
				
			} else {
				Toast.makeText(FabricDetailActivity.this,getString(R.string.net_error), Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(result);

		}

	}
	
	private class CancelTask extends AsyncTask<String, Void, BaseResult> {
		
		String flg = "";

		@Override
		protected BaseResult doInBackground(String... arg0) {

			JSONAccessor accessor = new JSONAccessor(FabricDetailActivity.this,HttpAccessor.METHOD_GET);
			accessor.enableJsonLog(true);
			ZanCaiParam param = new ZanCaiParam();
			param.setFlag(arg0[0]);
			flg = arg0[0];
			param.setImageId(imageList.get(mIndexFlg).getId());
			param.setImageType(imageList.get(mIndexFlg).getImageType());
			param.setUserId(AppContents.getUserInfo().getId());
			return accessor.execute(Settings.CANCEL, param, BaseResult.class);
		}

		@Override
		protected void onPostExecute(BaseResult result) {
			if (result != null && result.getCode() == 0) {
				
				if(flg.equals("0")){
				mZanListView.get(mIndexFlg).setCompoundDrawablesWithIntrinsicBounds(getBaseContext().getResources().getDrawable(R.drawable.zan_bg_n), null, null, null);
				imageList.get(mIndexFlg).setPraiseFlag(0);
				String number = imageList.get(mIndexFlg).getPraiseCount();
				imageList.get(mIndexFlg).setPraiseCount(Integer.parseInt(number)-1+"");
				mZanListView.get(mIndexFlg).setText(imageList.get(mIndexFlg).getPraiseCount());
				}else{
					mCaiListView.get(mIndexFlg).setCompoundDrawablesWithIntrinsicBounds(getBaseContext().getResources().getDrawable(R.drawable.cai_bg_n), null, null, null);
					imageList.get(mIndexFlg).setStepFlag(0);
					String number = imageList.get(mIndexFlg).getStepCount();
					imageList.get(mIndexFlg).setStepCount(Integer.parseInt(number)-1+"");
					mCaiListView.get(mIndexFlg).setText(imageList.get(mIndexFlg).getStepCount());
				}
				
			} else {
				Toast.makeText(FabricDetailActivity.this,getString(R.string.net_error), Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(result);

		}

	}
	
	/**
	 * 晒物列表适配器
	 */
	public class FabricAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return commentList.size();
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
				convertView = getLayoutInflater().from(FabricDetailActivity.this).inflate(R.layout.comment_item, parent, false);
				holer.head = (ImageView) convertView.findViewById(R.id.head);
				holer.name = (TextView) convertView.findViewById(R.id.name);
				holer.title = (TextView) convertView.findViewById(R.id.title);
				holer.titme = (TextView) convertView.findViewById(R.id.titme);
				convertView.setTag(holer);
			}else{
				holer = (HolerView) convertView.getTag();
			}
			holer.head.setImageBitmap(BitmapUtil.getRoundBitmap(BitmapFactory.decodeStream(getResources().openRawResource(R.drawable.head_img_bg))));
			if(commentList.get(position).getHeadPic() != null && commentList.get(position).getHeadPic().length() >0){
				mCacheImageLoader.loadImage(commentList.get(position).getHeadPic(), holer.head, new OnLoadListener() {
					
					@Override
					public void onLoad(Bitmap bitmap, View targetView) {
						if(bitmap != null){
							((ImageView) targetView).setImageBitmap(BitmapUtil.getRoundBitmap(bitmap)); 
						}
					}
				});
			}
			holer.name.setText(commentList.get(position).getUserName());
			holer.title.setText(commentList.get(position).getContent());
			holer.titme.setText(commentList.get(position).getPublishedTime());
			return convertView;
		}

		class HolerView {
			
			ImageView head;
			TextView name, title, titme;
		}

	}
	
	private class FabricListTask extends AsyncTask<String, Void, CommentListResult>{
		
		JSONAccessor accessor;
		boolean bl;
		public FabricListTask(boolean bl) {
			if(bl){
				this.bl = bl;
				latestime = "";
				pageNo =1;
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
		protected CommentListResult doInBackground(String... arg0) {
			
			accessor = new JSONAccessor(FabricDetailActivity.this, HttpAccessor.METHOD_GET);
			accessor.enableJsonLog(true);
			CommentListParam param = new CommentListParam();
			param.setLatestime(latestime);
			param.setPageNo(pageNo);
			param.setTopicId(topicId);
			return accessor.execute(Settings.COMMENTLIST,param , CommentListResult.class);
		}
		
		@Override
		protected void onPostExecute(CommentListResult result) {
			
			mFabricList.onRefreshComplete();
			
			if(result != null && result.getCommentList() != null){
				if(bl){
					commentList.clear();
				}
				comment.setVisibility(View.VISIBLE);
				commentList.addAll(result.getCommentList());
				if(pageNo >= result.getTotalPage()){
					mFabricList.setMode(Mode.PULL_FROM_START);
					mMore.setVisibility(View.GONE);
				}else{
					pageNo +=1;
					latestime = commentList.get(0).getLongPublishedTime();
					mMore.setVisibility(View.VISIBLE);
				}
				mFabricAdapter.notifyDataSetChanged();
				setListViewHeightBasedOnChildren(mFabricList);
			} else {
				comment.setVisibility(View.GONE);
				Toast.makeText(FabricDetailActivity.this, getString(R.string.net_error), Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(result);
		}

	}
	
	public void setListViewHeightBasedOnChildren(PullToRefreshListView listView) {
		ListView mRefreshListView = listView.getRefreshableView();
		ListAdapter listAdapter = mRefreshListView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (mRefreshListView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}
	
	private class PostCommentTask extends AsyncTask<String, Void, BaseResult>{

		@Override
		protected BaseResult doInBackground(String... params) {
			JSONAccessor accessor = new JSONAccessor(FabricDetailActivity.this, HttpAccessor.METHOD_GET);
			PostComemnt param = new PostComemnt();
			param.setUserId(AppContents.getUserInfo().getId());
			param.setTopicId(topicId);
			param.setContent(mEditText.getText().toString());
			return accessor.execute(Settings.POSTCOMMENT,param , BaseResult.class);
		}
		
		@Override
		protected void onPostExecute(BaseResult result) {
			mPostView.setClickable(true);
			if (result != null && result.getCode() == 0) {
				
				mFabricList.setRefreshing(true);
				mEditText.setText("");
				
			} else {
				Toast.makeText(FabricDetailActivity.this,getString(R.string.net_error), Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(result);

		}
		
	}
	//收藏
	private class CollectTask extends AsyncTask<String, Void, BaseResult> {
		
		@Override
		protected BaseResult doInBackground(String... arg0) {

			JSONAccessor accessor = new JSONAccessor(FabricDetailActivity.this,HttpAccessor.METHOD_GET);
			accessor.enableJsonLog(true);
			CollectParam param = new CollectParam();
			param.setUserId(AppContents.getUserInfo().getId());
			param.setTopicId(topicId);
			param.setCollectFlag(CollectFlag);
			return accessor.execute(Settings.COLLECT, param, BaseResult.class);
		}

		@Override
		protected void onPostExecute(BaseResult result) {
			if (result != null && result.getCode() == 0) {
				
				if(CollectFlag== 0){
					CollectFlag = 1;
					mLove.setCompoundDrawablesWithIntrinsicBounds(getBaseContext().getResources().getDrawable(R.drawable.love_p), null, null, null);
					CollectCount = Integer.parseInt(CollectCount) +1+"";
					mLove.setText(CollectCount);
				}else{
					mLove.setCompoundDrawablesWithIntrinsicBounds(getBaseContext().getResources().getDrawable(R.drawable.love_n), null, null, null);
					CollectCount = Integer.parseInt(CollectCount) -1+"";
					mLove.setText(CollectCount);
					CollectFlag = 0;
				}
				
			} else {
				Toast.makeText(FabricDetailActivity.this,getString(R.string.net_error), Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(result);

		}

	}
}
