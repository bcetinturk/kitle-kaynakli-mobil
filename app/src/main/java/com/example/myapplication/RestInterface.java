package com.example.myapplication;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RestInterface {
    @Multipart
    @POST("new-receipt")
    Call<ReceiptResults> getReceiptResults(@Part MultipartBody.Part file);
}
