package com.datapush.buyhand.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.daoshun.lib.view.OnSingleClickListener;
import com.datapush.buyhand.R;
/**
 * 商品二级类
 * 
 * @author yanpf
 *
 */
public class ChoiceItemActivity extends BaseActivity{
	
	private Button mBack;//返回键
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choice_item_layout);
		findView();
		initView();
	}
	
	public void findView(){
		mBack = (Button) findViewById(R.id.back);
	}
	public void initView(){
		mBack.setOnClickListener(new OnSingleClickListener() {
			
			@Override
			public void doOnClick(View v) {
				finish();
				overridePendingTransition(R.anim.in_form_left, R.anim.out_from_left);
			}
		});
	}

}
