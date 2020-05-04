package com.example.myapplication;

import java.util.ArrayList;

public class ShoppingList {
    private static ShoppingList mList;
    private ArrayList<ShoppingListItem> items;

    public static ShoppingList getInstance(){
        if(mList==null){
            mList = new ShoppingList();
            mList.items = new ArrayList<>();
        }

        return mList;
    }

    public ArrayList<ShoppingListItem> getItems() {
        return items;
    }

    public void addItem(ShoppingListItem item) {
        items.add(item);
    }
}
