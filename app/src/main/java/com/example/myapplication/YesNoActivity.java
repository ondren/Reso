package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.example.myapplication.databinding.YesNoActivityBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import api.ApiAccess;



public class YesNoActivity extends Activity {

    YesNoActivityBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = YesNoActivityBinding.inflate(getLayoutInflater());
        System.out.println("YesNoActivity" + " is working");

        setContentView(binding.getRoot());

        binding.textView.setText(getIntent().getStringExtra("text"));
        binding.yesBtn.setOnClickListener(v -> {
            Intent intent = getIntent();
            setResult(RESULT_OK, intent);
            finish();
        });
        binding.noBtn.setOnClickListener(v -> {
            Intent intent = getIntent();
            setResult(RESULT_CANCELED, intent);
            finish();
        });
    }



}
