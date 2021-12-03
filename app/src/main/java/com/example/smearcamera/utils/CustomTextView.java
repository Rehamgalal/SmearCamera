package com.example.smearcamera.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.smearcamera.R;

public class CustomTextView extends androidx.appcompat.widget.AppCompatTextView {
    private final Paint paint, strokePaint;
    private final Path path;
    private  int green, color;
    private Canvas canvas;
    public CustomTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        path = new Path();
        strokePaint = new Paint();
        green = context.getColor(R.color.green);
        this.color = android.graphics.Color.WHITE;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(configurePath((float) getWidth(),path),configurePaint(paint));
        canvas.drawPath(configurePath((float) getWidth(),path),configureStrokePaint(color));

    }

        private float getHeight(double width){
            return (float) Math.sqrt((Math.pow(width, 2.0) - (float) Math.pow((width / 2), 2.0)));
        }

        private Paint configurePaint(Paint paint) {
            paint.setColor(android.graphics.Color.WHITE);
            paint.setAntiAlias(true);

            return paint;
        }

        private Paint configureStrokePaint(int color) {
        this.strokePaint.setStyle(Paint.Style.STROKE);
        this.strokePaint.setColor(color);
        this.strokePaint.setStrokeWidth(4);
        this.strokePaint.setStrokeCap(Paint.Cap.ROUND);
            return strokePaint;
        }
        private Path configurePath(Float sideLength,Path path) {
//        pointing up
            path.moveTo(0f, sideLength);
            path.lineTo(sideLength / 2f, sideLength - getHeight((double)sideLength));
            path.lineTo(sideLength, sideLength);

//        pointing down
//        path.lineTo((sideLength / 2f), getHeight(sideLength.toDouble()))
//        path.lineTo(sideLength, 0f)
//        path.lineTo(0f, 0f)

//        pointing right
//        path.lineTo(0f, 0f)
//        path.lineTo(0f, sideLength)
//        path.lineTo(getHeight(sideLength.toDouble()), (sideLength / 2f))

//        pointing left
//        path.moveTo(sideLength, 0f)
//        path.lineTo(sideLength, sideLength)
//        path.lineTo(sideLength - getHeight(sideLength.toDouble()), sideLength / 2f)
            return path;
        }

        public void setStroke(boolean selected) {
            if (selected) {
                this.color = green;
            } else {
                this.color = android.graphics.Color.WHITE;
            }
            invalidate();
        }
    }