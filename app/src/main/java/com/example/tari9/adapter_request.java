package com.example.tari9;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tari9.data.get_requests;
import com.example.tari9.data.request;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class adapter_request extends RecyclerView.Adapter<adapter_request.myviewholder> {
    Context context;
    ArrayList<get_requests> get_requests;
    private double distance;

    public adapter_request(Context context, ArrayList<get_requests> get_requests) {
        this.context = context;
        this.get_requests = get_requests;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.get_requests, parent, false);
        return new myviewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
        get_requests request = get_requests.get(position);

        /*if (request.getAddress()!=null ){
            holder.address.setText(request.getAddress().get("country").toString() + " " +
            request.getAddress().get("city").toString());
        } else {
            holder.ll_address.setVisibility(View.GONE);
        }*/
        if(request.getDate()==null){

            holder.ll_date.setVisibility(View.GONE);
        } else {
            Date date = null;
            try {
                date = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US).parse(String.valueOf(request.getDate().toDate()));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            holder.date.setText(date.toString());
        }
        if(request.getType()==null){
            holder.ll_request.setVisibility(View.GONE);
        } else {
            holder.type.setText(request.getType());
        }
        if(request.getState()==null){
            holder.ll_state.setVisibility(View.GONE);
        } else {
            holder.state.setText(request.getState());
        }
        if (request.getVehicle()!=null){
            holder.veh.setText(request.getVehicle().getType()+" "+request.getVehicle().getMark());
        } else {
            holder.ll_vehicle.setVisibility(View.GONE);
        }
        if (request.getWorker_phone()!=null){
            holder.phone.setText(request.getWorker_phone());
        } else {
            holder.ll_phone.setVisibility(View.GONE);
        }
        if(request.getPrice()==0){
            holder.ll_price.setVisibility(View.GONE);
        } else {
            holder.price.setText(String.valueOf(request.getPrice()));
        }
    }

    @Override
    public int getItemCount() {
        return get_requests.size();
    }

    public static class myviewholder extends RecyclerView.ViewHolder{
        TextView address, date, type, veh, state, price, p, distance, phone, name;
        LinearLayout v, ll_vehicle, ll_address, ll_phone, ll_price, ll_request, ll_date, ll_state;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            address = itemView.findViewById(R.id.address);
            date = itemView.findViewById(R.id.date);
            type = itemView.findViewById(R.id.type);
            phone = itemView.findViewById(R.id.phone);
            name = itemView.findViewById(R.id.name);
            distance = itemView.findViewById(R.id.distance);
            state = itemView.findViewById(R.id.state);
            p = itemView.findViewById(R.id.p);
            price = itemView.findViewById(R.id.price);
            veh = itemView.findViewById(R.id.vehicle);
            ll_vehicle = itemView.findViewById(R.id.ll_vehicle);
            ll_address = itemView.findViewById(R.id.ll_address);
            ll_phone = itemView.findViewById(R.id.ll_phone);
            ll_date = itemView.findViewById(R.id.ll_date);
            ll_price = itemView.findViewById(R.id.ll_price);
            ll_request = itemView.findViewById(R.id.ll_request);
            ll_state = itemView.findViewById(R.id.ll_state);
        }
    }
}
