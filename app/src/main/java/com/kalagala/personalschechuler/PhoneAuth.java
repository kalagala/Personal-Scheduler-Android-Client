package com.kalagala.personalschechuler;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneAuth extends
        PhoneAuthProvider.OnVerificationStateChangedCallbacks{
    private Activity mActivity;
    private String mPhoneNumber;
    private String TAG = "Phone Verification";

    PhoneAuth(Activity activity, String phoneNumber){
        mActivity = activity;
        mPhoneNumber = phoneNumber;
    }

    public void authorize(){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mPhoneNumber,
                60,
                TimeUnit.SECONDS,
                mActivity,
                this);
    }
    @Override
    public void onVerificationCompleted(
            @NonNull PhoneAuthCredential phoneAuthCredential) {
        Log.d(TAG, "phone verification succeed"
                +phoneAuthCredential);

    }

    @Override
    public void onVerificationFailed(@NonNull FirebaseException e) {
        Log.d(TAG, "phone verification failed");
    }


}
