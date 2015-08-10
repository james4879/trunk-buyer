package com.datapush.buyhand.activity;



import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.daoshun.lib.view.OnSingleClickListener;
import com.datapush.buyhand.R;
import com.datapush.buyhand.common.Settings;
import com.datapush.buyhand.view.MyGallery;
import com.datapush.buyhand.view.ScrollLayout;
public class MainActivity extends BaseActivity{
	
	private int [] ImageList = new int[]{R.drawable.img1,R.drawable.img2,R.drawable.img1};
	
	
	private ScrollLayout mScrollLayout;//底部栏父控件
	private RadioButton[] mButtons;//底部单选按钮数组
	private int mViewCount;//父控件包含子控件个数
	private int mCurSel;//当前底部选项焦点
	
	/**T台界面定义**/
	private RelativeLayout mTplatformFrame;
	private Gallery mTplatformGallery;
	private TplatformAdapter mTplatformAdapter;
	/**选款界面定义**/ 
	private ImageView mSelectionSearchIv;//筛选
	/***搭配界面定义*/
	private Button mCollcationDiscussion;//讨论区
	private Button mCollcationShare;//分享
	private Button mCollcationLatest;//最先
	private Button mCollcationHottest;//最热
	private Button mCollcationAdd;//搭配
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		initPageScroll();
		findView();
		initView();
	}
	
	/**
	 * 初始化水平滚动翻页
	 */
	private void initPageScroll() {
		mScrollLayout = (ScrollLayout) findViewById(R.id.main_scrolllayout);
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.main_linearlayout_footer);
		mViewCount = mScrollLayout.getChildCount();
		mButtons = new RadioButton[mViewCount];

		for (int i = 0; i < mViewCount; i++) {
			mButtons[i] = (RadioButton) linearLayout.getChildAt(i * 2);
			mButtons[i].setTag(i);
			mButtons[i].setChecked(false);
			mButtons[i].setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					int pos = (Integer) (v.getTag());
					mScrollLayout.snapToScreen(pos);
				}
			});
		}
		// 设置第一显示屏
		mCurSel = 0;
		mButtons[mCurSel].setChecked(true);
		mScrollLayout
				.SetOnViewChangeListener(new ScrollLayout.OnViewChangeListener() {
					public void OnViewChange(int viewIndex) {
						setCurPoint(viewIndex);
					}
				});
	}
	
	/**
	 * 设置底部栏当前焦点
	 * 
	 * @param index
	 */
	private void setCurPoint(int index) {
		if (index < 0 || index > mViewCount - 1 || mCurSel == index)
			return;
		mButtons[mCurSel].setChecked(false);
		mButtons[index].setChecked(true);
		mCurSel = index;
	}

	/**
	 * 初始化所有页面 
	 **/
	public void findView(){
		TplatformFindView();
		SelectionFindView();
		CollocationFindView();
	}
	/**
	 * 所有页面的事件监听 
	 **/
	public void initView(){
		TplatformInitView();
		SelectionInitView();
		CollocationInitView();
	}
	/**
	 * T台界面初始化
	 */
	public void TplatformFindView(){
//		mTplatformFrame = (RelativeLayout) findViewById(R.id.frame);
	}
	
	/**
	 * T台界面初始化
	 */
	public void TplatformInitView(){
		LinearLayout.LayoutParams pl = (LinearLayout.LayoutParams) mTplatformFrame.getLayoutParams();
		pl.width = Settings.DISPLAY_WIDTH;
		pl.height = Settings.DISPLAY_WIDTH;
		mTplatformFrame.setLayoutParams(pl);
		mTplatformAdapter = new TplatformAdapter();
		mTplatformGallery.setAdapter(mTplatformAdapter);
	}
	
	/**
	 * 选款界面初始化
	 */
	public void SelectionFindView(){
//		mSelectionSearchIv = (ImageView) findViewById(R.id.search);
	}
	/**
	 * 选款界面事件监听
	 */
	public void SelectionInitView(){
		
		mSelectionSearchIv.setOnClickListener(new OnSingleClickListener() {
			
			@Override
			public void doOnClick(View v) {
				Intent intent = new Intent(MainActivity.this,ChoiceActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.in_from_right, R.anim.in_form_left);
			}
		});
	}
	/**
	 * 搭配界面初始化
	 */
	public void CollocationFindView(){
//		mCollcationAdd = (Button) findViewById(R.id.add);
	}
	/**
	 * 搭配界面事件监听
	 */
	public void CollocationInitView(){
		mCollcationAdd.setOnClickListener(new OnSingleClickListener() {
			
			@Override
			public void doOnClick(View v) {
				Intent intent = new Intent(MainActivity.this,CollocationActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.in_from_right, R.anim.in_form_left);
			}
		});
	}
	
	public class TplatformAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return ImageList.length;
		}

		@Override
		public Object getItem(int arg0) {
			return arg0;
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@SuppressWarnings("deprecation")
		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			HolerView holerView = null;
			if(arg1 == null){
				holerView = new HolerView();
				arg1 = LayoutInflater.from(MainActivity.this).inflate(R.layout.tplatform_item, arg2, false);
				holerView.img = (ImageView) arg1.findViewById(R.id.img);
//				holerView.frame = (RelativeLayout) arg1.findViewById(R.id.frame);
				arg1.setTag(holerView);
			}else{
				holerView = (HolerView) arg1.getTag();
			}
			Log.e("第", arg0+"");
			holerView.frame.setLayoutParams(new Gallery.LayoutParams(
					mTplatformGallery.getWidth(), mTplatformGallery.getHeight()));
			holerView.img.setBackgroundResource(ImageList[arg0]);
			return arg1;
		}
		class HolerView{
			ImageView img;
			RelativeLayout frame;
		}
	}

}


