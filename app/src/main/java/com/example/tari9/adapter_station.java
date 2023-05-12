package com.example.tari9;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class adapter_station extends RecyclerView.Adapter<adapter_station.myviewholder> {

    Context context;
    ArrayList<com.example.tari9.data.station> station;
    listener_station ocl;

    public adapter_station(Context context, ArrayList<com.example.tari9.data.station> station, listener_station ocl) {
        this.context = context;
        this.station = station;
        this.ocl = ocl;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.get_station, parent, false);
        return new myviewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
        holder.id.setText(station.get(position).getId());
        holder.name.setText(station.get(position).getName());
        holder.distance.setText(String.valueOf(station.get(position).getDistance()));
        holder.dunit.setText(station.get(position).getDunit());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dc = String.valueOf(holder.id.getText());
                ocl.onItemClicked(dc,station.get(position),position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return station.size();
    }

    public static class myviewholder extends RecyclerView.ViewHolder{
        TextView id, name, distance, dunit;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.id);
            name = itemView.findViewById(R.id.name);
            distance = itemView.findViewById(R.id.distance);
            dunit = itemView.findViewById(R.id.dunit);
        }
    }
}
