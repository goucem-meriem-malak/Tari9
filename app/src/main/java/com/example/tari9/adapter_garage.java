package com.example.tari9;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tari9.data.garage;

import java.util.ArrayList;

public class adapter_garage extends RecyclerView.Adapter<adapter_garage.myviewholder> {

    Context context;
    ArrayList<garage> garages;
    listener_garage ocl;

    public adapter_garage(Context context, ArrayList<garage> garages, listener_garage ocl) {
        this.context = context;
        this.garages = garages;
        this.ocl = ocl;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.get_garage, parent, false);
        return new myviewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
        holder.id.setText(garages.get(position).getId());
        if (garages.get(position).getName()==null){
            holder.name.setText("");
        } else {
            holder.name.setText(garages.get(position).getName());
        }
        if (garages.get(position).getDistance()==0){
            holder.distance.setText("");
        } else {
            holder.distance.setText(String.valueOf(garages.get(position).getDistance()));
        }
        if (garages.get(position).getDunit()==null){
            holder.dunit.setText("");
        } else {
            holder.dunit.setText(garages.get(position).getDunit());
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dc = String.valueOf(holder.id.getText());
                ocl.onItemClicked(dc, garages.get(position),position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return garages.size();
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
