package com.example.tari9;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tari9.data.mechanic;

import java.util.ArrayList;

public class adapter_mechanics extends RecyclerView.Adapter<adapter_mechanics.myviewholder> {

    Context context;
    ArrayList<mechanic> mechanics;
    listener_mechanic ocl;

    public adapter_mechanics(Context context, ArrayList<mechanic> mechanics, listener_mechanic ocl) {
        this.context = context;
        this.mechanics = mechanics;
        this.ocl = ocl;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.get_mechanic, parent, false);
        return new myviewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
        holder.id.setText(mechanics.get(position).getId());
        if (mechanics.get(position).getFirstname()!=null) {
            holder.firstname.setText(mechanics.get(position).getFirstname());
        } else {
            holder.firstname.setText("");
        }
        if (mechanics.get(position).getLastname()!=null) {
            holder.lastname.setText(mechanics.get(position).getLastname());
        } else {
            holder.lastname.setText("");
        }
        if (mechanics.get(position).getDistance()!=0){
            holder.distance.setText(String.valueOf(mechanics.get(position).getDistance()));
        } else {
            holder.distance.setText("");
        }
        if (mechanics.get(position).getDunit()!=null){
            holder.dunit.setText(mechanics.get(position).getDunit());
        } else {
            holder.dunit.setText(mechanics.get(position).getDunit());
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dc = String.valueOf(holder.id.getText());
                ocl.onItemClicked(dc, mechanics.get(position),position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mechanics.size();
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
