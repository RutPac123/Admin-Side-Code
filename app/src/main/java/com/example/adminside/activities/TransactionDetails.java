package com.example.adminside.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.adminside.R;
import com.example.adminside.adapters.TransAdapter;
import com.example.adminside.model.TransModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

public class TransactionDetails extends AppCompatActivity {
    private Intent intentGet;
    private RecyclerView listOfTrans;
    private String clientN;
    private String clientA;
    private ArrayList<Long> transamntlist;
    private ArrayList<String> transdateList;
    private AVLoadingIndicatorView loadingIndicatorView;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference3 = firestore.collection("Transactions");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_details);

        listOfTrans = findViewById(R.id.transList);
        loadingIndicatorView = findViewById(R.id.loader);

        transamntlist = new ArrayList<>();
        transdateList = new ArrayList<>();

        intentGet = getIntent();
        clientN = intentGet.getStringExtra("myc");
        clientA = intentGet.getStringExtra("myAc");

        listOfTrans.setHasFixedSize(true);
        listOfTrans.setLayoutManager(new LinearLayoutManager(this));
        loadTransactions(clientN,clientA);
    }

    private void loadTransactions(String clientName , String clientAcc){
        loadingIndicatorView.show();
        Query query = collectionReference3.orderBy("transId").whereEqualTo("cName",clientName).whereEqualTo("acNo",clientAcc);
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        transamntlist.clear();
                        transdateList.clear();
                        if (task.isSuccessful()){
                            for (DocumentSnapshot snapshot : task.getResult()){
                                TransModel model = snapshot.toObject(TransModel.class);
                                String mydate = model.getDate();
                                long myAmount = model.getTransaction();

                                transdateList.add(mydate);
                                transamntlist.add(myAmount);

                            }
                        }
                        TransAdapter transAdapter = new TransAdapter(transdateList,transamntlist);
                        listOfTrans.setAdapter(transAdapter);
                        loadingIndicatorView.hide();
                    }
                });

    }
}
