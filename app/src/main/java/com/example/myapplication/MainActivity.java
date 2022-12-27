package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;

import org.json.JSONObject;

import api.ApiAccess;


public class MainActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_test_activity);
        ApiAccess.setContext(this);

        String token = ApiAccess.getToken();

        if(token.equals("")){
            System.out.println("no token");
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            JSONObject jsonBody = new JSONObject();
            ApiAccess.get("users/get",
                    response ->{
                        Intent intent = new Intent(this, HubActivity.class);
                        startActivity(intent);
                        finish();

                    },
                    error -> {
                        Intent intent = new Intent(this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    );
        }





    }
}