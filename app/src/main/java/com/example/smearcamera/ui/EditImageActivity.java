package com.example.smearcamera.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import com.example.smearcamera.R;

public class EditImageActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int REQUEST_PICK_IMAGE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent,REQUEST_PICK_IMAGE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Uri data1 = data.getData();
            Intent intent = new Intent(EditImageActivity.this,MainEditActivity.class);
            intent.putExtra("uri",data1.toString());
            startActivity(intent);
        }else {
            finish();
        }
    }





    private void saveImage() {
        String fileName = System.currentTimeMillis() + ".jpg";
     //   mGPUImageView.saveToPictures("GPUImage", fileName, (GPUImageView.OnPictureSavedListener) this);
    }

    @Override
    public void onClick(View v) {

    }
}