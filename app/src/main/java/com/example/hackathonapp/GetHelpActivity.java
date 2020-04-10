package com.example.hackathonapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GetHelpActivity extends AppCompatActivity {

    TextView Food, Medical, Financial, Other, Confirm;
    boolean sel1 = false, sel2 = false, sel3 = false, sel4 = false;

    long maxid = 0;

    EditText food, medical, financial, others, add1, locality, city, state, nop;
    DatabaseReference myRef;
    HelpDetails helpDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_help);

        Food = (TextView)findViewById(R.id.tvghafood);
        Medical = (TextView)findViewById(R.id.tvghamedical);
        Financial = (TextView)findViewById(R.id.tvghafinancial);
        Other = (TextView)findViewById(R.id.tvghaother);
        Confirm = (TextView)findViewById(R.id.tvghaconfirm);

        food = (EditText)findViewById(R.id.etfood);
        medical = (EditText)findViewById(R.id.etmedical);
        financial = (EditText)findViewById(R.id.etfinancial);
        others = (EditText)findViewById(R.id.etothers);
        add1 = (EditText)findViewById(R.id.etadd1);
        locality = (EditText)findViewById(R.id.etlocality);
        city = (EditText)findViewById(R.id.etcity);
        state = (EditText)findViewById(R.id.etstate);
        nop = (EditText)findViewById(R.id.etnop);

        myRef = FirebaseDatabase.getInstance().getReference().child("Help");

        Food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sel1){ deselect(Food); sel1 = false; }
                else{ select(Food); sel1 = true; }
            }
        });

        Medical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sel2){ deselect(Medical); sel2 = false; }
                else{ select(Medical); sel2 = true; }
            }
        });

        Financial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sel3){ deselect(Financial); sel3 = false; }
                else{ select(Financial); sel3 = true; }
            }
        });

        Other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sel4){ deselect(Other); sel4 = false; }
                else{ select(Other); sel4 = true; }
            }
        });

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

        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helpDetails = new HelpDetails();
                helpDetails.setFood(food.getText().toString().trim());
                helpDetails.setMedical(medical.getText().toString().trim());
                helpDetails.setFinancial(financial.getText().toString().trim());
                helpDetails.setOther(others.getText().toString().trim());
                helpDetails.setAddress(add1.getText().toString().trim()+", "+locality.getText().toString().trim()+", "+city.getText().toString().trim()+", "+state.getText().toString().trim());
                helpDetails.setNOP(nop.getText().toString().trim());

                myRef.child(String.valueOf(maxid + 1)).setValue(helpDetails);

                Toast.makeText(GetHelpActivity.this, "Successfully Posted!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    void select(TextView view){
        view.setBackgroundResource(R.drawable.card_corners_highlighted);
        view.setTextColor(Color.parseColor("#ffffff"));
    }

    void deselect(TextView view){
        view.setBackgroundResource(R.drawable.card_corners);
        view.setTextColor(Color.parseColor("#000000"));
    }
}
