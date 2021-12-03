package com.example.smearcamera.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Region;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.imagefilter.imageprocessors.Filter;
import com.example.smearcamera.R;
import com.example.smearcamera.ui.fragments.BrushFragment;
import com.example.smearcamera.ui.fragments.FiltersFragment;
import com.example.smearcamera.utils.CropView;
import com.example.smearcamera.utils.DrawView;
import com.example.smearcamera.utils.LoadBitmap;
import com.example.smearcamera.utils.SaveUtil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainEditActivity extends AppCompatActivity implements View.OnClickListener, FiltersFragment.OnFragmentInteractionListener, BrushFragment.OnFragmentInteractionListener {

    private static final int PIC_CROP = 1;
    private DrawView mGPUImageView;
    private CropView mCropView;
    private int color;
    private Bitmap  mainBitmap, original, generatedBitmap;
    private SaveUtil saveUtil;
    private FrameLayout frameLayout;
    private boolean saved=false;
    private ImageView brush, crop, filter, delete,back,save;

    private FragmentManager fragmentManager;
    private String path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_edit);

        System.loadLibrary("NativeImageProcessor");

        color = Color.BLUE;
        fragmentManager = getSupportFragmentManager();
        saveUtil = new SaveUtil(this);
        Bundle extras = getIntent().getExtras();
        path = extras.getString("uris");
        Uri resourceUri = Uri.parse(path);
        initView();
        handleImage(resourceUri);
        original = mainBitmap;
        generatedBitmap = mainBitmap;
    }
    private void handleImage(final Uri selectedImage) {
        try {

            if(Build.VERSION.SDK_INT < 28) {
                mainBitmap = MediaStore.Images.Media.getBitmap(
                        getContentResolver(),selectedImage);
            } else {
                ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), selectedImage);
                mainBitmap = ImageDecoder.decodeBitmap(source);
            }
            mGPUImageView.setImageURI(selectedImage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initView(){

        mCropView = findViewById(R.id.crop_view);
        brush = findViewById(R.id.brush);
        crop = findViewById(R.id.crop);
        filter = findViewById(R.id.filter);
        delete = findViewById(R.id.delete);
        back = findViewById(R.id.back);
        save = findViewById(R.id.save);
        brush.setOnClickListener(this);
        crop.setOnClickListener(this);
        filter.setOnClickListener(this);
        delete.setOnClickListener(this);
        back.setOnClickListener(this);
        save.setOnClickListener(this);
        mGPUImageView =  findViewById(R.id.gpuimage);
        frameLayout = findViewById(R.id.framelayout);
        BrushFragment brushFragment = new BrushFragment();
        fragmentManager.beginTransaction().replace(R.id.framelayout,brushFragment).commit();
        mGPUImageView.setOnTouchListener((v, event) -> {
            DrawView mcustomImagview = (DrawView) v;
            mcustomImagview.setErase(false);
            v.onTouchEvent(event);
            v.performClick();
            mcustomImagview.invalidate();
            saved = false;
            return true;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        if (v == brush) {
            mCropView.setVisibility(View.GONE);
            frameLayout.setVisibility(View.VISIBLE);
            BrushFragment brushFragment = new BrushFragment();
            fragmentManager.beginTransaction().replace(R.id.framelayout,brushFragment).commit();
            brush.setImageResource(R.drawable.icon_brushtool2);
            crop.setImageResource(R.drawable.icon_crop1);
            filter.setImageResource(R.drawable.icon_filter1);
            delete.setImageResource(R.drawable.icon_delete1);
        } else if (v == crop) {
            mCropView.setVisibility(View.VISIBLE);
            mCropView.performTouch();
            frameLayout.setVisibility(View.GONE);
            brush.setImageResource(R.drawable.icon_brushtool1);
            crop.setImageResource(R.drawable.icon_crop2);
            filter.setImageResource(R.drawable.icon_filter1);
            delete.setImageResource(R.drawable.icon_delete1);
        } else if(v == filter) {
            frameLayout.setVisibility(View.VISIBLE);
            mCropView.setVisibility(View.GONE);
            FiltersFragment filtersFragment = new FiltersFragment();
            Bundle bundle = new Bundle();
            bundle.putString("uri",path);
            filtersFragment.setArguments(bundle);
            fragmentManager.beginTransaction().replace(R.id.framelayout,filtersFragment).commit();
            brush.setImageResource(R.drawable.icon_brushtool1);
            crop.setImageResource(R.drawable.icon_crop1);
            filter.setImageResource(R.drawable.icon_filter2);
            delete.setImageResource(R.drawable.icon_delete1);
        } else if (v == delete) {
            brush.setImageResource(R.drawable.icon_brushtool1);
            crop.setImageResource(R.drawable.icon_crop1);
            filter.setImageResource(R.drawable.icon_filter1);
            delete.setImageResource(R.drawable.icon_delete2);
            AlertDialog.Builder builder = new AlertDialog.Builder(MainEditActivity.this);
            builder.setMessage("Are you sure you want to delete ?");
            builder.setCancelable(false);
            builder.setPositiveButton("Yes", (dialog, which) -> onBackPressed());
            builder.setNegativeButton("No", (dialog, which) -> dialog.cancel());
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } else if (v == back) {
            if (!saved) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainEditActivity.this);
                builder1.setMessage("You haven't saved it yet, are you sure you want to go back?");
                builder1.setCancelable(false);
                builder1.setPositiveButton("Yes", (dialog, which) -> onBackPressed());
                builder1.setNegativeButton("No", (dialog, which) -> dialog.cancel());
                AlertDialog alert = builder1.create();
                alert.show();}
            else {
                onBackPressed();
            }
        } else if (v == save) {
            if (mCropView.getVisibility() == View.VISIBLE){
                try {
                    if (mCropView.doTheCrop(mainBitmap,original)!=null) {
                        mainBitmap = mCropView.doTheCrop(mainBitmap,original);
                        generatedBitmap = mCropView.doTheCrop(generatedBitmap,original);;
                        mGPUImageView.setImageBitmap(mainBitmap);
                        saved = false;
                    }
                    else {
                        Toast.makeText(this,"Too small to be cropped",Toast.LENGTH_LONG).show();
                    }
                } catch (IOException e) {

                    e.printStackTrace();
                }
            } else {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainEditActivity.this);
                builder1.setMessage("Your image has been saved");
                builder1.setCancelable(true);
                AlertDialog alert = builder1.create();

                alert.show();
                saved = true;

                Bitmap bitmap = LoadBitmap.loadBitmapFromView(mGPUImageView);
                saveUtil.checkWriteStoragePermission(bitmap);
            }
        }
         }

    @Override
    public void onFragmentInteraction(Bitmap bitmap) {
        mGPUImageView.setImageBitmap(bitmap);
        mainBitmap = bitmap;
        saved = false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == SaveUtil.REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION_RESULT) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this,"Permission successfully granted!", Toast.LENGTH_SHORT).show();

            }
            else{
                Toast.makeText(this, "App needs premission", Toast.LENGTH_SHORT).show();}

        }
    }

    @Override
    public void onFragmentInteraction(int value) {
        switch (value) {
            case 1:
            case 2:
            case 3:
                mGPUImageView.setShape(value);
                break;
            case 15:
            case 20:
            case 25:
                mGPUImageView.setWidth(value);
                break;
            default:
                mGPUImageView.setColor(getColor(value));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}