package com.example.smearcamera.ui;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.smearcamera.R;
import com.example.smearcamera.ui.fragments.two.Two1Fragment;
import com.example.smearcamera.ui.fragments.two.Two2Fragment;
import com.example.smearcamera.ui.fragments.two.Two3Fragment;
import com.example.smearcamera.ui.fragments.two.Two4Fragment;
import com.example.smearcamera.ui.fragments.two.Two5Fragment;
import com.example.smearcamera.ui.fragments.two.Two6Fragment;
import com.example.smearcamera.ui.fragments.two.Two7Fragment;
import com.example.smearcamera.utils.SaveUtil;


import java.util.ArrayList;

public class TwoPhotosActivity extends AppCompatActivity implements View.OnClickListener {

    private FragmentManager fragmentManager;
    private BaseFragment currentFragment, one, two, three, four, five, six, seven;
    private FrameLayout frameLayout;
    private SaveUtil saveUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tow_photos);
        findViewById(R.id.one).setOnClickListener(this);
        findViewById(R.id.two).setOnClickListener(this);
        findViewById(R.id.three).setOnClickListener(this);
        findViewById(R.id.four).setOnClickListener(this);
        findViewById(R.id.five).setOnClickListener(this);
        findViewById(R.id.six).setOnClickListener(this);
        findViewById(R.id.seven).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.save).setOnClickListener(this);
        frameLayout = findViewById(R.id.frame_layout);
        fragmentManager = getSupportFragmentManager();
        saveUtil = new SaveUtil(this);
        ArrayList<String> uris = new ArrayList<>();
        uris = getIntent().getStringArrayListExtra("uris");
        Bundle bundle = new Bundle();
        bundle.putString("uri1",uris.get(0));
        bundle.putString("uri2",uris.get(1));
        one = new Two1Fragment();
        two = new Two2Fragment();
        three = new Two3Fragment();
        four = new Two4Fragment();
        five = new Two5Fragment();
        six = new Two6Fragment();
        seven = new Two7Fragment();
        one.setArguments(bundle);
        two.setArguments(bundle);
        three.setArguments(bundle);
        four.setArguments(bundle);
        five.setArguments(bundle);
        six.setArguments(bundle);
        seven.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.frame_layout,one).commit();
        currentFragment = one;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.one:
                currentFragment = one;
                fragmentManager.beginTransaction().replace(R.id.frame_layout,one).commit();
                break;
            case R.id.two:
                currentFragment = two;
                fragmentManager.beginTransaction().replace(R.id.frame_layout,two).commit();
                break;
            case R.id.three:
                currentFragment = three;
                fragmentManager.beginTransaction().replace(R.id.frame_layout,three).commit();
                break;
            case R.id.four:
                currentFragment = four;
                fragmentManager.beginTransaction().replace(R.id.frame_layout,four).commit();
                break;
            case R.id.five:
                currentFragment = five;
                fragmentManager.beginTransaction().replace(R.id.frame_layout,five).commit();
                break;
            case R.id.six:
                currentFragment = six;
                fragmentManager.beginTransaction().replace(R.id.frame_layout,six).commit();
                break;
            case R.id.seven:
                currentFragment = seven;
                fragmentManager.beginTransaction().replace(R.id.frame_layout,seven).commit();
                break;
            case R.id.back:
                onBackPressed();
                break;
            case R.id.save:
                saveUtil.checkWriteStoragePermission(currentFragment.loadBitmap());
                AlertDialog.Builder builder1 = new AlertDialog.Builder(TwoPhotosActivity.this);
                builder1.setMessage("Your image has been saved");
                builder1.setCancelable(true);
                AlertDialog alert = builder1.create();

                alert.show();

        }
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

        }}
}