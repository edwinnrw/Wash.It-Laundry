package com.project.edn.washit_laundry.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.project.edn.washit_laundry.Activity.DetailHistoryActivity;
import com.project.edn.washit_laundry.Fragment.CompleteFragment;
import com.project.edn.washit_laundry.Model.History;
import com.project.edn.washit_laundry.R;

import java.util.List;

/**
 * Created by EDN on 10/6/2016.
 */

public class HistoryListAdapter extends RecyclerView.Adapter<HistoryListAdapter.MyViewHolder> {

    private List<History> historyList;
    private Context context;
    String lgt,lat;



    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public String ket,customername,orderno,datefinish,cost,orderdate,iduser,telp;

        public TextView date, status,addresspick;
        public ImageView image;
        public LinearLayout map;
        public MyViewHolder(View view) {
            super(view);
            addresspick=(TextView)view.findViewById(R.id.addresspick);
            date = (TextView) view.findViewById(R.id.datepick);
            status = (TextView) view.findViewById(R.id.statusHistory);


            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Intent in=new Intent(context, DetailHistoryActivity.class);
            in.putExtra("status",status.getText().toString());
            in.putExtra("idorder",orderno);
            in.putExtra("customername",customername);
            in.putExtra("addresscustomer",addresspick.getText().toString());
            in.putExtra("orderDate",orderdate);
            in.putExtra("datefinish",datefinish);
            in.putExtra("datepick",date.getText().toString());
            in.putExtra("cost",cost);
            in.putExtra("Ket",ket);
            in.putExtra("telp",telp);
            context.startActivity(in);
        }
    }
    public HistoryListAdapter(Context context, List<History> historyList) {
        this.historyList = historyList;
        this.context = context;


    }
    public HistoryListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list_history, null);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HistoryListAdapter.MyViewHolder holder, int position) {
        History history =historyList.get(position);

        holder.date.setText(history.getDatepick());
        holder.status.setText(history.getStatus());
        holder.addresspick.setText(history.getAddressCustomer());

        holder.orderno=history.getId();
        holder.customername=history.getName();
        holder.orderdate=history.getOrderdate();
        holder.datefinish=history.getDatefinish();
        holder.cost=history.getPrice();
        holder.ket=history.getKeterangan();
        holder.iduser=history.getIduser();
        holder.telp=history.getTelp();

    }


    @Override
    public int getItemCount() {
        return (null != historyList? historyList.size() : 0);
    }
}
