package com.mikhail.pokedex.activities;

import android.content.*;
import android.os.*;
import android.support.v7.app.*;
import android.view.*;
import android.view.GestureDetector.*;
import android.view.View.*;
import com.mikhail.pokedex.data.*;
import com.mikhail.pokedex.misc.*;
import android.animation.*;
import java.util.concurrent.*;

public abstract class InfoActivity<T> extends ActionBarActivity{

	protected int[] mIdArray;
	protected int mCurrentIndex;
	protected PokedexDatabase mPokedexDatabase;
	protected OnSwipeTouchListener mOnSwipeListener;
	protected View mContentView;
	
	private static final long ANIMATION_TIME = 400;

	public static final String EXTRA_ID_ARRAY = "id_arr";
	public static final String EXTRA_ID_INDEX = "id_index";


	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		Bundle extras = getIntent().getExtras();
		if (extras != null){
			mIdArray = extras.getIntArray(EXTRA_ID_ARRAY);
			mCurrentIndex = extras.getInt(EXTRA_ID_INDEX);
		}

		mOnSwipeListener = new OnSwipeTouchListener(this){
			public void onSwipeTop(){
				new Thread(new Runnable(){

						@Override
						public void run(){
							if(++mCurrentIndex >= mIdArray.length){
								mCurrentIndex = 0;
							}
							final T data = getData(mIdArray[mCurrentIndex]);
							try{
								Thread.sleep((long)(ANIMATION_TIME*1.1));
							}catch (InterruptedException e){}
							runOnUiThread(new Runnable(){

									@Override
									public void run(){
										displayData(data);
										ObjectAnimator.ofFloat(mContentView, "y", mContentView.getHeight(), 0).setDuration(ANIMATION_TIME).start();

									}


								});
						}
						
					
				}).start();
				ObjectAnimator.ofFloat(mContentView, "y", 0, -mContentView.getHeight()).setDuration(ANIMATION_TIME).start();
				
				
				
			}
			public void onSwipeBottom(){
				new Thread(new Runnable(){

						@Override
						public void run(){
							if(--mCurrentIndex < 0){
								mCurrentIndex = mIdArray.length-1;
							}
							final T data = getData(mIdArray[mCurrentIndex]);
							try{
								Thread.sleep((long)(ANIMATION_TIME*1.1));
							}catch (InterruptedException e){}
							runOnUiThread(new Runnable(){

									@Override
									public void run(){
										displayData(data);
										ObjectAnimator.ofFloat(mContentView, "y", -mContentView.getHeight(), 0).setDuration(ANIMATION_TIME).start();

									}


								});
						}


					}).start();
				ObjectAnimator.ofFloat(mContentView, "y", 0, mContentView.getHeight()).setDuration(ANIMATION_TIME).start();

				
			}
			
		};

		mPokedexDatabase = PokedexDatabase.getInstance(this);
		loadData();

	}

	protected void setSwipeListener(View v){
		v.setOnTouchListener(mOnSwipeListener);
	}
	
	private void loadData(){
		new Thread(new Runnable(){
				@Override
				public void run(){
					final T data = getData(mIdArray[mCurrentIndex]);
					runOnUiThread(new Runnable(){
							@Override
							public void run(){
								displayData(data);
							}
						});
				}
			}).start();

	}


	
	public abstract T getData(int id);

	
	
	public abstract void displayData(T curentItem);




}
