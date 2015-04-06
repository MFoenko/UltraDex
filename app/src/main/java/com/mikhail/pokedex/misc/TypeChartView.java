package com.mikhail.pokedex.misc;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.mikhail.pokedex.data.PokedexDatabase;

/**
 * Created by mchail on 4/5/15.
 */
public class TypeChartView extends View {

    private int mTypeVersion;
    private float[][] mTypeChart;

    private Paint mChartItemPaint;
    private Paint mHorizontalHeaderTextPaint;
    private Paint mVerticalHeaderTextPaint;
    private Paint mTypePaint;
    private Paint mOverLapPaint;

    private int xOffset = 0;
    private int yOffset = 0;
    private int mCellSize = 48;
    private int mHeaderSize = 2*mCellSize;


    public TypeChartView(Context context) {
        super(context);
        init();
    }

    public TypeChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        mCellSize *= getContext().getResources().getDisplayMetrics().density;
        mHeaderSize *= getContext().getResources().getDisplayMetrics().density;

        mChartItemPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mChartItemPaint.setTextAlign(Paint.Align.CENTER);
        mChartItemPaint.setTextSize(mCellSize/2);

        mTypePaint = new Paint();
        mOverLapPaint = new Paint();
        mOverLapPaint.setColor(0xFFFFFFFF);
        mHorizontalHeaderTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHorizontalHeaderTextPaint.setTextAlign(Paint.Align.CENTER);
        mHorizontalHeaderTextPaint.setTextSize(mCellSize/2);
        mVerticalHeaderTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mVerticalHeaderTextPaint.setTextAlign(Paint.Align.CENTER);
        mVerticalHeaderTextPaint.setTextSize(mCellSize/2);

         setTypeVersion(PokedexDatabase.getTypeVersion());
    }

    private void setTypeVersion(int version){
        mTypeVersion = version;
        mTypeChart = PokedexDatabase.TYPE_EFFICIENCY[version];
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for(int r=0;r<mTypeChart.length;r++) {
            for (int c = 0; c < mTypeChart[r].length; c++) {
                int x = mHeaderSize + xOffset + mCellSize / 2 + mCellSize * (c);
                int y = mHeaderSize + yOffset + mCellSize / 2 + mCellSize * (r);
                String symbol = "x";
                if(mTypeChart[c][r] == .5f) symbol += "\u00BD";
                if(mTypeChart[c][r] == .25f) symbol += "\u00BC";
                if(mTypeChart[c][r] == 2) symbol += "2";
                if(mTypeChart[c][r] == 4) symbol += "4";
                if(mTypeChart[c][r] != 1)
                    canvas.drawText(symbol, x, y, mChartItemPaint);

            }
            mTypePaint.setColor(PokedexDatabase.TYPE_COLORS[mTypeVersion][r]);
            int posX =  mHeaderSize + xOffset + mCellSize * (r);
            int posY =  mHeaderSize + yOffset + mCellSize * (r);


            canvas.drawRect(0,posY, mHeaderSize, posY+mCellSize, mTypePaint);
            canvas.drawText(PokedexDatabase.TYPE_NAMES[mTypeVersion][r],mHeaderSize/2,posY+mCellSize/2, mChartItemPaint);
            canvas.drawRect(posX, 0, posX+mCellSize, mHeaderSize, mTypePaint);
           // canvas.drawText(PokedexDatabase.TYPE_NAMES[mTypeVersion][r],posX/2,mHeaderSize/2, mChartItemPaint);
            canvas.drawRect(0,0,mHeaderSize, mHeaderSize, mOverLapPaint);
        }
    }

    boolean isHeld = false;
    float touchStartX;
    float touchStartY;
    int ogXOffset;
    int ogYOffset;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:

                isHeld = true;
                touchStartX = event.getX();
                touchStartY = event.getY();
                ogXOffset = xOffset;
                ogYOffset = yOffset;
                   return true;

            case MotionEvent.ACTION_MOVE:
                if(isHeld) {
                    xOffset = (int)(ogXOffset + event.getX() - touchStartX);
                    yOffset = (int)(ogYOffset + event.getY() - touchStartY);
                    checkOffsetBounds();
                    invalidate();
                }
                return true;
            case MotionEvent.ACTION_UP:
                isHeld = false;
                return true;
            case MotionEvent.ACTION_CANCEL:
                isHeld = false;
                return true;
        }

        return super.onTouchEvent(event);
    }

    public void checkOffsetBounds(){
        if(xOffset > 0) xOffset = 0;
        if(yOffset > 0) yOffset = 0;

        int chartSize = mCellSize*mTypeChart.length;

        if(chartSize > getWidth() && xOffset < getWidth()-chartSize) xOffset = getWidth()-chartSize;
        if(chartSize > getHeight() && yOffset < getHeight()-chartSize) yOffset = getHeight()-chartSize;
    }

}
