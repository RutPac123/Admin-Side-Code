package com.example.adminside.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.adminside.R;

import java.util.ArrayList;

public class TransAdapter extends RecyclerView.Adapter<TransAdapter.TransViewHolder> {

    private ArrayList<String> dateList;
    private ArrayList<Long> amntList;

    public TransAdapter(ArrayList<String> dateList, ArrayList<Long> amntList) {
        this.dateList = dateList;
        this.amntList = amntList;
    }

    @NonNull
    @Override
    public TransViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.trans_item,viewGroup,false);
        return new TransViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransViewHolder transViewHolder, int i) {
            transViewHolder.date.setText(dateList.get(i));
            transViewHolder.amount.setText("₹. "+String.valueOf(amntList.get(i)));
    }

    @Override
    public int getItemCount() {
        return dateList.size();
    }

    public class TransViewHolder extends RecyclerView.ViewHolder{

        TextView date;
        TextView amount;
        public TransViewHolder(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.transdate);
            amount = itemView.findViewById(R.id.amntpaidtill);
        }
    }
}
