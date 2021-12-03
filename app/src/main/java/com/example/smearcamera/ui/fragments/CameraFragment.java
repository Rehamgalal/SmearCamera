package com.example.smearcamera.ui.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Rational;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.smearcamera.R;
import com.example.smearcamera.ui.GalleryActivity;
import com.example.smearcamera.ui.MainEditActivity;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class CameraFragment extends Fragment {

    private PreviewView textureView;
    private TextView one, four,sixteen;
    private ImageCapture imageCapture;
    private CameraSelector cameraSelector;
    private static final int PERMISSIONS_REQUEST_CODE = 10;
    private Rational aspectRation;

    private ExecutorService cameraExecutor;
    public static final String CAMERA_IMAGE_BUCKET_NAME = Environment.getExternalStorageDirectory().toString()
                    + "/DCIM/Camera";
    private OnFragmentInteractionListener listener;

    private boolean flash = false;
    private String[] PERMISSIONS_REQUIRED =
            {Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private File outputDirectory;

    boolean is_front = false;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            this.listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException("$context must implement OnFragmentInteractionListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        return inflater.inflate(R.layout.fragment_camera, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textureView = view.findViewById(R.id.textureView);
        ImageView imageView = view.findViewById(R.id.image_view);
        ImageView capture = view.findViewById(R.id.capture);
        ImageView switch_cam = view.findViewById(R.id.switch_cam);
        SwitchCompat flash_btn = view.findViewById(R.id.flash_btn);
        one = view.findViewById(R.id.one);
        four = view.findViewById(R.id.four);
        sixteen = view.findViewById(R.id.sixteen);
        aspectRation = RATIO_1_1_VALUE;
        Glide.with(requireContext()).load(getBucketId()).placeholder(R.drawable.album).into(imageView);
        one.setOnClickListener(v -> {
                one.setBackgroundResource(R.drawable.green_background);
                four.setBackgroundResource(R.drawable.gray_background);
                sixteen.setBackgroundResource(R.drawable.gray_background);
                aspectRation = RATIO_1_1_VALUE;
        });

        four.setOnClickListener(v -> {
            four.setBackgroundResource(R.drawable.green_background);
            one.setBackgroundResource(R.drawable.gray_background);
            sixteen.setBackgroundResource(R.drawable.gray_background);
            aspectRation = RATIO_4_3_VALUE;
            startCamera();
        });
        sixteen.setOnClickListener(v -> {
            sixteen.setBackgroundResource(R.drawable.green_background);
            four.setBackgroundResource(R.drawable.gray_background);
            one.setBackgroundResource(R.drawable.gray_background);
            aspectRation = RATIO_16_9_VALUE;
            startCamera();
        });
        if (!hasPermission(requireContext())) {
            // Request camera-related permissions
            requestPermissions(PERMISSIONS_REQUIRED, PERMISSIONS_REQUEST_CODE);

        } else {
            startCamera();
        }

        outputDirectory = getOutputDirectory(requireContext());
        cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;
        cameraExecutor = Executors.newSingleThreadExecutor();
        imageView.setOnClickListener(v -> {
          Intent intent = new Intent(getActivity(), GalleryActivity.class);
          startActivity(intent);
        });

        switch_cam.setOnClickListener(v -> {

            if (!is_front) {
                cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA;
            } else {
                cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;
            }
            startCamera();
            is_front = !is_front;
        });

        flash_btn.setOnCheckedChangeListener((buttonView, isChecked) -> flash = isChecked);

        capture.setOnClickListener (v -> takePhoto());

    }

    private void takePhoto() {

        if (flash) {
           imageCapture.setFlashMode(ImageCapture.FLASH_MODE_ON);
        } else {
            imageCapture.setFlashMode(ImageCapture.FLASH_MODE_OFF);
        }
        File photoFile = createFile(outputDirectory,FILENAME,PHOTO_EXTENSION);
        ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(photoFile).build();
        imageCapture.takePicture(outputFileOptions, ContextCompat.getMainExecutor(requireContext()), new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                Uri savedUri = outputFileResults.getSavedUri();
                Intent scanIntent =   new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(photoFile.getAbsoluteFile());
                scanIntent.setData(contentUri);
                getActivity().sendBroadcast(scanIntent);
                Intent intent = new Intent(getActivity(), MainEditActivity.class);
                intent.putExtra("uris",contentUri.toString());
                startActivity(intent);

            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                Toast.makeText(requireContext(),exception.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext());
        cameraProviderFuture.addListener(new Runnable() {
            @SuppressLint("RestrictedApi")
            @Override
            public void run() {
                try {
                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                    DisplayMetrics metrics = new  DisplayMetrics();
                    WindowManager windowManager = (WindowManager) textureView.getContext().getSystemService(Context.WINDOW_SERVICE);
                    windowManager.getDefaultDisplay().getRealMetrics(metrics);
                    Size screenSize = new Size(metrics.widthPixels, metrics.heightPixels);
                    Preview preview = new Preview.Builder().setTargetAspectRatioCustom(aspectRation).build();
                    preview.setSurfaceProvider(textureView.getPreviewSurfaceProvider());
                        imageCapture = new ImageCapture.Builder().setTargetAspectRatioCustom(aspectRation).setTargetResolution(screenSize).setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY).setFlashMode(ImageCapture.FLASH_MODE_ON).build();
                    cameraProvider.unbindAll();
                    cameraProvider.bindToLifecycle(getViewLifecycleOwner(),cameraSelector,preview,imageCapture);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },ContextCompat.getMainExecutor(requireContext()));
    }
    @Override
    public void onResume() {
        super.onResume();

    }


    private boolean hasPermission(Context context) {
        for (String i : PERMISSIONS_REQUIRED) {
        if (ContextCompat.checkSelfPermission(context,i) != PackageManager.PERMISSION_GRANTED){
            return false;}
       }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_CODE && grantResults.length != 0) {

            if (PackageManager.PERMISSION_GRANTED == grantResults[0]) {
                startCamera();
                outputDirectory = getOutputDirectory(requireContext());

            } else {
                Toast.makeText(requireContext(),
                        "Permissions not granted by the user.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

/**
 * Matches code in MediaProvider.computeBucketValues. Should be a common
 * function.
 */
        public  Uri getBucketId() {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    String[] projection = {
                            MediaStore.Images.ImageColumns._ID,
                            MediaStore.Images.Media._ID,
                            MediaStore.Images.ImageColumns.DATE_ADDED,
                            MediaStore.Images.ImageColumns.MIME_TYPE
                    };
                    Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

                    Cursor cursor = requireContext().getContentResolver().query(
                            contentUri,
                            projection,
                            null,
                            null,
                            MediaStore.Files.FileColumns.DATE_ADDED + " DESC"
                    );
                    if (cursor.moveToFirst()) {
                        int columnIndexID = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
                        long imageId = cursor.getLong(columnIndexID);
                        Uri imageURI = Uri.withAppendedPath(contentUri, "" + imageId);
                         return imageURI;}
                } else {
                    if(shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        Toast.makeText(requireContext(), "app needs to be able to access save", Toast.LENGTH_SHORT).show();
                    }

                    requestPermissions(PERMISSIONS_REQUIRED, PERMISSIONS_REQUEST_CODE);

                }
            } else {
                if (ContextCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(requireActivity(),
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            PERMISSIONS_REQUEST_CODE);
                } else {
                String selection =
                        "(" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?" + " OR " + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?)" + " AND " + MediaStore.MediaColumns.SIZE + ">0";
                String[] SELECTION_ALL_ARGS = {
                        String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE)};


                String[] projection = {
                        MediaStore.Files.FileColumns._ID,
                };
                Uri queryUri = MediaStore.Files.getContentUri("external");
                Cursor cursor = requireContext().getContentResolver().query(
                        queryUri.buildUpon().encodedQuery("limit=" + 0 + "," + 1).build(),
                        projection,
                        selection,
                        SELECTION_ALL_ARGS,
                        MediaStore.Files.FileColumns.DATE_ADDED + " DESC"
                );
                if (cursor.getCount()!=0){
                cursor.moveToPosition(0);
                String id = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID));
                Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                Uri uri = ContentUris.withAppendedId(contentUri, Integer.parseInt(id));

                return uri  ;}}
            }
            return null;


        }






    @Override
    public void onDestroyView() {
        super.onDestroyView();
        cameraExecutor.shutdown();
    }

    private File getOutputDirectory(Context context) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
                File myDir = new File(root +"/"+ requireContext().getString(R.string.directory_name));
                if(!myDir.exists()) {

                    myDir.mkdirs();}
                return myDir;
            } else {
                if(shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(requireContext(), "app needs to be able to access save", Toast.LENGTH_SHORT).show();
                }

                requestPermissions(PERMISSIONS_REQUIRED, PERMISSIONS_REQUEST_CODE);

            }
        } else {
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root +"/"+  requireContext().getString(R.string.directory_name));
            if(!myDir.exists()) {

                myDir.mkdirs();}

            return  myDir;

        }
       return null;
    }

        private static String TAG = "CameraXBasic";
        private static String FILENAME = "yyyyMMdd_HHmmss";
        private static String PHOTO_EXTENSION = ".jpg";
        private static Rational RATIO_1_1_VALUE = new Rational(1,1);
        private static Rational RATIO_4_3_VALUE = new Rational(4,3);
        private static Rational RATIO_16_9_VALUE = new Rational(16 ,9);

        private static File createFile (File baseFolder, String format, String extension) {
            String timeStamp = new SimpleDateFormat(format).format( new Date());
            String fname = "Shutta_"+ timeStamp + PHOTO_EXTENSION;

            File file = new File(baseFolder, fname);
            if (file.exists()) file.delete ();
            return file;
        }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(int color);

    }


}