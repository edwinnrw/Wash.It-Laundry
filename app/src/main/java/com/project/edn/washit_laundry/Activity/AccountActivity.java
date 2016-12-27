package com.project.edn.washit_laundry.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.project.edn.washit_laundry.R;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    TextView editbtn1,editbtn2;
    private TextView Storename,Address,Material,Hour,Service;
    private TextView Nama,Email,Telp;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        editbtn1=(TextView)findViewById(R.id.editbtn1);
        editbtn1.setOnClickListener(this);
        editbtn2=(TextView)findViewById(R.id.editbtn2);
        editbtn2.setOnClickListener(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Nama = (TextView) findViewById(R.id.txtName);
        Nama.setText(this.sharedPreferences.getString("name", ""));
        Email = (TextView) findViewById(R.id.txtEmail);
        Email.setText(this.sharedPreferences.getString("email", ""));
        Telp = (TextView) findViewById(R.id.txtPhone);
        Telp.setText(this.sharedPreferences.getString("telp", ""));
        Storename = (TextView) findViewById(R.id.namelaundry);
        Storename.setText(sharedPreferences.getString("storename",""));
        Address = (TextView) findViewById(R.id.addresLaundry);
        Address.setText(sharedPreferences.getString("address",""));
        Material = (TextView) findViewById(R.id.txtMaterial);
        Material.setText(sharedPreferences.getString("material",""));
        Service = (TextView) findViewById(R.id.txtService);
        Service.setText(sharedPreferences.getString("service",""));
        Hour = (TextView) findViewById(R.id.txtHour);
        Hour.setText(sharedPreferences.getString("hour",""));
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
        Intent in;
        switch (view.getId()){
            case R.id.editbtn1:
                in=new Intent(this,EditDetailStore.class);
                startActivity(in);
                break;
            case R.id.editbtn2:
                in=new Intent(this,EditDetailOwner.class);
                startActivity(in);
                break;

        }
    }
}
