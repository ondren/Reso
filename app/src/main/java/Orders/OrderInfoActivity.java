package Orders;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.example.myapplication.YesNoActivity;
import com.example.myapplication.databinding.OrderInfoActivityBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import api.ApiAccess;

public class OrderInfoActivity extends Activity {
    String code;
    String order_id;
    String comment;
    OrderInfoActivityBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = OrderInfoActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        code = getIntent().getStringExtra("code");
        order_id = getIntent().getStringExtra("order_id");
        comment = getIntent().getStringExtra("comment");

        binding.backBtn.setOnClickListener(view -> {
            finish();
        });
        binding.takeOrderId.setOnClickListener(view -> {
            Intent intent = new Intent(OrderInfoActivity.this, YesNoActivity.class);
            intent.putExtra("text", "Привязать заказ " + order_id + " к коду '" + comment + "'?");
            startActivityForResult(intent, 209);
        });
        redraw();
    }

    void redraw(){
        ApiAccess.setContext(this);
        binding.orderInfoList.removeAllViews();
        ApiAccess.get("codes/" + ApiAccess.getPlace() + "/orders/" + order_id,
            response -> {
                try {
                    JSONObject order = response.getJSONObject("order");
                    System.out.println(order.toString());

                    TextView to = new TextView(this);
                    to.setTypeface(null, Typeface.BOLD);
                    to.setPadding(0, 20, 0,0);
                    to.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    to.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    to.setText(getResources().getString(R.string.order_info_name, order.optString("comment")));
                    binding.orderInfoList.addView(to); to = new TextView(this);

                    to.setPadding(0, 20, 0,0);
                    to.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    to.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    to.setText(getResources().getString(R.string.order_info_code, order.optString("id")));
                    binding.orderInfoList.addView(to);

                    to.setPadding(0, 20, 0,0);
                    to.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    to.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    to.setText(getResources().getString(R.string.order_info_place, order.optString("place")));
                    binding.orderInfoList.addView(to);

                    JSONArray positions =  order.optJSONArray("positions");
                    if(positions != null && positions.length() > 0){
                        for (int i = 0; i < positions.length(); i++) {
                            JSONObject pos = positions.getJSONObject(i);
                            to = new TextView(this);
                            to.setPadding(40, 20, 0,0);
                            to.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            to.setText((i+1) + ": " + pos.getString("name") + " - " + pos.getString("amount") + "ед.");
                            binding.orderInfoList.addView(to);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            },
            error ->{
                System.out.println(error.getMessage());
            }
        );
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
