package com.example.smearcamera.ui.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.imagefilter.imageprocessors.Filter;
import com.example.smearcamera.R;
import com.example.smearcamera.adapter.ColorsAdapter;
import com.example.smearcamera.adapter.FiltersAdapter;
import com.example.smearcamera.utils.CustomTextView;

import java.util.ArrayList;

public class BrushFragment extends Fragment implements ColorsAdapter.ClickListener, View.OnClickListener {

    private OnFragmentInteractionListener listener;
    private ImageView small, medium, large, circle, rectangle;
    private CustomTextView triangle;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            this.listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException("$context must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_brush, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        small = view.findViewById(R.id.small);
        medium = view.findViewById(R.id.medium);
        large = view.findViewById(R.id.large);
        circle = view.findViewById(R.id.circle);
        rectangle = view.findViewById(R.id.rectangle);
        triangle = view.findViewById(R.id.traingle);
        small.setOnClickListener(this);
        medium.setOnClickListener(this);
        large.setOnClickListener(this);
        circle.setOnClickListener(this);
        rectangle.setOnClickListener(this);
        triangle.setOnClickListener(this);
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(R.color.white);
        colors.add(R.color.red);
        colors.add(R.color.blue);
        colors.add(R.color.green);
        colors.add(R.color.yellow);
        colors.add(R.color.orange);
        colors.add(R.color.purple);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        ColorsAdapter colorsAdapter = new ColorsAdapter(colors,this::onClickListener);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(false);
        recyclerView.setAdapter(colorsAdapter);
    }

    @Override
    public void onClickListener(int color) {
        listener.onFragmentInteraction(color);
    }

    @Override
    public void onClick(View v) {
        if (v == small) {
            listener.onFragmentInteraction(15);
            small.setBackgroundResource(R.drawable.white_selected_drawable);
            medium.setBackgroundResource(R.drawable.white_drawable);
            large.setBackgroundResource(R.drawable.white_drawable);
        } else if (v == medium) {
            listener.onFragmentInteraction(20);
            small.setBackgroundResource(R.drawable.white_drawable);
            medium.setBackgroundResource(R.drawable.white_selected_drawable);
            large.setBackgroundResource(R.drawable.white_drawable);
        } else if (v == large) {
            listener.onFragmentInteraction(25);
            small.setBackgroundResource(R.drawable.white_drawable);
            medium.setBackgroundResource(R.drawable.white_drawable);
            large.setBackgroundResource(R.drawable.white_selected_drawable);
        } else if (v == circle) {
            listener.onFragmentInteraction(1);
            circle.setBackgroundResource(R.drawable.white_selected_drawable);
            rectangle.setBackgroundResource(R.drawable.white_rect_unselected);
            triangle.setStroke(false);
        } else if (v == rectangle) {
            listener.onFragmentInteraction(2);
            circle.setBackgroundResource(R.drawable.white_drawable);
            rectangle.setBackgroundResource(R.drawable.white_rect_selected);
            triangle.setStroke(false);
        } else if (v == triangle) {
            listener.onFragmentInteraction(3);
            circle.setBackgroundResource(R.drawable.white_drawable);
            rectangle.setBackgroundResource(R.drawable.white_rect_unselected);
            triangle.setStroke(true);
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(int value);

    }
}