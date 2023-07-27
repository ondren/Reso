package api;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;


import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.BuildConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ApiAccess {
    private static String host = "lite-menu.com"; // BuildConfig.HOST;
    private static String api = "/api";
    private static String protocol = "https"; // BuildConfig.HOST_PROTOCOL;
    private static int port = 0;
    private static Context context;


    public static Context getContext() {
        return context;
    }
    public static void setContext(Context ctx) {
        ApiAccess.context = ctx;
    }
    public static String getHost() {
        return host;
    }
    public static void setHost(String host) {
        ApiAccess.host = host;
    }
    public static String getApi() {
        return api;
    }
    public static void setApi(String api) {
        ApiAccess.api = api;
    }
    public static String getProtocol() {
        return protocol;
    }
    public static void setProtocol(String protocol) {
        ApiAccess.protocol = protocol;
    }
    public static String getToken() {
        SharedPreferences myPreferences
                = PreferenceManager.getDefaultSharedPreferences(getContext());
        String token = myPreferences.getString("token", "");
        return token;
    }
    public static String getFirebaseToken() {
        SharedPreferences myPreferences
                = PreferenceManager.getDefaultSharedPreferences(getContext());
        String token = myPreferences.getString("firebase_token", "");
        return token;
    }
    public static String getPlace() {
        SharedPreferences myPreferences
                = PreferenceManager.getDefaultSharedPreferences(getContext());
        String token = myPreferences.getString("place", "");
        return token;
    }
    public static void setAutoData(String token, String place) {
        SharedPreferences myPreferences
                = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor myEditor = myPreferences.edit();
        myEditor.putString("token", token);
        myEditor.putString("place", place);
        myEditor.commit();
    }
    public static void setFirebaseToken(String token) {
        SharedPreferences myPreferences
                = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor myEditor = myPreferences.edit();
        myEditor.putString("firebase_token", token);
        myEditor.commit();
    }
    public static boolean hasToken(){
        SharedPreferences myPreferences
                = PreferenceManager.getDefaultSharedPreferences(getContext());
        return !myPreferences.getString("token", "").equals("");
    }

    public static int getPort() {
        return port;
    }
    public static void setPort(int port) {
        ApiAccess.port = port;
    }



    public static String getUrl(String endpoint){
        return getUrl(endpoint, null);
    }
    public static String getUrl(String endpoint, Map<String, String> params){
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(protocol)
                .authority(host);

        builder.appendPath("api");
        builder.appendEncodedPath(endpoint);
        if(params != null && params.size() > 0){
            for(Map.Entry<String, String> param : params.entrySet()){
                builder.appendQueryParameter(param.getKey(), param.getValue());
            }
        }

        return builder.build().toString();
    }
    private static void sendRequest(String endpoint,HashMap<String, String> params, int method,
        JSONObject data, Response.Listener<JSONObject> onSuccess, Response.ErrorListener onFailure){
        if(params == null)
            params = new HashMap<>();
        params.put("place", getPlace());
        System.out.println("place: " + getPlace());
        String url = getUrl(endpoint, params);
        final String requestBody = data == null ? "" : data.toString();
        StringRequest stringRequest = new StringRequest(
                method,
                url,
                response ->{
                    try {
                        onSuccess.onResponse(new JSONObject(response));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                onFailure
        )
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
//                        params.put("password", Pass.getText().toString());x
                return params;

            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  headers = new HashMap<>();
                headers.put("x-token", getToken());
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return requestBody.getBytes(StandardCharsets.UTF_8);
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    responseString = new String(response.data, StandardCharsets.UTF_8);
                    // can get more details such as response.headers
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }


    public static void get(String endpoint, HashMap<String, String> params,
               Response.Listener<JSONObject> onSuccess, Response.ErrorListener onFailure){
        sendRequest(endpoint, params, Request.Method.GET, null, onSuccess, onFailure);
    }
    public static void get(String endpoint, HashMap<String, String> params,
               Response.Listener<JSONObject> onSuccess){
        get(endpoint, params, onSuccess, error -> {});
    }
    public static void get(String endpoint,
               Response.Listener<JSONObject> onSuccess){
        get(endpoint, new HashMap<>(), onSuccess, error -> {});
    }
    public static void get(String endpoint,
               Response.Listener<JSONObject> onSuccess, Response.ErrorListener onFailure){
        get(endpoint, new HashMap<>(), onSuccess, onFailure);
    }

    public static void post(String endpoint, HashMap<String, String> params, JSONObject data,
               Response.Listener<JSONObject> onSuccess, Response.ErrorListener onFailure){
        sendRequest(endpoint, params, Request.Method.POST, data, onSuccess, onFailure);
    }
    public static void post(String endpoint, JSONObject data,
               Response.Listener<JSONObject> onSuccess){
        post(endpoint, new HashMap<>(), data, onSuccess, error -> {});
    }
    public static void post(String endpoint, JSONObject data,
               Response.Listener<JSONObject> onSuccess, Response.ErrorListener onFailure){
        post(endpoint, new HashMap<>(), data, onSuccess,onFailure);
    }

}

