package com.example.adminside.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.adminside.fragments.NewClientFragment;
import com.example.adminside.R;

import java.util.ArrayList;

public class SearchAdapter2 extends RecyclerView.Adapter<SearchAdapter2.SearchViewHolder> {
    Context context;
    ArrayList<String> clientsDescList;
    public String myClientDesc;


    class SearchViewHolder extends RecyclerView.ViewHolder{
        TextView descTxt;
        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            descTxt = itemView.findViewById(R.id.clientDesc);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION){
                        myClientDesc = clientsDescList.get(position);
                        NewClientFragment.clientDescEdtTxt.setText(myClientDesc);
                    }
                }
            });
        }
    }

    public SearchAdapter2(Context context, ArrayList<String> clientsDescList) {
        this.context = context;
        this.clientsDescList = clientsDescList;
    }

    @NonNull
    @Override
    public SearchAdapter2.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View myView = LayoutInflater.from(context).inflate(R.layout.desc_search_layout,viewGroup,false);

        return new SearchViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder searchViewHolder, int i) {
        searchViewHolder.descTxt.setText(clientsDescList.get(i));
    }


    @Override
    public int getItemCount() {
        return clientsDescList.size();
    }
}
