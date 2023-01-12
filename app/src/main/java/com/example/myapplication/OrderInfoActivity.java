package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;

import org.json.JSONException;

import java.util.HashMap;

import api.ApiAccess;

public class OrderInfoActivity extends Activity {
    String code;
    String order_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_info_activity);
        Button back_btn = findViewById(R.id.back_btn);
        System.out.println("OrderInfo"+"Activity " + " is working");
        code = getIntent().getStringExtra("code");
        order_id = getIntent().getStringExtra("order_id");
        System.out.println("OrderInfoActivity got extras: "+ "code = " + code + " order_id = " + order_id);
        back_btn.setOnClickListener(view -> {
            finish();
        });
        Button take_order_btn = findViewById(R.id.take_order_id);
        take_order_btn.setOnClickListener(view -> {
            Intent intent = new Intent(OrderInfoActivity.this, YesNoActivity.class);
            startActivityForResult(intent, 209);
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {return;}
        if(resultCode == RESULT_OK && requestCode == 209){
            ApiAccess.setContext(this);
            HashMap<String, String> params = new HashMap<>();
            params.put("code", code);
            params.put("order", order_id);
            ApiAccess.get("qr/orders/set", params,
                    response -> {

                        try {
                            String result = response.getString("message");


                            System.out.println(result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    });
            setResult(RESULT_OK, data);

            System.out.println("Result ok ");
            finish();

        }
    }
}
