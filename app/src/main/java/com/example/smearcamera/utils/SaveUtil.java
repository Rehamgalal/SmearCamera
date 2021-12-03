package com.example.smearcamera.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static androidx.core.app.ActivityCompat.requestPermissions;
import static androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale;
import static java.sql.DriverManager.println;

public class SaveUtil{

    private Activity activity;
    public SaveUtil(Activity activity){
        this.activity = activity;
    }
    public static int REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION_RESULT=0;
    public void checkWriteStoragePermission(Bitmap bitmap) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                savePublicImage(bitmap);

            } else {
                if(shouldShowRequestPermissionRationale(activity,Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(activity, "app needs to be able to access save", Toast.LENGTH_SHORT).show();
                }

                requestPermissions( activity,new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION_RESULT);

            }
        } else {
            savePublicImage(bitmap);
        }
    }

    private String savePrivateBitmap(Bitmap bitmap){

        ContextWrapper cw = new ContextWrapper(activity);
        File mImageFolder = cw.getDir("smearImage", Context.MODE_PRIVATE);
        if(!mImageFolder.exists()) {
            mImageFolder.mkdirs();
        }

        long timeStamp =  System.currentTimeMillis();//("yyyyMMdd_HHmmss").format( Date())
        String fName = "$timeStamp.jpg";

        File file = new File(mImageFolder, fName);
        if (file.exists()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            println("savePrivateBitmap bitmap!!.width "+bitmap.getWidth());
            println("savePrivateBitmap bitmap!!.height "+bitmap.getHeight());
            out.flush();
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    private void savePublicImage(Bitmap finalBitmap) {

        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        File myDir = new File(root + "/saved_images");
        if(!myDir.exists()) {

            myDir.mkdirs();}

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format( new Date());
        String fname = "Shutta_"+ timeStamp +".jpg";

        File file = new File(myDir, fname);
        if (file.exists()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intent scanIntent =   new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(file.getAbsoluteFile());
        scanIntent.setData(contentUri);
        activity.sendBroadcast(scanIntent);
    }
}