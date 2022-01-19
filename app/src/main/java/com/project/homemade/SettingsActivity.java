package com.project.homemade;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;


import com.google.firebase.auth.FirebaseAuth;


public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

    }

    public void SignOut(View view) {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(SettingsActivity.this, "Signed Out.",
                Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(SettingsActivity.this, WelcomeActivity.class);
        startActivity(intent);
    }


}