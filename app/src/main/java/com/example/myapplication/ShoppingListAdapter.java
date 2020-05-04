package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.MyViewHolder>{
    ArrayList<ShoppingListItem> mProductList;
    LayoutInflater inflater;

    public ShoppingListAdapter(Context context, ArrayList<ShoppingListItem> products) {
        inflater = LayoutInflater.from(context);
        this.mProductList = products;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.shopping_list_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ShoppingListItem selectedProduct = mProductList.get(position);
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
            mAmount = itemView.findViewById(R.id.amount);
        }

        public void setData(ShoppingListItem selectedProduct, int position) {

            this.mName.setText(selectedProduct.getName());
            this.mAmount.setText(selectedProduct.getAmount());
        }

        @Override
        public void onClick(View v) {

        }
    }
}
