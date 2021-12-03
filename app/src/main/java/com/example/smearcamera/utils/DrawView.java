package com.example.smearcamera.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Toast;

import java.util.ArrayList;

public class DrawView extends androidx.appcompat.widget.AppCompatImageView {
    private ArrayList<ColouredPoint> paths;
    private ColouredPoint mPath;
    private Paint mPaint;
    private static final float TOUCH_TOLERANCE = 4;
    boolean erase, triangle;
    // Current used colour
    private int  mCurrColour,width,shape;
    public void setErase(boolean era) {
        erase = era;
    }

    public void setColor(int colour) {
        mPath.colour = colour;
        mCurrColour = colour;
    }

    public DrawView(Context context) {
        super(context);
        init();
    }

    // XML constructor
    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setWidth(int width) {
        this.width = width;
        mPath.width = width;
    }

    public void setShape(int shape) {
        if (shape == 3){
            triangle = true;
        } else  {
            triangle = false;
        }
        this.shape = shape;
    }
    private void init() {
        paths = new ArrayList<>();
        mPaint = new Paint();
        mPaint.setColor(mCurrColour);
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(width);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.BEVEL);
        mPaint.setStrokeCap(Paint.Cap.SQUARE);
        mPath = new ColouredPoint(mCurrColour, width,shape);
        paths.add(mPath);
    }

    private float mX, mY;

    @Override
    protected void onDraw(Canvas c) {
        // Let the image be drawn first
        super.onDraw(c);
        // Draw your custom points here
        if (!erase) {
            for (ColouredPoint i : paths) {
                mPaint.setColor(i.colour);
                mPaint.setStrokeWidth(i.width);
                if (shape == 2){
                mPaint.setStrokeJoin(Paint.Join.MITER);
                mPaint.setStrokeCap(Paint.Cap.SQUARE);
                triangle = false;
                }
                else if (shape == 1){
                    mPaint.setStrokeJoin(Paint.Join.ROUND);
                    mPaint.setStrokeCap(Paint.Cap.ROUND);
                    triangle = false;
                } else {
                    triangle = true;
                }

                c.drawPath(i, mPaint);
            }
        } else if (paths.size() > 0) {
            paths.remove(paths.size() - 1);

        } else {
            Toast.makeText(getContext(), "Undo unknown", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPath.reset();
                mPath.moveTo(x, y);
                mX = x;
                mY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = Math.abs(x - mX);
                float dy = Math.abs(y - mY);
                if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                    mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
                    mX = x;
                    mY = y;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (triangle){
                mPath.lineTo(mX-(width/3), mY-(width/3));
                mPaint.setStrokeWidth(1);
                mPath.lineTo(mX, mY);}
                else {
                    mPath.lineTo(mX, mY);
                }
                mPath = new ColouredPoint(mCurrColour, width,shape);
                paths.add(mPath);
                performClick();
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean performClick() {
         super.performClick();
         return true;
    }

    /**
     * Class to store the coordinate and the colour of the point.
     */
    private static class ColouredPoint extends Path {
        int colour;
        int width;
        int shape;
        public ColouredPoint(int colour,int width,int shape) {

            this.colour = colour;
            this.width = width;
            this.shape = shape;
        }

    }

}
