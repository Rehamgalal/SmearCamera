package com.example.smearcamera.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.smearcamera.R;
import com.example.smearcamera.adapter.PagedListAdapter;
import com.example.smearcamera.data.Image;
import com.example.smearcamera.data.MediaRepository;
import com.example.smearcamera.ui.viewmodel.GalleryViewModel;
import com.example.smearcamera.ui.viewmodel.MyViewModelFactory;
import com.example.smearcamera.utils.State;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private GalleryViewModel galleryViewModel;
    private PagedListAdapter pagedListAdapter;
    private MediaRepository mediaRepository;
    private ImageView collection, delete;
    private ArrayList<Image> mediaList;
    private ArrayList<String> positios;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        init();
        assign();
    }

    private void init(){
        recyclerView = findViewById(R.id.recycler_view);
        collection = findViewById(R.id.collection);
        delete = findViewById(R.id.delete);
        delete.setOnClickListener(v -> {
            for (Image image : mediaList) {
                    deleteImage(image.getImagePath());
            }
            mediaList.clear();
            positios.clear();
            initAdapter();

        });
        collection.setOnClickListener(v -> {
            Intent intent;
            ArrayList<String> uris = new ArrayList<>();
            for (Image image: mediaList){
                uris.add(image.getMediaThumbnail().toString());
            }
            switch (mediaList.size()) {
                case 2:
                    intent = new Intent(GalleryActivity.this, TwoPhotosActivity.class);
                    intent.putExtra("uris",uris);
                    startActivity(intent);
                    break;
                case 3:
                    intent = new Intent(GalleryActivity.this, ThreePhotosActivity.class);
                    intent.putExtra("uris",uris);
                    startActivity(intent);
                    break;
                case 4:

                    intent = new Intent(GalleryActivity.this, FourPhotosActivity.class);
                    intent.putExtra("uris",uris);
                    startActivity(intent);
                    break;
                case 5:
                    intent = new Intent(GalleryActivity.this, FivePhotosActivity.class);
                    intent.putExtra("uris",uris);
                    startActivity(intent);
                    break;
                case 6:
                    intent = new Intent(GalleryActivity.this, SixPhotosActivity.class);
                    intent.putExtra("uris",uris);
                    startActivity(intent);
                    break;
                default:
                    Toast.makeText(GalleryActivity.this, "minimum number of selection is 0, maximum is 6",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void assign() {
        mediaRepository = new MediaRepository(this);
        galleryViewModel = new ViewModelProvider(this,new MyViewModelFactory(getApplication(),mediaRepository)).get(GalleryViewModel.class);
        mediaList = new ArrayList<>();
        positios = new ArrayList<>();
        initAdapter();
    }

    private void initAdapter() {
        pagedListAdapter = new PagedListAdapter(new PagedListAdapter.ImagesDiffCallBack(), this, new PagedListAdapter.OnItemClickListener() {
            @Override
            public void onItemCheck(Image item, int position) {
                mediaList.add(item);
                positios.add(String.valueOf(position));
                if (mediaList.size() == 0) {

                } else {

                }
                if (mediaList.size() == 0) {
                    delete.setImageResource(R.drawable.icon_delete1);
                    collection.setImageResource(R.drawable.icon_jigsaw1);
                    collection.setClickable(false);
                    delete.setClickable(false);
                } else if (mediaList.size() == 1 || mediaList.size() > 6){
                    delete.setImageResource(R.drawable.icon_delete2);
                    collection.setImageResource(R.drawable.icon_jigsaw1);
                    collection.setClickable(false);
                    delete.setClickable(true);
                } else {
                    delete.setImageResource(R.drawable.icon_delete2);
                    collection.setImageResource(R.drawable.icon_jigsaw2);
                    collection.setClickable(true);
                    delete.setClickable(true);
                }
            }


            @Override
            public void onItemUnCheck(Image item, int position) {
                for (int i = 0; i < mediaList.size(); i++) {
                    if (mediaList.get(i).getSelectionNum() > item.getSelectionNum()) {
                        mediaList.get(i).setSelectionNum(mediaList.get(i).getSelectionNum() - 1);
                        pagedListAdapter.notifyItemChanged(Integer.parseInt(positios.get(i)));
                    }
                }
                positios.remove(String.valueOf(position));
                mediaList.remove(item);
                if (mediaList.size() == 0) {
                    delete.setImageResource(R.drawable.icon_delete1);
                    collection.setImageResource(R.drawable.icon_jigsaw1);
                    collection.setClickable(false);
                    delete.setClickable(false);
                } else if (mediaList.size() == 1 || mediaList.size() > 6){
                    delete.setImageResource(R.drawable.icon_delete2);
                    collection.setImageResource(R.drawable.icon_jigsaw1);
                    collection.setClickable(false);
                    delete.setClickable(true);
                } else {
                    delete.setImageResource(R.drawable.icon_delete2);
                    collection.setImageResource(R.drawable.icon_jigsaw2);
                    collection.setClickable(true);
                    delete.setClickable(true);
                }
            }
        },
                (item, position) -> {

                    Intent intent = new Intent(GalleryActivity.this, MainEditActivity.class);
                    intent.putExtra("uris",item.getMediaThumbnail().toString());
                    startActivity(intent);

                });
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setAdapter(pagedListAdapter);
        galleryViewModel.fetchData();
        galleryViewModel.imagesList.observe(this, new Observer<PagedList<Image>>() {
            @Override
            public void onChanged(PagedList<Image> images) {
                pagedListAdapter.submitList(images);
            }
        }
        );
    }

    private void deleteImage(String file) {
        final String where = MediaStore.MediaColumns.DATA + "=?";
        final String[] selectionArgs = new String[] {
                file
        };
        final ContentResolver contentResolver = getContentResolver();
        final Uri filesUri = MediaStore.Files.getContentUri("external");
        contentResolver.delete(filesUri, where, selectionArgs);
            contentResolver.delete(filesUri, where, selectionArgs);
    }
}