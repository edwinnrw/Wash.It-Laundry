package com.project.edn.washit_laundry.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.project.edn.washit_laundry.Config;
import com.project.edn.washit_laundry.Fragment.CompleteFragment;
import com.project.edn.washit_laundry.Fragment.InComing;
import com.project.edn.washit_laundry.Fragment.InProgressFragment;
import com.project.edn.washit_laundry.R;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ViewPager viewPager;
    private SmartTabLayout viewPagerTab;
    private boolean loggedIn;
    private String token;
    private          boolean twice;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add("Incoming", InComing.class)
                .add("Inprogress", InProgressFragment.class)
                .add("Complete", CompleteFragment.class)
                .create());

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(viewPager);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
        token = sharedPreferences.getString(Config.TOKEN_SHARED_PREF, "");
        loggedIn = sharedPreferences.getBoolean(Config.LOGGEDIN_SHARED_PREF, false);
        if (!loggedIn) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        if(twice==true){
            Intent intent= new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

            //Buatkan Sebuah variabel Editor Untuk penyimpanan Nilai shared preferences
            SharedPreferences.Editor editor = sharedPreferences.edit();

            //Tambahkan Nilai ke Editor
            editor.putBoolean("ad",false);
            //Simpan Nilai ke Variabel editor
            editor.commit();
            startActivity(intent);

            finish();
            System.exit(0);
        }
        twice=true;


        Toast.makeText(MainActivity.this, "Please Press Back again to Exit", Toast.LENGTH_LONG).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                twice = false;

            }
        }, 3000);
        twice=true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Intent in;
        int id = item.getItemId();

        if (id == R.id.nav_account) {
            in = new Intent(MainActivity.this, AccountActivity.class);
            startActivity(in);
            // Handle the camera action
        } else if (id == R.id.nav_logout) {
            progressDialog.show();
            RequestJsonLogout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void RequestJsonLogout() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.API_LOGOUT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Jika Respon server sukses
                        progressDialog.dismiss();

                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
                        editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, false);
                        editor.putString(Config.TOKEN_SHARED_PREF, "");
                        editor.putString("name", "");
                        editor.putString("email", "");
                        editor.putString("telp","");
                        editor.commit();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Tambahkan apa yang terjadi setelah Pesan Error muncul, alternatif
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Logout Failed,Check Your Connection", Toast.LENGTH_LONG).show();

                    }
                }){
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put(Config.TOKEN_SHARED_PREF,token);
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

}
