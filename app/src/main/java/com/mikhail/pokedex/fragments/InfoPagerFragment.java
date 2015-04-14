package com.mikhail.pokedex.fragments;

import android.os.*;
import android.support.v4.app.*;
import android.util.*;
import android.view.*;

public abstract class InfoPagerFragment<I> extends Fragment{

	private boolean isDisplayed = false;
	protected boolean isPrimary = true;

	public InfoPagerFragment(){
		setRetainInstance(true);
	}
	
	
	
	public void loadData(final I data){
		isDisplayed = false;
		new Thread(new Runnable(){
				@Override
				public void run(){
					setData(data);
					new Handler(Looper.getMainLooper()).post(
						new Runnable(){
							@Override
							public void run(){
								//Log.i("AAA", this + " data set " + isDisplayed);
								if(!isDisplayed)
									isDisplayed = displayData();
							}
						});
				}
			}).start();

	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState){
		super.onViewCreated(view, savedInstanceState);
		//Log.i("AAA", this + " created " + isDisplayed);
		if (!isDisplayed){
			isDisplayed = displayData();
		}
	}
	
	public void setPagePrimary(boolean isPrimary){
		this.isPrimary = isPrimary;
	}
	
	

	@Override
	public void onStart(){
		super.onStart();
		
		if (!isDisplayed){
			isDisplayed = displayData();
		}
		
	}

	
	@Override
	public void onResume(){
		super.onResume();

		if (!isDisplayed){
			isDisplayed = displayData();
		}
	}

	@Override
	public void onPause(){
		super.onPause();
		//Log.i("AAA", this + " paused " + isDisplayed);
		
		//isDisplayed = false;
		}

	@Override
	public void onStop(){
		super.onStop();
		//Log.i("AAA", this + " stopped " + isDisplayed);
		isDisplayed = false;
	}

	@Override
	public void onDestroyView(){
		super.onDestroyView();
		isDisplayed = false;
	}

	
	
	
	public abstract void setData(I data);
	public abstract boolean displayData();
	public abstract String getTitle();

	
	
}
