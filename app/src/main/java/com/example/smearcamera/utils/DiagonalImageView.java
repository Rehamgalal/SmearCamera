package com.example.smearcamera.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import com.example.smearcamera.R;

import java.util.jar.Attributes;

import static com.example.smearcamera.utils.Direction.LEFT;
import static com.example.smearcamera.utils.Direction.NONE;
import static com.example.smearcamera.utils.Direction.TOP;

/**
 * Created by fatih.santalu on 7/24/2018.
 */

public class DiagonalImageView extends AppCompatImageView {


  public DiagonalImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  protected void onDraw(Canvas canvas) {

    Drawable drawable = getDrawable();

    if (drawable == null) {
      return;
    }

    if (getWidth() == 0 || getHeight() == 0) {
      return;
    }
    Bitmap b = ((BitmapDrawable) drawable).getBitmap();
    Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);

    int w = getWidth(), h = getHeight();

    Bitmap roundBitmap = getRoundedCroppedBitmap(bitmap, w,h);
    canvas.drawBitmap(roundBitmap, 0, 0, null);

  }

  public static Bitmap getRoundedCroppedBitmap(Bitmap bitmap, int widht, int height) {
    Bitmap finalBitmap;
    if (bitmap.getWidth() != widht || bitmap.getHeight() != height)
      finalBitmap = Bitmap.createScaledBitmap(bitmap, widht, height,
              false);
    else
      finalBitmap = bitmap;
    Bitmap output = Bitmap.createBitmap(finalBitmap.getWidth(),
            finalBitmap.getHeight(), Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(output);

    Paint paint = new Paint();
    final Rect rect = new Rect(0, 0, finalBitmap.getWidth(),
            finalBitmap.getHeight());

    Point point1_draw = new Point(widht, height);
    Point point2_draw = new Point(0, 0);
    Point point3_draw = new Point(0, height);

    Path path = new Path();
    path.moveTo(point1_draw.x, point1_draw.y);
    path.lineTo(point2_draw.x, point2_draw.y);
    path.lineTo(point3_draw.x, point3_draw.y);
    path.lineTo(point1_draw.x, point1_draw.y);
    path.close();
    canvas.drawARGB(0, 0, 0, 0);
    paint.setColor(Color.parseColor("#BAB399"));
    canvas.drawPath(path, paint);
    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
    canvas.drawBitmap(finalBitmap, rect, rect, paint);

    return output;
  }

}