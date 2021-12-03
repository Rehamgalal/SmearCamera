package com.example.smearcamera.data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.example.smearcamera.utils.State;

import java.util.function.Function;

public class MediaRepository {

    private final Context context;
    public MediaRepository(Context context) {
        this.context = context;
    }
    public LiveData<PagedList<Image>> mediaPagedList;
    private DataSourceFactory dataSourceFactory;
    public LiveData<PagedList<Image>> fetchMedia() {
        dataSourceFactory = new DataSourceFactory(context);
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(10)
                .setInitialLoadSizeHint(10)
                .build();
        mediaPagedList = new LivePagedListBuilder(dataSourceFactory,config).build();

        return mediaPagedList;
    }


    }

