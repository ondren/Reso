package QR;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import Orders.OrdersActivity;

import com.example.myapplication.databinding.QrSettingsActivityBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import api.ApiAccess;

public class QRSettingsActivity extends Activity {
    String code;
    String link;
    String comment;
    int waiter_id;
    QrSettingsActivityBinding binding;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = QrSettingsActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        System.out.println("QRSettings" + "Activity " + " is working");
        code = getIntent().getStringExtra("code");


        initInterface(code);
    }

    void initInterface(String code){
        binding.ordersBtn.setOnClickListener(view -> {
            Intent intent = new Intent(QRSettingsActivity.this, OrdersActivity.class);
            intent.putExtra("code", code);
            startActivityForResult(intent, 400);
        });
        binding.backBtn.setOnClickListener(view -> {
            finish();
        });

//        binding.btnIntentTables.setOnClickListener(view -> {
//            Intent intent = new Intent(QRSettingsActivity.this, QRTablesActivity.class);
//            intent.putExtra("code", code);
//            startActivityForResult(intent, 201);
//        });
        binding.btnIntentQrShow.setOnClickListener(view -> {
            System.out.println("Link in showQRActivity " + link);
            Intent intent = new Intent(QRSettingsActivity.this, ShowQrActivity.class);
            System.out.println("intent worked link is " + link);
            intent.putExtra("link", link);
            startActivity(intent);
        });

        binding.btnSetMeWaiter.setOnClickListener((l)->{
            binding.btnSetMeWaiter.setVisibility(View.GONE);
            binding.setMeWaiterConfirmBlock.setVisibility(View.VISIBLE);
        });
        binding.btnSetMeWaiterRefuse.setOnClickListener((l)->{
            binding.btnSetMeWaiter.setVisibility(View.VISIBLE);
            binding.setMeWaiterConfirmBlock.setVisibility(View.GONE);
        });
        binding.btnSetMeWaiterConfirm.setOnClickListener((l)->{
            binding.btnSetMeWaiter.setVisibility(View.VISIBLE);
            binding.setMeWaiterConfirmBlock.setVisibility(View.GONE);
            SharedPreferences sp = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

            HashMap<String, String> params = new HashMap<>();
            params.put("waiter", sp.getString("phone_number", ""));
            ApiAccess.get("codes/" + ApiAccess.getPlace() + "/waiter/bind/" + code, params,
                resp ->{
                    redraw();
                },
                error -> {
                    Log.i("some", error.getMessage());
                }
            );
        });

        binding.btnCloseCode.setOnClickListener((l)->{
            binding.btnCloseCode.setVisibility(View.GONE);
            binding.closeCodeConfirmBlock.setVisibility(View.VISIBLE);
        });
        binding.closeCodeRefuse.setOnClickListener((l)->{
            binding.btnCloseCode.setVisibility(View.VISIBLE);
            binding.closeCodeConfirmBlock.setVisibility(View.GONE);
        });
        binding.closeCodeConfirm.setOnClickListener((l)->{
            binding.btnCloseCode.setVisibility(View.VISIBLE);
            binding.closeCodeConfirmBlock.setVisibility(View.GONE);
            ApiAccess.get("codes/" + ApiAccess.getPlace() + "/close/" + code,
                resp ->{
                    redraw();
                },
                error -> {
                    Log.i("some", error.getMessage());
                }
            );
        });

        redraw(code);
    }

    void set_staff_text(){
        ApiAccess.setContext(this);
        ApiAccess.get("qr",
                response -> {

                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        if (resultCode == RESULT_OK && requestCode == 201) {
            redraw();
        }
        if (resultCode == RESULT_OK && requestCode == 400) {
            redraw();
        }
    }

    void redraw(String code) {
        ApiAccess.setContext(this);
        HashMap<String, String> params = new HashMap<>();
        params.put("code", code);


        ApiAccess.get("codes/" + ApiAccess.getPlace() + "/" + code, params,
            response -> {
                try {
                    JSONObject _code = response.getJSONObject("item");
                    link = _code.isNull("link") ? "" : _code.getString("link");
                    comment = _code.isNull("comment") ? "" : _code.getString("comment");
                    waiter_id = _code.isNull("waiter_id") ? 0 : _code.getInt("waiter_id");
                    if(waiter_id > 0)
                        binding.btnSetMeWaiter.setText("Официант назначен");
                    else
                        binding.btnSetMeWaiter.setText("Назначить меня официантом");
                    redrawFromData(_code);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });

    }

    void redrawFromData(JSONObject code){
        binding.infoList.removeAllViews();
        try {
            String c = code.toString();
            String closed_at = code.optString("closed_at", "");
            String comment = code.optString("comment","");
            boolean code_closed = !(closed_at == null || closed_at.equals("") || closed_at.equals("null"));

            TextView t = new TextView(this);
            t.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            t.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            t.setText("Комментарий: " + comment);
            binding.infoList.addView(t);

            t = new TextView(this);
            t.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            t.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            t.setText("Статус: " + (code_closed ? "закрыт" : "активен"));
            binding.infoList.addView(t);

            JSONArray orders = code.isNull("orders") ? null : code.getJSONArray("orders");
            if(orders != null && orders.length() > 0){
                for (int i = 0; i < orders.length(); i++) {
                    JSONObject order = orders.getJSONObject(i);
                    JSONObject data = order.has("data") ?  order.getJSONObject("data"): null;

                    TextView to = new TextView(this);
                    to.setTypeface(null, Typeface.BOLD);
                    to.setPadding(0, 20, 0,0);
                    to.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    to.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    to.setText("Заказ " + order.getString("order_id"));
                    binding.infoList.addView(to);

//                    to = new TextView(this);
//                    to.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//                    to.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//                    to.setText(order.toString());
//                    binding.infoList.addView(to);

                    String sum = data.isNull("sum") ? "" : data.getString("sum");
                    if(!sum.equals("")) {
                        to = new TextView(this);
                        to.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        to.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        to.setText("Сумма: " + sum + " руб.");
                        binding.infoList.addView(to);
                    }

                    if(data != null) {
                        JSONArray positions = data.getJSONArray("positions");
                        to = new TextView(this);
                        to.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        to.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        to.setText("Позиций: " + positions.length());
                        binding.infoList.addView(to);
                    }
                }
            }


            binding.ordersBtn.setVisibility( code_closed ? View.GONE: View.VISIBLE );
            binding.btnSetMeWaiter.setVisibility( code_closed ? View.GONE: View.VISIBLE );
            binding.btnCloseCode.setVisibility( code_closed ? View.GONE: View.VISIBLE );
        } catch (JSONException e) {e.printStackTrace();}
    }

    void redraw() {
        redraw(code);
    }
}

