package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.MyViewHolder>{
    ArrayList<Product> mProductList;
    LayoutInflater inflater;

    public ProductListAdapter(Context context, ArrayList<Product> products) {
        inflater = LayoutInflater.from(context);
        this.mProductList = products;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.product_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Product selectedProduct = mProductList.get(position);
        holder.setData(selectedProduct, position);

    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mName, mAmount;

        public MyViewHolder(View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.name);
            mAmount = itemView.findViewById(R.id.price);
        }

        public void setData(Product selectedProduct, int position) {

            this.mName.setText(selectedProduct.getName());
            this.mAmount.setText(selectedProduct.price);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
