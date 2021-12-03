package com.example.smearcamera.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.constraintlayout.solver.widgets.Rectangle;

import com.example.smearcamera.R;


public class CropView extends View {

    Point[] points = new Point[4];
    Rect mCropRectangle = new Rect();
    /**
     * point1 and point 3 are of same group and same as point 2 and point4
     */
    int groupId = -1;
    private final ArrayList<ColorBall> colorballs = new ArrayList<>();
    // array that holds the balls
    private int balID = 0;
    // variable to know what ball is being dragged
    Paint paint;
    Canvas canvas;

    public CropView(Context context) {
        super(context);
        paint = new Paint();
        setFocusable(true); // necessary for getting the touch events

    }

    public CropView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CropView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        setFocusable(true); // necessary for getting the touch events

    }

    // the method that draws the balls
    @Override
    protected void onDraw(Canvas canvas) {
        this.canvas = canvas;
        if(points[3]==null) //point4 null when user did not touch and move on screen.
            return;
        int left, top, right, bottom;
        left = points[0].x;
        top = points[0].y;
        right = points[0].x;
        bottom = points[0].y;
        for (int i = 1; i < points.length; i++) {
            left = left > points[i].x ? points[i].x:left;
            top = top > points[i].y ? points[i].y:top;
            right = right < points[i].x ? points[i].x:right;
            bottom = bottom < points[i].y ? points[i].y:bottom;
        }
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(5);

        //draw stroke
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.parseColor("#AA1255DB"));
        paint.setStrokeWidth(2);
        mCropRectangle.left = left + colorballs.get(0).getWidthOfBall() / 2;
        mCropRectangle.top = top + colorballs.get(0).getWidthOfBall() / 2;
        mCropRectangle.right = right + colorballs.get(2).getWidthOfBall() / 2;
        mCropRectangle.bottom = bottom + colorballs.get(2).getWidthOfBall() / 2;
        this.canvas.drawRect(
                left + colorballs.get(0).getWidthOfBall() / 2,
                top + colorballs.get(0).getWidthOfBall() / 2,
                right + colorballs.get(2).getWidthOfBall() / 2,
                bottom + colorballs.get(2).getWidthOfBall() / 2, paint);
        //fill the rectangle
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor("#551255DB"));
        paint.setStrokeWidth(0);
        this.canvas.drawRect(
                left + colorballs.get(0).getWidthOfBall() / 2,
                top + colorballs.get(0).getWidthOfBall() / 2,
                right + colorballs.get(2).getWidthOfBall() / 2,
                bottom + colorballs.get(2).getWidthOfBall() / 2, paint);

        //draw the corners
        BitmapDrawable bitmap = new BitmapDrawable();
        // draw the balls on the canvas
        paint.setColor(Color.BLUE);
        paint.setTextSize(18);
        paint.setStrokeWidth(0);
        for (int i =0; i < colorballs.size(); i ++) {
            ColorBall ball = colorballs.get(i);
            this.canvas.drawBitmap(ball.getBitmap(), ball.getX(), ball.getY(),
                    paint);
        }
    }

    public void performTouch() {
        ColorBall.count = 0;
        if(points[0] == null) {
        points[0] = new Point();
        points[0].x = 200;
        points[0].y = 200;

        points[1] = new Point();
        points[1].x = 200;
        points[1].y = 200 + 300;

        points[2] = new Point();
        points[2].x = 200 + 300;
        points[2].y = 200 + 300;

        points[3] = new Point();
        points[3].x = 200 +300;
        points[3].y = 200;

        balID = 2;
        groupId = 1;
        // declare each ball with the ColorBall class
        for (Point pt : points) {
            colorballs.add(new ColorBall(getContext(), R.drawable.ic_circle, pt));
        }}

    }
    // events when touching the screen
    public boolean onTouchEvent(MotionEvent event) {
        int eventaction = event.getAction();

        int X = (int) event.getX();
        int Y = (int) event.getY();

        switch (eventaction) {

            case MotionEvent.ACTION_DOWN: // touch down so check if the finger is on
                // a ball
                if (points[0] == null) {
                    //initialize rectangle.
                    points[0] = new Point();
                    points[0].x = X;
                    points[0].y = Y;

                    points[1] = new Point();
                    points[1].x = X;
                    points[1].y = Y + 30;

                    points[2] = new Point();
                    points[2].x = X + 30;
                    points[2].y = Y + 30;

                    points[3] = new Point();
                    points[3].x = X +30;
                    points[3].y = Y;

                    balID = 2;
                    groupId = 1;
                    // declare each ball with the ColorBall class
                    for (Point pt : points) {
                        colorballs.add(new ColorBall(getContext(), R.drawable.ic_circle, pt));
                    }
                } else {
                    //resize rectangle
                    balID = -1;
                    groupId = -1;
                    for (int i = colorballs.size()-1; i>=0; i--) {
                        ColorBall ball = colorballs.get(i);
                        // check if inside the bounds of the ball (circle)
                        // get the center for the ball
                        int centerX = ball.getX() + ball.getWidthOfBall();
                        int centerY = ball.getY() + ball.getHeightOfBall();
                        paint.setColor(Color.BLUE);
                        // calculate the radius from the touch to the center of the
                        // ball
                        double radCircle = Math
                                .sqrt((double) (((centerX - X) * (centerX - X)) + (centerY - Y)
                                        * (centerY - Y)));

                        if (radCircle < ball.getWidthOfBall()) {

                            balID = ball.getID();
                            if (balID == 1 || balID == 3) {
                                groupId = 2;
                            } else {
                                groupId = 1;
                            }
                            invalidate();
                            break;
                        }
                        invalidate();
                    }
                }
                break;

            case MotionEvent.ACTION_MOVE: // touch drag with the ball

                if (X > this.getRight()- colorballs.get(0).getHeightOfBall()/2){
                    X = getRight()- colorballs.get(0).getWidthOfBall()/2;
                }

                if (Y < - colorballs.get(0).getHeightOfBall()/2) {
                    Y =- colorballs.get(0).getHeightOfBall()/2;
                }
                if (Y > this.getBottom() - colorballs.get(0).getHeightOfBall()/2) {
                    Y = this.getBottom() - colorballs.get(0).getHeightOfBall()/2;
                }
                if (balID < 4 && balID >-1) {

                    colorballs.get(balID).setX(X);
                    colorballs.get(balID).setY(Y);

                    paint.setColor(Color.CYAN);
                    if (groupId == 1) {
                        colorballs.get(1).setX(colorballs.get(0).getX());
                        colorballs.get(1).setY(colorballs.get(2).getY());
                        colorballs.get(3).setX(colorballs.get(2).getX());
                        colorballs.get(3).setY(colorballs.get(0).getY());
                    } else {
                        colorballs.get(0).setX(colorballs.get(1).getX());
                        colorballs.get(0).setY(colorballs.get(3).getY());
                        colorballs.get(2).setX(colorballs.get(3).getX());
                        colorballs.get(2).setY(colorballs.get(1).getY());
                    }

                    invalidate();}


                break;

            case MotionEvent.ACTION_UP:
                // touch drop - just do things here after dropping

                break;
        }
        // redraw the canvas
        invalidate();
        return true;

    }
    public Bitmap doTheCrop(Bitmap sourceBitmap, Bitmap originalBitmap) throws IOException {

        //Bitmap sourceBitmap = null;
        //Drawable backgroundDrawable = getBackground();
    /*
    if (backgroundDrawable instanceof BitmapDrawable) {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) backgroundDrawable;
        if(bitmapDrawable.getBitmap() != null) {
            sourceBitmap = bitmapDrawable.getBitmap();
        }
    }*/
        //source bitmap was scaled, you should calculate the rate
        float widthRate = ((float) sourceBitmap.getWidth()) / getWidth();
        float heightRate =  ((float) sourceBitmap.getHeight()) / getHeight();

        //crop the source bitmap with rate value
        int left = (int) (mCropRectangle.left * widthRate);
        int top = (int) (mCropRectangle.top * heightRate);
        int right = (int) (mCropRectangle.right * widthRate);
        int bottom = (int) (mCropRectangle.bottom * heightRate);
        if ((sourceBitmap.getHeight() >= top+bottom -top) && (sourceBitmap.getWidth() >= left+right - left) &&right - left> originalBitmap.getWidth()/3&&bottom - top>originalBitmap.getHeight()/3) {
            return Bitmap.createBitmap(sourceBitmap, left, top, right - left, bottom - top);}

        return null;
        /*
        setContentView(R.layout.fragment_dashboard);
        Button btn = (Button)findViewById(R.id.capture);
        if (btn == null){
            System.out.println("NULL");
        }
        try{
            btn.setText("HI");
        }
        catch (Exception e){

        }
        //setBackground(drawable);*/
        //savebitmap(croppedBitmap);
    }



    private File savebitmap(Bitmap bmp) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 60, bytes);
        File f = new File(Environment.getExternalStorageDirectory()
                + "/" + "testimage.jpg");
        Toast.makeText(getContext(), "YUP", Toast.LENGTH_LONG).show();
        f.createNewFile();
        FileOutputStream fo = new FileOutputStream(f);
        fo.write(bytes.toByteArray());
        fo.close();
        return f;
    }
    public static class ColorBall {

        Bitmap bitmap;
        Context mContext;
        Point point;
        int id;
        static int count = 0;

        public ColorBall(Context context, int resourceId, Point point) {
            this.id = count++;
            bitmap = BitmapFactory.decodeResource(context.getResources(),
                    resourceId);
            mContext = context;
            this.point = point;
        }

        public int getWidthOfBall() {
            return bitmap.getWidth();
        }

        public int getHeightOfBall() {
            return bitmap.getHeight();
        }

        public Bitmap getBitmap() {
            return bitmap;
        }

        public int getX() {
            return point.x;
        }

        public int getY() {
            return point.y;
        }

        public int getID() {
            return id;
        }

        public void setX(int x) {
            point.x = x;
        }

        public void setY(int y) {
            point.y = y;
        }
    }
}