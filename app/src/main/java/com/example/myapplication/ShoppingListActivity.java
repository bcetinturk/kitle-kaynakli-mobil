package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class ShoppingListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    EditText mProductName, mProductQuantity;
    Button mAddToList;
    ShoppingList mShoppingList;
    ShoppingListAdapter mShoppingListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_shopping_list);
        mShoppingList = ShoppingList.getInstance();

        recyclerView = findViewById(R.id.recycler_view);
        mProductName = findViewById(R.id.product_name);
        mProductQuantity = findViewById(R.id.product_quantity);
        mAddToList = findViewById(R.id.add_to_list);

        mAddToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productName = mProductName.getText().toString().trim();
                String productQuantity = mProductQuantity.getText().toString().trim();

                if(productName.isEmpty() || productQuantity.isEmpty()) {
                    return;
                }

                ShoppingListItem item = new ShoppingListItem(productName, productQuantity);
                mShoppingList.addItem(item);
                mShoppingListAdapter.notifyDataSetChanged();
                mProductName.getText().clear();
                mProductQuantity.getText().clear();
                mProductName.requestFocus();
            }
        });

        ArrayList<ShoppingListItem> items = mShoppingList.getItems();

        mShoppingListAdapter = new ShoppingListAdapter(this, items);
        recyclerView.setAdapter(mShoppingListAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
    }
}
