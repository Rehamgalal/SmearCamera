package com.example.smearcamera.ui.fragments.two;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.smearcamera.R;
import com.example.smearcamera.ui.BaseFragment;

public class Two5Fragment extends BaseFragment {

    private Uri uri1,uri2;
    private ImageView imageView1, imageView2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            uri1 = Uri.parse(getArguments().getString("uri1"));
            uri2 = Uri.parse(getArguments().getString("uri2"));
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_two5, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageView1 = view.findViewById(R.id.first);
        imageView2 = view.findViewById(R.id.second);
        Context context = requireContext();
        Glide.with(context).load(uri1).centerCrop().into(imageView1);
        Glide.with(context).load(uri2).centerCrop().into(imageView2);
    }
}