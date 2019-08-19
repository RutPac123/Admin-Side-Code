package com.example.adminside.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adminside.R;
import com.example.adminside.model.ProjectModel;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProjectDetails extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText partyName;
    private EditText accountNo;
    private EditText description;
    private TextView dateOfCompletion;
    private TextView dateOfDelivery;
    private TextView statustxt;
    private TextView hasVideotxt;
    private TextView hasTitletxt;
    private Spinner statusSpin;
    private Spinner vSpin;
    private Spinner tSpin;
    private Button saveBtn;
    private EditText difficultytxt;
    private EditText amounttxt;
    private EditText quantitytxt;
    private EditText discounttxt;
    private TextView totaltxt;
    private TextView bookingdatetxt;
    private TextView projectId;
    private EditText delTo;
    private  Calendar c;
    private int count=0;
    private  String mdateOfCompletion;

    private Intent getIntent;

    private FirebaseFirestore firestore= FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = firestore.collection("Projects");
    private DocumentReference documentReference;

    private String latestStatus;
    private String latestVStatus;
    private String latestTStatus;
    private List<String> statusList;
    private List<String> vstatusList;
    private List<String> tstatusList;
    private int mYear,mMonth,mDay;
    private String date="";
    private Button save;
    private ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details);

        projectId = findViewById(R.id.id);
        partyName = findViewById(R.id.myCname);
        accountNo = findViewById(R.id.myaccno);
        description = findViewById(R.id.myPDesc);
        dateOfCompletion = findViewById(R.id.comdatetxt);
        dateOfDelivery = findViewById(R.id.deldatetxt);
        statustxt = findViewById(R.id.mStatus);
        hasVideotxt = findViewById(R.id.videodata);
        hasTitletxt = findViewById(R.id.titledata);
        statusSpin = findViewById(R.id.mSpinner);
        vSpin = findViewById(R.id.mSpinnervid);
        tSpin = findViewById(R.id.mSpinnertit);
        saveBtn = findViewById(R.id.saveDataBtn);
        difficultytxt = findViewById(R.id.diff);
        bookingdatetxt = findViewById(R.id.myDateBook);
        amounttxt = findViewById(R.id.amount);
        quantitytxt = findViewById(R.id.quantity);
        discounttxt = findViewById(R.id.discount);
        totaltxt = findViewById(R.id.total);
        delTo = findViewById(R.id.deliveryto);
        save = findViewById(R.id.saveDataBtn);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateMyStatus();
            }
        });

        getIntent = getIntent();
        c = Calendar.getInstance();

        if (getIntent != null){

            String mpartyName = getIntent.getStringExtra("part_name");
            String stat = getIntent.getStringExtra("status");
            String maccountNo = getIntent.getStringExtra("accountNo");
            String projectDesc = getIntent.getStringExtra("projectDesc");
            String vstat = getIntent.getStringExtra("vstat");
            String tstat = getIntent.getStringExtra("tastat");
            String diff = getIntent.getStringExtra("diff");
            String amount = getIntent.getStringExtra("amount");
            String quant = getIntent.getStringExtra("quant");
            String discount = getIntent.getStringExtra("disc");
            String mprojectId = getIntent.getStringExtra("projectId");
            String bookingdate = getIntent.getStringExtra("dateOfBook");
            String deliveryTo = getIntent.getStringExtra("delTo");
            final String totalamnt = getIntent.getStringExtra("total");
            String compldate = getIntent.getStringExtra("com");
            String delidte = getIntent.getStringExtra("deldate");

            String clientName = getIntent.getStringExtra("clName");

            if (stat.equals("Finished !")){
                Query query = collectionReference.whereEqualTo("clientName",clientName);
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            long finalTotal=0L;
                            for (DocumentSnapshot documentSnapshot : task.getResult()){
                                ProjectModel projectModel = documentSnapshot.toObject(ProjectModel.class);
                                long total = Long.valueOf(projectModel.getTotalAmount());
                                finalTotal = finalTotal + total;
                            }
                        }
                    }
                });
            }

            description.setText(projectDesc);
            bookingdatetxt.setText(bookingdate);
            statustxt.setText(stat);
            partyName.setText(mpartyName);
            accountNo.setText(maccountNo);
            hasVideotxt.setText(vstat);
            hasTitletxt.setText(tstat);
            difficultytxt.setText(diff);
            amounttxt.setText(amount);
            quantitytxt.setText(quant);
            discounttxt.setText(discount);
            projectId.setText(String.valueOf(mprojectId));
            delTo.setText(deliveryTo);
            totaltxt.setText(totalamnt);
            dateOfCompletion.setText(compldate);
            dateOfDelivery.setText(delidte);


        }

        statusList = new ArrayList<>();
        vstatusList = new ArrayList<>();
        tstatusList = new ArrayList<>();

        statusList.add("Booked !");
        statusList.add("In Progress...");
        statusList.add("Finished !");
        statusList.add("Delivering");

        vstatusList.add("Yes");
        vstatusList.add("No");

        tstatusList.add("Yes");
        tstatusList.add("No");

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,statusList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,vstatusList);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,tstatusList);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpin.setAdapter(adapter);
        statusSpin.setOnItemSelectedListener(this);
        statusSpin.setSelection(0,true);  //must
        statusSpin.setSelected(false);


        vSpin.setAdapter(adapter1);
        vSpin.setSelected(false);
        vSpin.setOnItemSelectedListener(this);
        vSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                   latestVStatus = String.valueOf(parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        tSpin.setAdapter(adapter2);
        tSpin.setOnItemSelectedListener(this);
        tSpin.setSelected(false);
        tSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                latestTStatus = tstatusList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        amounttxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.toString().equals("")){
                    totaltxt.setText(String.valueOf(0));
                }else{
                    String input = s.toString().replaceAll("\\s+", "");
                    long latestAmnt = Long.valueOf(input);
                    float dis = Float.valueOf(discounttxt.getText().toString());
                    int quan = Integer.valueOf(quantitytxt.getText().toString());
                    latestAmnt = latestAmnt*quan;
                    long total = Math.round(latestAmnt - (latestAmnt * (dis/100)));
                    totaltxt.setText(String.valueOf(total));
                }
            }
        });
        quantitytxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int quan;
                long latestAmnt = Long.valueOf(amounttxt.getText().toString());
                float dis = Float.valueOf(discounttxt.getText().toString());
                if (s.toString().equals("")){
                    quan =0;
                    latestAmnt = latestAmnt*quan;
                    long total = Math.round(latestAmnt - (latestAmnt * (dis/100)));
                    totaltxt.setText(String.valueOf(total));
                }else{
                    String input = s.toString().replaceAll("\\s+", "");
                    quan = Integer.valueOf(input);
                    latestAmnt = latestAmnt*quan;
                    long total = Math.round(latestAmnt - (latestAmnt * (dis/100)));
                    totaltxt.setText(String.valueOf(total));
                }
            }
        });
        discounttxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                float dis;
                long latestAmnt = Long.valueOf(amounttxt.getText().toString());
                if (s.toString().equals("")){
                    dis =0.0f;
                    int quan = Integer.valueOf(quantitytxt.getText().toString());
                    latestAmnt = latestAmnt*quan;
                    long total = Math.round(latestAmnt - (latestAmnt * (dis/100)));
                    totaltxt.setText(String.valueOf(total));
                }else {
                    String input = s.toString().replaceAll("\\s+", "");
                    dis = Float.valueOf(input);
                    int quan = Integer.valueOf(quantitytxt.getText().toString());
                    latestAmnt = latestAmnt*quan;
                    long total = Math.round(latestAmnt - (latestAmnt * (dis/100)));
                    totaltxt.setText(String.valueOf(total));
                }
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        latestStatus = statusList.get(position);

        mYear = c.get(Calendar.YEAR);
        mMonth= c.get(Calendar.MONTH);
        mDay= c.get(Calendar.DAY_OF_MONTH);

        if (latestStatus.equals("Finished !")){

            mdateOfCompletion = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            dateOfCompletion.setText(mdateOfCompletion);
            dateOfDelivery.setText("");
        }
        else if (latestStatus.equals("In Progress...")){

            statustxt.setTextColor(Color.parseColor("#6A89CC"));
            dateOfDelivery.setText("");
            dateOfCompletion.setText("");
        }else if (latestStatus.equals("Delivering")){
            String mdateOfDelivery = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            dateOfDelivery.setText(mdateOfDelivery);

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void updateMyStatus(){

        String mid = getIntent.getStringExtra("id");
        documentReference = firestore.document("Projects/"+mid);
        updateValues("status",latestStatus);
        updateValues("videoData",latestVStatus);
        updateValues("titleData",latestTStatus);
        updateValues("completeDate",dateOfCompletion.getText().toString());
        updateValues("deliveryDate",dateOfDelivery.getText().toString());
        updateValues("amount",amounttxt.getText().toString());
        updateValues("qty",quantitytxt.getText().toString());
        updateValues("discount",discounttxt.getText().toString());
        updateValues("acNo",accountNo.getText().toString());
        updateValues("deliverTo",delTo.getText().toString());
        updateValues("diff",difficultytxt.getText().toString());
        updateValues("totalAmount",totaltxt.getText().toString());
        updateValues("party_Name",partyName.getText().toString());
        updateValues("projectDes",description.getText().toString());
        Intent intent =new Intent(ProjectDetails.this,MainActivity.class);
        startActivity(intent);
    }
    private void updateValues(String key,String value){
        documentReference.update(key,value)
        .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProjectDetails.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
