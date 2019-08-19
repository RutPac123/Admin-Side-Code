package com.example.adminside.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.adminside.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;


public class OTP_Login extends AppCompatActivity {

    ProgressDialog progress;
    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mAuth;
    private EditText phoneNoEdt;
    private Button b1;
    private CoordinatorLayout coordinatorLayout;
    private  String imei;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            getSupportActionBar().hide();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_otp__login);


        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        phoneNoEdt =  findViewById(R.id.Phonenoedittext);
        b1 = findViewById(R.id.PhoneVerify);
        coordinatorLayout = findViewById(R.id.coord);


        if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this ,new String[]{Manifest.permission.SEND_SMS,Manifest.permission.READ_SMS},10);

        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this ,new String[]{Manifest.permission.READ_PHONE_STATE,Manifest.permission.READ_PHONE_STATE},10);

        }

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                mVerificationInProgress = false;
                signInWithPhoneAuthCredential(credential);
                Snackbar.make(coordinatorLayout,"Verification complete",Snackbar.LENGTH_SHORT).show();
                progress.hide();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(OTP_Login.this,"InValid Phone Number",Toast.LENGTH_SHORT).show();
                    progress.hide();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Toast.makeText(OTP_Login.this, "Something went wrong...please try again", Toast.LENGTH_SHORT).show();
                    progress.hide();
                }

            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
                progress.hide();

            }
        };
        String ts = Context.TELEPHONY_SERVICE;
        TelephonyManager mTelephonyMgr = (TelephonyManager) getSystemService(ts);

        imei = mTelephonyMgr.getDeviceId();

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (imei.equals("865397034855308") || imei.equals("865397034855316")) {
                    progress = ProgressDialog.show(OTP_Login.this, "Please Wait..",
                            "Authenticating...", false,true);
                    String myPhoneNumber = phoneNoEdt.getText().toString().trim();
                    if(myPhoneNumber.isEmpty()){
                        Snackbar.make(coordinatorLayout,"Enter phone number!",Snackbar.LENGTH_SHORT).show();
                        progress.hide();
                    }else{
                        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                "+91"+myPhoneNumber,
                                60,
                                java.util.concurrent.TimeUnit.SECONDS,
                                OTP_Login.this,
                                mCallbacks);   // this wil verify phone number
                    }
                } else {
                   Snackbar.make(coordinatorLayout,"Not the registered device !",Snackbar.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            OTP_Login.this.startActivity(new Intent(OTP_Login.this, MainActivity.class));

                            progress.hide();

                            OTP_Login.this.finish();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                progress.hide();
                                Toast.makeText(OTP_Login.this, "Verification Failed!", Toast.LENGTH_SHORT).show();
                                b1.setText("Try Again");
                            }
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            startActivity(new Intent(OTP_Login.this,MainActivity.class));
            finish();
        }
    }
}
