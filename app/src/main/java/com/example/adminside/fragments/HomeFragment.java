package com.example.adminside.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.example.adminside.R;
import com.example.adminside.adapters.ProjectsAdapter;
import com.example.adminside.model.ProjectModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private TextView totalProj;
    private TextView progressProj;
    private TextView completedProj;
    private TextView deliveredProj;
    private RecyclerView progressProjList;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = firestore.collection("Projects");
    private ArrayList<String> projInProgList;
    private ArrayList<String> projInProgDescList;
    private ArrayList<String> statusList;
    private PullRefreshLayout refreshLayout;
    private AVLoadingIndicatorView loadingIndicatorView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.home_layout,container,false);

        totalProj = myView.findViewById(R.id.totalProj);
        progressProj = myView.findViewById(R.id.progressproj);
        deliveredProj = myView.findViewById(R.id.projDel);
        completedProj = myView.findViewById(R.id.projCompl);
        progressProjList = myView.findViewById(R.id.proglist);
        refreshLayout = myView.findViewById(R.id.refresh);
        loadingIndicatorView = myView.findViewById(R.id.loader);

        projInProgList = new ArrayList<String>();
        projInProgDescList = new ArrayList<String>();
        statusList = new ArrayList<>();

        refreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setTRecyclerView();
                        refreshLayout.setRefreshing(false);
                        refreshLayout.setRefreshStyle(PullRefreshLayout.STYLE_CIRCLES);
                    }
                },3000);

            }
        });




        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            int counter=0;
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()){
                        for (DocumentSnapshot snapshot : task.getResult()){
                            counter++;
                        }
                        totalProj.setText(String.valueOf(counter));
                    }
            }
        });

        getData("status","Finished !",completedProj);
        getData("status","Delivering",deliveredProj);

        setTRecyclerView();
        return myView;
    }

    private void setTRecyclerView() {
        loadingIndicatorView.show();
        Query query = collectionReference.whereEqualTo("status","In Progress...");
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            int counter =0;
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    projInProgList.clear();
                    projInProgDescList.clear();
                    statusList.clear();
                    for (DocumentSnapshot snapshot : task.getResult()){
                        counter++;
                        ProjectModel projectModel = snapshot.toObject(ProjectModel.class);
                        String partyname = projectModel.getParty_Name();
                        String projectDesc = projectModel.getProjectDes();
                        String status = projectModel.getStatus();
                        projInProgList.add(partyname);
                        projInProgDescList.add(projectDesc);
                        statusList.add(status);
                    }
                    if (counter==0){
                        Toast.makeText(getActivity(), "No projects in progress at the moment !", Toast.LENGTH_SHORT).show();
                    }
                    progressProj.setText(String.valueOf(counter));
                }


                ProjectsAdapter projectsAdapter = new ProjectsAdapter(getActivity(),projInProgList,projInProgDescList,statusList);
                progressProjList.setHasFixedSize(true);
                progressProjList.setLayoutManager(new LinearLayoutManager(getActivity()));
                progressProjList.setAdapter(projectsAdapter);
                loadingIndicatorView.hide();

                projectsAdapter.setOnItemClickListener(new ProjectsAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                    }
                });
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Error !", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void getData(String status, String dataName, final TextView textView){
        Query query = collectionReference.whereEqualTo(status,dataName);
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    int counter =0;
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot documentSnapshot : task.getResult()){
                            counter++;
                        }
                        textView.setText(String.valueOf(counter));
                    }
                });
    }
}
