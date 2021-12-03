package com.example.smearcamera.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.smearcamera.R;
import com.example.smearcamera.data.Image;
import com.example.smearcamera.utils.State;

public class PagedListAdapter extends androidx.paging.PagedListAdapter<Image, PagedListAdapter.ImagesViewHolder> {

    private Context context;
    private OnItemClickListener onItemClickListener;
    private ClickListener onClick;
    private int selectionCount=0;
    private Drawable background;
    private Drawable transBackground;
    public PagedListAdapter(@NonNull ImagesDiffCallBack diffCallback
            , Context context
            ,OnItemClickListener onItemClickListener
            ,ClickListener clickListener
            ) {
        super(diffCallback);
        this.context= context;
        this.onItemClickListener = onItemClickListener;
        this.onClick = clickListener;
        this.background = AppCompatResources.getDrawable(context,R.drawable.selected_corner);
        this.transBackground = AppCompatResources.getDrawable(context,R.drawable.rounded_corner);
    }

    public static class ImagesDiffCallBack extends DiffUtil.ItemCallback<Image> {

        @Override
        public boolean areItemsTheSame(@NonNull Image oldItem, @NonNull Image newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Image oldItem, @NonNull Image newItem) {
            return oldItem.getImagePath().equals(newItem.getImagePath());
        }
    }
    @NonNull
    @Override
    public PagedListAdapter.ImagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image, parent, false);
        GridLayoutManager.LayoutParams lp = (GridLayoutManager.LayoutParams) view.getLayoutParams();
        lp.width = (int) (parent.getMeasuredWidth() / 2.05);
        lp.height = (int) (parent.getMeasuredHeight() / 4.05);
        view.setLayoutParams(lp);
        return new ImagesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PagedListAdapter.ImagesViewHolder holder, int position) {
        Image image = getItem(position);
        Glide.with(holder.itemView.getContext()).load(image.getMediaThumbnail()).centerCrop().into(holder.imageView);
        if (image.isChecked()) {
            holder.countText.setVisibility(View.VISIBLE);
            holder.countText.setText(String.valueOf(image.getSelectionNum()));
            holder.checkView.setBackground(background);
        } else {
            holder.countText.setVisibility(View.GONE);
            holder.checkView.setBackground(transBackground);
        }

        holder.setOnCheckChangedListener((View.OnLongClickListener) v -> {
            if (image.isChecked() && selectionCount>0) {
                selectionCount --;
                image.setChecked(false);
                holder.countText.setVisibility(View.GONE);
                onItemClickListener.onItemUnCheck(image, position);
                image.setSelectionNum(0);
                holder.checkView.setBackground(transBackground);
            }
            else if (!image.isChecked() && selectionCount < 6) {
                selectionCount++;
                image.setChecked(true);
                holder.countText.setVisibility(View.VISIBLE);
                holder.countText.setText(String.valueOf(selectionCount));
                image.setSelectionNum(selectionCount);
                onItemClickListener.onItemCheck(image,position);
                holder.checkView.setBackground(background);
            } else {
                Toast.makeText(
                        context,
                        "maximum limit is 6 photos and videos",
                        Toast.LENGTH_SHORT
                ).show();

            }
        return true;
        });

        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onItemCheck(image,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }
    static class ImagesViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageView;
        public TextView countText;
        public LinearLayout checkView;
        public ImagesViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            countText = itemView.findViewById(R.id.count_text);
            checkView = itemView.findViewById(R.id.check_view);
        }
        public void setOnCheckChangedListener(View.OnLongClickListener checkListener) {
            itemView.setOnLongClickListener(checkListener);
        }
        public void setOnClickListener(View.OnClickListener clickListener) {
            itemView.setOnClickListener(clickListener);
        }
    }

    public interface OnItemClickListener {
        void onItemCheck(Image item, int position);
        void onItemUnCheck(Image item, int position);
    }

    public interface ClickListener {
        void onItemCheck(Image item,int position);
    }
}
