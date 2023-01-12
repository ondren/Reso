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

public class QRTablesActivity extends Activity {
    LinearLayout layout;
    String table_no;
    String code;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_tables_activity);
        layout=findViewById(R.id.qr_tables_id);
        Button back_btn = findViewById(R.id.back_btn);
        System.out.println("QRTables"+"Activity " + " is working");



        back_btn.setOnClickListener(view -> {
            finish();
        });


        redraw();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        code = getIntent().getStringExtra("code");
        String table_code = data.getStringExtra("table");
        if (data == null) {return;}
        if (resultCode == RESULT_OK && requestCode == 209){
            System.out.println("code in QRTablesActivity = " + code +  " table_code = " + table_code);
            ApiAccess.setContext(this);
            HashMap<String, String> params = new HashMap<>();
            params.put("code", code);
            params.put("table", table_code);
            ApiAccess.get("qr/tables/set", params,
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
            startActivity(getIntent());
            redraw();

        }
    }
    void redraw(){
        ApiAccess.setContext(this);
        ApiAccess.get("qr/tables",
                response -> {
                    try {
                        JSONArray JSONArray_tables = response.getJSONArray("tables");
                        for (int i = 0; i < JSONArray_tables.length(); i++){
                            System.out.println("table nO " + JSONArray_tables.get(i));
                            final Button table_btn = new Button(this);
                            table_btn.setLayoutParams(new LinearLayout.LayoutParams(350, 150));
                            table_btn.setId(i);
                            JSONObject table = JSONArray_tables.getJSONObject(i);
                            String tableName = "Место " + table.getString("name");
                            String table_code = table.getString("name");
                            table_btn.setText(tableName);
                            table_btn.setGravity(Gravity.CENTER);
                            layout.addView(table_btn);
                            table_btn.setOnClickListener(view -> {

                                Intent intent = new Intent(QRTablesActivity.this, YesNoActivity.class);
                                intent.putExtra("table", table_code);
                                startActivityForResult(intent, 209);

                            });

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
    }
}
