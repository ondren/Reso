package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import api.ApiAccess;

public class OrdersActivity extends Activity {
    Button back_btn;
    LinearLayout layout;
    String code;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orders_activity);
        layout=findViewById(R.id.orders_id);
        back_btn = findViewById(R.id.back_btn);
        System.out.println("Orders"+"Activity " + " is working");
        back_btn.setOnClickListener(view -> {
            finish();
        });
        code = getIntent().getStringExtra("code");

        redraw();

        System.out.println("code in OrdersActivity is " + code);

        }

        void redraw(){
        ApiAccess.setContext(this);
            HashMap<String, String> params = new HashMap<>();
            params.put("code", code);
        ApiAccess.get("qr/orders",params,
                response -> {
            try {
                JSONArray jsonArray = response.getJSONArray("orders");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject j = jsonArray.getJSONObject(i);
                    String order_id = j.getString("id");
                    System.out.println("order id  = "+order_id);
                    final Button order_id_btn = new Button(this);
                    order_id_btn.setLayoutParams(new LinearLayout.LayoutParams(350, 150));
                    order_id_btn.setId(i);
                    order_id_btn.setHeight(80);
                    order_id_btn.setText("order id: " + order_id);
                    order_id_btn.setGravity(Gravity.CENTER);
                    layout.addView(order_id_btn);
                    order_id_btn.setOnClickListener(view -> {
                        Intent intent = new Intent(OrdersActivity.this, OrderInfoActivity.class);
                        intent.putExtra("code",code);
                        intent.putExtra("order_id",order_id);
                        startActivityForResult(intent, 208);
                        int v;

                    });
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        },
                error ->{
                    String message = new String(error.networkResponse.data);
                    int v = 4;
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {return;}
        if (resultCode == RESULT_OK && requestCode == 208){
            setResult(RESULT_OK, data);
            System.out.println("Result ok ");
            layout.removeAllViews();
            redraw();
        }
    }


}
