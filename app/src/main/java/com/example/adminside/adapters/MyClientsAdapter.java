package com.example.adminside.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.adminside.R;

import java.util.ArrayList;

public class MyClientsAdapter extends RecyclerView.Adapter<MyClientsAdapter.ClientsHolder> {

    Context context;
    ArrayList<String> clinetsList;
    ArrayList<String> accntList;
    private OnItemClickListener listener;
    private OnItemLongClickListener longClickListener;

    public MyClientsAdapter(Context context,ArrayList<String> clinetsList,ArrayList<String> accntList){
        this.context = context;
        this.clinetsList = clinetsList;
        this.accntList = accntList;
    }
    @NonNull
    @Override
    public ClientsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View myView = LayoutInflater.from(context).inflate(R.layout.list_client_item,viewGroup,false);
        return new ClientsHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull ClientsHolder clientsHolder, final int i) {
        clientsHolder.nametxt.setText(clinetsList.get(i));
        clientsHolder.acctxt.setText(accntList.get(i));
        clientsHolder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v,i);
            }
        });
        clientsHolder.card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                longClickListener.onItemLongClick(v,i);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return clinetsList.size();
    }

    public class ClientsHolder extends RecyclerView.ViewHolder{
        TextView nametxt;
        TextView acctxt;
        CardView card;

        public ClientsHolder(@NonNull View itemView) {
            super(itemView);
            nametxt = itemView.findViewById(R.id.mClientnew);
            card = itemView.findViewById(R.id.mCard);
            acctxt = itemView.findViewById(R.id.actxt);
        }
    }
    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public interface OnItemLongClickListener{
        void onItemLongClick(View view,int position);
    }
    public void setOnItemLongClickListener(OnItemLongClickListener longClickListener){
        this.longClickListener = longClickListener;
    }
}
