package com.techease.registryblocks.Activities.Activities;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.adamnoor.registryblocks.R;
import com.techease.registryblocks.Activities.Fragments.Login;
import com.techease.registryblocks.Activities.Fragments.Registration;

public class FullScreenActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);

        sharedPreferences = this.getSharedPreferences("abc", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        status=sharedPreferences.getString("token","");


        if (!status.equals(""))
        {
        startActivity(new Intent(FullScreenActivity.this, BottomNavigationActivity.class));
        finish();
        }
        else
        {
            Fragment fragment=new Login();
            getFragmentManager().beginTransaction().replace(R.id.mainContainer,fragment).commit();
        }




    }
}
