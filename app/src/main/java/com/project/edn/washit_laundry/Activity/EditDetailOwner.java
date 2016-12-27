package com.project.edn.washit_laundry.Activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class EditDetailOwner extends AppCompatActivity implements View.OnClickListener{
    private  Toolbar toolbar;
    private EditText editNama,editEmail,editTelp;
    private Button btnedit;
    private SharedPreferences sharedPreferences;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_detail_owner);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit Owner");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editNama = (EditText) findViewById(R.id.editNama);
        editNama.setText(this.sharedPreferences.getString("name", ""));
        editEmail = (EditText) findViewById(R.id.editEmail);
        editEmail.setText(this.sharedPreferences.getString("email", ""));
        editTelp = (EditText) findViewById(R.id.editTelp);
        editTelp.setText(this.sharedPreferences.getString("telp", ""));
        btnedit = (Button) findViewById(R.id.btnedit);
        btnedit.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);

    }

    @Override
    public void onClick(View view) {
        int editNamaLenght=editNama.getText().toString().trim().length();
        int editEmailLenght=editEmail.getText().toString().trim().length();
        int editTelpLenght=editTelp.getText().toString().trim().length();
        if (editNamaLenght==0||editEmailLenght==0||editTelpLenght==0){
            if (editNamaLenght==0){
                editNama.setError("This Field is Required");
            }if (editEmailLenght==0){
                editEmail.setError("This Field is Required");

            }if (editTelpLenght==0){
                editTelp.setError("This Field is Required");

            }
        }else {
            progressDialog.setMessage("Loading");
            progressDialog.show();
            requestChange();
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void requestChange() {
        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, Config.API_OWNERCHANGE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        if (Success(response).equalsIgnoreCase("true")) {
                            Toast.makeText(EditDetailOwner.this, "Data successfully changed ", Toast.LENGTH_LONG).show();
                            jsonParse(response);
                            return;
                        }
                        Toast.makeText(EditDetailOwner.this, "Data Not Successfully changed", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();

                        //Tambahkan apa yang terjadi setelah Pesan Error muncul, alternatif
                        Toast.makeText(EditDetailOwner.this, "Failed Load Your Data,Check Your Connection" , Toast.LENGTH_LONG).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Parameter
                params.put(Config.TOKEN_SHARED_PREF, sharedPreferences.getString(Config.TOKEN_SHARED_PREF,""));
                params.put("email", editEmail.getText().toString().trim());
                params.put("name", editNama.getText().toString().trim());
                params.put("telp", editTelp.getText().toString().trim());
                return params;
            }
        };

        //Tambahkan Request String ke dalam Queue
        RequestQueue requestQueue = Volley.newRequestQueue(EditDetailOwner.this);
//        int socketTimeout = 10000;//30 seconds - change to what you want
//        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        stringRequest1.setRetryPolicy(policy);
        requestQueue.add(stringRequest1);
    }
    public String Success(String json) {
        String succes = "";
        try {
            String JSON_STRING = "{\"response\":" + json + "}";
            JSONObject emp = (new JSONObject(JSON_STRING)).getJSONObject("response");
            succes = emp.getString("success");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return succes;
    }
    public void jsonParse(String json) {
        this.progressDialog.dismiss();
        String token = "";
        String name = "";
        String email = "";
        String telp ="";
        try {
            JSONObject json2 = ((JSONObject) new JSONTokener(json).nextValue()).getJSONObject("data");
            name = json2.getString("name");
            email = json2.getString("email");
            telp = json2.getString("telp");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, true);
        editor.putString("name", name);
        editor.putString("email", email);
        editor.putString("telp", telp);
        editor.putString(Config.TOKEN_SHARED_PREF, token);
        editor.commit();
    }

}
