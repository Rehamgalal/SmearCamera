package com.example.smearcamera.ui;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.smearcamera.R;
import com.example.smearcamera.ui.fragments.five.Five1Fragment;
import com.example.smearcamera.ui.fragments.five.Five2Fragment;
import com.example.smearcamera.ui.fragments.five.Five3Fragment;
import com.example.smearcamera.ui.fragments.five.Five4Fragment;
import com.example.smearcamera.ui.fragments.five.Five5Fragment;
import com.example.smearcamera.ui.fragments.five.Five6Fragment;
import com.example.smearcamera.ui.fragments.five.Five7Fragment;
import com.example.smearcamera.ui.fragments.six.Six1Fragment;
import com.example.smearcamera.ui.fragments.six.Six2Fragment;
import com.example.smearcamera.ui.fragments.six.Six3Fragment;
import com.example.smearcamera.ui.fragments.six.Six4Fragment;
import com.example.smearcamera.ui.fragments.six.Six5Fragment;
import com.example.smearcamera.ui.fragments.six.Six6Fragment;
import com.example.smearcamera.ui.fragments.six.Six7Fragment;
import com.example.smearcamera.utils.SaveUtil;

import java.util.ArrayList;

public class SixPhotosActivity extends AppCompatActivity implements View.OnClickListener {

    private FragmentManager fragmentManager;
    private BaseFragment currentFragment, one, two, three, four, five, six, seven;
    private FrameLayout frameLayout;
    private SaveUtil saveUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_six_photos);
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
        bundle.putString("uri3",uris.get(2));
        bundle.putString("uri4",uris.get(3));
        bundle.putString("uri5",uris.get(4));
        bundle.putString("uri6",uris.get(5));
        one = new Six1Fragment();
        one.setArguments(bundle);
        two = new Six2Fragment();
        two.setArguments(bundle);
        three = new Six3Fragment();
        three.setArguments(bundle);
        four = new Six4Fragment();
        four.setArguments(bundle);
        five = new Six5Fragment();
        five.setArguments(bundle);
        six = new Six6Fragment();
        six.setArguments(bundle);
        seven = new Six7Fragment();
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
                AlertDialog.Builder builder1 = new AlertDialog.Builder(SixPhotosActivity.this);
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