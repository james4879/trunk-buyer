package com.datapush.buyhand.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daoshun.lib.util.DensityUtils;
import com.daoshun.lib.view.OnSingleClickListener;
import com.datapush.buyhand.R;
/**
 * 宝贝选择类
 * 
 * @author yanpf
 *
 */
public class ChoiceActivity extends BaseActivity{
	private ImageView mBackIv;//返回按钮
	private LinearLayout mPriceSectionView;//时间区间
	private ListView mCommodityListView;//商品
	private GridView mClassificationGridView;//分类
	private List<String> mCommodityListData = new ArrayList<String>();
	private CommodityAdapter mCommodityAdapter;
	private ClassificationAdapter mClassificationAdapter;
	private int oldIndex = 0;//价格选择标识
	private int oldListIndex = 0; //商品选择标识
	private String[] str = new String[]{"0-49","49-99","99-299","299-499","500以上"};
	private List<Boolean> mCommodityListFlg = new ArrayList<Boolean>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choice_layout);
		getCommoditydata();
		findView();
		initView();
	}
/**
 * 初始化控件
 */
	public void findView(){
		mBackIv = (ImageView) findViewById(R.id.back);
		mPriceSectionView = (LinearLayout) findViewById(R.id.price_section);
		mCommodityListView = (ListView) findViewById(R.id.list);
		mClassificationGridView = (GridView) findViewById(R.id.gridview);
	}
	/**
	 * 事件监听
	 */
	@SuppressLint("ResourceAsColor")
	public void initView(){
		mBackIv.setOnClickListener( new OnSingleClickListener() {
			
			@Override
			public void doOnClick(View v) {
				finish();
				overridePendingTransition(R.anim.in_form_left, R.anim.out_from_left);
			}
		});
		
		for(int i = 0; i<str.length; i++){
			final int index = i;
			TextView tv = new TextView(this);
			int childWidth = -1, childHeight = -1,space = 10;
			childWidth = (mPriceSectionView.getWidth()-space*3)/4;
			childHeight = childWidth/2;
			LayoutParams flp = new LayoutParams(childWidth, childHeight);
			flp.leftMargin = space;
			flp.gravity = Gravity.CENTER;
			tv.setGravity(Gravity.CENTER);
			tv.setTextColor(R.color.item_cd);
			if(i==0){
				tv.setBackgroundResource(R.drawable.button_bg_n);
			}else{
			tv.setBackgroundResource(R.drawable.button_bg);
			}
			tv.setText(str[i]);
			tv.setOnClickListener(new OnSingleClickListener() {
				
				@Override
				public void doOnClick(View v) {
					mPriceSectionView.getChildAt(index).setBackgroundResource(R.drawable.button_bg_n);
					mPriceSectionView.getChildAt(oldIndex).setBackgroundResource(R.drawable.button_bg);
					oldIndex = index;
					
				}
			});
			mPriceSectionView.addView(tv, flp);
		}
		
		mCommodityAdapter = new CommodityAdapter();
		mCommodityListView.setAdapter(mCommodityAdapter);
		mCommodityListFlg.set(0, true);
		mCommodityListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
					if(arg2 == oldListIndex){
						return;
					}else{
						mCommodityListFlg.set(arg2, true);
						mCommodityListFlg.set(oldListIndex, false);
						oldListIndex = arg2;
						mCommodityAdapter.notifyDataSetChanged();
						mCommodityListView.setSelection(arg2);
					}
			}
		});
		
		
		mClassificationAdapter = new ClassificationAdapter();
		mClassificationGridView.setAdapter(mClassificationAdapter);
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
				arg1 = LayoutInflater.from(ChoiceActivity.this).inflate(R.layout.commodity_item, arg2, false);
				holerView.table = (TextView) arg1.findViewById(R.id.table);
				arg1.setTag(holerView);
			}else{
				holerView = (HolerView) arg1.getTag();
			}
			RelativeLayout.LayoutParams param = (RelativeLayout.LayoutParams) holerView.table.getLayoutParams();
			param.height =  mCommodityListView.getHeight()/9;
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
				arg1 = LayoutInflater.from(ChoiceActivity.this).inflate(R.layout.classification_item, arg2, false);
				holerView.table = (TextView) arg1.findViewById(R.id.table);
				holerView.img = (ImageView) arg1.findViewById(R.id.img);
				arg1.setTag(holerView);
			}else{
				holerView = (HolerView) arg1.getTag();
			}
			holerView.table.setText(mCommodityListData.get(arg0));
			LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) holerView.img.getLayoutParams();
			param.width = (mClassificationGridView.getWidth()-DensityUtils.dp2px(ChoiceActivity.this, 16))/3;
			param.height =  (mClassificationGridView.getWidth()-DensityUtils.dp2px(ChoiceActivity.this, 16))/3;
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
	
}
