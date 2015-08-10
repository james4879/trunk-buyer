package com.datapush.buyhand.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * 多TextView显示FrameLayout 注意在XML文件中使用此控件的时候要定义此控件的宽度为match_parent,不能由子控件来决定其宽度
 * 
 * @author yanpf
 * 
 */
public class TextViewFrameLayout extends FrameLayout {

    private int showClumn = 3;// 定义显示的列数,默认为3
    private int space = 1; // 定义图片之间的间隔。默认为10像素
    private int width = -1;// 控件宽度，在未取到的时候为-1.
    private int childWidth = -1, childHeight = -1;// 子View宽度和高度

    public TextViewFrameLayout(Context context) {
        super(context);
    }

    public TextViewFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextViewFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public int getShowClumn() {
        return showClumn;
    }

    public void setShowClumn(int showClumn) {
        this.showClumn = showClumn;
    }

    public int getSpace() {
        return space;
    }

    public void setSpace(int space) {
        this.space = space;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getChildWidth() {
        return childWidth;
    }

    public void setChildWidth(int childWidth) {
        this.childWidth = childWidth;
    }

    public int getChildHeight() {
        return childHeight;
    }

    public void setChildHeight(int childHeight) {
        this.childHeight = childHeight;
    }

    public void addView(TextView text) {
        if (width == -1) {
            return;
        }
        if (childWidth == -1 || childHeight == -1) {
            childWidth =(width - getPaddingLeft() - getPaddingRight() - space * (showClumn - 1))/ showClumn;
//            childWidth =(width)/ showClumn;
            childHeight = childWidth/2;
        }
        int index = getChildCount();
        FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(childWidth, childHeight);
        flp.leftMargin = index % 3 * (childWidth+space);
        flp.topMargin = index / 3 * (childHeight+space);
        addView(text, flp);
    }

}
