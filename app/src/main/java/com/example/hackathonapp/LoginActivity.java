package com.example.hackathonapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    Button Login;
    EditText Otp1, Otp2, Otp3, Otp4, Otp5, Otp6;
    String phoneno, codeSent;
    TextView DisplayPhone, ResendOTP;
    ProgressDialog mProgressDialog;

    FirebaseAuth mAuth;

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Login = (Button)findViewById(R.id.btnverify);
        Otp1 = (EditText)findViewById(R.id.etotp1);
        Otp2 = (EditText)findViewById(R.id.etotp2);
        Otp3 = (EditText)findViewById(R.id.etotp3);
        Otp4 = (EditText)findViewById(R.id.etotp4);
        Otp5 = (EditText)findViewById(R.id.etotp5);
        Otp6 = (EditText)findViewById(R.id.etotp6);
        DisplayPhone = (TextView)findViewById(R.id.tvdisplayphoneno);
        ResendOTP = (TextView)findViewById(R.id.tvresendotp);

        mAuth = FirebaseAuth.getInstance();

        final Dialog dialog = new Dialog(this, R.style.myDialog);
        dialog.setCancelable(false);
        View view = getLayoutInflater().inflate(R.layout.activity_enter_number, null);
        final EditText PhoneNo = (EditText)view.findViewById(R.id.etenaphoneno);
        Button SendOtp = (Button)view.findViewById(R.id.btnsendotp);

        SendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneno = PhoneNo.getText().toString().trim();
                dialog.dismiss();
                DisplayPhone.setText(phoneno);
                Toast.makeText(LoginActivity.this, "Sending OTP...", Toast.LENGTH_SHORT).show();
                sendVerificationCode();
            }
        });

        dialog.setContentView(view);
        dialog.show();

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressDialog = new ProgressDialog(LoginActivity.this);
                mProgressDialog.setMessage("Verifying OTP...");
                mProgressDialog.show();
                verifySignIn();
            }
        });
    }

    private void sendVerificationCode(){

        String phno = "+91"+phoneno;

        if(phno.length()!=13){
            Toast.makeText(this, "Please Enter a valid phone number!", Toast.LENGTH_SHORT).show();
            return;
        }

        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                String code = phoneAuthCredential.getSmsCode();
                Otp1.setText(""+code.charAt(0));
                Otp2.setText(""+code.charAt(1));
                Otp3.setText(""+code.charAt(2));
                Otp4.setText(""+code.charAt(3));
                Otp5.setText(""+code.charAt(4));
                Otp6.setText(""+code.charAt(5));

                if(!Otp6.getText().toString().equals("")){
                    Login.performClick();
                }
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(getApplicationContext(), "Sorry, Try again after sometime!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                codeSent = s;
            }

        };

        PhoneAuthProvider.getInstance().verifyPhoneNumber(phno, 90, TimeUnit.SECONDS, this, mCallbacks );

    }

    private void verifySignIn(){

        String code = Otp1.getText().toString().trim()+Otp2.getText().toString().trim()+Otp3.getText().toString().trim()+Otp4.getText().toString().trim()+Otp5.getText().toString().trim()+Otp6.getText().toString().trim();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    mProgressDialog.dismiss();
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), ProfileSetupActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(getApplicationContext(), "Error in Login!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
    }
}
