package com.example.tari9;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class viewholder extends RecyclerView.ViewHolder {

    public TextView tvfullname;
    public TextView tvdistance;
    public CardView each;
    private String fullname;
    private float distance;

    public viewholder(@NonNull View itemView) {
        super(itemView);

        tvfullname = itemView.findViewById(R.id.name);
        each = itemView.findViewById(R.id.each);
    }
}
