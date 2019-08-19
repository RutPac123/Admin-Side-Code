package com.example.adminside.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.adminside.R;

import java.util.ArrayList;

public class ProjectsAdapter extends RecyclerView.Adapter<ProjectsAdapter.ProjectsViewHolder> {
    OnItemClickListener clicklistener;
    Context context;
    ArrayList<String> projList;
    ArrayList<String> partyNameList;
    ArrayList<String> statusList;
    public String myProject;
    public String myParty;
    public String myStatus;

    public ProjectsAdapter(Context context,ArrayList<String> partyNameList,ArrayList<String> projList,ArrayList<String> statusList){
        this.context = context;
        this.partyNameList = partyNameList;
        this.projList = projList;
        this.statusList = statusList;
    }

    @NonNull
    @Override
    public ProjectsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.list_item,viewGroup,false);

        return new ProjectsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectsViewHolder projectsViewHolder, final int i) {
      projectsViewHolder.partyName.setText(partyNameList.get(i));
      projectsViewHolder.projectDes.setText(projList.get(i));
      if (statusList.get(i).equals("Booked !")){
          projectsViewHolder.status.setText("B");
          projectsViewHolder.status.setTextColor(Color.parseColor("#FF3E4D"));
      }else if (statusList.get(i).equals("In Progress...")){
          projectsViewHolder.status.setText("I");
          projectsViewHolder.status.setTextColor(Color.parseColor("#6A89CC"));
      }else if (statusList.get(i).equals("Delivering")){
          projectsViewHolder.status.setText("D");
          projectsViewHolder.status.setTextColor(Color.parseColor("#8B78E6"));
      }else{
          projectsViewHolder.status.setText("F");
          projectsViewHolder.status.setTextColor(Color.parseColor("#26ae60"));
      }
      projectsViewHolder.cardMy.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              clicklistener.onItemClick(v,i);
          }
      });
    }

    @Override
    public int getItemCount() {
        return projList.size();
    }

    public class ProjectsViewHolder extends RecyclerView.ViewHolder{
        TextView partyName;
        TextView projectDes;
        TextView status;
        CardView cardMy;
        public ProjectsViewHolder(@NonNull View itemView) {
            super(itemView);

            partyName = itemView.findViewById(R.id.mPartyName);
            projectDes = itemView.findViewById(R.id.mProjDesc);
            status = itemView.findViewById(R.id.stat);
            cardMy = itemView.findViewById(R.id.mCard);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION){
                        myProject = projList.get(position);
                        myParty = partyNameList.get(position);
                        myStatus = statusList.get(position);
                    }
                }
            });
        }
    }
    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }
    public void setOnItemClickListener(OnItemClickListener clicklistener){
        this.clicklistener = clicklistener;
    }

}
