package com.example.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {

    public static final String EXTRA_USER_ID = "com.example.myapplication.user_id";
    private static final int CAMERA_PERMISSION = 1000;
    private RequestQueue requestQueue;
    private User mUser;
    private int image_count_before;

    TextView mNameSurnameTV, mPointsTV;
    Button mUploadReceipt;
    ImageView mImageView;

    private static final int PIC_ID = 500;

    public HomeFragment() {
    }

    private RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getActivity());
        }
        return requestQueue;
    }

    public static HomeFragment newInstance(String uid) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_USER_ID, uid);

        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String uid = getArguments().getString(EXTRA_USER_ID);

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, Constants.API_URL + "/user/" + uid, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String name = response.getString("name");
                            String surname = response.getString("surname");
                            int points = response.getInt("points");
                            mUser = new User(name, surname, points);
                            mNameSurnameTV.setText(mUser.getName() + " " + mUser.getSurname());
                            mPointsTV.setText(String.valueOf(mUser.getPoints()));
                        } catch (Exception e) {

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences sp = getActivity().getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);
                String token = sp.getString("token", "");
                Log.d("HomeFragment", "getParams: " + token);
                params.put("Authorization", "Bearer " + token);
                return params;
            }
        };

        requestQueue = getRequestQueue();
        requestQueue.add(jsonRequest);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        mNameSurnameTV = v.findViewById(R.id.name_surname_text_view);
        mPointsTV = v.findViewById(R.id.points_text_view);
        mUploadReceipt = v.findViewById(R.id.upload_receipt_button);
        mImageView = v.findViewById(R.id.imageView2);

        mUploadReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                    String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
                    ActivityCompat.requestPermissions(getActivity(), permissions, CAMERA_PERMISSION);
                    return;
                }

                Cursor cursor = loadCursor();
                image_count_before = cursor.getCount();
                cursor.close();

                Intent i = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
                startActivityForResult(i, PIC_ID);
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PIC_ID) {
            exitingCamera();
        }
    }

    public Cursor loadCursor() {

        final String[] columns = { MediaStore.Images.Media.DATA,
                MediaStore.Images.Media._ID };

        final String orderBy = MediaStore.Images.Media.DATE_ADDED;

        return getActivity().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);
    }

    public String[] getImagePaths(Cursor cursor, int startPosition) {

        int size = cursor.getCount() - startPosition;

        if (size <= 0)
            return null;

        String[] paths = new String[size];

        int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);

        for (int i = startPosition; i < cursor.getCount(); i++) {

            cursor.moveToPosition(i);

            paths[i - startPosition] = cursor.getString(dataColumnIndex);
        }

        return paths;
    }

    private void exitingCamera() {

        Cursor cursor = loadCursor();
        String[] paths = getImagePaths(cursor, image_count_before);
        cursor.close();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(getActivity(), "Camera permission is necessary", Toast.LENGTH_SHORT).show();
            }
            if (grantResults[1] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(getActivity(), "Storage permission is necessary", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
