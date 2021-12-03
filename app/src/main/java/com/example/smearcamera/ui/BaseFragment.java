package com.example.smearcamera.ui;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.smearcamera.R;
import com.example.smearcamera.utils.LoadBitmap;


public class BaseFragment extends Fragment {
    private ConstraintLayout mainView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_base, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainView = view.findViewById(R.id.main_view);
    }

    public Bitmap loadBitmap() {
        return LoadBitmap.loadBitmapFromView(mainView);
    }
}
