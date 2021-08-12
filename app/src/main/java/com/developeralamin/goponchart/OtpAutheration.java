package com.developeralamin.goponchart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import es.dmoral.toasty.Toasty;

public class OtpAutheration extends AppCompatActivity {

    TextView mchangenumber;
    EditText mgetotp;
    android.widget.Button mverifyotp;
    String enteredotp;

    FirebaseAuth firebaseAuth;
    ProgressBar mprogressBarVerifyOtp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_autheration);


        mchangenumber = findViewById(R.id.changeNumber);
        mverifyotp = findViewById(R.id.verifyOtp);
        mgetotp = findViewById(R.id.getotp);
        mprogressBarVerifyOtp = findViewById(R.id.progressBarVerifyOtp);

        firebaseAuth = FirebaseAuth.getInstance();

        mchangenumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OtpAutheration.this, ChartActivity.class);
                startActivity(intent);

            }
        });
        
        mverifyotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enteredotp = mgetotp.getText().toString();
                if (enteredotp.isEmpty()){
                    Toasty.success(getApplicationContext(), "Enter your OTP First", Toasty.LENGTH_SHORT).show();
                    //Toast.makeText(getApplicationContext(), "Enter your OTP First", Toast.LENGTH_SHORT).show();
                }else {
                    mprogressBarVerifyOtp.setVisibility(View.VISIBLE);
                    String coderecieved = getIntent().getStringExtra("OTP");
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(coderecieved,enteredotp);
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    mprogressBarVerifyOtp.setVisibility(View.VISIBLE);
                    Toasty.success(getApplicationContext(), "Login Success", Toasty.LENGTH_SHORT).show();
                    //Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(OtpAutheration.this, Setprofile.class);
                    startActivity(intent);
                    finish();
                }else {
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                        mprogressBarVerifyOtp.setVisibility(View.VISIBLE);
                        Toasty.error(getApplicationContext(), "Login Failed", Toasty.LENGTH_SHORT).show();
                        //Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }
}