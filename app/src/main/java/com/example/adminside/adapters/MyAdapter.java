package com.example.adminside.adapters;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.adminside.R;
import com.example.adminside.model.ProjectModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class MyAdapter extends  FirestoreRecyclerAdapter<ProjectModel, MyAdapter.MyViewHolder>  {
    OnItemClickListener listener;

    public MyAdapter(@NonNull FirestoreRecyclerOptions<ProjectModel> options) {
        super(options);
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.list_item,viewGroup,false);
        return new MyViewHolder(view);

    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull ProjectModel model) {
        holder.partyName.setText(model.getParty_Name());
        holder.projectDes.setText(model.getProjectDes());
        if (model.getStatus().equalsIgnoreCase("In Progress...")){
            holder.status.setText("P");
            holder.status.setTextColor(Color.parseColor("#6A89CC"));
        }else if (model.getStatus().equalsIgnoreCase("Booked !")){
            holder.status.setText("B");
            holder.status.setTextColor(Color.parseColor("#FF3E4D"));
        }
        else if (model.getStatus().equalsIgnoreCase("Finished !")){
            holder.status.setText("F");
            holder.status.setTextColor(Color.parseColor("#26ae60"));
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView partyName;
        TextView projectDes;
        TextView status;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            partyName = itemView.findViewById(R.id.mPartyName);
            projectDes = itemView.findViewById(R.id.mProjDesc);
            status = itemView.findViewById(R.id.stat);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION && listener!=null){
                        listener.onItemClick(getSnapshots().getSnapshot(position),position);
                    }
                }
            });
        }
    }
    public interface OnItemClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}
