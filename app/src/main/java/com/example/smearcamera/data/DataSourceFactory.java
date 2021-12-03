package com.example.smearcamera.data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

public class DataSourceFactory extends DataSource.Factory<Integer,Image>{
    private final Context context;
    public DataSourceFactory(Context context) {
        this.context = context;
    }
    @NonNull
    @Override
    public DataSource<Integer,Image> create() {
        return new ImagesDataSource(context);
    }
}
