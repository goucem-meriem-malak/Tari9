package com.example.tari9;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tari9.data.get_requests;

import java.util.ArrayList;

public class adapter_requests_worker extends RecyclerView.Adapter<adapter_requests_worker.myviewholder> {
    Context context;
    ArrayList<get_requests> get_requests;
    listener_request_worker ocl;

    public adapter_requests_worker(Context context, ArrayList<get_requests> get_requests, listener_request_worker ocl) {
        this.context = context;
        this.get_requests = get_requests;
        this.ocl = ocl;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.get_requests_worker, parent, false);
        return new myviewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
        holder.id.setText(get_requests.get(position).getId());
        holder.price.setText(String.valueOf(get_requests.get(position).getPrice()));
        holder.state.setText(get_requests.get(position).getState());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dc = String.valueOf(holder.id.getText());
                ocl.onItemClicked(dc, get_requests.get(position) ,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return get_requests.size();
    }

    public static class myviewholder extends RecyclerView.ViewHolder{
        TextView id, client_id, worker_id, client_address, worker_location, type, state, price, distance;
        LinearLayout buttons;
        Button reject, accept;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.id);

            client_address = itemView.findViewById(R.id.address);
            state = itemView.findViewById(R.id.state);
            price = itemView.findViewById(R.id.price);
        }
    }
}
