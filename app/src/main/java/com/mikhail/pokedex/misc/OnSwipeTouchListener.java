package com.mikhail.pokedex.misc;

import android.content.*;
import android.util.*;
import android.view.*;
import android.view.GestureDetector.*;
import android.view.View.*;

public class OnSwipeTouchListener implements OnTouchListener {

	boolean run;
	
		@Override
		public boolean onTouch(View p1, MotionEvent p2){
			gestureDetector.onTouchEvent(p2);
			run = !run;
			return run;
		}
		
	

		private final GestureDetector gestureDetector;

		public OnSwipeTouchListener (Context ctx){
			gestureDetector = new GestureDetector(ctx, new GestureListener());
		}

		private final class GestureListener extends SimpleOnGestureListener {

			private static final int SWIPE_THRESHOLD = 50;
		private static final int SWIPE_VELOCITY_THRESHOLD = 50;

		@Override
		public boolean onDown(MotionEvent e)
		{
			return true;
		}

		
			
			
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
				boolean result = false;
				try {
					float diffY = e2.getY() - e1.getY();
					float diffX = e2.getX() - e1.getX();
					if (Math.abs(diffX) > Math.abs(diffY)) {
						if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
							if (diffX > 0) {
								onSwipeRight();
							} else {
								onSwipeLeft();
							}
						}
						result = false;
					} 
					else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            onSwipeBottom();
							result = true;
                        } else {
                            onSwipeTop();
							result = true;
                        }
                    }
                    

				} catch (Exception exception) {
					exception.printStackTrace();
				}
				return result;
			}
		}

		public void onSwipeRight() {
		}

		public void onSwipeLeft() {
		}

		public void onSwipeTop() {
		}

		public void onSwipeBottom() {
		}
	}
