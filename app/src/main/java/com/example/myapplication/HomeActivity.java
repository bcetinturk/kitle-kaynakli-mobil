package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

public class HomeActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        String uid = getIntent().getStringExtra(HomeFragment.EXTRA_USER_ID);

        return HomeFragment.newInstance(uid);
    }
}
