package com.mikhail.pokedex.misc;

import android.content.*;
import android.view.*;
import android.util.*;
import android.graphics.*;
import android.mtp.*;

public class StatBarView extends View{

	private static final int TEXT_OFFSET_DP = 4;

	Paint mBarPaint;
	Paint mTextPaint;
	//Paint mShadowPaint;

	Rect mTotalBounds;
	Rect mBarBounds;
	Point mStatTextOrigin;
	

	int mStat;
	int mMaxStat;
	String mLabel = "";

	public StatBarView(Context c){
		super(c);
		init();
	}

	public StatBarView(Context c, AttributeSet set){
		super(c, set);
		init();
	}

	private void init(){
		mBarPaint = new Paint();
		mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		//mShadowPaint = new Paint();
		//mShadowPaint.setColorFilter(ColorFilter.
		}

	public void setTextSize(float textSize){
		mTextPaint.setTextSize(textSize);
		if (mTotalBounds != null)
			mStatTextOrigin.y = (int)(mTotalBounds.bottom / 2f + textSize / 2);
		invalidate();
	}

	public void setColor(int argb){
		mBarPaint.setColor(argb);
		invalidate();
	}

	public void setStat(int stat){
		mStat = stat;
		invalidate();
	}

	public void setMax(int max){
		mMaxStat = max;
		invalidate();
	}

	public void setLabel(String label){
		mLabel = label;
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);
		mBarBounds.right = (int)(mTotalBounds.right * getPercentage());
		
		mBarPaint.setShadowLayer(4, mBarBounds.right, mBarBounds.bottom/2, 20);
		
		canvas.drawRect(mTotalBounds,mBarPaint);
		canvas.drawRect(mBarBounds, mBarPaint);
		canvas.drawText(mLabel + mStat, mStatTextOrigin.x, mStatTextOrigin.y, mTextPaint);

	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh){
		super.onSizeChanged(w, h, oldw, oldh);
		mTotalBounds = new Rect(0, 0, w, h);
		mBarBounds = new Rect(0, 0, (int)(w * getPercentage()), h); 
		mStatTextOrigin = new Point((int)(getContext().getResources().getDisplayMetrics().density * TEXT_OFFSET_DP), (int)(h / 2 + mTextPaint.getTextSize() / 2));
	}

	private float getPercentage(){
		float percent = (float)mStat / mMaxStat;
		if (percent > 1)
			percent = 1;
		if (percent < 0){
			percent = 0;
		}
		return percent;
	}




}
