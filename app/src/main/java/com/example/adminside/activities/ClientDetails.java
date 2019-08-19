package com.example.adminside.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.example.adminside.R;
import com.example.adminside.model.DataModel;
import com.example.adminside.model.ProjectModel;
import com.example.adminside.model.TransModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ClientDetails extends AppCompatActivity {

    private TextView clientName;
    private TextView address;
    private TextView accNo;
    private TextView email;
    private TextView pno;
    private TextView stduioname;
    private TextView totalAmnt;
    private TextView paidAmnt;
    private TextView remainAmnt;
    private EditText getAmntEdt;
    private Button save;
    private String mclientname;
    private String mclientaccNo;
    private Intent intent;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = firestore.collection("Projects");
    private CollectionReference collectionReference2 = firestore.collection("users");
    private CollectionReference collectionReference3 = firestore.collection("Transactions");
    private DocumentReference documentReference;
    private String name;
    private String acc;
    private String add;
    private String phone;
    private String memail;
    private String studio;
    private String total;
    private String paid;
    private long rem;
    private String id;
    private String dateOfTransaction;
    private long totalLatest,paidTill,amountRem;
    private PullRefreshLayout refreshLayout;
    private CoordinatorLayout coordinatorLayout;
    private long transId;
    private AVLoadingIndicatorView loadingIndicatorView;

    private FloatingActionButton fabTrans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_details);

        clientName = findViewById(R.id.mStatus);
        address = findViewById(R.id.myCname);
        accNo = findViewById(R.id.id);
        email = findViewById(R.id.myaccno);
        pno = findViewById(R.id.myPDesc);
        stduioname = findViewById(R.id.myDateBook);
        totalAmnt = findViewById(R.id.comdatetxt);
        paidAmnt = findViewById(R.id.deldatetxt);
        remainAmnt = findViewById(R.id.deliveryto);
        save = findViewById(R.id.saveDataBtn);
        refreshLayout = findViewById(R.id.refContainer);
        coordinatorLayout = findViewById(R.id.cood);
        fabTrans = findViewById(R.id.fabtrans);
        getAmntEdt = findViewById(R.id.mAmntNow);
        loadingIndicatorView = findViewById(R.id.loader);

        intent = getIntent();



        mclientname = intent.getStringExtra("clientName");
        mclientaccNo = intent.getStringExtra("Myacc");

        fabTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClientDetails.this,TransactionDetails.class);
                intent.putExtra("myc",mclientname);
                intent.putExtra("myAc",mclientaccNo);
                startActivity(intent);
            }
        });
        refreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setMyData();
                        refreshLayout.setRefreshing(false);
                        refreshLayout.setRefreshStyle(PullRefreshLayout.STYLE_CIRCLES);
                    }
                },3000);

            }
        });

        Query query2 = collectionReference.whereEqualTo("clientName",mclientname).whereEqualTo("acNo",mclientaccNo).whereEqualTo("status","Finished !");
        query2.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            totalLatest = 0;
                            paidTill =0;
                            amountRem =0;
                            for (DocumentSnapshot snapshot : task.getResult()){
                                ProjectModel model = snapshot.toObject(ProjectModel.class);
                                DataModel dataModel = snapshot.toObject(DataModel.class);
                                long total = Long.valueOf(model.getTotalAmount());
                                totalLatest = totalLatest + total;
                            }
                            setMyData();
                        }
                    }
                });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (paidAmnt.getText().toString().equals("")){
                    paidAmnt.setText("0");
                }
                try {
                    if (Long.valueOf(getAmntEdt.getText().toString()) > Long.valueOf(totalAmnt.getText().toString())){
                        Snackbar.make(coordinatorLayout,"Amount should be less than total !",Snackbar.LENGTH_SHORT).show();
                    }else if (!getAmntEdt.getText().toString().equals("")){

                        long myAmnt;
                        String input = getAmntEdt.getText().toString().replaceAll("\\s+", "");
                        myAmnt = Long.valueOf(input);
                        long latestPaidTillNow = Long.valueOf(paidAmnt.getText().toString());
                        long total =  Long.valueOf(totalAmnt.getText().toString());
                        latestPaidTillNow = latestPaidTillNow + myAmnt; //db

                        total = total - latestPaidTillNow;

                        remainAmnt.setText(String.valueOf(total));
                        Snackbar.make(coordinatorLayout,"Saving your precious data..",Snackbar.LENGTH_SHORT).show();
                        updateValues("totalAmount",totalAmnt.getText().toString());
                        updateValues("totalAmountPaid",String.valueOf(latestPaidTillNow));

                        addTransaction();
                    }else{
                        Snackbar.make(coordinatorLayout,"Empty amount !",Snackbar.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    Snackbar.make(coordinatorLayout,"Something went wrong !",Snackbar.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void setMyData(){
        loadingIndicatorView.show();
        Query query = collectionReference2.whereEqualTo("clientName",mclientname).whereEqualTo("acNo",mclientaccNo);
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (DocumentSnapshot snapshot : task.getResult()){

                                DataModel dataModel = snapshot.toObject(DataModel.class);
                                id = snapshot.getId();
                                name = dataModel.getClientName();
                                acc = dataModel.getAcNo();
                                add = dataModel.getAddress();
                                phone = dataModel.getPhoneNo();
                                memail = dataModel.getEmail();
                                studio = dataModel.getStudioName();
                                total = dataModel.getTotalAmount();
                                paid = dataModel.getTotalAmountPaid();

                                if (total.equals("") || paid.equals("")){
                                    Log.i("empty","empty fields !");
                                }else{
                                    rem = Long.valueOf(total) - Long.valueOf(paid);
                                    remainAmnt.setText(String.valueOf(rem));
                                }
                            }
                            clientName.setText(name);
                            address.setText(add);
                            accNo.setText(acc);
                            email.setText(memail);
                            pno.setText(phone);
                            stduioname.setText(studio);
                            totalAmnt.setText(String.valueOf(totalLatest));
                            paidAmnt.setText(paid);

                            loadingIndicatorView.hide();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ClientDetails.this, "Something went wrong ! ", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateValues(String key , String value){

        documentReference = firestore.document("users/"+id);
        documentReference.update(key,value)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar.make(coordinatorLayout,"Something went wrong !",Snackbar.LENGTH_SHORT).show();
            }
        });

    }
    private void addTransaction(){

        dateOfTransaction = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        collectionReference3.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            int counter=0;
                            for (DocumentSnapshot snapshot : task.getResult()){
                                counter++;
                            }
                            transId = counter;
                            TransModel model1 = new TransModel(Long.valueOf(getAmntEdt.getText().toString()),mclientname,mclientaccNo,dateOfTransaction,transId);
                            collectionReference3.add(model1);
                        }
                    }
                });




    }
}
