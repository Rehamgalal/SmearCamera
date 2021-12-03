package com.example.smearcamera.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.example.smearcamera.data.DataSourceFactory;
import com.example.smearcamera.data.Image;
import com.example.smearcamera.data.ImagesDataSource;
import com.example.smearcamera.data.MediaRepository;
import com.example.smearcamera.utils.State;

import java.util.Objects;

import io.reactivex.disposables.CompositeDisposable;

public class GalleryViewModel extends AndroidViewModel {
    private final MediaRepository mediaRepository;
    public LiveData<PagedList<Image>> imagesList ;
    public GalleryViewModel(@NonNull Application application, MediaRepository mediaRepository) {
        super(application);
        this.mediaRepository = mediaRepository;
    }

    public void fetchData() {

        imagesList = mediaRepository.fetchMedia();
    }

    public LiveData<PagedList<Image>> getLiveData() {
        return imagesList;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}


