package com.datapush.buyhand.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.daoshun.lib.view.OnSingleClickListener;
import com.datapush.buyhand.BuyHandAppApplication;
import com.datapush.buyhand.R;
import com.datapush.buyhand.common.AppContents;

public class MenuActivity extends BaseActivity {
	
	private FragmentManager mFragmentManager;
	private LinearLayout mTtaiLayout , mXuanbanLayout , mDapeiLayout , mMineLayout ;
	private ImageView mTtaiImage , mXuanbanImage , mDapeiImage , mMineImage ;
	
	private int mSelectedModule;
	
	private long mExitTime;
	
	private String mCurrentfragmentTag = "";
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu1_layout);
		
		mFragmentManager = getSupportFragmentManager();
		
		findViews();
		
		initViews();
		
		mSelectedModule = getIntent().getIntExtra("module", R.id.module_ttai);
		switchModule(mSelectedModule, 0);
		
	}
	
	private void findViews() {
		
		mTtaiLayout = (LinearLayout) findViewById(R.id.module_ttai);
		mXuanbanLayout = (LinearLayout) findViewById(R.id.module_xuanban);
		mDapeiLayout = (LinearLayout) findViewById(R.id.module_dapei);
		mMineLayout = (LinearLayout) findViewById(R.id.module_mine);
		
		mTtaiImage = (ImageView) findViewById(R.id.ttai);
		mDapeiImage = (ImageView) findViewById(R.id.dapei);
		mMineImage = (ImageView) findViewById(R.id.mine);
		mXuanbanImage = (ImageView) findViewById(R.id.xuanban);
		
	}
	
	private void initViews() {
		
		mTtaiLayout.setOnClickListener(new OnSingleClickListener() {
			
			@Override
			public void doOnClick(View v) {
				if (v.getId() != mSelectedModule) {
					switchModule(R.id.module_ttai, mSelectedModule);
				}
			}
		});
		
		mXuanbanLayout.setOnClickListener(new OnSingleClickListener() {
			
			@Override
			public void doOnClick(View v) {
					if (v.getId() != mSelectedModule) {
						switchModule(R.id.module_xuanban, mSelectedModule);
					}
			}
		});
		
		mDapeiLayout.setOnClickListener(new OnSingleClickListener() {
			
			@Override
			public void doOnClick(View v) {
					if (v.getId() != mSelectedModule) {
						switchModule(R.id.module_dapei, mSelectedModule);
					}
			}
		});
		
		mMineLayout.setOnClickListener(new OnSingleClickListener() {
			
			@Override
			public void doOnClick(View v) {
					if (v.getId() != mSelectedModule) {
						
						switchModule(R.id.module_mine, mSelectedModule);
				}
			}
		});
		
	}
	
	public void switchModule(int moudleId, int preMoudleId) {
		
		if (moudleId != preMoudleId) {
			
			String frontTag = "fragment" + moudleId;
			FragmentTransaction transaction = mFragmentManager.beginTransaction();
			// 之前存在的fragment--detach
			Fragment currentFragment = mFragmentManager.findFragmentByTag(mCurrentfragmentTag);
			if (currentFragment != null) {
				transaction.detach(currentFragment);
			}
			// 要显示的fragment，存在attach,不存在创建
			Fragment frontFragment = mFragmentManager.findFragmentByTag(frontTag);
			if (frontFragment != null) {
				transaction.attach(frontFragment);
			} else {
				frontFragment = makeFragment(moudleId);
				transaction.add(R.id.fragment_layout, frontFragment, frontTag);
			}
			// 保存当前显示fragment标志
			mCurrentfragmentTag = frontTag;
			transaction.commit();
			
			setSelecteModule(moudleId);
		}
	}
	
	private Fragment makeFragment(int moudleId) {
		
		Fragment fragment = null;
		switch (moudleId) {
			case R.id.module_ttai:
				fragment = new MineFragment();
				break;
			case R.id.module_xuanban:
				fragment = new SelectionFragment();
				break;
			case R.id.module_dapei:
				fragment = new CollcationFragment();
				break;
			case R.id.module_mine:
				fragment = new TplatformFragment();
				break;
			default:
				break;
		}
		return fragment;
	}
	
	private void setSelecteModule(int moudleId) {
		
		mSelectedModule = moudleId;
		
		setYouquModuleState(false);
		setMessageModuleState(false);
		setContactModuleState(false);
		setFindModuleState(false);
		
		switch (moudleId) {
			case R.id.module_ttai:
				setYouquModuleState(true);
				break;
			case R.id.module_xuanban:
				setMessageModuleState(true);
				break;
			case R.id.module_dapei:
				setContactModuleState(true);
				break;
			case R.id.module_mine:
				setFindModuleState(true);
				break;
		}
	}
	
	private void setYouquModuleState(boolean state) {
		mTtaiLayout.setSelected(state);
		mTtaiImage.setSelected(state);
	}
	
	private void setMessageModuleState(boolean state) {
		mXuanbanLayout.setSelected(state);
		mXuanbanImage.setSelected(state);
	}
	
	private void setContactModuleState(boolean state) {
		mDapeiLayout.setSelected(state);
		mDapeiImage.setSelected(state);
	}
	
	private void setFindModuleState(boolean state) {
		mMineLayout.setSelected(state);
		mMineImage.setSelected(state);
	}
	
	@Override
	public void onBackPressed() {
		
		if (System.currentTimeMillis() - mExitTime > 2000) {
			mExitTime = System.currentTimeMillis();
			Toast.makeText(this, R.string.warn_exit_msg, Toast.LENGTH_SHORT).show();
		} else {
			BuyHandAppApplication.finish();
		}
	}
	
}
