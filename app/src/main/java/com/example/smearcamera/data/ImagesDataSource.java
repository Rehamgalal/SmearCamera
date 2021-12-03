package com.example.smearcamera.data;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PositionalDataSource;

import com.example.smearcamera.utils.State;

import java.util.ArrayList;

public class ImagesDataSource extends PositionalDataSource<Image> {

    private final Context context;
    public static MutableLiveData<State> state;


    public ImagesDataSource(Context context) {
        this.context = context;
        state = new MutableLiveData<>();

    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams params, @NonNull LoadInitialCallback callback) {
        updateState(State.LOADING);
        callback.onResult(getImage(params.requestedLoadSize, params.requestedStartPosition),0);

    }


    @Override
    public void loadRange(@NonNull LoadRangeParams params, @NonNull LoadRangeCallback callback) {
        updateState(State.LOADING);
        callback.onResult(getImage(params.loadSize, params.startPosition));


    }
    private ArrayList<Image> getImage(int limit,int offset) {
        String[] projection = {
                MediaStore.Files.FileColumns._ID,
                MediaStore.MediaColumns.DISPLAY_NAME,
                MediaStore.Files.FileColumns.MEDIA_TYPE,
                MediaStore.MediaColumns.MIME_TYPE,
                MediaStore.Video.Media.DATA,
                MediaStore.MediaColumns.SIZE,
                "duration"
        };

// Return only video and image metadata.
        String selection =
                "(" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?" + " OR " + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?)" + " AND " + MediaStore.MediaColumns.SIZE + ">0";
        String[] SELECTION_ALL_ARGS = {
                String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE)};


        Uri queryUri = MediaStore.Files.getContentUri("external");


        Cursor cursor = context.getContentResolver().query(
                queryUri.buildUpon().encodedQuery("limit=" + offset + "," + limit).build(),
                projection,
                selection,
                SELECTION_ALL_ARGS,
                MediaStore.Files.FileColumns.DATE_ADDED + " DESC"
        );
        int image_column_index = cursor.getColumnIndex(MediaStore.Files.FileColumns._ID);

        ArrayList<Image> strings= new ArrayList<>();
        Image image;
        for (int i=0;i<cursor.getCount();i++) {
            cursor.moveToPosition(i);
            String imagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
            long id = cursor.getLong(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            String display_name = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME));


            Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            Uri uri = ContentUris.withAppendedId(contentUri, id);

            image = new Image(
                    id,
                    imagePath,
                    uri,
                    false,
                    0,
                    cursor.getLong(cursor.getColumnIndex(MediaStore.MediaColumns.SIZE)),
                    0,
                    0,
                    display_name
            );
            strings.add(image);


        }
        cursor.close();
        return strings;
    }


    private void updateState(State state) {
        this.state.postValue(state);
    }
}
