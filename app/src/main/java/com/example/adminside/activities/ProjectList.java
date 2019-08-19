package com.example.adminside.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

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

import java.util.ArrayList;

public class ProjectList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = firestore.collection("Projects");
    private Intent getdataIntent;
    private String clientName1;
    private String cAcc;
    private ArrayList<String> projectNameList;
    private ArrayList<String> partynamelist;
    private ArrayList<String> statusList;
    private ArrayList<String> acclist;
    private ArrayList<String> projDesList;
    private ArrayList<String> vstatlist;
    private ArrayList<String> tstatlist;
    private ArrayList<String> difflist;
    private ArrayList<String> amountlist;
    private ArrayList<String> totallist;
    private ArrayList<String> quantlist;
    private ArrayList<String> disclist;
    private ArrayList<String> dofbooklist;
    private ArrayList<String> doComlist;
    private ArrayList<String> doDelList;
    private ArrayList<String> projIdList;
    private ArrayList<String> deltolist;
    private ArrayList<String> clientlist;
    private ArrayList<String> idList;
    private ProgressDialog progressDialog;
    private String projectName;
    private String parname;
    private String partyName;
    private String id;
    private String status;
    private String accountNo;
    private String projectDesc;
    private String vstat;
    private String tstat;
    private String diff;
    private String amount;
    private String quant;
    private String disc;
    private String total;
    private int projectId;
    private String dateOfBook;
    private String delTo;
    private String comdate;
    private String deldate;
    private String clientName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_list);

        recyclerView = findViewById(R.id.listOfProjects);
        getdataIntent = getIntent();
        projectNameList = new ArrayList<>();
        partynamelist = new ArrayList<>();
        statusList = new ArrayList<>();
        acclist = new ArrayList<>();
        projDesList = new ArrayList<>();
        vstatlist = new ArrayList<>();
        tstatlist = new ArrayList<>();
        difflist = new ArrayList<>();
        amountlist = new ArrayList<>();
        totallist = new ArrayList<>();
        quantlist = new ArrayList<>();
        disclist = new ArrayList<>();
        dofbooklist = new ArrayList<>();
        doComlist = new ArrayList<>();
        doDelList = new ArrayList<>();
        projIdList = new ArrayList<>();
        deltolist = new ArrayList<>();
        clientlist = new ArrayList<>();
        idList = new ArrayList<>();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        setMyRecyclerView();
    }

    private void setMyRecyclerView() {
        progressDialog = ProgressDialog.show(this,"Please wait","Projects of client are loading..",false,true);
        clientName1 = getdataIntent.getStringExtra("clientName");
        parname = getdataIntent.getStringExtra("party");
        cAcc = getdataIntent.getStringExtra("Myacc");
        Query query = collectionReference.whereEqualTo("clientName",clientName1).whereEqualTo("acNo",cAcc);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    projectNameList.clear();
                    partynamelist.clear();
                    statusList.clear();
                    acclist.clear();
                    projDesList.clear();
                    vstatlist.clear();
                    tstatlist.clear();
                    difflist.clear();
                    amountlist.clear();
                    totallist.clear();
                    statusList.clear();
                    quantlist.clear();
                    disclist.clear();
                    dofbooklist.clear();
                    doComlist.clear();
                    doDelList.clear();
                    projIdList.clear();
                    deltolist.clear();
                    clientlist.clear();
                    idList.clear();

                    for (DocumentSnapshot snapshot : task.getResult()){
                         progressDialog.show();
                         ProjectModel projectModel = snapshot.toObject(ProjectModel.class);
                         id = snapshot.getId();
                         partyName = projectModel.getParty_Name();
                         status = projectModel.getStatus();
                         vstat= projectModel.getVideoData();
                         tstat = projectModel.getTitleData();
                         diff = projectModel.getDiff();
                         accountNo = projectModel.getAcNo();
                         projectDesc = projectModel.getProjectDes();
                         amount = projectModel.getAmount();
                         quant = projectModel.getQty();
                         disc = projectModel.getDiscount();
                         total = projectModel.getTotalAmount();
                         projectId = projectModel.getProjectId();
                         dateOfBook = projectModel.getBookingDate();
                         delTo = projectModel.getDeliverTo();
                         comdate = projectModel.getCompleteDate();
                         deldate = projectModel.getDeliveryDate();
                         clientName = projectModel.getClientName();


                        projectNameList.add(projectDesc);
                        partynamelist.add(partyName);
                        statusList.add(status);
                        acclist.add(accountNo);
                        projDesList.add(projectDesc);
                        vstatlist.add(vstat);
                        tstatlist.add(tstat);
                        difflist.add(diff);
                        amountlist.add(amount);
                        totallist.add(total);
                        quantlist.add(quant);
                        disclist.add(disc);
                        dofbooklist.add(dateOfBook);
                        doComlist.add(comdate);
                        doDelList.add(deldate);
                        deltolist.add(delTo);
                        clientlist.add(clientName);
                        projIdList.add(String.valueOf(projectId));
                        idList.add(id);
                    }
                }
                progressDialog.cancel();
                ProjectsAdapter projectsAdapter = new ProjectsAdapter(ProjectList.this,partynamelist,projIdList,statusList);
                recyclerView.setAdapter(projectsAdapter);

                projectsAdapter.setOnItemClickListener(new ProjectsAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(ProjectList.this,ProjectDetails.class);
                        intent.putExtra("part_name",partynamelist.get(position));
                        intent.putExtra("status",statusList.get(position));
                        intent.putExtra("accountNo",acclist.get(position));
                        intent.putExtra("projectDesc",projDesList.get(position));
                        intent.putExtra("vstat",vstatlist.get(position));
                        intent.putExtra("tastat",tstatlist.get(position));
                        intent.putExtra("diff",difflist.get(position));
                        intent.putExtra("amount",amountlist.get(position));
                        intent.putExtra("quant",quantlist.get(position));
                        intent.putExtra("disc",disclist.get(position));
                        intent.putExtra("total",totallist.get(position));
                        intent.putExtra("projectId",projIdList.get(position));
                        intent.putExtra("dateOfBook",dofbooklist.get(position));
                        intent.putExtra("delTo",deltolist.get(position));
                        intent.putExtra("id",idList.get(position));
                        intent.putExtra("com",doComlist.get(position));
                        intent.putExtra("deldate",doDelList.get(position));
                        intent.putExtra("clName",clientlist.get(position));
                        startActivity(intent);
                }
                });
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProjectList.this, "Error !", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
