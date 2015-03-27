package com.mikhail.pokedex.misc;

import android.animation.*;
import android.content.*;
import android.graphics.*;
import android.support.v7.widget.*;
import android.util.*;
import android.view.*;

public class ScrollBarView extends View
{

	int mRailTop;
	int mRailBottom;
	int mRailPos;
	Rect mScroller = new Rect(-25,0,25,125);
	public static final int TOUCH_TOLERANCE = 20;
	public static final int BG_PAD = 5;
	
	Paint mScrollerPaint;
	Paint mScrollerBgPaint;
	boolean scrollerDragged = false;
	float inPos;
	public static final int ANIMATION_DURATION = 500;
	public static final int SCROLLER_LINGER_DURATION = 1500;
	
	ObjectAnimator outAnim;
	
	RecyclerView mRecyclerView;
	RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener(){

		@Override
		public void onScrollStateChanged(RecyclerView recyclerView, int newState){
			super.onScrollStateChanged(recyclerView, newState);
			switch(newState){
				case RecyclerView.SCROLL_STATE_DRAGGING:
						cancelAnimationOut();
						animateIn();
						
					break;
				case RecyclerView.SCROLL_STATE_IDLE:
						queueAnimateOut();
						Log.i("AAA","out");
					break;
			}
		}

		@Override
		public void onScrolled(RecyclerView recyclerView, int dx, int dy){
			int item = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
			int count = recyclerView.getAdapter().getItemCount();
			setPercentScrolled((float)item/count);
		}
		
		
	};
	
	
	public ScrollBarView(Context c){
		super(c);
		init();
	}
	

	public ScrollBarView(Context c, AttributeSet set){
		super(c,set);
		init();
	}
	
	public void setRecyclerView(RecyclerView mRecyclerView){
		this.mRecyclerView = mRecyclerView;
		mRecyclerView.setOnScrollListener(mScrollListener);
	}

	public void init(){
		mScrollerPaint = new Paint();
		mScrollerPaint.setColor(0xDD0022DD);
		mScrollerBgPaint = new Paint();
		mScrollerPaint.setColor(0xDD3355EE);

		outAnim = ObjectAnimator.ofFloat(this, "x", inPos+mScroller.width()*2).setDuration(ANIMATION_DURATION);
		outAnim.setStartDelay(SCROLLER_LINGER_DURATION);
		queueAnimateOut();
	}

	@Override
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);
		if(scrollerDragged){
			canvas.drawRect(mScroller.left-BG_PAD, mScroller.top-BG_PAD, mScroller.right+BG_PAD, mScroller.bottom+BG_PAD, mScrollerBgPaint);
		}
		canvas.drawRect(mScroller, mScrollerPaint);
	}
	
	public void animateIn(){
		ObjectAnimator.ofFloat(this, "x", inPos).setDuration(ANIMATION_DURATION).start();
	}
	
	public void queueAnimateOut(){
		outAnim.start();
	}
	
	public void cancelAnimationOut(){
		outAnim.cancel();
		if(getX() != inPos+2*mScroller.width())
		setX(inPos);
	}
	
	
	
	public void calculateScrollerRec(){
		int h = mScroller.height();
		
		mScroller.top = mRailPos+mRailTop-h/2;
		mScroller.bottom = mRailPos+mRailTop+h/2;
		invalidate();
	}
	
	public void scrollerScrolled(){
		if(mRailPos < 0){
			mRailPos = 0;
		}else
		if(mRailPos > mRailBottom){
			mRailPos = mRailBottom-mRailTop;
		}
		
		calculateScrollerRec();
		if(mRecyclerView != null){
			int count = mRecyclerView.getAdapter().getItemCount();
			mRecyclerView.scrollToPosition((int)(getPercentScrolled()*count));
		}
	}

	public float getPercentScrolled(){
		return (float)(mRailPos)/(mRailBottom-mRailTop);
	}
	
	public void setPercentScrolled(float percent){
		mRailPos =(int)(percent*(mRailBottom-mRailTop));
		calculateScrollerRec();
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int w=resolveSize(getPaddingLeft()+getPaddingRight()+mScroller.width(), widthMeasureSpec);
		int h=heightMeasureSpec;
		setMeasuredDimension(w,h);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh){
		super.onSizeChanged(w, h, oldw, oldh);
	
		mRailTop = mScroller.height()/2;
		mRailBottom = h-mScroller.height()/2;
		mRailPos = 0;
		
		inPos = getX();
		outAnim.setFloatValues(inPos+2*mScroller.width());
	}

	@Override
	public boolean onTouchEvent(MotionEvent event){
		
		switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:
				if(isTouchInRect(event, mScroller)){
					cancelAnimationOut();
					return scrollerDragged = true;
				}
				break;
			case MotionEvent.ACTION_MOVE:
				if(scrollerDragged){
					mRailPos = (int)event.getY();
					cancelAnimationOut();
					scrollerScrolled();
					
					return true;
				}
				break;
			case MotionEvent.ACTION_UP:
				queueAnimateOut();
				scrollerDragged = false;
				break;
			case MotionEvent.ACTION_CANCEL:
				queueAnimateOut();
				scrollerDragged = false;
		}
		Log.i("AAA", event.actionToString(event.getAction()));
		return true;
		
		//return super.onTouchEvent(event);
	}
	
	private static boolean isTouchInRect(MotionEvent e, Rect r){
		return e.getX()<= r.right+TOUCH_TOLERANCE && e.getX() >= r.left-TOUCH_TOLERANCE && e.getY() <= r.bottom+TOUCH_TOLERANCE && e.getY() >= r.top-TOUCH_TOLERANCE;
	}
	
	
}
