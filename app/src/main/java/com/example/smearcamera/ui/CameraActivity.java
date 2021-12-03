package com.example.smearcamera.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.imagefilter.imageprocessors.Filter;
import com.example.smearcamera.R;
import com.example.smearcamera.ui.fragments.CameraFragment;

public class CameraActivity extends AppCompatActivity implements CameraFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        getSupportFragmentManager().beginTransaction().add(R.id.container_view,new CameraFragment()).commit();
    }
    @Override
    public void onFragmentInteraction(int color) {

    }
}