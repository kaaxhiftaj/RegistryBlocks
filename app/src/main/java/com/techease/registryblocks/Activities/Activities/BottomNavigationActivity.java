package com.techease.registryblocks.Activities.Activities;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;


import com.techease.registryblocks.Activities.Fragments.MyItemsFragment;
import com.techease.registryblocks.R;

public class BottomNavigationActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Fragment fragment;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_items:
                fragment=new MyItemsFragment();
                getFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
                    return true;
                case R.id.navigation_market:

                    Toast.makeText(BottomNavigationActivity.this, "Sorry, this feature is not available in the prototype app", Toast.LENGTH_SHORT).show();
                    return false;
                case R.id.navigation_document:
                    Toast.makeText(BottomNavigationActivity.this, "Sorry, this feature is not available in the prototype app", Toast.LENGTH_SHORT).show();
                    return false;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);


        sharedPreferences = this.getSharedPreferences("abc", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        fragment=new MyItemsFragment();
        getFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
