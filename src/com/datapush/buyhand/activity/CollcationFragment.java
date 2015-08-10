package com.datapush.buyhand.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.daoshun.lib.util.DensityUtils;
import com.daoshun.lib.view.OnSingleClickListener;
import com.datapush.buyhand.R;
import com.datapush.buyhand.activity.CombinationActivity.VoidHoler;
import com.datapush.buyhand.common.Settings;

/**
 * 搭配
 * 
 * @author yanpf
 *
 */
public class CollcationFragment extends BaseFragment{
	
	private MenuActivity mMenuActivity;
	private int[] ImageList = new int[] {R.drawable.a1,
			R.drawable.a2, R.drawable.a3, R.drawable.a4, R.drawable.a5,
			R.drawable.a6 };
	private ViewGroup mFrameGroup;
	private ImageView mAdd;

	
	@Override
	public void onAttach(Activity activity) {
		mMenuActivity = (MenuActivity) getActivity();
		super.onAttach(activity);
	}

	@Override
	public View onInitView(LayoutInflater inflater, ViewGroup container) {
		View view = inflater.inflate(R.layout.collocation_layout, container, false);
		mFrameGroup = (ViewGroup) view.findViewById(R.id.three_ll);
		mAdd = (ImageView) view.findViewById(R.id.add);
		initView();
		return view;
	}

	public void initView(){
		
		mAdd.setOnClickListener(new OnSingleClickListener() {
			
			@Override
			public void doOnClick(View v) {
				Intent intent = new Intent(mMenuActivity,CombinationActivity.class);
				startActivity(intent);
				getActivity().overridePendingTransition(0, 0);
			}
		});
		
		
		VoidHoler holer = null;
		View view = null;
		int w = (Settings.DISPLAY_WIDTH - DensityUtils.dp2px(mMenuActivity, 90)) / 5;
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(w, w*3/2);
		lp.setMargins(DensityUtils.dp2px(mMenuActivity, 8),
				DensityUtils.dp2px(mMenuActivity, 8),
				DensityUtils.dp2px(mMenuActivity, 8),
				DensityUtils.dp2px(mMenuActivity, 8));
		for (int i = 0; i < ImageList.length; i++) {
			final int index = i;
			holer = new VoidHoler();
			view = getLayoutInflater().inflate(R.layout.picture_item, null,false);
			holer.image = (ImageView) view.findViewById(R.id.image);
			holer.image.setBackgroundResource(ImageList[i]);
			holer.image.setOnClickListener(new OnSingleClickListener() {
				
				@Override
				public void doOnClick(View v) {
					Intent intent = new Intent(mMenuActivity,CombinationActivity.class);
					intent.putExtra("index", index+1);
					startActivity(intent);
					getActivity().overridePendingTransition(0, 0);
				}
			});
			view.setLayoutParams(lp);
			mFrameGroup.addView(view);
		}
	}
	
	class VoidHoler {

		ImageView image;
		RelativeLayout layout;
	}
}
