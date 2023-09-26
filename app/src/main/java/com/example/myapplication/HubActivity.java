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
import Messages.UnimportantMessagesActivity;
import Orders.QrCreateActivity;
import QR.QrActivity;
import Reservations.ReservationsActivity;
import Service.MyService;
import api.ApiAccess;

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 300) {
            Intent intent = new Intent(HubActivity.this, QrActivity.class);
            startActivity(intent);
        }
    }

    void initBtns(){
        binding.chatBtn.setOnClickListener((l)->{
            Intent intent = new Intent(HubActivity.this, MessagesActivity.class);
            startActivity(intent);
        });
        binding.dishBtn.setOnClickListener((l)->{
            Intent intent = new Intent(HubActivity.this, UnimportantMessagesActivity.class);
            startActivity(intent);
        });
        binding.qrBtn.setOnClickListener((l)->{
            Intent intent = new Intent(HubActivity.this, QrActivity.class);
            startActivity(intent);
        });
        binding.qrNewBtn.setOnClickListener((l)->{
            Intent intent = new Intent(HubActivity.this, QrCreateActivity.class);
            startActivityForResult(intent, 300);
        });

        binding.firebaseCheckBtn.setOnLongClickListener((l)->{
            ApiAccess.setContext(this);
            String endpoint = "users/" + ApiAccess.getPlace() + "/firebaseCheck";
            binding.firebaseCheckBtn.setText("Отправляем запрос...");
            ApiAccess.get(endpoint,
                    (data)->{
                        binding.firebaseCheckBtn.setText("Проверка Firebase");
                    },
                    error->{
                        binding.firebaseCheckBtn.setText("Проверка Firebase");
                    });
            return true;
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
