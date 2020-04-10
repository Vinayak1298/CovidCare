package com.example.hackathonapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileSetupActivity extends AppCompatActivity {

    Button Submit;

    EditText FullName, Gender, Age, PhoneNumber, EmailAddress;

    long maxid = 0;

    DatabaseReference myRef;
    UserDetails userDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);

        Submit = (Button)findViewById(R.id.btnsubmitdetails);
        FullName = (EditText)findViewById(R.id.etpsafullname);
        Gender = (EditText)findViewById(R.id.etpsagender);
        Age = (EditText)findViewById(R.id.etpsaage);
        PhoneNumber = (EditText)findViewById(R.id.etpsaphonenumber);
        EmailAddress = (EditText)findViewById(R.id.etpsaemailaddress);

        myRef = FirebaseDatabase.getInstance().getReference().child("User");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    maxid = (dataSnapshot.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "There was an error!", Toast.LENGTH_SHORT).show();
            }
        });

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userDetails = new UserDetails();
                userDetails.setFullName(FullName.getText().toString().trim());
                userDetails.setGender(Gender.getText().toString().trim());
                userDetails.setAge(Age.getText().toString().trim());
                userDetails.setPhoneNumber(PhoneNumber.getText().toString().trim());
                userDetails.setEmailAddress(EmailAddress.getText().toString().trim());

                myRef.child(String.valueOf(maxid + 1)).setValue(userDetails);

                Toast.makeText(ProfileSetupActivity.this, "Profile Setup Successful!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
