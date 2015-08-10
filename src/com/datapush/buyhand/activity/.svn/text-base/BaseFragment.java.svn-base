package com.datapush.buyhand.activity;


import com.datapush.buyhand.R;
import com.datapush.buyhand.common.AppContents;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * 基础Fragment
 *
 */
public class BaseFragment extends Fragment {

	private View mPreserveView;
	private LayoutInflater mLayoutInflater;

	@Override
	public final View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		mLayoutInflater = inflater;
		if (mPreserveView != null) {
			return mPreserveView;
		} else {
			return onInitView(inflater, container);
		}
	}

	// Fragment初次载入
	public View onInitView(LayoutInflater inflater, ViewGroup container) {
		return null;
	}

	public void onReloadView(View preserveView) {
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

		mPreserveView = getView();
	}

	public LayoutInflater getLayoutInflater() {
		return mLayoutInflater;
	}
	/**
	 * 判断是否登入
	 * 
	 * @return 
	 */
	public boolean isLogin(){
		if(AppContents.getUserInfo().getUserCode() !=null && AppContents.getUserInfo().getUserCode().length() >0){
			return true;
		}else{
			Toast.makeText(getActivity(), getString(R.string.not_login_msg), Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(getActivity(),LoginActivity.class);
			startActivity(intent);
		}
		return false;
	}
}