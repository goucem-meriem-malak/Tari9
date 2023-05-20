package com.example.tari9;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tari9.data.taxi;

import java.util.ArrayList;

public class adapter_taxi extends RecyclerView.Adapter<adapter_taxi.myviewholder> {

    Context context;
    ArrayList<taxi> taxis;
    listener_taxi ocl;

    public adapter_taxi(Context context, ArrayList<taxi> taxis, listener_taxi ocl) {
        this.context = context;
        this.taxis = taxis;
        this.ocl = ocl;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.get_taxi, parent, false);
        return new myviewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
        holder.id.setText(taxis.get(position).getId());
        holder.firstname.setText(taxis.get(position).getFirstname());
        holder.lastname.setText(taxis.get(position).getLastname());
        holder.distance.setText(String.valueOf(taxis.get(position).getDistance()));
        holder.dunit.setText(taxis.get(position).getDunit());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dc = String.valueOf(holder.id.getText());
                ocl.onItemClicked(dc, taxis.get(position),position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return taxis.size();
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
