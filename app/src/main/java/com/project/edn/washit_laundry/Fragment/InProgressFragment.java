package com.project.edn.washit_laundry.Fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project.edn.washit_laundry.Adapter.HistoryListAdapter;
import com.project.edn.washit_laundry.Config;
import com.project.edn.washit_laundry.Model.History;
import com.project.edn.washit_laundry.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InProgressFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private ProgressBar progressBar;
    private List<History> historyList;
    private RecyclerView mRecyclerView;
    private HistoryListAdapter adapter;
    private TextView noorder;
    private Handler splashHandler = new Handler();
    private String token;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private SharedPreferences sharedPreferences;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_in_progress, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        noorder=(TextView)view.findViewById(R.id.noorder);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar123);
        progressBar.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        sharedPreferences=PreferenceManager.getDefaultSharedPreferences(getActivity());
        token=sharedPreferences.getString("token","");
        getJsonHistory("new","complete");
        parseJson();

    }

    public void getJsonHistory(final String ketlist,final String ketParam) {
        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, Config.API_LISTORDER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

                        //Jika Respon server sukses
                        if (Success(response).equalsIgnoreCase("true")){
                            if (!sharedPreferences.getString("json","").equalsIgnoreCase(response)){
                                if (ketlist.equalsIgnoreCase("update")) {

                                    //Getting editor
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    // put nilai false untuk login
                                    editor.putString("json", response);
                                    editor.commit();
                                    getData();


                                }if (ketlist.equalsIgnoreCase("new")){
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    // put nilai false untuk login
                                    editor.putString("json", response);
                                    editor.commit();
                                    parseJson();
                                }

                            }else {
                                progressBar.setVisibility(View.GONE);

                                if(ketlist.equalsIgnoreCase("new")){
                                    parseJson();

                                }
//                                    }
                                //                                    parceJson();
//                                    mSwipeRefreshLayout.setRefreshing(false);
                            }

                        }else {

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Tambahkan apa yang terjadi setelah Pesan Error muncul, alternatif
                        Toast.makeText(getActivity(), "Failed Load Your Data,Check Your Connection" , Toast.LENGTH_LONG).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Parameter
                params.put("token",token);
                params.put("ket", "In Progress");//                params.put("token", token);
                //Kembalikan Nilai parameter
                return params;
            }
        };

        //Tambahkan Request String ke dalam Queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
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
    public void parseJson(){
        progressBar.setVisibility(View.GONE);
        historyList = new ArrayList<>();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String jsonCache=sharedPreferences.getString("json", "");


        try {
//            Parsing json
            JSONObject json1 = (JSONObject) new JSONTokener(jsonCache).nextValue();
            JSONArray json2 = json1.getJSONArray("data");
            for (int i = 0; i < json2.length(); i++) {
                JSONObject post = json2.optJSONObject(i);
                History item = new History();
                item.setName(post.optString("name"));
                item.setId(post.optString("orderid"));
                item.setAddressLaundry(post.optString("addressLaundry"));
                item.setAddressCustomer(post.optString("addressCustomer"));
                item.setStatus(post.optString("status"));
                item.setDatepick(post.optString("datepick"));
                item.setDatefinish(post.optString("datefinish"));
                item.setOrderdate(post.optString("orderdate"));
                item.setPrice(post.optString("cost"));
                item.setKeterangan(post.optString("keterangan"));
                item.setIduser(post.optString("id_user"));
                historyList.add(item);
                adapter = new HistoryListAdapter(getActivity(), historyList);
                mRecyclerView.setAdapter(adapter);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onRefresh() {
        updateOperation();
    }

    protected void updateOperation() {

        Runnable r =new Runnable() {
            @Override
            public void run() {
//                historyList.clear();
                getJsonHistory("update","complete");
                mSwipeRefreshLayout.setRefreshing(false);

            }
        };
        splashHandler.postDelayed(r,5000);

    }

    private void getData() {
        History item =new History();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String jsonCache=sharedPreferences.getString("json", "");
        try {
            JSONObject json1= (JSONObject) new JSONTokener(jsonCache).nextValue();
            JSONArray json2 = json1.getJSONArray("data");

            for (int i=0 ; i<json2.length();i++){
                JSONObject post = json2.optJSONObject(i);
                item.setName(post.optString("name"));
                item.setImage(post.optString("image"));
                item.setId(post.optString("orderid"));
                item.setAddressLaundry(post.optString("addressLaundry"));
                item.setAddressCustomer(post.optString("addressCustomer"));
                item.setStatus(post.optString("status"));
                item.setDatepick(post.optString("datepick"));
                item.setDatefinish(post.optString("datefinish"));
                item.setOrderdate(post.optString("orderdate"));
                item.setPrice(post.optString("cost"));
                item.setKeterangan(post.optString("keterangan"));
                item.setIduser(post.optString("id_user"));
                historyList.add(0,item);
            }


        }  catch (JSONException e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();

    }

}
