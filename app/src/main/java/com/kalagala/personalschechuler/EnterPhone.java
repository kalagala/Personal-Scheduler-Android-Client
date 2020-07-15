package com.kalagala.personalschechuler;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.PhoneNumberUtils;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hbb20.CountryCodePicker;


public class EnterPhone extends AppCompatActivity {
    EditText mPhoneNumber;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_phone);
        mPhoneNumber = (EditText) findViewById(R.id.phoneNumber);

        mPhoneNumber.addTextChangedListener(
                new PhoneNumberFormattingTextWatcher());


    }
}
