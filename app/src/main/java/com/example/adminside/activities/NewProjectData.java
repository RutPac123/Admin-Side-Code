package com.example.adminside.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.adminside.R;
import com.example.adminside.model.ProjectModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NewProjectData extends AppCompatActivity {

    private EditText quantityed;
    private EditText delivertoed;
    private EditText amounted;
    private EditText discounted;
    private EditText diffed;
    private Button saveBtn;
    private String deliverTo,
            difficulty,video,title,accNo,dateOfEven,dateOfBooking,partyName,projDesc;
    private Intent getDataIntent;
    private long amount,totalAmount;
    private int quantity,projId;
    private float discount;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = firestore.collection("Projects");
    private ProgressDialog progressDialog;
    private String clientName;
    private String studioName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_project_data);

        quantityed = findViewById(R.id.quant);
        delivertoed = findViewById(R.id.delito);
        amounted = findViewById(R.id.amnt);
        discounted = findViewById(R.id.discnt);
        diffed = findViewById(R.id.diff);
        saveBtn = findViewById(R.id.saveAllDataBtn);

        getDataIntent = getIntent();

        Toast.makeText(this, getIntent().getStringExtra("docid"), Toast.LENGTH_SHORT).show();
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = ProgressDialog.show(NewProjectData.this,"Please wait","Saving your data...",false,true);
                progressDialog.show();
                quantity = Integer.valueOf(quantityed.getText().toString());
                deliverTo = delivertoed.getText().toString();
                amount = Long.valueOf(amounted.getText().toString());

                difficulty = diffed.getText().toString();

                video = getDataIntent.getStringExtra("video");
                title = getDataIntent.getStringExtra("title");
                accNo = getDataIntent.getStringExtra("accNo");
                dateOfEven = getDataIntent.getStringExtra("eDate");
                partyName = getDataIntent.getStringExtra("party");
                projDesc = getDataIntent.getStringExtra("pDesc");
                clientName = getDataIntent.getStringExtra("client");
                studioName = getDataIntent.getStringExtra("studio");

                collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        int counter=0;
                        for (DocumentSnapshot documents : task.getResult()){
                            counter++;
                        }

                        projId = counter+1;
                        dateOfBooking = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

                        if (String.valueOf(quantity).trim().isEmpty() || deliverTo.trim().isEmpty() || String.valueOf(amount).trim().isEmpty()){
                            Toast.makeText(NewProjectData.this, "Empty fields!", Toast.LENGTH_SHORT).show();
                        }else if (!String.valueOf(discounted.getText().toString()).trim().isEmpty()){

                            discount = Float.valueOf(discounted.getText().toString());
                            amount = quantity * amount;
                            totalAmount = Math.round(amount - (amount * (discount/100)));

                            saveMyAllData(String.valueOf(quantity),deliverTo,String.valueOf(amount),String.valueOf(discount),
                                    difficulty,video,title,String.valueOf(totalAmount),accNo,dateOfEven,dateOfBooking,
                                    partyName,projDesc,projId,clientName,studioName);

                        }else{
                            totalAmount = quantity*amount;
                            saveMyAllData(String.valueOf(quantity),deliverTo,String.valueOf(amount),String.valueOf(0),difficulty,video,
                                    title,String.valueOf(totalAmount),accNo,dateOfEven,dateOfBooking,partyName,projDesc,projId,clientName,studioName);

                        }
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(NewProjectData.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                            }
                        });


            }
        });
    }

    private void saveMyAllData(String quantity, String deliverTo, String amount, String discount,
                               String difficulty, String video, String title, String totalAmount,String accNo,
                               String dateOfEven, String dateOfBooking, String partyName,
                               final String projDesc, int projId,String clientName,String studioName)  {

        ProjectModel projectModel = new   ProjectModel ("",difficulty,discount,
                dateOfEven,partyName,projDesc,projId,quantity,
                "Booked !",title,totalAmount,video,accNo,
                "Arambh Media",amount,dateOfBooking,"",deliverTo,clientName,studioName);

        collectionReference.add(projectModel)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Toast.makeText(NewProjectData.this, "Data saved !", Toast.LENGTH_SHORT).show();
                        progressDialog.cancel();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.cancel();
                        Toast.makeText(NewProjectData.this, "Data saving failed !", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
