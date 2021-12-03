package com.example.smearcamera.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.smearcamera.data.MediaRepository;

public class MyViewModelFactory implements ViewModelProvider.Factory {
    private Application application;
    private MediaRepository mediaRepository;
    public MyViewModelFactory(Application application, MediaRepository mediaRepository) {
        this.application = application;
        this.mediaRepository = mediaRepository;
    }
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new GalleryViewModel(application,mediaRepository);
    }
}
