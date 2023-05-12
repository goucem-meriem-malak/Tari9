package com.example.tari9;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tari9.data.ambulance;

import java.util.ArrayList;

public class adapter_ambulance extends RecyclerView.Adapter<adapter_ambulance.myviewholder> {

    Context context;
    ArrayList<ambulance> ambulances;
    listener_ambulance ocl;

    public adapter_ambulance(Context context, ArrayList<ambulance> ambulances, listener_ambulance ocl) {
        this.context = context;
        this.ambulances = ambulances;
        this.ocl = ocl;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.get_ambulance, parent, false);
        return new myviewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
        holder.id.setText(ambulances.get(position).getId());
        holder.firstname.setText(ambulances.get(position).getFirstname());
        holder.lastname.setText(ambulances.get(position).getLastname());
        holder.distance.setText(String.valueOf(ambulances.get(position).getDistance()));
        holder.dunit.setText(ambulances.get(position).getDunit());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dc = String.valueOf(holder.id.getText());
                ocl.onItemClicked(dc, ambulances.get(position),position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ambulances.size();
    }

    public static class myviewholder extends RecyclerView.ViewHolder{
        TextView id, firstname, lastname, distance, dunit;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.id);
            firstname = itemView.findViewById(R.id.firstname);
            lastname = itemView.findViewById(R.id.lastname);
            distance = itemView.findViewById(R.id.distance);
            dunit = itemView.findViewById(R.id.dunit);
        }
    }
}
