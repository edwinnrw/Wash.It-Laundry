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

import java.util.HashMap;
import java.util.Map;

public class EditDetailStore extends AppCompatActivity implements View.OnClickListener{
    private Toolbar toolbar;
    private EditText editStorename,editAddress,editMaterial,editHour,editService;
    private Button btnedit;
    private SharedPreferences sharedPreferences;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_detail_store);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit Store");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editStorename = (EditText) findViewById(R.id.editStorename);
        editStorename.setText(sharedPreferences.getString("storename",""));
        editAddress = (EditText) findViewById(R.id.editAddress);
        editAddress.setText(sharedPreferences.getString("address",""));
        editMaterial = (EditText) findViewById(R.id.editMaterial);
        editMaterial.setText(sharedPreferences.getString("material",""));
        editService = (EditText) findViewById(R.id.editService);
        editService.setText(sharedPreferences.getString("service",""));
        editHour = (EditText) findViewById(R.id.editBussinesshour);
        editHour.setText(sharedPreferences.getString("hour",""));
        btnedit = (Button) findViewById(R.id.btnedit);
        btnedit.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
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
    @Override
    public void onClick(View view) {
        int editStorenameLenght=editStorename.getText().toString().trim().length();
        int editAddressLenght=editAddress.getText().toString().trim().length();
        int editMateriialLenght=editMaterial.getText().toString().trim().length();
        int editServicelLenght=editService.getText().toString().trim().length();
        int editHourLenght=editHour.getText().toString().trim().length();

        if (editStorenameLenght==0||editAddressLenght==0||editMateriialLenght==0||editServicelLenght==0||editHourLenght==0){
            if (editStorenameLenght==0){
                editStorename.setError("This Field is Required");
            }if (editAddressLenght==0){
                editAddress.setError("This Field is Required");

            }if (editMateriialLenght==0){
                editMaterial.setError("This Field is Required");

            }if (editServicelLenght==0){
                editService.setError("This Field is Required");

            }if (editHourLenght==0){
                editHour.setError("This Field is Required");

            }
        }else{
            progressDialog.show();
            requestChange();
        }
    }

    private void requestChange() {
        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, Config.API_STORECHANGE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        //Jika Respon server sukses
                        if (Success(response).equalsIgnoreCase("true")) {
                            Toast.makeText(EditDetailStore.this, "Data successfully changed ", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(EditDetailStore.this, "Data Not Successfully changed",Toast.LENGTH_LONG).show();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();

                        //Tambahkan apa yang terjadi setelah Pesan Error muncul, alternatif
                        Toast.makeText(EditDetailStore.this, "Failed Load Your Data,Check Your Connection" , Toast.LENGTH_LONG).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("storename", editStorename.getText().toString().trim());
                params.put("address", editAddress.getText().toString().trim());
                params.put("material",editMaterial.getText().toString().trim());
                params.put("service", editService.getText().toString().trim());
                params.put("hour", editHour.getText().toString().trim());
                params.put(Config.TOKEN_SHARED_PREF, sharedPreferences.getString(Config.TOKEN_SHARED_PREF,""));
                return params;
            }
        };

        //Tambahkan Request String ke dalam Queue
        RequestQueue requestQueue = Volley.newRequestQueue(EditDetailStore.this);
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


}
