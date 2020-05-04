package com.example.myapplication;

public class ShoppingListItem {
    private String mName;
    private String mAmount;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getAmount() {
        return mAmount;
    }

    public void setAmount(String amount) {
        mAmount = amount;
    }

    public ShoppingListItem(String name, String amount) {
        mName = name;
        mAmount = amount;
    }
}
