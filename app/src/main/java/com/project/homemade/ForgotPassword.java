package com.project.homemade;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    private EditText emailEditText1;
    private Button resetPasswordButton;
    private ProgressBar progressBar;

    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailEditText1 = (EditText) findViewById(R.id.edit_text_enter_email);
        resetPasswordButton = (Button) findViewById(R.id.ResetPassword);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();


        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();

            }
        });

    }

    private void resetPassword() {
        String email = emailEditText1.getText().toString().trim();

        if (email.isEmpty()) {
            emailEditText1.setError("Email is required!");
            emailEditText1.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText1.setError("Email is required!");
            emailEditText1.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForgotPassword .this,"Check your email to reset your Password!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ForgotPassword.this,WelcomeActivity.class);
                    startActivity(intent);
                }else{
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(ForgotPassword.this, "Something went wrong , try again", Toast.LENGTH_LONG).show();
                    return;
                }

            }
        });
    }


}