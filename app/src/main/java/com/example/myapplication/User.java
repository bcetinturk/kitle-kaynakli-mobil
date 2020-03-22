package com.example.myapplication;

public class User {
    String mName;
    String mSurname;
    int mPoints;

    public String getName() {
        return mName;
    }

    public String getSurname() {
        return mSurname;
    }


    public int getPoints() {
        return mPoints;
    }

    public User(String name, String surname, int points) {
        mName = name;
        mSurname = surname;
        mPoints = points;
    }
}
