package Orders;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.myapplication.databinding.QrCreateActivityBinding;

import org.json.JSONException;
import org.json.JSONObject;

import QR.QRSettingsActivity;
import api.ApiAccess;

public class QrCreateActivity extends Activity {

    QrCreateActivityBinding binding;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = QrCreateActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backBtn.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });
        binding.goButton.setOnClickListener(v -> {
            String comment = binding.comment.getText().toString();
            if(comment.equals(""))
                return;
            binding.createLayout.setVisibility(View.GONE);
            binding.loadingLayout.setVisibility(View.VISIBLE);
            Intent intent = getIntent();
            ApiAccess.setContext(this);
            try {
                JSONObject code_data = new JSONObject();
                code_data.put("comment", comment);
                ApiAccess.post("codes/" + ApiAccess.getPlace(), code_data,
                    response -> {
                        try {
                            JSONObject item = response.getJSONObject("item");
                            String code = item.getString("code");
                            Intent i = new Intent(QrCreateActivity.this, QRSettingsActivity.class);
                            i.putExtra("code", code);
                            startActivity(i);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            finish();
                        }
                    },
                    error ->{
                        System.out.println(error.getMessage());

                        binding.createLayout.setVisibility(View.VISIBLE);
                        binding.loadingLayout.setVisibility(View.GONE);
                    }
                );
            } catch (JSONException e) {
                binding.createLayout.setVisibility(View.VISIBLE);
                binding.loadingLayout.setVisibility(View.GONE);
            }
        });
    }
}