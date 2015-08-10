package com.datapush.buyhand.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.daoshun.lib.util.DensityUtils;
import com.daoshun.lib.view.OnSingleClickListener;
import com.datapush.buyhand.R;
import com.datapush.buyhand.common.Constants;

public class ProductsActivity extends BaseActivity{
	private ImageView mBack;
	private GridView mProductsGridView;
	private ProductsAdapter mProductsAdapter;
	private int [] ImageList = new int[]{
			R.drawable.a1,
			R.drawable.a2,
			R.drawable.a3,
			R.drawable.a4,
			R.drawable.a5,
			R.drawable.a6};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.products_layout);
		findView();
		initView();
	}
	
	public void findView(){
		mBack = (ImageView) findViewById(R.id.back);
		mProductsGridView = (GridView) findViewById(R.id.gridview);
	}

	public void initView(){
		mBack.setOnClickListener(new OnSingleClickListener() {
			
			@Override
			public void doOnClick(View v) {
					finish();
					overridePendingTransition(R.anim.in_form_left, R.anim.out_from_left);
			}
		});
		mProductsAdapter = new ProductsAdapter();
		mProductsGridView.setAdapter(mProductsAdapter);
		mProductsGridView.setOnItemClickListener(new OnItemClickListener() {

			@SuppressWarnings("unused")
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
//				ChoiceCollActivity.isConText.finish();
				if (Constants.ISFIRST == 0) {
					Constants.ISFIRST = 1;
					ChoiceCollActivity.isConText.finish();
					finish();
					Intent intent = new Intent(ProductsActivity.this,CombinationActivity.class);
					intent.putExtra("id", arg2);
					startActivity(intent);
					overridePendingTransition(0, 0);
				} else {
					ChoiceCollActivity.isConText.finish();
					finish();
					overridePendingTransition(0, 0);
				}
			}
		});
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
				arg1 = LayoutInflater.from(ProductsActivity.this).inflate(R.layout.products_item, arg2, false);
				holerView.img = (ImageView) arg1.findViewById(R.id.img);
				arg1.setTag(holerView);
			}else{
				holerView = (HolerView) arg1.getTag();
			}
			LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) holerView.img.getLayoutParams();
			param.width = (mProductsGridView.getWidth()-DensityUtils.dp2px(ProductsActivity.this, 16))/3;
			param.height =  (mProductsGridView.getWidth()-DensityUtils.dp2px(ProductsActivity.this, 16))/2;
			holerView.img.setLayoutParams(param);
			holerView.img.setBackgroundResource(ImageList[arg0]);
			return arg1;
		}
		class HolerView{
			ImageView img;
		}
	}
}
