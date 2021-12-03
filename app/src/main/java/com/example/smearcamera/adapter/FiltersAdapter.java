package com.example.smearcamera.adapter;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imagefilter.imageprocessors.Filter;
import com.example.imagefilter.utils.ThumbnailItem;
import com.example.smearcamera.R;

import java.util.ArrayList;
import java.util.List;

public class FiltersAdapter extends RecyclerView.Adapter<FiltersAdapter.FilterViewHolder> {

    private final List<Bitmap> dataSet;
    private ClickListener clickListener;
    private int selectedItem;

    public FiltersAdapter(List<Bitmap> dataSet, ClickListener clickListener) {
        this.dataSet = dataSet;
        this.clickListener = clickListener;
    }
    @NonNull
    @Override
    public FilterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_filter, parent, false);
        return new FilterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterViewHolder holder, int position) {
        holder.imageView.setImageBitmap(dataSet.get(position));
        if (selectedItem == position) {
            holder.root.setBackgroundColor(Color.parseColor("#22aa55"));
        } else {
            holder.root.setBackgroundColor(Color.parseColor("#00000000"));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onClickListener(dataSet.get(position));
                selectedItem = position;
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    static class FilterViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        LinearLayout root;
        public FilterViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_filter);
            root = itemView.findViewById(R.id.root);
        }
    }


    public interface ClickListener{
        void onClickListener(Bitmap bitmap);
    }

}
