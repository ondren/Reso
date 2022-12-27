package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import api.ApiAccess;

public class YesNoActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("YesNoActivity" + " is working");

        setContentView(R.layout.yes_no_activity);



        Button yes_btn = findViewById(R.id.yes_btn);
        yes_btn.setOnClickListener(v -> {
            Intent intent = getIntent();
            setResult(RESULT_OK, intent);
            finish();
        });
        Button no_btn = findViewById(R.id.no_btn);
        no_btn.setOnClickListener(v -> {
            Intent intent = getIntent();
            setResult(RESULT_CANCELED, intent);
            finish();
        });
    }



}
