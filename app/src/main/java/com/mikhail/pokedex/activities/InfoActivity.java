package com.mikhail.pokedex.activities;

import android.content.*;
import android.os.*;
import android.support.v7.app.*;
import android.view.*;
import android.view.GestureDetector.*;
import android.view.View.*;
import com.mikhail.pokedex.data.*;

public abstract class InfoActivity<T> extends ActionBarActivity{

	protected int[] mIdArray;
	protected int mCurrentIndex;
	protected PokedexDatabase mPokedexDatabase;

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


		mPokedexDatabase = PokedexDatabase.getInstance(this);
		loadData();

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

	@Override
	public boolean onTouch(View p1, MotionEvent p2){
		//return gestureDetector.onTouchEvent(p2);
		return false;
	}
/*
	private class OnSwipeGestureListener implement{



		private final GestureDetector gestureDetector = new GestureDetector(this, new GestureListener());;

		private final class GestureListener extends SimpleOnGestureListener{

			private static final int SWIPE_THRESHOLD = 100;
			private static final int SWIPE_VELOCITY_THRESHOLD = 100;

			@Override
			public boolean onDown(MotionEvent e){
				return true;
			}

			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY){
				boolean result = false;
				try{
					float diffY = e2.getY() - e1.getY();
					float diffX = e2.getX() - e1.getX();
					if (Math.abs(diffX) > Math.abs(diffY)){
						if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD){
							if (diffX > 0){
								onSwipeRight();
							}else{
								onSwipeLeft();
							}
						}
						result = true;
					}else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD){
						if (diffY > 0){
							onSwipeBottom();
						}else{
							onSwipeTop();
						}
					}
					result = true;

				}catch (Exception exception){
					exception.printStackTrace();
				}
				return result;
			}


			public void onSwipeRight(){
			}

			public void onSwipeLeft(){
			}

			public void onSwipeTop(){

				mCurrentIndex--;
				if (mCurrentIndex < 0){
					mCurrentIndex = mIdArray.length - 1;
				}
				//loadData();

			}

			public void onSwipeBottom(){
			}
		}
	}
*/

	public abstract T getData(int id);

	public abstract void displayData(T curentItem);




}
