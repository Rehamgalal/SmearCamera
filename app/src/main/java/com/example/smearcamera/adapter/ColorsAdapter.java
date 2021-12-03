package com.example.smearcamera.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smearcamera.R;

import java.util.ArrayList;

public class ColorsAdapter extends RecyclerView.Adapter<ColorsAdapter.ColorsViewHolder>{

    private final ArrayList<Integer> colors;
    private ClickListener clickListener;
    private int selectedItem;

    public ColorsAdapter(ArrayList<Integer> colors, ClickListener clickListener) {
        this.colors = colors;
        this.clickListener = clickListener;
    }
    @NonNull
    @Override
    public ColorsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_color, parent, false);
        return new ColorsViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ColorsViewHolder holder, int position) {
        holder.imageView.setBackgroundResource(colors.get(position));
        if (selectedItem == position) {
            holder.root.setBackgroundColor(Color.parseColor("#22aa55"));
        } else {
            holder.root.setBackgroundColor(Color.parseColor("#00000000"));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onClickListener(colors.get(position));
                selectedItem = position;
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return colors.size();
    }

    static class ColorsViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ConstraintLayout root;
        public ColorsViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.color);
            root = itemView.findViewById(R.id.root);
        }
    }

    public interface ClickListener{
        void onClickListener(int color);
    }

}
