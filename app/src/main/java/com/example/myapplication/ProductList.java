package com.example.myapplication;

import java.util.ArrayList;

public class ProductList {
    private ArrayList<Product> mProducts;

    public ArrayList<Product> getProducts() {
        return mProducts;
    }

    public void addItem(Product product) {
        mProducts.add(product);
    }
}
