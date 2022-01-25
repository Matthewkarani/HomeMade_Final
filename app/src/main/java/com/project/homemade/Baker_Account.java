package com.project.homemade;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.project.homemade.Fragment.HomeFragment;
import com.project.homemade.Fragment.NotificationFragment;
import com.project.homemade.Fragment.ProfileFragment;
import com.project.homemade.Fragment.SearchFragment;


public class Baker_Account extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    Fragment selectFragment = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baker_account);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemReselectedListener(navigationItemSelectListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();

    }
    private  BottomNavigationView.OnNavigationItemReselectedListener navigationItemSelectListener =
          new BottomNavigationView.OnNavigationItemReselectedListener() {
              @Override
              public void onNavigationItemReselected(@NonNull MenuItem item) {

                  switch (item.getItemId()) {
                      case R.id.nav_home:
                          selectFragment = new HomeFragment();

                          break;
                      case R.id.nav_search:
                          selectFragment = new SearchFragment();
                          break;
                      case R.id.nav_add:
                          selectFragment = null;
                          startActivity(new Intent(Baker_Account.this, PostActivity.class));
                          break;
                      case R.id.nav_favorite:
                          selectFragment = new NotificationFragment();
                          break;
                      case R.id.nav_profile:
                          SharedPreferences.Editor editor = getSharedPreferences("PREPS", MODE_PRIVATE).edit();
                          editor.putString("profileid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                          editor.apply();
                          selectFragment = new ProfileFragment();


                          break;
                  }
                  if (selectFragment != null) {
                      getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectFragment).commit();
                  }
              }



          };
}