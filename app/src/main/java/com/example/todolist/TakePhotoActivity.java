package com.example.todolist;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.todolist.data.ImageDB;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class TakePhotoActivity extends AppCompatActivity {
    private static final int REQUEST_CAMERA_PERMISSION = 69;
    private static final int REQUEST_EXTERNAL_STORAGE = 34;
    private static final int REQUEST_EXTERNAL_READ = 17;

    public static final String RETURN_INTENT = "IMAGE_RETURNED";

    private ImageCapture imgCapture;
    private Button photoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);


        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }
        else
        {
            Log.d("photo", "camera permission granted");
            StartCamera();
        }

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_EXTERNAL_STORAGE);
        }
        else
        {
            Log.d("photo", "external storage permission granted");
        }

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_EXTERNAL_READ);
        }
        else
        {
            Log.d("photo", "external storage permission granted");
        }

        photoButton = findViewById(R.id.photo_button);
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TakePhoto();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CAMERA_PERMISSION) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                StartCamera();
            }
            else
            {
                Toast.makeText(this, R.string.camera_perms_denied, Toast.LENGTH_SHORT).show();
            }
        }
    }

    void StartCamera()
    {
        ListenableFuture providerFuture = ProcessCameraProvider.getInstance(this);
        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(((PreviewView)findViewById(R.id.viewFinder)).getSurfaceProvider());
        ProcessCameraProvider provider;
        try{
            provider = (ProcessCameraProvider) providerFuture.get();
        }
        catch(Exception e)
        {
            Log.e("photo", "KURWA", e);
            return;
        }
        CameraSelector selector = CameraSelector.DEFAULT_BACK_CAMERA;
        imgCapture = new ImageCapture.Builder().build();

        provider.unbindAll();
        provider.bindToLifecycle(this, selector, preview, imgCapture);

    }

    void TakePhoto()
    {
        if(imgCapture == null)
        {
            Log.d("photo", "imgCapture has not been initialised");
            return;
        }

        String name = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US).format(System.currentTimeMillis());
        File dir = new File(Environment.getExternalStorageDirectory(), "SaveImage");
        if(!dir.exists())
        {
            dir.mkdir();
        }
        File img = new File(dir, name+".jpeg");

        ImageCapture.OutputFileOptions options = new ImageCapture.OutputFileOptions.Builder(img).build();


        imgCapture.takePicture(options, ContextCompat.getMainExecutor(this), new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                Log.d("photo", outputFileResults.getSavedUri().toString());
                Bitmap bmp = BitmapFactory.decodeFile(outputFileResults.getSavedUri().getPath());
                Matrix correction = new Matrix();
                correction.postRotate(90);
                Bitmap corrected = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), correction, true);
                ImageDB.setImage(corrected, outputFileResults.getSavedUri().getPath());
                Intent returnIntent = new Intent();
                returnIntent.putExtra(RETURN_INTENT, outputFileResults.getSavedUri());
                setResult(RESULT_OK, new Intent());
                finish();
            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                Log.e("photo", "kurwa", exception);
                Toast.makeText(getBaseContext(), "Failed to save photo", Toast.LENGTH_LONG);
            }
        });
    }

}