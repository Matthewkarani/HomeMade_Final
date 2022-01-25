package com.project.homemade;



import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class RegistrationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //Declare
    private static final String TAG = "RegistrationActivity";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_OCC = "Occupation";
    FirebaseFirestore fstore;
    String UserID;
    private FirebaseAuth fAuth;
    private EditText editTextUsername;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextPhoneNumber;
    private FirebaseUser mCurrentUser;
    private Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Occupation, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);

        //how to find the views
        editTextUsername = findViewById(R.id.edit_text_username);
        editTextEmail = findViewById(R.id.edit_text_email);
        editTextPassword = findViewById(R.id.edit_text_password);
        editTextPhoneNumber = findViewById(R.id.edit_text_phone_no);


        // ...
// Initialize Firebase Auth
        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();


    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        parent.getItemAtPosition(position);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     *
     */
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = fAuth.getCurrentUser();
        if (currentUser != null) {
            currentUser.reload();
        }
    }

    //saving to firebase firestore

    public void Save(View view) {
        String username = editTextUsername.getText().toString();
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        String phoneNo = editTextPhoneNumber.getText().toString();
        String Occupation = spinner.getSelectedItem().toString();

        //error toggle messages
        if (TextUtils.isEmpty(username)) {
            editTextUsername.setError("Username is Required");
            return;
        }
        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Email is Required");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please enter a valid email");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Password is Required");
            return;
        }
        if (password.length() < 7) {
            editTextPassword.setError("Min Password length is 7 Characters");
            return;

        }
        if (TextUtils.isEmpty(phoneNo)) {
            editTextPhoneNumber.setError("Phone Number is Required");
            return;
        }

//main registration activity


        UserID = fAuth.getCurrentUser().getUid();
        fAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = fAuth.getCurrentUser();
                            updateUI(user);

                            user.sendEmailVerification()
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            Log.d(TAG, "Email sent.");

                                        }
                                    });
                            //Storing user data to firebase cloud store

                            UserID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fstore.collection("Profile Data").document(UserID);
                            Map<String, Object> note = new HashMap<>();
                            note.put(KEY_USERNAME, username);
                            note.put(KEY_EMAIL, email);
                            note.put(KEY_PASSWORD, password);
                            note.put(KEY_PHONE, phoneNo);
                            note.put(KEY_OCC, Occupation);

                            documentReference.set(note).addOnSuccessListener(aVoid -> {
                                Log.d(TAG, "onSuccess:user Profile is created for " + UserID);
                                Intent intent = new Intent(RegistrationActivity.this, WelcomeActivity.class);
                                startActivity(intent);
                                Toast.makeText(RegistrationActivity.this, "Registration Successful" +
                                                " Check email to verify account",
                                        Toast.LENGTH_SHORT).show();

                            });

                        } else {
                            Toast.makeText(RegistrationActivity.this, "Opps Something went wrong",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    private void updateUI(Object o) {
                    }
                });


    }
}