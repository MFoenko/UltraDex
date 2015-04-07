package com.mikhail.pokedex.misc;

import android.content.*;
import android.graphics.*;
import android.util.*;
import android.view.*;
import com.mikhail.pokedex.data.*;
import android.view.ScaleGestureDetector.*;

/**
 * Created by mchail on 4/5/15.
 */
public class TypeChartView extends View {

    private int mTypeVersion;
    private float[][] mTypeChart;

    private Paint mChartItemPaint;
    private Paint mHeaderTextPaint;
    private Paint mTypePaint;
    private Paint mOverLapPaint;

    private float xOffset = 0;
    private float yOffset = 0;
    private float mCellSize = 48;
    private float mHeaderSize = 2 * mCellSize;
	private boolean[] selectedRows;
	private boolean[] selectedCols;

	private float MAX_CELL_SIZE = mCellSize * 2;
	private float MIN_CELL_SIZE = mCellSize / 4;


	private Rect textBounds = new Rect();

    public TypeChartView(Context context) {
        super(context);
        init();
    }

    public TypeChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {

        mCellSize *= getContext().getResources().getDisplayMetrics().density;
		MAX_CELL_SIZE = mCellSize * 2;
		MIN_CELL_SIZE = mCellSize / 2;
		
        mChartItemPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        
        mTypePaint = new Paint();
        mOverLapPaint = new Paint();
        mOverLapPaint.setColor(0xFFFFFFFF);
        mHeaderTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

		calcSizes();
		setTypeVersion(PokedexDatabase.getTypeVersion());
		mScaleDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
    }

	public void calcSizes() {
		mHeaderSize = 2 * mCellSize;
	
		mChartItemPaint.setTextSize(mCellSize / 4);
		
		mHeaderTextPaint.setTextSize(mCellSize / 3);
		
	}

    private void setTypeVersion(int version) {
        mTypeVersion = version;
        mTypeChart = PokedexDatabase.TYPE_EFFICIENCY[version];
		selectedRows = new boolean[mTypeChart.length];
        selectedCols = new boolean[mTypeChart.length];
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int r=0;r < mTypeChart.length;r++) {
            for (int c = 0; c < mTypeChart[r].length; c++) {
                float x = mHeaderSize + xOffset + mCellSize / 2 + mCellSize * (r);
                float y = mHeaderSize + yOffset + mCellSize / 2 + mCellSize * (c);
                String symbol = "x";
                if (mTypeChart[c][r] == .5f) symbol += "\u00BD";
                if (mTypeChart[c][r] == .25f) symbol += "\u00BC";
                if (mTypeChart[c][r] == 2) symbol += "2";
                if (mTypeChart[c][r] == 4) symbol += "4";
                if (mTypeChart[c][r] == 0) symbol += "0";
                if (mTypeChart[c][r] != 1)
				// canvas.drawText(symbol, x, y, mChartItemPaint);
					drawTextCentred(canvas, mChartItemPaint, symbol, x, y);

            }

        }

		for (int t=0;t < mTypeChart.length;t++) {
			mTypePaint.setColor(PokedexDatabase.TYPE_COLORS[mTypeVersion][t]);
            float posX =  mHeaderSize + xOffset + mCellSize * (t);
            float posY =  mHeaderSize + yOffset + mCellSize * (t);


            canvas.drawRect(0, posY, mHeaderSize, posY + mCellSize, mTypePaint);
            //canvas.drawText(PokedexDatabase.TYPE_NAMES[mTypeVersion][r], mHeaderSize / 2, posY + mCellSize / 2, mHeaderTextPaint);
          	drawTextCentred(canvas, mHeaderTextPaint, PokedexDatabase.TYPE_NAMES[mTypeVersion][t], mHeaderSize / 2, posY + mCellSize / 2);
			canvas.drawRect(posX, 0, posX + mCellSize, mHeaderSize, mTypePaint);
			canvas.save();
			canvas.rotate(-90, posX + mCellSize / 2, mHeaderSize / 2);
			//canvas.drawText(PokedexDatabase.TYPE_NAMES[mTypeVersion][r],posX+mCellSize/2,mHeaderSize/2, mHeaderTextPaint);
         	drawTextCentred(canvas, mHeaderTextPaint, PokedexDatabase.TYPE_NAMES[mTypeVersion][t], posX + mCellSize / 2, mHeaderSize / 2);
			canvas.restore();
		}
		canvas.drawRect(0, 0, mHeaderSize, mHeaderSize, mOverLapPaint);
    }

	public void drawTextCentred(Canvas canvas, Paint paint, String text, float cx, float cy) {
		paint.getTextBounds(text, 0, text.length(), textBounds);
		canvas.drawText(text, cx - textBounds.exactCenterX(), cy - textBounds.exactCenterY(), paint);
	}

    boolean isHeld = false;
	int pointerId;
	//long timeStart;
    float touchStartX;
    float touchStartY;
    float ogXOffset;
    float ogYOffset;
	ScaleGestureDetector mScaleDetector;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
		//if(mScaleDetector.onTouchEvent(event)) return true;
		mScaleDetector.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
				//timeStart = System.currentTimeMillis();
                isHeld = true;
				pointerId = event.getPointerId(0);
                touchStartX = event.getX();
                touchStartY = event.getY();
                ogXOffset = xOffset;
                ogYOffset = yOffset;
				return true;

            case MotionEvent.ACTION_MOVE:
                if (isHeld && !mScaleDetector.isInProgress()) {
					int pointer = event.findPointerIndex(pointerId);
                    xOffset = (int)(ogXOffset + event.getX(pointer) - touchStartX);
                    yOffset = (int)(ogYOffset + event.getY(pointer) - touchStartY);
                    checkOffsetBounds();
                    invalidate();
                }
                return true;
            case MotionEvent.ACTION_UP:
				/*if(System.currentTimeMillis() - timeStart <500){
					if(event.getX() < mHeaderSize || event.getY() < mHeaderSize
				}*/
                isHeld = false;
                return true;
            case MotionEvent.ACTION_CANCEL:
                isHeld = false;
                return true;
        }

        return true;
    }


	private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {


		@Override
		public boolean onScale(ScaleGestureDetector detector) {

			mCellSize *= detector.getScaleFactor();
			//xOffset*= detector.getScaleFactor();
			//yOffset*= detector.getScaleFactor();
			//Don't let the object get too small or too large.
			//mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));
			mCellSize = Math.max(MIN_CELL_SIZE, Math.min(mCellSize, MAX_CELL_SIZE));
			calcSizes();

			invalidate();
			return true;
		}
	}

    public void checkOffsetBounds() {
        if (xOffset > 0) xOffset = 0;
        if (yOffset > 0) yOffset = 0;

        float chartSize = mCellSize * mTypeChart.length;

        if (chartSize > getWidth() && xOffset < getWidth() - chartSize) xOffset = getWidth() - chartSize;
        if (chartSize > getHeight() && yOffset < getHeight() - chartSize) yOffset = getHeight() - chartSize;
    }



}
