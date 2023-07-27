package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.databinding.HubActivityBinding;

import Messages.MessagesActivity;
import QR.QrActivity;
import Reservations.ReservationsActivity;
import Service.MyService;

public class HubActivity extends Activity {
    SharedPreferences sp;
    HubActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = HubActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        RequestQueue queue = Volley.newRequestQueue(this);
        startService(new Intent(this, MyService.class));

        sp = getApplicationContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        initBtns();
    }

    void initBtns(){
        binding.chatBtn.setOnClickListener((l)->{
            Intent intent = new Intent(HubActivity.this, MessagesActivity.class);
            startActivity(intent);
        });
        binding.qrBtn.setOnClickListener((l)->{
            Intent intent = new Intent(HubActivity.this, QrActivity.class);
            startActivity(intent);
        });
        binding.exitBtn.setOnClickListener((l)->{
            SharedPreferences.Editor editor = sp.edit();
            System.out.println("Hub"+"Activity " + " is working");
            editor.remove("token").apply();
            editor.remove("firebase_token").apply();
            editor.remove("phone_number").apply();
            editor.remove("place").apply();
            editor.commit();

            Intent intent = new Intent(HubActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

}
