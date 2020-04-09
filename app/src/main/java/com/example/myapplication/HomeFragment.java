package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


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
    private RequestQueue requestQueue;
    private User mUser;

    TextView mNameSurnameTV, mPointsTV;
    Button mUploadReceipt;

    public HomeFragment() {}

    private RequestQueue getRequestQueue(){
        if(requestQueue==null){
            requestQueue = Volley.newRequestQueue(getActivity());
        }
        return requestQueue;
    }

    public static HomeFragment newInstance(String uid){
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

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, Constants.API_URL+"/user/"+uid, null,
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
            }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                SharedPreferences sp = getActivity().getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);
                String token = sp.getString("token", "");
                Log.d("HomeFragment", "getParams: "+token);
                params.put("Authorization", "Bearer "+token);
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

        mUploadReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), CameraActivity.class);
                startActivity(i);
            }
        });

        return v;
    }

}
