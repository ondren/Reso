package Orders;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.example.myapplication.YesNoActivity;
import com.example.myapplication.databinding.OrderInfoActivityBinding;

import org.json.JSONException;

import java.util.HashMap;

import api.ApiAccess;

public class OrderInfoActivity extends Activity {
    String code;
    String order_id;
    OrderInfoActivityBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = OrderInfoActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        code = getIntent().getStringExtra("code");
        order_id = getIntent().getStringExtra("order_id");

        binding.backBtn.setOnClickListener(view -> {
            finish();
        });
        binding.takeOrderId.setOnClickListener(view -> {
            Intent intent = new Intent(OrderInfoActivity.this, YesNoActivity.class);
            intent.putExtra("text", "Привязать заказ " + order_id + " к коду " + code + "?");
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
            ApiAccess.get("codes/" + ApiAccess.getPlace() + "/orders/bind/" + code, params,
                response -> {
                    try {
                        String result = response.getString("message");
                        System.out.println(result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error ->{
                    System.out.println(error.getMessage());
                }
            );
            setResult(RESULT_OK, data);

            System.out.println("Result ok ");
            finish();

        }
    }
}
