package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import org.json.JSONException;

import java.util.HashMap;

import api.ApiAccess;

public class QRSettingsActivity extends Activity {
    String code;
    String link;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_settings_activity);
        Button back_btn = findViewById(R.id.back_btn);
        Button orders_btn = findViewById(R.id.orders_btn);
        System.out.println("QRSettings"+"Activity " + " is working");
        code = getIntent().getStringExtra("code");

        orders_btn.setOnClickListener(view -> {
            Intent intent = new Intent(QRSettingsActivity.this, OrdersActivity.class);
            intent.putExtra("code",code);
            startActivity(intent);
        });


        back_btn.setOnClickListener(view -> {
            finish();
        });


        redraw(code);




        Button btn_intent_qr_show = findViewById(R.id.btn_intent_qr_show);
        Button btn_intent_tables = findViewById(R.id.btn_intent_tables);

        TextView staff_id = findViewById(R.id.staff_id);
        TextView tables_id = findViewById(R.id.tables_id);

        btn_intent_tables.setOnClickListener(view -> {
            Intent intent = new Intent(QRSettingsActivity.this, QRTablesActivity.class);
            intent.putExtra("code", code);
            startActivityForResult(intent, 201);
        });
        code =  getIntent().getStringExtra("code");
        System.out.println("Code in QRSettingsActivity: " + code);
        btn_intent_qr_show.setOnClickListener(view -> {

            System.out.println("Link in showQRActivity "+link);
            Intent intent = new Intent(QRSettingsActivity.this, ShowQrActivity.class);
            System.out.println("intent worked link is "+ link);
            intent.putExtra("link",link);
            startActivity(intent);
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {return;}
        if(resultCode == RESULT_OK && requestCode == 201){
            redraw();
        }
    }

    void redraw(String code)
    {
        ApiAccess.setContext(this);
        HashMap<String, String> params = new HashMap<>();
        params.put("code", code);


        ApiAccess.get("qr/get", params,
                response -> {
                    try {
                        link = response.getJSONObject("qr").getString("link");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });

    }
    void redraw()
    {
        redraw(code);
    }
}
