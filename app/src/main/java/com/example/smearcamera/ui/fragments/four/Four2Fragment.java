package com.example.smearcamera.ui.fragments.four;

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


public class Four2Fragment extends BaseFragment {

    private Uri uri1, uri2,uri3,uri4;
    private ImageView imageView1, imageView2, imageView3,imageView4;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            uri1 = Uri.parse(getArguments().getString("uri1"));
            uri2 = Uri.parse(getArguments().getString("uri2"));
            uri3 = Uri.parse(getArguments().getString("uri3"));
            uri4 = Uri.parse(getArguments().getString("uri4"));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_four2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageView1 = view.findViewById(R.id.first);
        imageView2 = view.findViewById(R.id.second);
        imageView3 = view.findViewById(R.id.third);
        imageView4 = view.findViewById(R.id.fourth);
        Context context = requireContext();
        Glide.with(context).load(uri1).centerCrop().into(imageView1);
        Glide.with(context).load(uri2).centerCrop().into(imageView2);
        Glide.with(context).load(uri3).centerCrop().into(imageView3);
        Glide.with(context).load(uri4).centerCrop().into(imageView4);
    }
}