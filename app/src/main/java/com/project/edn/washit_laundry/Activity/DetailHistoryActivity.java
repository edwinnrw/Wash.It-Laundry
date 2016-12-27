package com.project.edn.washit_laundry.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.project.edn.washit_laundry.Config;
import com.project.edn.washit_laundry.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DetailHistoryActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnreport,btnconfirm,btnReject;
    private TextView noorder,addresspick,orderdate,datefinish,datepick,cost,customername,status,number;
    private Toolbar toolbar;
    private LinearLayout lnbtn;
    private String iduser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Order Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Intent in=getIntent();
        datefinish = (TextView) findViewById(R.id.datefinish);
        datefinish.setText(in.getStringExtra("datefinish"));
        datepick = (TextView) findViewById(R.id.datePick);
        datepick.setText(in.getStringExtra("datepick"));
        cost = (TextView) findViewById(R.id.cost);
        cost.setText(in.getStringExtra("cost"));
        addresspick = (TextView) findViewById(R.id.addressCostomer);
        addresspick.setText(in.getStringExtra("addresscustomer"));
        orderdate = (TextView) findViewById(R.id.dateOrder);
        orderdate.setText(in.getStringExtra("orderdate"));
        customername = (TextView) findViewById(R.id.customername);
        customername.setText(in.getStringExtra("customername"));
        status = (TextView) findViewById(R.id.status);
        status.setText(in.getStringExtra("status"));
        noorder = (TextView) findViewById(R.id.orderno);
        this.noorder.setText("Order No:" + in.getStringExtra("noorder"));
        lnbtn=(LinearLayout)findViewById(R.id.lnbtn);
        btnreport=(Button)findViewById(R.id.btnreport);
        btnconfirm=(Button)findViewById(R.id.btnconfirm);
        btnReject=(Button)findViewById(R.id.btnReject);
        btnreport.setOnClickListener(this);
        btnconfirm.setOnClickListener(this);
        btnReject.setOnClickListener(this);
        iduser=in.getStringExtra("iduser");

        switch (in.getStringExtra("Ket")){
            case "Complete":
                lnbtn.setVisibility(View.GONE);
                btnreport.setVisibility(View.GONE);
                break;
            case "Inprogress":
                lnbtn.setVisibility(View.GONE);
                btnreport.setVisibility(View.VISIBLE);
                break;
            case "Incoming":
                lnbtn.setVisibility(View.VISIBLE);
                btnreport.setVisibility(View.GONE);
                datefinish.setText("Waiting");
                break;

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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnconfirm:
                Intent in =new Intent(this,ConfirmOrderActivity.class);
                in.putExtra("id", this.noorder.getText().toString());
                startActivity(in);
                break;
            case R.id.btnReject:
                sendInfoOrder("reject");
                break;
            case R.id.btnreport:
                sendInfoOrder("progress");
                break;
        }
    }

    private void sendInfoOrder(final String ket) {
        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, Config.API_CONFIRMORDER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //Jika Respon server sukses
                        if (Success(response).equalsIgnoreCase("true")) {
                            Toast.makeText(DetailHistoryActivity.this, "Success ", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(DetailHistoryActivity.this, "Failed ", Toast.LENGTH_LONG).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Tambahkan apa yang terjadi setelah Pesan Error muncul, alternatif
                        Toast.makeText(DetailHistoryActivity.this, "Failed Load Your Data,Check Your Connection" , Toast.LENGTH_LONG).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Parameter
                params.put("idorder", noorder.getText().toString());
                params.put("keterangan",ket);
//                params.put("")
//                params.put("token", token);
                //Kembalikan Nilai parameter
                return params;
            }
        };

        //Tambahkan Request String ke dalam Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
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
