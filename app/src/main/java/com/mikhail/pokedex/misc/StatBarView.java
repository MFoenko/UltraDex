package com.mikhail.pokedex.misc;

import android.content.*;
import android.view.*;
import android.util.*;
import android.graphics.*;
import android.mtp.*;
import android.widget.*;

public class StatBarView extends View{

	private static final int TEXT_OFFSET_DP = 4;

	Paint mBarPaint;
	Paint mRemPaint;
	Paint mTextPaint;
	//Paint mShadowPaint;

	Rect mTotalBounds;
	Rect mBarBounds;
	Rect mRemBounds;
	Point mLeftTextOrigin;
	Point mRightTextOrigin;
	Point mCenterTextOrigin;
	boolean usingLabel = false;

	public int mStat;
	int mMaxStat;
	String mLabel = "";
	String mLeftText = "";
	String mRightText = "";
	String mCenterText = "";

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
		mRemPaint = new Paint();

		mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		//mShadowPaint = new Paint();
		//mShadowPaint.setColorFilter(ColorFilter.
	}

	public void setTextSize(float textSize){
		mTextPaint.setTextSize(textSize);
		if (mTotalBounds != null){
			mCenterTextOrigin.y = mRightTextOrigin.y = mLeftTextOrigin.y = (int)(mTotalBounds.bottom / 2f + textSize / 2);
		}
		invalidate();
	}

	public void setColor(int argb){
		mBarPaint.setColor(argb);
		mRemPaint.setColor(argb - ((argb / 0x4000000)) * 0x3000000);
		invalidate();
	}

	public void setColor(int argbBar, int argbRem){
		mBarPaint.setColor(argbBar);
		mRemPaint.setColor(argbRem);
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
		usingLabel = true;
		invalidate();
	}

	public void setLeftText(String text){
		mLeftText = text;
		usingLabel = false;
		invalidate();
	}

	public void setRightText(String text){
		mRightText = text;
		invalidate();
	}

	public void setCenterText(String text){
		mCenterText = text;
		invalidate();
	}
	
	public void resetText(){
		usingLabel = false;
		mCenterText = "";
		mRightText = "";
		mLeftText = "";
	}
	
	@Override
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);
		//mBarPaint.setShadowLayer(4, mBarBounds.right, mBarBounds.bottom / 2, 20);
		reCalcBarBounds();
		canvas.drawRect(mRemBounds, mRemPaint);
		canvas.drawRect(mBarBounds, mBarPaint);
		canvas.drawText((usingLabel?mLabel+mStat:mLeftText), mLeftTextOrigin.x, mLeftTextOrigin.y, mTextPaint);
		canvas.drawText(mRightText, mRightTextOrigin.x, mRightTextOrigin.y, mTextPaint);
		canvas.drawText(mCenterText, mCenterTextOrigin.x, mCenterTextOrigin.y, mTextPaint);
		
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh){
		super.onSizeChanged(w, h, oldw, oldh);
		mTotalBounds = new Rect(0, 0, w, h);
		mBarBounds = new Rect(0, 0, w, h); 
		mRemBounds = new Rect(0, 0, w, h);
		reCalcBarBounds();
		Rect rightTextBounds = new Rect();
		mTextPaint.getTextBounds(mRightText, 0, mRightText.length(), rightTextBounds);
		mLeftTextOrigin = new Point((int)(getContext().getResources().getDisplayMetrics().density * TEXT_OFFSET_DP), (int)(h / 2 + mTextPaint.getTextSize() / 2));
		mRightTextOrigin = new Point((int)(w - getContext().getResources().getDisplayMetrics().density * TEXT_OFFSET_DP-rightTextBounds.width()), (int)(h / 2 + mTextPaint.getTextSize() / 2));
		
		Rect centerTextBounds = new Rect();
		mTextPaint.getTextBounds(mRightText, 0, mRightText.length(), centerTextBounds);
		mCenterTextOrigin = new Point((w-centerTextBounds.width())/2, (int)(h / 2 + mTextPaint.getTextSize() / 2));
		
	}


	private void reCalcBarBounds(){
		int end = (int)(mTotalBounds.right * getPercentage());
		mBarBounds.right = end;
		mRemBounds.left = end;

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
