package com.mikhail.pokedex.misc;

import android.content.*;
import android.util.*;
import android.view.*;
import android.view.GestureDetector.*;
import android.widget.*;

public class GestureContainer extends FrameLayout
{

	protected OnGestureListener mOnGestureListener;
	private SimpleOnGestureListener mSimpleListener =  new SimpleOnGestureListener(){
		private static final int SWIPE_THRESHOLD = 50;
		private static final int SWIPE_VELOCITY_THRESHOLD = 50;

		@Override
		public boolean onDown(MotionEvent e)
		{
			return true;
		}




		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
		{
			boolean result = false;
			try
			{
				float diffY = e2.getY() - e1.getY();
				float diffX = e2.getX() - e1.getX();
				if (Math.abs(diffX) > Math.abs(diffY))
				{
					if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD)
					{
						if (diffX > 0)
						{
							onSwipeRight();
						}
						else
						{
							onSwipeLeft();
						}
					}
					result = false;
				} 
				else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD)
				{
					if (diffY > 0)
					{
						onSwipeDown();
						result = true;
					}
					else
					{
						onSwipeUp();
						result = true;
					}
				}


			}
			catch (Exception exception)
			{
				exception.printStackTrace();
			}
			return result;
		}


		public void onSwipeRight()
		{
			if (mOnGestureListener != null) mOnGestureListener.onSwipeRight();
		}

		public void onSwipeLeft()
		{

			if (mOnGestureListener != null) mOnGestureListener.onSwipeLeft();
		}

		public void onSwipeUp()
		{

			if (mOnGestureListener != null) mOnGestureListener.onSwipeUp();
		}

		public void onSwipeDown()
		{

			if (mOnGestureListener != null) mOnGestureListener.onSwipeDown();
		}
	};
	private GestureDetector mGestureParser;

	public GestureContainer(Context c)
	{
		super(c);

		mGestureParser = new GestureDetector(c, mSimpleListener);
	}
	public GestureContainer(Context c, AttributeSet set)
	{
		super(c, set);

		mGestureParser = new GestureDetector(c, mSimpleListener);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		return true;
	}

	
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev)
	{
		mGestureParser.onTouchEvent(ev);
		return false;
	}

	public void setOnGestureListener(OnGestureListener listener)
	{
		mOnGestureListener = listener;
	}

	public static interface OnGestureListener
	{
		public void onSwipeRight();

		public void onSwipeLeft();

		public void onSwipeUp();

		public void onSwipeDown();
	}




}
