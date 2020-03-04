package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    // Components
    EditText etEmail;
    EditText etPassword;
    Button btnLogin;
    TextView tvLog;

    RequestQueue requestQueue;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etEmail = findViewById(R.id.emailInput);
        etPassword = findViewById(R.id.passwordInput);
        btnLogin = findViewById(R.id.loginButton);
        tvLog = findViewById(R.id.textView);

        btnLogin.setOnClickListener(this);
    }

    private RequestQueue getRequestQueue(){
        if(requestQueue==null){
            requestQueue = Volley.newRequestQueue(this);
        }
        return requestQueue;
    }

    @Override
    public void onClick(View v) {

        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if(email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Fill the credentials", Toast.LENGTH_SHORT).show();
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
                    String token = response.getString("token");
                    sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("token", token);
                    editor.apply();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error:  " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue rq = getRequestQueue();
        rq.add(jsonRequest);
    }
}
