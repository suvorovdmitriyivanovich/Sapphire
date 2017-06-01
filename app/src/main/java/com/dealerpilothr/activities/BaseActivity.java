package com.dealerpilothr.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dealerpilothr.utils.Language;

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Language.translate();
    }
}
