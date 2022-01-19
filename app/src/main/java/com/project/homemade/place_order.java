package com.project.homemade;



import androidx.appcompat.app.AppCompatActivity;

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


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class place_order extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    private static final String KEY_NAME = "Customer Name";
    private static final String KEY_Quantity = "Order Quantity";
    private static final String KEY_AdditionalInfo = "Additional Info";
    private static final String KEY_Date = "DueDate";
    private static final String KEY_Category = "Pastry Category";


    private EditText editTextEnterName;
    private EditText enterTextEnterQuantity;
    private EditText editTextAdditionalInformation;
    private EditText editTextDate;

    FirebaseFirestore fstore;
    String UserID;
    private FirebaseAuth fAuth;
    private Spinner spinner2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);

        editTextEnterName = findViewById(R.id.Enter_name);
        enterTextEnterQuantity= findViewById(R.id.Enter_Quantity);
        editTextAdditionalInformation = findViewById(R.id.Additional_Information);
        editTextDate = findViewById(R.id.editTextDate);

        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        spinner2 = findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Pastry_Category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter);

        spinner2.setOnItemSelectedListener(this);

    }

    public void Confirm_Order(View view) {
        UserID = fAuth.getCurrentUser().getUid();
        String CName = editTextEnterName.getText().toString();
        String Quantity = enterTextEnterQuantity.getText().toString();
        String AdditionInfo = editTextAdditionalInformation.getText().toString();
        String DueDate = editTextDate.getText().toString();
        String Category = spinner2.getSelectedItem().toString();

        if (TextUtils.isEmpty(CName)) {
            editTextEnterName.setError("Enter Name");
            return;
        }
        if (TextUtils.isEmpty(Quantity)) {
            enterTextEnterQuantity.setError("Specify the Quantity");
            return;
        }

        if (TextUtils.isEmpty(AdditionInfo)) {
            editTextAdditionalInformation.setError("Specify your Order Details");
            return;
        }

        if (TextUtils.isEmpty(DueDate)) {
            editTextDate.setError("Please add a due date");
            return;
        }


        DocumentReference documentReference = fstore
                .collection("Profile Data").document(UserID)
                .collection("My Orders").document();
        Map<String, Object> order = new HashMap<>();
        order.put(KEY_NAME, CName );
        order.put(KEY_Category, Category);
        order.put(KEY_Quantity, Quantity);
        order.put(KEY_AdditionalInfo, AdditionInfo);
        order.put(KEY_Date,DueDate);

        documentReference.set(order).addOnSuccessListener(aVoid -> {
            //Log.d(TAG, "onSuccess:user Profile is created for " + UserID);
            Toast.makeText(place_order.this, "Order Placed wait for Confirmation", Toast.LENGTH_SHORT).show();


        });



    }

    @Override
    public void onItemSelected(AdapterView<?>  parent, View view, int position, long id) {
        parent.getItemAtPosition(position);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}