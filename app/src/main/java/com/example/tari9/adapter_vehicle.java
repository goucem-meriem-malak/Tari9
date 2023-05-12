package com.example.tari9;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tari9.data.veh;

import java.util.ArrayList;

public class adapter_vehicle extends RecyclerView.Adapter<adapter_vehicle.myviewholder> {

    Context context;
    ArrayList<veh> vehicles;
    listener_vehicle ocl;

    public adapter_vehicle(Context context, ArrayList<veh> vehicles, listener_vehicle ocl) {
        this.context = context;
        this.vehicles = vehicles;
        this.ocl = ocl;
    }
    public void setVehicles(ArrayList<veh> vehicles) {
        this.vehicles = vehicles;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.get_vehicle, parent, false);
        return new myviewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
        holder.matriculation.setText(vehicles.get(position).getId());
        holder.type.setText(vehicles.get(position).getType());
        holder.mark.setText(vehicles.get(position).getMark());
        holder.model.setText(vehicles.get(position).getModel());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dc = String.valueOf(holder.matriculation.getText());
                ocl.onItemClicked(dc, vehicles.get(position),position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return vehicles.size();
    }

    public static class myviewholder extends RecyclerView.ViewHolder{
        TextView matriculation, type, mark, model;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            matriculation = itemView.findViewById(R.id.matriculation);
            type = itemView.findViewById(R.id.type);
            mark = itemView.findViewById(R.id.mark);
            model = itemView.findViewById(R.id.model);
        }
    }
}