package com.datapush.buyhand.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

public class MyDialog extends Dialog {

	private View contentView;
	private int contentId;

	public MyDialog(Context context, int theme, int contentId) {
		super(context, theme);
		this.contentId = contentId;
	}

	public MyDialog(Context context, int contentId) {
		super(context);
		this.contentId = contentId;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (contentView == null) {
			LayoutInflater layoutInflater = getLayoutInflater();
			contentView = layoutInflater.inflate(contentId, null);
		}
		setContentView(contentView);
	}

	public View getContentView() {
		if (contentView == null) {
			LayoutInflater layoutInflater = getLayoutInflater();
			contentView = layoutInflater.inflate(contentId, null);
		}
		return contentView;
	}
}
