package com.example.myapplication;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReceiptResults {
    @SerializedName("market_name")
    @Expose
    public String marketName;

    @SerializedName("market_address")
    @Expose
    public String marketAddress;

    @SerializedName("products")
    @Expose
    public List<Product> products;

}
