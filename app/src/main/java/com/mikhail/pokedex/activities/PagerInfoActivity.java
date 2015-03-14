package com.mikhail.pokedex.activities;
import android.os.*;
import android.support.v4.view.*;
import android.view.*;
import com.mikhail.pokedex.*;
import com.mikhail.pokedex.misc.*;

public abstract class PagerInfoActivity<T> extends InfoActivity<T> implements ViewPager.OnPageChangeListener
{
	
	ViewPager mViewPager;
	InfoFragmentPagerAdapter<T> mAdapter;
	

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pager_info_activity);
		mViewPager = (ViewPager)findViewById(R.id.details);
		mViewPager.setAdapter(mAdapter = getNewAdapter());
		mViewPager.setCurrentItem(getDefaultPage());
		mViewPager.setOnPageChangeListener(this);
		mViewPager.setOffscreenPageLimit(2);
		ViewGroup contentContainer = (ViewGroup)findViewById(R.id.top_content); 
		contentContainer.addView(getContentView(getLayoutInflater(), contentContainer));
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
	}


	
	@Override
	public void onPageScrolled(int p1, float p2, int p3){
		// TODO: Implement this method
	}

	@Override
	public void onPageScrollStateChanged(int p1){
		// TODO: Implement this method
	}



	
	
	
	
	public abstract int getDefaultPage();
	public abstract InfoFragmentPagerAdapter<T> getNewAdapter();
	public abstract View getContentView(LayoutInflater inflater, ViewGroup container);
	
	
}
