package com.example.homework_8;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;

public class    MainActivity extends AppCompatActivity
{
    TextView textView;
    ImageView imageView;
    Button buttonTakePhoto;

    private Bitmap imageBitMap;
    private String currentPath;
    private GeminiManager geminiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        imageView = findViewById(R.id.imageView);
        buttonTakePhoto = findViewById(R.id.buttonTakePhoto);

        textView.setMovementMethod(new ScrollingMovementMethod());
        geminiManager = GeminiManager.getInstance();

        buttonTakePhoto.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v)
            {
                requestPermissions();
            }
        });

        Log.d("API_KEY", "The API key is: " + BuildConfig.GeminiAPIKey);
    }

    private void requestPermissions()
    {
        String[] permissions = {android.Manifest.permission.CAMERA};

        if(ContextCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED)
        {
            takePhoto();
        }
        else
        {
            ActivityCompat.requestPermissions(this, permissions, 101);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 101)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                takePhoto();
            }
            else
            {
                Toast.makeText(this, "The camera permission is required to take a photo", Toast.LENGTH_SHORT).show();
            }

        }

    }

    public void takePhoto()
    {
        String fileName = "photo";
        File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try
        {
            File image = File.createTempFile(fileName, ".jpg", storageDirectory);
            currentPath = image.getAbsolutePath();

            Uri imageUri = FileProvider.getUriForFile(this, "com.example.homework_8.fileProvider", image);

            Intent intent = new Intent();
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            if(intent.resolveActivity(getPackageManager()) != null)
            {
                startActivityForResult(intent, 202);
            }
            else
            {
                Toast.makeText(this, "No Camera application was found", Toast.LENGTH_SHORT).show();
            }
        }
        catch (IOException e)
        {
            Toast.makeText(MainActivity.this, "Something went wrong with creating the file", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if((resultCode == Activity.RESULT_OK) && requestCode == 202)
        {
            imageBitMap = BitmapFactory.decodeFile(currentPath);
            imageView.setImageBitmap(imageBitMap);

            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Photo taken..");
            progressDialog.setMessage("Finishing the upload to receive the response..");
            progressDialog.setCancelable(false);
            progressDialog.show();

            geminiManager.sendPhotoPrompt(PhotoPrompts.PhotoPrompt, imageBitMap, new GeminiCallBack(){
                @Override
                public void onSuccess(String responseGiven)
                {
                    textView.setText(responseGiven);
                    progressDialog.dismiss();
                }
                @Override
                public void onFailure(Throwable error)
                {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Something went wrong with generating the response", Toast.LENGTH_SHORT).show();
                }

            });
        }
    }
}