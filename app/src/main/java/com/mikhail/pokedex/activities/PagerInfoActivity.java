package com.mikhail.pokedex.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mikhail.pokedex.R;
import com.mikhail.pokedex.misc.InfoFragmentPagerAdapter;
import com.mikhail.pokedex.misc.UsesRightDrawer;

public abstract class PagerInfoActivity<T> extends InfoActivity<T> implements ViewPager.OnPageChangeListener
{
	
	ViewPager mViewPager;
	InfoFragmentPagerAdapter<T> mAdapter;
	ViewGroup mRightDrawer;
	DrawerLayout mDrawerLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pager_info_activity);
		mContentView = findViewById(R.id.content);
		mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
		mRightDrawer = (ViewGroup)findViewById(R.id.right_drawer);
		mViewPager = (ViewPager)findViewById(R.id.details);
		mViewPager.setAdapter(mAdapter = getNewAdapter());
		mViewPager.setCurrentItem(getDefaultPage());
		mViewPager.setOnPageChangeListener(this);
		mViewPager.setOffscreenPageLimit(2);
		ViewGroup contentContainer = (ViewGroup)findViewById(R.id.top_content); 
		contentContainer.addView(getContentView(getLayoutInflater(), contentContainer));
		
		setSwipeListener(mContentView);
	}

	@Override
	public void displayData(T currentItem){
		mAdapter.setData(currentItem);
		onPageSelected(mViewPager.getCurrentItem());
	}

	@Override
	public void onPageSelected(int p1){
		mAdapter.onPageSelected(p1);
		invalidateOptionsMenu();
		if(mAdapter.getFragment(p1) instanceof UsesRightDrawer){
			mRightDrawer.removeAllViews();
			View filters = ((UsesRightDrawer)mAdapter.getFragment(p1)).getRightDrawerLayout(getLayoutInflater(), mRightDrawer);
			if(filters.getParent() != null){
				((ViewGroup)filters.getParent()).removeAllViews();
			}
			mRightDrawer.addView(filters);
			
		}
		mDrawerLayout.setDrawerLockMode(
		mAdapter.getFragment(p1) instanceof UsesRightDrawer
		? DrawerLayout.LOCK_MODE_UNLOCKED
		: DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		
		}


	
	@Override
	public void onPageScrolled(int p1, float p2, int p3){
		// TODO: Implement this method
	}

	@Override
	public void onPageScrollStateChanged(int p1){
		// TODO: Implement this method
	}



	@Override
	public void onDestroy() {
		super.onDestroy();
		//mAdapter.destroy();
	}
	
	
	
	
	public abstract int getDefaultPage();
	public abstract InfoFragmentPagerAdapter<T> getNewAdapter();
	public abstract View getContentView(LayoutInflater inflater, ViewGroup container);

	
}
