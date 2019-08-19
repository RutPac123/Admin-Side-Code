package com.example.adminside.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.example.adminside.R;
import com.example.adminside.activities.ClientDetails;
import com.example.adminside.activities.ProjectList;
import com.example.adminside.adapters.MyClientsAdapter;
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
import java.util.LinkedHashSet;
import java.util.Set;

public class ClientListFragment extends Fragment {

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = firestore.collection("Projects");
    private RecyclerView clientNameList;
    private ArrayList<String> clientsNameslist;
    private ArrayList<String> studioNameslist;
    private ArrayList<String> idList;
    private ArrayList<String> accList;
    private String myclient;
    private String id;
    private MyClientsAdapter clientsAdapter;
    private PullRefreshLayout refreshLayout;
    private AVLoadingIndicatorView loadingIndicatorView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_clients,container,false);
        clientNameList = view.findViewById(R.id.listOfClients);
        refreshLayout = view.findViewById(R.id.swipeContainer);
        loadingIndicatorView = view.findViewById(R.id.loader);

        clientNameList.setHasFixedSize(true);
        clientNameList.setLayoutManager(new LinearLayoutManager(getActivity()));

        clientsNameslist = new ArrayList<>();
        studioNameslist  = new ArrayList<>();
        idList = new ArrayList<>();
        accList = new ArrayList<>();

        refreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setMyRecyclerList();
                        refreshLayout.setRefreshing(false);
                        refreshLayout.setRefreshStyle(PullRefreshLayout.STYLE_CIRCLES);
                    }
                },3000);

            }
        });
        clientNameList.setHasFixedSize(true);
        clientNameList.setLayoutManager(new LinearLayoutManager(getActivity()));

        setMyRecyclerList();

        return view;
    }

    private void setMyRecyclerList() {
        loadingIndicatorView.show();
        Query query = collectionReference.orderBy("projectId",Query.Direction.ASCENDING);
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            clientsNameslist.clear();
                            studioNameslist.clear();
                            idList.clear();
                            accList.clear();

                            for (DocumentSnapshot snapshots : task.getResult()){
                                ProjectModel model = snapshots.toObject(ProjectModel.class);
                                id = snapshots.getId();
                                myclient = model.getClientName();
                                String mystudio = model.getStudioName();
                                String acc = model.getAcNo();
                                clientsNameslist.add(myclient);
                                studioNameslist.add(mystudio);
                                idList.add(id);
                                accList.add(acc);
                            }
                        }
                        Set<String> set = new LinkedHashSet<>(accList);
                        accList.clear();
                        accList.addAll(set);

                        Set<String> set2 = new LinkedHashSet<>(clientsNameslist);
                        clientsNameslist.clear();
                        clientsNameslist.addAll(set2);

                        clientsAdapter = new MyClientsAdapter(getActivity(),accList,clientsNameslist);
                        clientNameList.setAdapter(clientsAdapter);
                        loadingIndicatorView.hide();

                        clientsAdapter.setOnItemClickListener(new MyClientsAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Intent intent = new Intent(getActivity(), ProjectList.class);
                                intent.putExtra("clientName",clientsNameslist.get(position));
                                intent.putExtra("Myacc",accList.get(position));
                                startActivity(intent);
                            }
                        });

                        clientsAdapter.setOnItemLongClickListener(new MyClientsAdapter.OnItemLongClickListener() {
                            @Override
                            public void onItemLongClick(View view, int position) {
                                Intent intent = new Intent(getActivity(), ClientDetails.class);
                                intent.putExtra("myID",idList.get(position));
                                intent.putExtra("clientName",clientsNameslist.get(position));
                                intent.putExtra("Myacc",accList.get(position));
                                startActivity(intent);
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

}
