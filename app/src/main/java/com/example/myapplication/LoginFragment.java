package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;

public class LoginFragment extends Fragment {
    EditText etEmail;
    EditText etPassword;
    Button btnLogin;
    TextView tvLog;

    SharedPreferences sharedPreferences;
    RequestQueue requestQueue;

    public LoginFragment(){}

    private RequestQueue getRequestQueue(){
        if(requestQueue==null){
            requestQueue = Volley.newRequestQueue(getActivity());
        }
        return requestQueue;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        etEmail = v.findViewById(R.id.emailInput);
        etPassword = v.findViewById(R.id.passwordInput);
        btnLogin = v.findViewById(R.id.loginButton);
        tvLog = v.findViewById(R.id.textView);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if(email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getActivity(), "Fill the credentials", Toast.LENGTH_SHORT).show();
                    return;
                }

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("email", email);
                    jsonObject.put("password", password);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Constants.API_URL+"/login", jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String uid = response.getString("user_id");
                            String token = response.getString("token");
                            sharedPreferences = getActivity().getSharedPreferences(Constants.SHARED_PREFERENCES, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("token", token);
                            editor.apply();

                            Intent i = new Intent(getActivity(), HomeActivity.class);
                            i.putExtra(HomeFragment.EXTRA_USER_ID, uid);
                            startActivity(i);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            String responseBody = new String(error.networkResponse.data, "utf-8");
                            JSONObject data = new JSONObject(responseBody);
                            String errorMessage = data.getString("message");
                            Toast.makeText(getActivity(), "Error:  " + errorMessage, Toast.LENGTH_SHORT).show();
                        } catch (Exception e){

                        }
                    }
                });

                RequestQueue rq = getRequestQueue();
                rq.add(jsonRequest);
            }
        });

        return v;
    }
}
