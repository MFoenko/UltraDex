package com.mikhail.pokedex.misc;

import android.support.v4.app.*;
import android.support.v4.view.*;
import android.view.*;
import com.mikhail.pokedex.fragments.*;

public abstract class InfoFragmentPagerAdapter<T> extends PagerAdapter{

	FragmentManager fragmentManager;
	InfoPagerFragment<T>[] fragments;

	public abstract int getNumFrags();
	public abstract InfoPagerFragment<T> getFragment(int position);


	public InfoFragmentPagerAdapter(FragmentManager fm){
		fragmentManager = fm;
		fragments = new InfoPagerFragment[getNumFrags()];

	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object){
		assert(0 <= position && position < fragments.length);
		FragmentTransaction trans = fragmentManager.beginTransaction();
		trans.remove(fragments[position]);
		trans.commit();
		fragments[position] = null;
	}

	@Override
	public Fragment instantiateItem(ViewGroup container, int position){
		Fragment fragment = getItem(position);
		FragmentTransaction trans = fragmentManager.beginTransaction();
		trans.add(container.getId(), fragment, "fragment:" + position);
		trans.commit();
		return fragment;
	}

	@Override
	public int getCount(){
		return fragments.length;
	}

	@Override
	public boolean isViewFromObject(View view, Object fragment){
		return ((Fragment) fragment).getView() == view;
	}

	public InfoPagerFragment<T> getItem(int position){
		assert(0 <= position && position < fragments.length);
		if (fragments[position] == null){
			fragments[position] = getFragment(position);
		}
		return fragments[position];
	}

	@Override
	public CharSequence getPageTitle(int position){
		return getItem(position).getTitle();
	}

	public void setData(T data){
		for (int f=0;f < getCount();f++){
			getItem(f).loadData(data);
		}
	}

	@Override
	public void onPageScrolled(int p1, float p2, int p3){
		// TODO: Implement this method
	}

	@Override
	public void onPageSelected(int p1){

		for (int i=0;i < fragments.length;i++){
			fragments[i].setPagePrimary(i==p1);
		}

	}

	@Override
	public void onPageScrollStateChanged(int p1){
		// TODO: Implement this method
	}




}
