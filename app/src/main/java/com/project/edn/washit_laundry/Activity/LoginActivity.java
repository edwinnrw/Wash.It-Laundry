package com.project.edn.washit_laundry.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project.edn.washit_laundry.Config;
import com.project.edn.washit_laundry.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener {
    private Button btnlogin;
    private EditText username;
    private EditText password;
    private SharedPreferences sharedPreferences;
    private boolean loggedIn;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnlogin = (Button) findViewById(R.id.email_sign_in_button);
        username = (EditText) findViewById(R.id.txt_email);
        username.addTextChangedListener(this);
        password = (EditText) findViewById(R.id.txt_password);
        password.addTextChangedListener(this);
        btnlogin.setOnClickListener(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        loggedIn = this.sharedPreferences.getBoolean(Config.LOGGEDIN_SHARED_PREF, false);
        if (loggedIn) {
            startActivity(new Intent(this, MainActivity.class));
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        if (this.loggedIn) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    public void onBackPressed() {
        finish();
    }

    @Override
    public void onClick(View view) {

        Intent in;
        switch (view.getId()) {
            case R.id.email_sign_in_button /*2131558566*/:
                int usernameLenght = username.getText().toString().trim().length();
                int passwordLength = password.getText().toString().trim().length();
                if (usernameLenght == 0 || passwordLength == 0) {
                    if (usernameLenght == 0) {
                        username.setError("This Field is Required");
                    } else if (!this.username.getText().toString().matches("[A-Za-z~@#$%^&*:;<>.,/}{+ ]")) {
                        username.setError("Invalid Username");
                    }
                    if (passwordLength == 0) {
                        password.setError("This Field is Required");
                    } else if (!this.password.getText().toString().matches("[A-Za-z~@#$%^&*:;<>.,/}{+ ]")) {
                        password.setError("Invalid Password");
                    }
                }else {
                    progressDialog.show();
                    RequestLogin(username.getText().toString().trim(), password.getText().toString().trim());
                }

            default:
        }
    }
    private void RequestLogin(final String username, final String password){
        //Buatkan Request Dalam bentuk String
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.API_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Jika Respon server sukses
                        progressDialog.dismiss();

                        if (LoginActivity.this.Success(response).equalsIgnoreCase("true")) {
                            logintomain(response);
                            return;
                        }else {
                            Toast.makeText(LoginActivity.this, "Invalid Email Or Password", Toast.LENGTH_LONG).show();

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Tambahkan apa yang terjadi setelah Pesan Error muncul, alternatif
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Login Failed,Check Your Connection"+error.getMessage(), Toast.LENGTH_LONG).show();

                    }
                }){
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("email", username);
                params.put("password", password);
                return params;
            }
        };

        //Tambahkan Request String ke dalam Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
//            int socketTimeout = 10000;//30 seconds - change to what you want
//            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//            stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }
    public void logintomain(String json) {
        this.progressDialog.dismiss();
        String token = "";
        String name ="";;
        String email ="";;
        String telp = "";;
        String storename="";
        String address="";
        String material="";
        String service="";
        String hour="";
        String id="";
        try {
            JSONObject json2 = ((JSONObject) new JSONTokener(json).nextValue()).getJSONObject("data");
//            storename = json3.getString("storename");
//            address = json3.getString("address");
//            material = json3.getString("material");
//            service = json3.getString("service");
//            hour = json3.getString("hour");
            token = json2.getString(Config.TOKEN_SHARED_PREF);
            name = json2.getString("name");
            email = json2.getString("email");
            telp = json2.getString("phone");
            id=json2.getString("id_laundry");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, true);
        editor.putString("name", name);
        editor.putString("email", email);
        editor.putString("telp", telp);
        editor.putString("storename", storename);
        editor.putString("address", address);
        editor.putString("material", material);
        editor.putString("service", service);
        editor.putString("hour", hour);

        editor.putString(Config.TOKEN_SHARED_PREF, token);
        editor.commit();
        startActivity(new Intent(this, MainActivity.class));
    }

    public String Success(String json) {
        String succes = "";
        try {
            succes = new JSONObject("{\"response\":" + json + "}").getJSONObject("response").getString("success");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return succes;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (username.getText().toString().trim().length()!=0){
            if (!username.getText().toString().matches("[A-Za-z~@#$%^&*:;<>.,/}{+ ]*")){
                username.setError(null);
            }
        }
        if (password.getText().toString().length()!=0){
            password.setError(null);
        }

    }
}
