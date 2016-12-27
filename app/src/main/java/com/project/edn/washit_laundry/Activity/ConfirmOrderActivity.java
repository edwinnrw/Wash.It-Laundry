package com.project.edn.washit_laundry.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
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
import com.project.edn.washit_laundry.Fragment.DatePickerFragment;
import com.project.edn.washit_laundry.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ConfirmOrderActivity extends AppCompatActivity implements View.OnClickListener {
    private  Toolbar toolbar;
    private EditText edtDate;
    private EditText edtPrice;
    private Button btnSubmit;
    private String iduser;
    private ProgressDialog progressdialog;
    private Intent in;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Order Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        edtDate=(EditText)findViewById(R.id.datefinish);
        edtDate.setKeyListener(null);
        edtDate.setFocusable(false);
        edtDate.setOnClickListener(this);
        edtPrice=(EditText)findViewById(R.id.price);
        btnSubmit=(Button)findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);
        progressdialog=new ProgressDialog(this);
        in=getIntent();
        iduser=in.getStringExtra("iduser");

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSubmit:
                int edtDateLenght=edtDate.getText().toString().trim().length();
                int edtPriceLenght=edtPrice.getText().toString().trim().length();
                if (edtDateLenght==0 || edtPriceLenght==0){
                    if (edtDateLenght==0){
                        edtDate.setError("This Field is Required");
                    }
                    if (edtPriceLenght==0){
                        edtPrice.setError("This Field is Required");
                    }
                }else{
                    showConfirm();
                }

                break;
            case R.id.datefinish:
                new DatePickerFragment(edtDate).show(getFragmentManager(), "datepicker");

        }

    }

    private void sendDataConfirm() {
        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, Config.API_CONFIRMORDER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressdialog.dismiss();

                        //Jika Respon server sukses
                        if (Success(response).equalsIgnoreCase("true")) {

                            Toast.makeText(ConfirmOrderActivity.this,"Success ",Toast.LENGTH_LONG).show();
                            Intent in=new Intent(ConfirmOrderActivity.this,MainActivity.class);
                            startActivity(in);

                        } else {
                            Toast.makeText(ConfirmOrderActivity.this, "Failed",Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressdialog.dismiss();

                        //Tambahkan apa yang terjadi setelah Pesan Error muncul, alternatif
                        Toast.makeText(ConfirmOrderActivity.this, "Failed Load Your Data,Check Your Connection" , Toast.LENGTH_LONG).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("idorder",in.getStringExtra("id"));
                params.put("date", edtDate.getText().toString());
                params.put("price", edtPrice.getText().toString());
                params.put("ket", "confirm");
                //Parameter

                return params;
            }
        };

        //Tambahkan Request String ke dalam Queue
        RequestQueue requestQueue = Volley.newRequestQueue(ConfirmOrderActivity.this);
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
    public  void showConfirm(){
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(ConfirmOrderActivity.this);
        alertDialogBuilder.setTitle("Confirm");
        TextView myMsg = new TextView(this);
        myMsg.setText(" Are You Sure ?");
        myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
        alertDialogBuilder.setView(myMsg);
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        progressdialog.setMessage("Loading");
                        sendDataConfirm();                    }
                });
        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        // Tampilkan alert dialog
        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        Button nbutton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPressed));
        Button pbutton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPressed));


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

}
