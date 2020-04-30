package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class CameraActivity extends FragmentActivity {

    private final String TAG = "CameraActivity";
    private static final int CAMERA_PERMISSION = 1000;
    private static final int PIC_ID = 500;
    private int image_count_before;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkPermissions();
    }

    private void checkPermissions(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(this, permissions, CAMERA_PERMISSION);
        } else {
            startCameraIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, "Camera permission is necessary", Toast.LENGTH_SHORT).show();
            }
            if (grantResults[1] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, "Storage permission is necessary", Toast.LENGTH_SHORT).show();
            }
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                startCameraIntent();
            }
        }
    }

    private void startCameraIntent(){
        Cursor cursor = loadCursor();
        image_count_before = cursor.getCount();
        cursor.close();

        Intent i = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
        startActivityForResult(i, PIC_ID);
    }

    private Cursor loadCursor() {
        final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID };

        final String orderBy = MediaStore.Images.Media.DATE_ADDED;

        return getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PIC_ID) {
            exitingCamera();
        }
    }

    private void exitingCamera() {
        Cursor cursor = loadCursor();
        String[] paths = getImagePaths(cursor, image_count_before);
        cursor.close();
        if(paths != null) {
            uploadReceipt(paths);
        } else {
            Toast.makeText(this, "No photo was taken", Toast.LENGTH_SHORT).show();
        }

    }

    private String[] getImagePaths(Cursor cursor, int startPosition) {

        int size = cursor.getCount() - startPosition;
        if (size <= 0)
            return null;

        String[] paths = new String[size];
        int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);

        for (int i = startPosition; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            paths[i - startPosition] = cursor.getString(dataColumnIndex);
            Log.d(TAG, "getImagePaths: "+paths[i - startPosition]);
        }

        return paths;
    }

    private void uploadReceipt(String[] paths){
        RestInterface service = Client.getClient().create(RestInterface.class);

        File file = new File(paths[0]);
        RequestBody requestBody = RequestBody.create(file, MediaType.get("image/*"));
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestBody);
        Call<ReceiptResults> call = service.getReceiptResults(body);

        call.enqueue(new Callback<ReceiptResults>() {
            @Override
            public void onResponse(Call<ReceiptResults> call, retrofit2.Response<ReceiptResults> response) {
                Toast.makeText(CameraActivity.this, "Done", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ReceiptResults> call, Throwable t) {
                Toast.makeText(CameraActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
    }
}
