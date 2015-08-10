package com.datapush.buyhand.view;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * ViewPager页面显示Adapter
 *
 */
public class ViewPagerAdapter extends PagerAdapter {
    
    private List<View> mPageViewList = new ArrayList<View>();
    /**
     * viewPager 构造函数
     * @param viewList
     */
    public ViewPagerAdapter(List<View> viewList) {
        this.mPageViewList = viewList;
    }

    @Override
    public int getCount() {
        return mPageViewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mPageViewList.get(position), 0);
        return mPageViewList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((View)object);
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        super.finishUpdate(container);
    }

    @Override
    public void startUpdate(ViewGroup container) {
        super.startUpdate(container);
    }
    
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
    
}