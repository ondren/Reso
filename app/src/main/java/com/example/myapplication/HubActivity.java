package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class HubActivity extends Activity {
    SharedPreferences sp;

    public View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.exit_btn){
                SharedPreferences.Editor editor = sp.edit();
                System.out.println("Hub"+"Activity " + " is working");
                editor.remove("token").commit();
                editor.remove("firebase_token").commit();
                editor.remove("phone_number").commit();


                Intent intent = new Intent(HubActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }

            if(view.getId() == R.id.qr_btn){
                Intent intent = new Intent(HubActivity.this, QrActivity.class);
                startActivity(intent);
            }
        }


                };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hub_activity);
        RequestQueue queue = Volley.newRequestQueue(this);
        startService(new Intent(this, MyService.class)); //start service which is MyService.java

//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        this.setContentView(binding.getRoot());
        Button exit_but = findViewById(R.id.exit_btn);
        Button qr_but = findViewById(R.id.qr_btn);
        Button chat_but = findViewById(R.id.chat_btn);
        Button tables_but = findViewById(R.id.tables_btn);
        sp = getApplicationContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        exit_but.setOnClickListener(onClick);
        qr_but.setOnClickListener(onClick);
        chat_but.setOnClickListener(onClick);
        tables_but.setOnClickListener(onClick);



    }

}
