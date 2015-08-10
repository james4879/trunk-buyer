package com.datapush.buyhand.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daoshun.lib.communication.http.HttpAccessor;
import com.daoshun.lib.communication.http.JSONAccessor;
import com.daoshun.lib.util.DensityUtils;
import com.daoshun.lib.util.ImageLoader.OnLoadListener;
import com.daoshun.lib.view.OnSingleClickListener;
import com.datapush.buyhand.R;
import com.datapush.buyhand.activity.TplatformFragment.SideAdapter.HolerView;
import com.datapush.buyhand.common.AppContents;
import com.datapush.buyhand.common.Settings;
import com.datapush.buyhand.net.data.BaseResult;
import com.datapush.buyhand.net.data.ClothingForMatchParam;
import com.datapush.buyhand.net.data.ClothingForMatchResult;
import com.datapush.buyhand.net.data.Combination;
import com.datapush.buyhand.net.data.ZanCaiParam;
import com.datapush.buyhand.net.data.cloth;
import com.datapush.buyhand.util.BitmapUtil;
import com.datapush.buyhand.util.CacheImageLoader;
import com.datapush.buyhand.view.HorizontalListView;
import com.datapush.buyhand.view.ScreenShot;

/**
 * 搭配组合类
 * 
 * @author yanpf
 *
 */
public class CombinationActivity extends BaseActivity {

	/** 商品 **/
	private ImageView mBack;
	private int[] ImageList = new int[] {R.drawable.a1,
			R.drawable.a2, R.drawable.a3, R.drawable.a4, R.drawable.a5,
			R.drawable.a6};
	private int index;
	
	private Button bt1,bt2,bt3,bt4;
	private List<Button> mListBt = new ArrayList<Button>();
	private int mSetectId = -1;

	/** 搭配 **/
	private ImageView mCombinationBack;
	private FrameLayout mFrameLayoutAddView;
	private ImageView mCombinationPost;

	private HorizontalScrollView mFrameScr;
	private ViewGroup mFrameGroup;
	private String mUuuid;
	
	private ListView mListView;//选择类目listView
	private List<Combination> combinationList = new ArrayList<Combination>();
	private  SideAdapter mSideAdapter;
	
	private HorizontalListView mHostListView;//横向listView
	private List<cloth> clothingList = new ArrayList<cloth>();
	private HostViewAdapter mHostViewAdapter;
	private CacheImageLoader mCacheImageLoader;
	
	private int mState = 0;
	private String categoryId = "";
	private String seasonId = "";
	private String styleId = "";

	/*** 图片处理 */
	Matrix matrix = new Matrix();
	Matrix savedMatrix = new Matrix();
	static final int NONE = 0; // nothing
	static final int DRAG = 1; // 拖拉
	static final int ZOOM = 2; // 放大或缩小
	int mode = NONE;
	PointF start = new PointF();
	PointF mid = new PointF();
	float oldDist = 1f;
	float oldRotation = 0f;
	private ArrayList<Holder> list_V;
	int selectImageCount = -1;
	int height = 0;
	int width = 0;
	int bitmapWidth = 0;
	int bitmapHeight = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.combination_layout);
		findView();
		initView();
		index = getIntent().getIntExtra("index", 0);
		if(index > 0){
			mFrameLayoutAddView.setBackgroundResource(ImageList[index-1]);
		}else{
			mFrameLayoutAddView.setBackgroundResource(R.drawable.morenkuang);
		}
		mCacheImageLoader = new CacheImageLoader(CombinationActivity.this);
		new HostTask().execute();
		}

	public void findView() {
		CombinationFindView();

	}

	public void initView() {
		CombinationInitView();
	}

	/** 搭配 **/
	public void CombinationFindView() {
		list_V = new ArrayList<Holder>();
		mFrameLayoutAddView = (FrameLayout) findViewById(R.id.addview);
		mCombinationBack = (ImageView) findViewById(R.id.combination_back);
		mCombinationPost = (ImageView) findViewById(R.id.post);
		mFrameScr = (HorizontalScrollView) findViewById(R.id.three_scoview);
		mFrameGroup = (ViewGroup) findViewById(R.id.three_ll);
		mListView = (ListView) findViewById(R.id.side_list);
		mHostListView = (HorizontalListView) findViewById(R.id.hostview);
		
		bt1 = (Button) findViewById(R.id.bt1);
		bt2 = (Button) findViewById(R.id.bt2);
		bt3 = (Button) findViewById(R.id.bt3);
		bt4 = (Button) findViewById(R.id.bt4);
		mListBt.add(bt1);
		mListBt.add(bt2);
		mListBt.add(bt3);
		mListBt.add(bt4);
	}

	public void CombinationInitView() {
		
		mSideAdapter = new SideAdapter();
		mListView.setAdapter(mSideAdapter);
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
					if(mState == 1){
						categoryId = combinationList.get(position).getId();
					}
					else	if(mState == 2){
						seasonId = combinationList.get(position).getId();
					}
					else if(mState == 3){
						styleId = combinationList.get(position).getId();
					}
					mListView.setVisibility(View.GONE);
					new HostTask().execute();
			}
		});
		

		mCombinationBack.setOnClickListener(new OnSingleClickListener() {

			@Override
			public void doOnClick(View v) {
				finish();
			}
		});
		
		mCombinationPost.setOnClickListener(new OnSingleClickListener() {

			@Override
			public void doOnClick(View v) {
				if(mFrameLayoutAddView.getChildCount() >0){
					if(isLogin()){
					mUuuid = UUID.randomUUID().toString();
					String str = Settings.TEMP_PATH + mUuuid + ".jpg";
					ScreenShot.shoot(CombinationActivity.this, str);
					mListView.setVisibility(View.GONE);
					Intent intent = new Intent(CombinationActivity.this,ScreenshotActivity.class);
					intent.putExtra("str", str);
					startActivity(intent);
					}
				}else{
					Toast.makeText(CombinationActivity.this, "您还没有搭配宝贝!", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		int w = (Settings.DISPLAY_WIDTH - DensityUtils.dp2px(CombinationActivity.this, 90)) / 5;
		mHostViewAdapter = new HostViewAdapter();
		mHostListView.getLayoutParams().height = w*3/2;
		mHostListView.setAdapter(mHostViewAdapter);
		
		
		
//
//		VoidHoler holer = null;
//		View view = null;
//		int w = (Settings.DISPLAY_WIDTH - DensityUtils.dp2px(CombinationActivity.this, 90)) / 5;
//		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(w, w*3/2);
//		lp.setMargins(DensityUtils.dp2px(CombinationActivity.this, 8),
//				DensityUtils.dp2px(CombinationActivity.this, 8),
//				DensityUtils.dp2px(CombinationActivity.this, 8),
//				DensityUtils.dp2px(CombinationActivity.this, 8));
//		for (int i = 0; i < ImageList.length; i++) {
//			final int index = i;
//			holer = new VoidHoler();
//			view = getLayoutInflater().inflate(R.layout.picture_item, null,false);
//			holer.image = (ImageView) view.findViewById(R.id.image);
//			holer.image.setBackgroundResource(ImageList[i]);
//			view.setLayoutParams(lp);
//			view.setOnClickListener(new OnSingleClickListener() {
//			
//				@Override
//				public void doOnClick(View v) {
//					
//					setImgView(ImageList[index]);
//				}
//			});
//			mFrameGroup.addView(view);
//		}
		
		mHostListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				setImgView(clothingList.get(position).getImagePath());
			}
		});
		
		for (int i = 0; i < mListBt.size(); i++) {
			final int index = i;
			
			mListBt.get(i).setOnClickListener(new OnSingleClickListener() {
				
				@Override
				public void doOnClick(View v) {
					setSelect(v.getId(),index);
				}
			});
		}
	}

	/**
	 * 添加桌面图片
	 */
	public void setImgView(String  resid) {
		

		height = getWindowManager().getDefaultDisplay().getHeight();
		width = getWindowManager().getDefaultDisplay().getWidth();
		ImageView imageView = new ImageView(this);
		imageView.setLayoutParams(new LayoutParams(width, height));
		imageView.setScaleType(ScaleType.MATRIX);
		imageView.setImageBitmap(BitmapFactory.decodeStream(getResources().openRawResource(R.drawable.pohoto_bg)));
		mCacheImageLoader.loadImage(resid, imageView, new OnLoadListener() {
			
			@Override
			public void onLoad(Bitmap bitmap, View targetView) {
				if(bitmap != null){
					bitmapWidth = bitmap.getWidth();
					bitmapHeight = bitmap.getHeight();
					((ImageView) targetView).setImageBitmap(bitmap); 
				}
			}
		});
		mFrameLayoutAddView.addView(imageView);
		Matrix imageMatrix = new Matrix(imageView.getImageMatrix());
		imageMatrix.postTranslate(width / 4, height / 4);
		imageView.setImageMatrix(imageMatrix);
		ImageState state1 = new ImageState();
		float[] values = new float[9];
		imageMatrix.getValues(values);
		state1.setLeft(values[2]);
		state1.setTop(values[5]);
		state1.setRight(values[2] + bitmapWidth * values[0]);
		state1.setBottom(values[5] + bitmapHeight * values[0]);
		Log.e("state", state1.toString());
		Holder holder1 = new Holder();
		holder1.setImgV(imageView);
		holder1.setState(state1);
		list_V.add(holder1);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			selectImG(event);
			if (selectImageCount != -1) {
				matrix.set(list_V.get(selectImageCount).getImgV()
						.getImageMatrix());
				// 保存之前的图片大小
				savedMatrix.set(matrix);
				// 保存第一次按下的坐标
				start.set(event.getX(), event.getY());
				mode = DRAG;
			}
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			selectImG(event);
			if (selectImageCount != -1) {
				oldDist = spacing(event);
				if (oldDist > 10f) {
					savedMatrix.set(matrix);
					// 得到两只手指按下去的时候的中点
					midPoint(mid, event);
					oldRotation = rotation(event);
					mode = ZOOM;
				}
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
			mode = NONE;
			break;
		case MotionEvent.ACTION_MOVE:

			if (selectImageCount != -1) {
				if (mode == DRAG) {
					matrix.set(savedMatrix);
					// 图片移动的距离
					matrix.postTranslate(event.getX() - start.x, event.getY()
							- start.y);
				} else if (mode == ZOOM) {
					float newDist = spacing(event);
					float newRotation = rotation(event) - oldRotation;
					if (newDist > 10f) {
						float scale = newDist / oldDist;
						matrix.set(savedMatrix);
						// 第一个参数是x轴的放大倍数，第三个参数是x轴的中点
						matrix.postScale(scale, scale, mid.x, mid.y);
						matrix.postRotate(newRotation, mid.x, mid.y);

					}
				}
			}

			break;
		}
		if (selectImageCount != -1) {
			float[] f = new float[9];
			matrix.getValues(f);
			Rect rect = list_V.get(selectImageCount).getImgV().getDrawable()
					.getBounds();
			int bWidth = rect.width();
			int bHeight = rect.height();
			// 原图左上角
			float x1 = f[2];
			float y1 = f[5];
			// 原图右上角
			float x2 = f[0] * bWidth + f[2];
			float y2 = f[3] * bWidth + +f[5];
			// 原图左下角
			float x3 = f[1] * bHeight + f[2];
			float y3 = f[4] * bHeight + f[5];
			// 原图右下角
			float x4 = f[0] * bWidth + f[1] * bHeight + f[2];
			float y4 = f[3] * bWidth + f[4] * bHeight + f[5];
			// 最左边x
			float minX = 0;
			// 最右边x
			float maxX = 0;
			// 最上边y
			float minY = 0;
			// 最下边y
			float maxY = 0;

			minX = Math.min(x4, Math.min(x3, Math.min(x1, x2)));
			maxX = Math.max(x4, Math.max(x3, Math.max(x1, x2)));
			minY = Math.min(y4, Math.min(y3, Math.min(y1, y2)));
			maxY = Math.max(y4, Math.max(y3, Math.max(y1, y2)));

			list_V.get(selectImageCount).getState().setLeft(minX);
			list_V.get(selectImageCount).getState().setTop(minY);
			list_V.get(selectImageCount).getState().setRight(maxX);
			list_V.get(selectImageCount).getState().setBottom(maxY);
			// 边界检测
			if (minX >= (-bWidth) && maxX <= (width + bWidth)
					&& minY >= (-bHeight) && maxY <= (height + bHeight)) {
				// 放大缩小检测
				// if (3 * (maxX - minX) >= bWidth && (maxX - minX) <= width &&
				// 3 * (maxY - minY) >= bHeight
				// && (maxY - minY) <= height)
				list_V.get(selectImageCount).getImgV().setImageMatrix(matrix);
			}
		}
		return false;
	}

	public void selectImG(MotionEvent event) {
		float x = event.getRawX();
		float y = event.getRawY();
		for (int i = (list_V.size() - 1); i >= 0; i--) {
			Holder holder = list_V.get(i);
			Rect rect = new Rect((int) holder.getState().getLeft(),
					(int) holder.getState().getTop(), (int) holder.getState()
							.getRight(), (int) holder.getState().getBottom());
			if (rect.contains((int) x, ((int) y) - 120)) {
				list_V.get(i).getImgV().bringToFront();
				selectImageCount = i;
				mFrameLayoutAddView.invalidate();
				break;
			}
			selectImageCount = -1;
		}
	}

	// 得到两个点的距离
	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	// 得到两个点的中点
	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}

	// 旋转
	private float rotation(MotionEvent event) {
		double delta_x = (event.getX(0) - event.getX(1));
		double delta_y = (event.getY(0) - event.getY(1));
		double radians = Math.atan2(delta_y, delta_x);
		return (float) Math.toDegrees(radians);
	}

	class VoidHoler {

		ImageView image;
		RelativeLayout layout;
	}
	
	//选择指向
	@SuppressLint("NewApi")
	public void setSelect(int resid,int index){
		
		int w = (Settings.DISPLAY_WIDTH-DensityUtils.dp2px(CombinationActivity.this, 3))/4;
		int h = Settings.DISPLAY_HEIGHT - DensityUtils.dp2px(CombinationActivity.this, 87);
		RelativeLayout.LayoutParams pl = new RelativeLayout.LayoutParams(w, h);
		
		if(mSetectId ==resid ){
			mSetectId = -1;
			mListView.setVisibility(View.GONE);
			return;
		}else{
			mSetectId =resid;
			mListView.setVisibility(View.VISIBLE);
		}
		
		for(int i = 0; i < mListBt.size(); i++){
			if(mListBt.get(i).getId() == mSetectId){
				mListBt.get(i).setSelected(true);
			}else{
				mListBt.get(i).setSelected(false);
			}
		}
		if(resid == R.id.bt1){
			mState = 1;
			pl.leftMargin = 0;
			pl.topMargin = DensityUtils.dp2px(CombinationActivity.this, 87);
			mListView.setLayoutParams(pl);
			getCategory();
		}
		else if(resid == R.id.bt2){
			mState = 2;
			pl.topMargin = DensityUtils.dp2px(CombinationActivity.this, 87);
			pl.leftMargin = w+DensityUtils.dp2px(CombinationActivity.this, 1);
			mListView.setLayoutParams(pl);
			getSeason();
		}
		else if(resid == R.id.bt3){
			mState = 3;
			pl.topMargin = DensityUtils.dp2px(CombinationActivity.this, 87);
			pl.leftMargin = w*2+DensityUtils.dp2px(CombinationActivity.this, 2);
			mListView.setLayoutParams(pl);
			getStyle();
		}else{
			mState = 4;
			mListView.setVisibility(View.GONE);
			new HostTask().execute();
		}
		mSideAdapter.notifyDataSetChanged();
		
	}
	@Override
	protected void onResume() {
		super.onResume();
		mFrameLayoutAddView.removeAllViewsInLayout();
		mFrameLayoutAddView.invalidate();
	}
	//类目数据
	public void getCategory(){
		
		combinationList.clear();
		Combination cb = null ;
		
		cb = null;
		cb = new Combination();
		cb.setId("101");
		cb.setName("上衣");
		combinationList.add(cb);
		
		cb = null;
		cb = new Combination();
		cb.setId("102");
		cb.setName("下装");
		combinationList.add(cb);
		
		cb = null;
		cb = new Combination();
		cb.setId("103");
		cb.setName("打底");
		combinationList.add(cb);
		
		cb = null;
		cb = new Combination();
		cb.setId("104");
		cb.setName("连衣裙");
		combinationList.add(cb);
		
		cb = null;
		cb = new Combination();
		cb.setId("105");
		cb.setName("女鞋");
		combinationList.add(cb);
		
		cb = null;
		cb = new Combination();
		cb.setId("106");
		cb.setName("配饰");
		combinationList.add(cb);
		
		cb = null;
		cb = new Combination();
		cb.setId("107");
		cb.setName("其他");
		combinationList.add(cb);
		
	}
	
	//类目数据
	public void getSeason(){
		
		combinationList.clear();
		Combination cb = null ;
		
		cb = null;
		cb = new Combination();
		cb.setId("501");
		cb.setName("春季");
		combinationList.add(cb);
		
		cb = null;
		cb = new Combination();
		cb.setId("502");
		cb.setName("夏季");
		combinationList.add(cb);
		
		cb = null;
		cb = new Combination();
		cb.setId("503");
		cb.setName("秋季");
		combinationList.add(cb);
		
		cb = null;
		cb = new Combination();
		cb.setId("504");
		cb.setName("冬季");
		combinationList.add(cb);
		
	}
	//风格
	public void getStyle(){
		combinationList.clear();
		Combination cb = null ;
		
		cb = null;
		cb = new Combination();
		cb.setId("301");
		cb.setName("欧美");
		combinationList.add(cb);
		
		cb = null;
		cb = new Combination();
		cb.setId("302");
		cb.setName("韩范");
		combinationList.add(cb);
		
		cb = null;
		cb = new Combination();
		cb.setId("303");
		cb.setName("甜美");
		combinationList.add(cb);
		
		cb = null;
		cb = new Combination();
		cb.setId("304");
		cb.setName("原创");
		combinationList.add(cb);
		
		
		cb = null;
		cb = new Combination();
		cb.setId("305");
		cb.setName("日系");
		combinationList.add(cb);
		
	}

	public class SideAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return combinationList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return combinationList.get(arg0);
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
				arg1 = LayoutInflater.from(CombinationActivity.this).inflate(R.layout.commodity_item, arg2, false);
				holerView.table = (TextView) arg1.findViewById(R.id.table);
				arg1.setTag(holerView);
			}else{
				holerView = (HolerView) arg1.getTag();
			}
			RelativeLayout.LayoutParams param = (RelativeLayout.LayoutParams) holerView.table.getLayoutParams();
			param.height =  mListView.getHeight()/9;
			param.width =  (Settings.DISPLAY_WIDTH-DensityUtils.dp2px(CombinationActivity.this, 3))/4;
			holerView.table.setLayoutParams(param);
			holerView.table.setText(combinationList.get(arg0).getName());
			return arg1;
		}
		class HolerView{
			TextView table;
		}
	}
	
	
	public class HostViewAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return clothingList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return clothingList.get(arg0);
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
				arg1 = LayoutInflater.from(CombinationActivity.this).inflate(R.layout.picture_item, arg2, false);
				holerView.img = (ImageView) arg1.findViewById(R.id.image);
				arg1.setTag(holerView);
			}else{
				holerView = (HolerView) arg1.getTag();
			}
			
			int w = (Settings.DISPLAY_WIDTH - DensityUtils.dp2px(CombinationActivity.this, 90)) / 5;
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(w, w*3/2);
			lp.setMargins(DensityUtils.dp2px(CombinationActivity.this, 8),
					DensityUtils.dp2px(CombinationActivity.this, 8),
					DensityUtils.dp2px(CombinationActivity.this, 8),
					DensityUtils.dp2px(CombinationActivity.this, 8));
			holerView.img.setLayoutParams(lp);
			
			holerView.img.setImageBitmap(BitmapFactory.decodeStream(getResources().openRawResource(R.drawable.pohoto_bg)));
			if(clothingList.get(arg0).getThumbnailPath() != null && clothingList.get(arg0).getThumbnailPath().length() > 0){
			mCacheImageLoader.loadImage(clothingList.get(arg0).getThumbnailPath(), holerView.img, new OnLoadListener() {
				
				@Override
				public void onLoad(Bitmap bitmap, View targetView) {
					if(bitmap != null){
						((ImageView) targetView).setImageBitmap(bitmap); 
					}
				}
			});}
			
			return arg1;
		}
		class HolerView{
			ImageView img;
		}
	}
	
	private class HostTask extends AsyncTask<String, Void, ClothingForMatchResult> {
		

		@Override
		protected ClothingForMatchResult doInBackground(String... arg0) {

			JSONAccessor accessor = new JSONAccessor(CombinationActivity.this,HttpAccessor.METHOD_GET);
			accessor.enableJsonLog(true);
			ClothingForMatchParam param = new ClothingForMatchParam();
			if(mState != 4){
			param.setUserId(AppContents.getUserInfo().getId());
			param.setCategoryId(categoryId);
			param.setSeasonId(seasonId);
			param.setStyleId(styleId);
			}
			return accessor.execute(Settings.ADDDAPEI, param, ClothingForMatchResult.class);
		}

		@Override
		protected void onPostExecute(ClothingForMatchResult result) {
			if (result != null) {
				clothingList.clear();
				clothingList.addAll(result.getClothingList());
				mHostViewAdapter.notifyDataSetChanged();
				mHostListView.post(new Runnable() {

	                @Override
	                public void run() {
	                	if(clothingList.size() >0)
	    				mHostListView.setSelection(0);
	                }
	            });
				
			} else {
				Toast.makeText(CombinationActivity.this,getString(R.string.net_error), Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(result);
		}

	}
	
	
}
