package com.mikhail.pokedex.activities;

import android.content.res.*;
import android.os.*;
import android.support.v4.view.*;
import android.support.v4.widget.*;
import android.util.*;
import android.view.*;
import com.mikhail.pokedex.*;
import com.mikhail.pokedex.misc.*;

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
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.filter,menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.filter).setVisible(mAdapter.getFragment(mViewPager.getCurrentItem()) instanceof UsesRightDrawer);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId()){
            case R.id.filter:
                if(mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                    mDrawerLayout.closeDrawer(Gravity.RIGHT);
                }else{
                     mDrawerLayout.openDrawer(Gravity.RIGHT);
                }

                return true;
        }


        return super.onOptionsItemSelected(item);

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
	public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState)
	{
		mAdapter.destroy();
		super.onSaveInstanceState(outState, outPersistentState);
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
	public void onConfigurationChanged(Configuration newConfig)
	{
		mAdapter.destroy();
		Log.i("AAA", ""+newConfig);
		super.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		
	}


	

	
	
	public abstract int getDefaultPage();
	public abstract InfoFragmentPagerAdapter<T> getNewAdapter();
	public abstract View getContentView(LayoutInflater inflater, ViewGroup container);

	
}
