package com.techease.registryblocks.Activities.Activities;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;


import com.techease.registryblocks.Activities.Fragments.LoginFragment;
import com.techease.registryblocks.R;

public class FullScreenActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ((AppCompatActivity) this).getSupportActionBar().hide();
        setContentView(R.layout.activity_full_screen);
        sharedPreferences = this.getSharedPreferences("abc", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        token=sharedPreferences.getString("token","");
        if (!token.equals(""))
        {
        startActivity(new Intent(FullScreenActivity.this, BottomNavigationActivity.class));
        finish();
        }
        else
        {
            Fragment fragment=new LoginFragment();
            getFragmentManager().beginTransaction().replace(R.id.mainContainer,fragment).commit();
        }
    }
}
