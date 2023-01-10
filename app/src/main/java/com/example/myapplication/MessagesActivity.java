package com.example.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class MessagesActivity extends Activity {
    LinearLayout layout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messages_activity);
        layout = findViewById(R.id.messages_activity_id);

        Button back_btn = findViewById(R.id.back_btn);
        System.out.println("Messages"+"Activity " + " is working");



        back_btn.setOnClickListener(view -> {
            finish();
        });

    }
}
