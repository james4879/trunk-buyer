package com.datapush.buyhand.activity;

import java.util.ArrayList;
import java.util.List;

import com.daoshun.lib.util.DensityUtils;
import com.daoshun.lib.view.OnSingleClickListener;
import com.datapush.buyhand.R;
import com.datapush.buyhand.common.Constants;
import com.datapush.buyhand.view.ScrollLayout;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout.LayoutParams;

public class CollocationActivity extends BaseActivity{
	
	private ScrollLayout mScrollLayout;
	/**类目**/
	public static ChoiceCollActivity isConText;
	private ImageView mBackIv;//返回按钮
	private ListView mCommodityListView;//商品
	private GridView mClassificationGridView;//分类
	private List<String> mCommodityListData = new ArrayList<String>();
	private CommodityAdapter mCommodityAdapter;
	private ClassificationAdapter mClassificationAdapter;
	private int oldListIndex = 0; //商品选择标识
	private List<Boolean> mCommodityListFlg = new ArrayList<Boolean>();
	/**商品**/
	private ImageView mBack;
	private GridView mProductsGridView;
	private ProductsAdapter mProductsAdapter;
	private int [] ImageList = new int[]{
			R.drawable.a1,R.drawable.a2,R.drawable.a3,
			R.drawable.a4,R.drawable.a5,R.drawable.a6};
	
	/**搭配**/
	private  ImageView mCombinationSearch;
	private ImageView mCombinationBack;
	private FrameLayout mFrameLayoutAddView;
	private int widthPixels,heightPixels;
	
	/***图片处理*/
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
		setContentView(R.layout.collocations_layout);
		getCommoditydata();
		findView();
		initView();
	}
	
	public void findView(){
//		ChiocieFindView();
//		ProductsFindView();
		CombinationFindView();
		
	}
	public void initView(){
//		ChocieInitView();
//		productsInitView();
		CombinationInitView();
	}
	/**类目**/
//	public void ChiocieFindView(){
//		mBackIv = (ImageView) findViewById(R.id.choice_back);
//		mCommodityListView = (ListView) findViewById(R.id.list);
//		mClassificationGridView = (GridView) findViewById(R.id.gridview);
//		mScrollLayout = (ScrollLayout) findViewById(R.id.main_scrolllayout);
//	}
//	public void ChocieInitView(){
//			mBackIv.setOnClickListener( new OnSingleClickListener() {
//				
//				@Override
//				public void doOnClick(View v) {
//					if(Constants.ISFIRST ==0){
//						finish();
//						overridePendingTransition(R.anim.in_form_left, R.anim.out_from_left);
//					}else{
//					mScrollLayout.snapToScreen(1);
//					}
//				}
//			});
//			
//			mCommodityAdapter = new CommodityAdapter();
//			mCommodityListView.setAdapter(mCommodityAdapter);
//			mCommodityListFlg.set(0, true);
//			mCommodityListView.setOnItemClickListener(new OnItemClickListener() {
//
//				@Override
//				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//						long arg3) {
//						if(arg2 == oldListIndex){
//							return;
//						}else{
//							mCommodityListFlg.set(arg2, true);
//							mCommodityListFlg.set(oldListIndex, false);
//							oldListIndex = arg2;
//							mCommodityAdapter.notifyDataSetChanged();
//							mCommodityListView.setSelection(arg2);
//						}
//				}
//			});
//			
//			
//			mClassificationAdapter = new ClassificationAdapter();
//			mClassificationGridView.setAdapter(mClassificationAdapter);
//			
//			mClassificationGridView.setOnItemClickListener(new OnItemClickListener() {
//
//				@Override
//				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//						long arg3) {
//					Constants.ISFIRST =1;
//					mScrollLayout.snapToScreen(2);
//				}
//			});
//	}
//	/**商品**/
//	public void ProductsFindView(){
//		mBack = (ImageView) findViewById(R.id.pouduts_back);
//		mProductsGridView = (GridView) findViewById(R.id.produts_gridview);
//	}
//	public void productsInitView(){
//		
//	mBack.setOnClickListener(new OnSingleClickListener() {
//			
//			@Override
//			public void doOnClick(View v) {
//				mScrollLayout.snapToScreen(0);
//			}
//		});
//		mProductsAdapter = new ProductsAdapter();
//		mProductsGridView.setAdapter(mProductsAdapter);
//		mProductsGridView.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) {
//
//				mScrollLayout.snapToScreen(1);
//				setImgView(ImageList[arg2]);
//			}
//		});
//	}
	/**搭配**/
	public void CombinationFindView(){
		list_V = new ArrayList<Holder>();
//		mCombinationSearch = (ImageView) findViewById(R.id.combination_search);
		mFrameLayoutAddView = (FrameLayout) findViewById(R.id.addview);
		mCombinationBack = (ImageView) findViewById(R.id.combination_back);
	}
	public void CombinationInitView(){
		
	mCombinationBack.setOnClickListener(new OnSingleClickListener() {
			
			@Override
			public void doOnClick(View v) {
				Constants.ISFIRST =0;
				finish();
				overridePendingTransition(R.anim.in_form_left, R.anim.out_from_left);
			}
		});
		mCombinationSearch.setOnClickListener(new OnSingleClickListener() {
			
			@Override
			public void doOnClick(View v) {
				mScrollLayout.snapToScreen(0);
			}
		});
	}
	
	@SuppressLint("ResourceAsColor")
	public class CommodityAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return mCommodityListData.size();
		}

		@Override
		public Object getItem(int arg0) {
			return mCommodityListData.get(arg0);
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
				arg1 = LayoutInflater.from(CollocationActivity.this).inflate(R.layout.commodity_item, arg2, false);
				holerView.table = (TextView) arg1.findViewById(R.id.table);
				arg1.setTag(holerView);
			}else{
				holerView = (HolerView) arg1.getTag();
			}
			RelativeLayout.LayoutParams param = (RelativeLayout.LayoutParams) holerView.table.getLayoutParams();
			param.height =  mCommodityListView.getHeight()/10;
			holerView.table.setLayoutParams(param);
			holerView.table.setText(mCommodityListData.get(arg0));
			if(mCommodityListFlg.get(arg0)){
				holerView.table.setBackgroundResource(R.color.white);
			}else{
				holerView.table.setBackgroundResource(R.color.item_of);
			}
			return arg1;
		}
		class HolerView{
			TextView table;
		}
	}
	
	public class ClassificationAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return mCommodityListData.size();
		}

		@Override
		public Object getItem(int arg0) {
			return mCommodityListData.get(arg0);
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
				arg1 = LayoutInflater.from(CollocationActivity.this).inflate(R.layout.classification_item, arg2, false);
				holerView.table = (TextView) arg1.findViewById(R.id.table);
				holerView.img = (ImageView) arg1.findViewById(R.id.img);
				arg1.setTag(holerView);
			}else{
				holerView = (HolerView) arg1.getTag();
			}
			holerView.table.setText(mCommodityListData.get(arg0));
			LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) holerView.img.getLayoutParams();
			param.width = (mClassificationGridView.getWidth()-DensityUtils.dp2px(CollocationActivity.this, 16))/3;
			param.height =  (mClassificationGridView.getWidth()-DensityUtils.dp2px(CollocationActivity.this, 16))/3;
			holerView.img.setLayoutParams(param);

			return arg1;
		}
		class HolerView{
			TextView table;
			ImageView img;
		}
	}
	public void getCommoditydata(){
		mCommodityListData.add("上装");
		mCommodityListFlg.add(true);
		mCommodityListData.add("裤装");
		mCommodityListFlg.add(false);
		mCommodityListData.add("裙装");
		mCommodityListFlg.add(false);
		mCommodityListData.add("包包");
		mCommodityListFlg.add(false);
		mCommodityListData.add("鞋子");
		mCommodityListFlg.add(false);
		mCommodityListData.add("配饰");
		mCommodityListFlg.add(false);
		mCommodityListData.add("内衣");
		mCommodityListFlg.add(false);
		mCommodityListData.add("美妆");
		mCommodityListFlg.add(false);
		mCommodityListData.add("男装");
		mCommodityListFlg.add(false);
		mCommodityListData.add("元素");
		mCommodityListFlg.add(false);
		
	}
	
	public class ProductsAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return ImageList.length;
		}

		@Override
		public Object getItem(int arg0) {
			return ImageList[arg0];
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
				arg1 = LayoutInflater.from(CollocationActivity.this).inflate(R.layout.products_item, arg2, false);
				holerView.img = (ImageView) arg1.findViewById(R.id.img);
				arg1.setTag(holerView);
			}else{
				holerView = (HolerView) arg1.getTag();
			}
			LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) holerView.img.getLayoutParams();
			param.width = (mProductsGridView.getWidth()-DensityUtils.dp2px(CollocationActivity.this, 16))/3;
			param.height =  (mProductsGridView.getWidth()-DensityUtils.dp2px(CollocationActivity.this, 16))/2;
			holerView.img.setLayoutParams(param);
			holerView.img.setBackgroundResource(ImageList[arg0]);
			return arg1;
		}
		class HolerView{
			ImageView img;
		}
	}
//	/**
//	 * 添加配件
//	 */
//	public void setView(int id){
//		final TouchView tl = new TouchView(CollocationActivity.this,widthPixels,heightPixels);
//		tl.setImageBitmap(BitmapFactory.decodeStream(getResources().openRawResource(id)));
//		tl.setFocusable(true);
//		tl.setOnClickListener(new OnSingleClickListener() {
//			
//			@Override
//			public void doOnClick(View v) {
//				tl.bringToFront();
//				mFrameLayoutAddView.invalidate();
//			}
//		});
//		FrameLayout.LayoutParams pl = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
//		mFrameLayoutAddView.addView(tl,pl);
//	}
	
/**
 * 添加桌面图片
 */
	public void setImgView(int resid){
//		  	height = getWindowManager().getDefaultDisplay().getHeight()-DensityUtils.dp2px(CollocationActivity.this, 48);
		  	height = getWindowManager().getDefaultDisplay().getHeight();
		  	width = getWindowManager().getDefaultDisplay().getWidth();
		      ImageView imageView = new ImageView(this);
		      imageView.setLayoutParams(new LayoutParams(width, height));
		      imageView.setScaleType(ScaleType.MATRIX);
		      imageView.setImageResource(resid);
		      mFrameLayoutAddView.addView(imageView);
		      Matrix imageMatrix = new Matrix(imageView.getImageMatrix());
		      imageMatrix.postTranslate(width / 4, height / 4);
		      imageView.setImageMatrix(imageMatrix);
		      Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resid);
		      bitmapWidth = bitmap.getWidth();
		      bitmapHeight = bitmap.getHeight();
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
	          matrix.set(list_V.get(selectImageCount).getImgV().getImageMatrix());
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
	            matrix.postTranslate(event.getX() - start.x, event.getY() - start.y);
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
	      Rect rect = list_V.get(selectImageCount).getImgV().getDrawable().getBounds();
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
	      if (minX >= (-bWidth) && maxX <= (width + bWidth) && minY >= (-bHeight)
	          && maxY <= (height + bHeight)) {
	        // 放大缩小检测
	        // if (3 * (maxX - minX) >= bWidth && (maxX - minX) <= width && 3 * (maxY - minY) >= bHeight
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
	      Rect rect =
	          new Rect((int) holder.getState().getLeft(), (int) holder.getState().getTop(),
	              (int) holder.getState().getRight(), (int) holder.getState().getBottom());
	      if (rect.contains((int) x, ((int) y)-120)) {
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
}
