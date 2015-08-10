package com.datapush.buyhand.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 顶部自定义控件
 * 
 * @author Fei
 *
 */

public class MyLinearLayout extends LinearLayout{
	
	private Context context;
	
	private int showClumn;//需要加载的子空间个数
	
	
    public MyLinearLayout(Context context) {
        super(context);
    	this.context = context;
    }

    public MyLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    	this.context = context;
    }

	@SuppressLint("NewApi")
	public MyLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    	this.context = context;
    }
	
	public void setCount(int count){
		showClumn = count;
	}
	/**
	 * 加载子VIEW
	 */
	public void setView(){
		removeAllViewsInLayout();//先清空
		
		for (int i = 0; i < showClumn; i++) {
			
			TextView  tv = new TextView(context);
			
		}
		
		
	}
	

}
