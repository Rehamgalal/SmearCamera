package com.example.smearcamera.ui.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.Matrix;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.example.imagefilter.FilterPack;
import com.example.imagefilter.imageprocessors.Filter;
import com.example.imagefilter.utils.ThumbnailItem;
import com.example.imagefilter.utils.ThumbnailsManager;
import com.example.smearcamera.R;
import com.example.smearcamera.adapter.FiltersAdapter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FiltersFragment extends Fragment implements FiltersAdapter.ClickListener{

    private Uri imageUri;
    private RecyclerView imageLL;
    private ProgressBar progressBar;
    private List<ThumbnailItem> thumbnailItems;
    private List<Filter> filters;
    private Bitmap bitmap;
    private OnFragmentInteractionListener listener;

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
        if (getArguments() != null) {
           Bundle bundle = getArguments();
           imageUri = Uri.parse(bundle.getString("uri"));
        }
        handleImage();
        initData();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageLL =  view.findViewById(R.id.recycler_view);
        progressBar = view.findViewById(R.id.progress_bar);
    }

    private void handleImage() {
        try {
            if(Build.VERSION.SDK_INT < 28) {
                bitmap = MediaStore.Images.Media.getBitmap(
                        getContext().getContentResolver(),imageUri);
            } else {
                ImageDecoder.Source source = ImageDecoder.createSource(getContext().getContentResolver(), imageUri);
                bitmap = ImageDecoder.decodeBitmap(source);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_filters, container, false);
    }
    private void initData(){
        filters = FilterPack.getFilterPack(requireContext());
    }
    @Override
    public void onResume() {
        super.onResume();
        if (bitmap!=null) {
            boolean handler = new Handler(Looper.getMainLooper()).postDelayed(() -> {
                Point screenSize = getScreenSize(requireContext());
                Bitmap bitmap1  = getScaledDownBitmap(bitmap,screenSize.y,true);
                ThumbnailsManager.clearThumbs();
                filters = FilterPack.getFilterPack(requireContext());
                for (Filter filter: filters) {
                    ThumbnailItem thumbnailItem = new ThumbnailItem();
                    thumbnailItem.image = bitmap1;
                    thumbnailItem.filter = filter;
                    ThumbnailsManager.addThumb(thumbnailItem);
                }
                thumbnailItems = ThumbnailsManager.processThumbs(requireContext());
                ArrayList<Bitmap> bitmaps = new ArrayList<>();
                for (ThumbnailItem thumbnailItem: thumbnailItems) {
                    bitmaps.add(thumbnailItem.filter.processFilter(bitmap));
                }
                FiltersAdapter filtersAdapter = new FiltersAdapter(bitmaps, FiltersFragment.this);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false);
                imageLL.setLayoutManager(linearLayoutManager);
                imageLL.setHasFixedSize(true);
                imageLL.setAdapter(filtersAdapter);
                progressBar.setVisibility(View.GONE);
            },100);
        }
    }
    private Bitmap getScaledDownBitmap(
            Bitmap bitmap,
            int threshold,
            boolean isNecessaryToKeepOrig
    ) {
        int  width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int  newWidth = width;
        int newHeight = height;
        if (width > height && width > threshold) {
            newWidth = threshold;
            newHeight = (int) (height *(float) newWidth / width);
        }
        if (width > height && width <= threshold) {
            //the bitmap is already smaller than our required dimension, no need to resize it
            return bitmap;
        }
        if (width < height && height > threshold) {
            newHeight = threshold;
            newWidth = (int) (width * (float) newHeight / height);
        }
        if (width < height && height <= threshold) {
            //the bitmap is already smaller than our required dimension, no need to resize it
            return bitmap;
        }
        if (width == height && width > threshold) {
            newWidth = threshold;
            newHeight = newWidth;
        }
        if (width == height && width <= threshold) {
            //the bitmap is already smaller than our required dimension, no need to resize it
            return bitmap;
        } else {
            return getResizedBitmap(bitmap, newWidth, newHeight, isNecessaryToKeepOrig);
        }
    }

    private Bitmap getResizedBitmap(
            Bitmap bm,
            int newWidth,
            int newHeight,
            boolean isNecessaryToKeepOrig
    ) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight)/ height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap =
                Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        if (!isNecessaryToKeepOrig) {
            bm.recycle();
        }
        return resizedBitmap;
    }
    public static Point getScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    @Override
    public void onClickListener(Bitmap bitmap) {
        listener.onFragmentInteraction(bitmap);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Bitmap bitmap);

    }
}