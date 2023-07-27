package QR;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.myapplication.R;
import com.example.myapplication.YesNoActivity;
import com.example.myapplication.databinding.QrActivityBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import Orders.OrderInfoActivity;
import Orders.QrCreateActivity;
import api.ApiAccess;

public class QrActivity extends Activity {
    QrActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = QrActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        System.out.println("QR"+"Activity " + " is working");

        binding.backBtn.setOnClickListener(view -> {
            finish();
        });
        binding.newQr.setOnClickListener( view -> {
            Intent intent = new Intent(QrActivity.this, QrCreateActivity.class);
            startActivityForResult(intent, 300);
        });

        redraw();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data == null)
            return;
        if(resultCode == RESULT_OK && requestCode == 300){
            String comment = data.getStringExtra("comment");
            ApiAccess.setContext(this);
            try {
                JSONObject code_data = new JSONObject();
                code_data.put("comment", comment);
                ApiAccess.post("codes/" + ApiAccess.getPlace(), code_data,
                    response -> {
                        redraw();
                    },
                    error ->{
                        System.out.println(error.getMessage());
                    }
                );
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    void redraw(){
        ApiAccess.get("codes/" + ApiAccess.getPlace(),
                response -> {
                    try {
                        binding.qrList.removeAllViews();
                        JSONObject codes_data = response.getJSONObject("codes");
                        JSONArray JSONArray_codes = codes_data.getJSONArray("data");
                        for (int i = 0; i < JSONArray_codes.length(); i++) {
                            JSONObject j = JSONArray_codes.getJSONObject(i);
                            String code = j.getString("code");
                            String comment = j.getString("comment");
                            System.out.println(code.toString());

                            final Button button = new Button(this);
                            button.setLayoutParams(new LinearLayout.LayoutParams(350, 150));
                            button.setId(i);
                            button.setMinHeight(60);

                            button.setLayoutParams(new ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                            ));
                            String btn_name = "QR " + i + ": " + comment;
                            button.setText(btn_name);

                            button.setOnClickListener(view -> {
                                Intent intent = new Intent(QrActivity.this, QRSettingsActivity.class);
                                intent.putExtra("code", code);
                                System.out.println(code);
                                startActivity(intent);

                            });

                            binding.qrList.addView(button);

                        }

                    }catch (JSONException e){
                        e.printStackTrace();
                    }

                },
                error -> {

                }
        );
    }
}
