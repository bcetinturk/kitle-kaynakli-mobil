package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etUserEmail;
    EditText etUserPassword;
    EditText etUSerPasswordConfirm;
    Button registerButton;

    RequestQueue requestQueue;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUserEmail = findViewById(R.id.userRegisterEmail);
        etUserPassword = findViewById(R.id.userRegisterPassword);
        etUSerPasswordConfirm = findViewById(R.id.userRegisterPasswordConfirm);
        registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(this);
    }

    private RequestQueue getRequestQueue(){
        if(requestQueue==null){
            requestQueue = Volley.newRequestQueue(this);
        }
        return requestQueue;
    }

    @Override
    public void onClick(View v) {
        String email = etUserEmail.getText().toString();
        String password = etUserPassword.getText().toString();
        String password2 = etUSerPasswordConfirm.getText().toString();

        if(email.isEmpty() || password.isEmpty() || password2.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Fill the credentials", Toast.LENGTH_SHORT).show();
            return;
        }

        if(password.compareTo(password2) != 0){
            Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", email);
            jsonObject.put("password", password);
            jsonObject.put("password2", password2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, "http://172.17.32.107:8080/register", jsonObject, new Response.Listener<JSONObject>() {
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
