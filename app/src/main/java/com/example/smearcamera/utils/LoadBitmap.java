package com.example.smearcamera.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;

public class LoadBitmap {
    public static Bitmap loadBitmapFromView(View v) {
        Bitmap b = Bitmap.createBitmap( v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(0, 0, v.getWidth(), v.getHeight());
        v.draw(c);
        return b;
    }
}
