package com.example.lottogo.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lottogo.R;
import com.example.lottogo.adapter.GalleryAdapter;

import java.util.ArrayList;

public class GalleryActivity extends BasicActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        if (ContextCompat.checkSelfPermission(GalleryActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(GalleryActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
            if (ActivityCompat.shouldShowRequestPermissionRationale(GalleryActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {
                startToast("권한을 허용해 주세요");
            }
        } else {
            recyclerInit();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    recyclerInit();
                } else {
                    finish();
                    startToast("권한을 허용해 주세요");
                }
            }
        }
    }

    private void recyclerInit(){
        final int numberOfColumns = 3;

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));

        RecyclerView.Adapter mAdapter = new GalleryAdapter(this, getImagesPath(this));
        recyclerView.setAdapter(mAdapter);
    }

    public ArrayList<String> getImagesPath(Activity activity) {
        Uri uri;
        ArrayList<String> listOfAllImages = new ArrayList<String>();
        Cursor cursor;
        int column_index_data;
        String PathOfImage = null;
        String[] projection;

        Intent intent = getIntent();
        if(intent.getStringExtra("media").equals("video")){
            uri = android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            projection = new String[] { MediaStore.MediaColumns.DATA, MediaStore.Video.Media.BUCKET_DISPLAY_NAME };
        }else{
            uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            projection = new String[] { MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME };
        }

        cursor = activity.getContentResolver().query(uri, projection, null, null, null);
        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);

        while (cursor.moveToNext()) {
            PathOfImage = cursor.getString(column_index_data);

            listOfAllImages.add(PathOfImage);
        }
        return listOfAllImages;
    }

    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
