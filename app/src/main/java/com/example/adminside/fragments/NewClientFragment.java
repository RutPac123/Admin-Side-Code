package com.example.adminside.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adminside.R;
import com.example.adminside.activities.NewProjectData;
import com.example.adminside.adapters.SearchAdapter;
import com.example.adminside.adapters.SearchAdapter2;
import com.example.adminside.model.DataModel;
import com.example.adminside.model.ProjDescModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NewClientFragment extends Fragment {

    private DatePickerDialog datePicker;
    private Button selectDate;
    private TextView datetxt;
    int mDay,mMonth,mYear;
    private DatabaseReference databaseReference;
    private  RecyclerView mySearchedList;
    private RecyclerView mySearchedListSec;
    public static EditText clientNameEdtTxt;
    public static EditText clientDescEdtTxt;
    private EditText studioNameEdttxt;
    private EditText accountNoEdttxt;
    private EditText partyNameEdtTxt;
    private Button saveBtn;
    private ArrayList<String> clientNameList;
    private TextView txtdate;
    private TextView mydate;
    private ImageView okBtnName;
    private ImageView okBtnDesc;
    private String date;
    private Spinner videoSpin;
    private Spinner titleSpin;
    private String mystatus;
    private Intent statusintent;
    private String selectedVidStatus;
    private String selectedTitStatus;
    private TextView haveV;
    private TextView haveT;

    private FirebaseFirestore firestore= FirebaseFirestore.getInstance();
    private CollectionReference clientReference = firestore.collection("users");
    private CollectionReference projectdesc = firestore.collection("ProjectDesc");
    private CollectionReference projectReference = firestore.collection("Projects");


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.newclient_layout,container,false);

        selectDate = myView.findViewById(R.id.opnCalBtn);
        datetxt = myView.findViewById(R.id.datetxt);
        txtdate = myView.findViewById(R.id.mDate);
        mydate = myView.findViewById(R.id.datetxt);
        saveBtn = myView.findViewById(R.id.btnSave);
        clientNameEdtTxt = myView.findViewById(R.id.cName);
        clientDescEdtTxt = myView.findViewById(R.id.cDesc);
        studioNameEdttxt = myView.findViewById(R.id.studioName);
        partyNameEdtTxt = myView.findViewById(R.id.pName);
        okBtnName = myView.findViewById(R.id.okBtn);
        okBtnDesc = myView.findViewById(R.id.okBtn2);
        mySearchedList = myView.findViewById(R.id.searchList);
        videoSpin = myView.findViewById(R.id.mSpinVideo);
        titleSpin = myView.findViewById(R.id.mSpinTitle);
        haveT = myView.findViewById(R.id.vidstattxt);
        haveV = myView.findViewById(R.id.titeltxt);
        accountNoEdttxt = myView.findViewById(R.id.cAcNo);

        clientNameList = new ArrayList<>();

        click(okBtnName);
        click(okBtnDesc);

        List<String> videoTitlestatlist = new ArrayList<>();
        videoTitlestatlist.add("Yes");
        videoTitlestatlist.add("No");

        ArrayAdapter<String> videotxtAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,videoTitlestatlist);
        videotxtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        videoSpin.setAdapter(videotxtAdapter);
        titleSpin.setAdapter(videotxtAdapter);

        videoSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedVidStatus = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        titleSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTitStatus = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth= c.get(Calendar.MONTH);
                mDay= c.get(Calendar.DAY_OF_MONTH);
                datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        datetxt.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                        date = String.valueOf(dayOfMonth)+"/"+String.valueOf(month)+"/"+String.valueOf(year);
                    }
                },mYear,mMonth,mDay);
                datePicker.show();
            }
        });


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String accountNoClinet = accountNoEdttxt.getText().toString();
                String studioname = studioNameEdttxt.getText().toString();
                String projdesc = clientDescEdtTxt.getText().toString();
                String partyName = partyNameEdtTxt.getText().toString();
                String clientName = clientNameEdtTxt.getText().toString();
                String eventdate = date;

                if (clientName.trim().isEmpty() || accountNoClinet.trim().isEmpty() || studioname.trim().isEmpty() || projdesc.trim().isEmpty() || partyName.trim().isEmpty() || eventdate.trim().isEmpty()){
                    Toast.makeText(getActivity(), "Empty fields !", Toast.LENGTH_SHORT).show();
                    return;
                }

                saveData(studioname,accountNoClinet,clientName);
                saveProjDesc(projdesc);

                statusintent = new Intent(getActivity(),NewProjectData.class);

                statusintent.putExtra("accNo",accountNoClinet);
                statusintent.putExtra("eDate",eventdate);
                statusintent.putExtra("party",partyName);
                statusintent.putExtra("pDesc",projdesc);
                statusintent.putExtra("video",selectedVidStatus);
                statusintent.putExtra("title",selectedTitStatus);
                statusintent.putExtra("client",clientName);
                statusintent.putExtra("studio",studioname);
                startActivity(statusintent);

            }
        });



        clientNameEdtTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mySearchedList.setVisibility(View.VISIBLE);
                studioNameEdttxt.setVisibility(View.INVISIBLE);
                clientDescEdtTxt.setVisibility(View.INVISIBLE);
                partyNameEdtTxt.setVisibility(View.INVISIBLE);
                mySearchedListSec.setVisibility(View.INVISIBLE);
                datetxt.setVisibility(View.INVISIBLE);
                saveBtn.setVisibility(View.INVISIBLE);
                txtdate.setVisibility(View.INVISIBLE);
                mydate.setVisibility(View.INVISIBLE);
                selectDate.setVisibility(View.INVISIBLE);
                okBtnDesc.setVisibility(View.INVISIBLE);
                haveT.setVisibility(View.INVISIBLE);
                haveV.setVisibility(View.INVISIBLE);
                titleSpin.setVisibility(View.INVISIBLE);
                videoSpin.setVisibility(View.INVISIBLE);

                if (!s.toString().isEmpty()){
                    setAdapterNew(s.toString());
                }else{
                    clientNameList.clear();
                    mySearchedList.removeAllViews();

                    mySearchedList.setVisibility(View.INVISIBLE);
                    mySearchedListSec.setVisibility(View.INVISIBLE);
                    clientDescEdtTxt.setVisibility(View.VISIBLE);
                    partyNameEdtTxt.setVisibility(View.VISIBLE);
                    datetxt.setVisibility(View.VISIBLE);
                    saveBtn.setVisibility(View.VISIBLE);
                    txtdate.setVisibility(View.VISIBLE);
                    mydate.setVisibility(View.VISIBLE);
                    selectDate.setVisibility(View.VISIBLE);
                    okBtnDesc.setVisibility(View.VISIBLE);
                    haveT.setVisibility(View.VISIBLE);
                    haveV.setVisibility(View.VISIBLE);
                    titleSpin.setVisibility(View.VISIBLE);
                    videoSpin.setVisibility(View.VISIBLE);
                    studioNameEdttxt.setVisibility(View.VISIBLE);
                }
            }

        });

        clientDescEdtTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                mySearchedList.setVisibility(View.INVISIBLE);
                mySearchedListSec.setVisibility(View.VISIBLE);
                clientDescEdtTxt.setVisibility(View.VISIBLE);
                partyNameEdtTxt.setVisibility(View.INVISIBLE);
                datetxt.setVisibility(View.INVISIBLE);
                saveBtn.setVisibility(View.INVISIBLE);
                txtdate.setVisibility(View.INVISIBLE);
                mydate.setVisibility(View.INVISIBLE);
                selectDate.setVisibility(View.INVISIBLE);
                okBtnDesc.setVisibility(View.VISIBLE);
                haveT.setVisibility(View.INVISIBLE);
                haveV.setVisibility(View.INVISIBLE);
                titleSpin.setVisibility(View.INVISIBLE);
                videoSpin.setVisibility(View.INVISIBLE);


                if (!s.toString().isEmpty()){
                    setAdapter2(s.toString());
                }else{
                    mySearchedListSec.removeAllViews();
                    mySearchedList.setVisibility(View.INVISIBLE);
                    mySearchedListSec.setVisibility(View.INVISIBLE);
                    clientDescEdtTxt.setVisibility(View.VISIBLE);
                    partyNameEdtTxt.setVisibility(View.VISIBLE);
                    datetxt.setVisibility(View.VISIBLE);
                    saveBtn.setVisibility(View.VISIBLE);
                    txtdate.setVisibility(View.VISIBLE);
                    mydate.setVisibility(View.VISIBLE);
                    selectDate.setVisibility(View.VISIBLE);
                    haveT.setVisibility(View.VISIBLE);
                    haveV.setVisibility(View.VISIBLE);
                    titleSpin.setVisibility(View.VISIBLE);
                    videoSpin.setVisibility(View.VISIBLE);

                }
            }
        });

        saveBtn = myView.findViewById(R.id.btnSave);

        mySearchedList = myView.findViewById(R.id.searchList);
        mySearchedList.setHasFixedSize(true);
        mySearchedList.setLayoutManager(new LinearLayoutManager(getActivity()));

        mySearchedListSec = myView.findViewById(R.id.searchList2);
        mySearchedListSec.setHasFixedSize(true);
        mySearchedListSec.setLayoutManager(new LinearLayoutManager(getActivity()));



        databaseReference = FirebaseDatabase.getInstance().getReference();

        return myView;
    }

    private void setAdapterNew(final String inputString){
        Query query = clientReference.whereGreaterThanOrEqualTo("clientName", inputString);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    int counter =0;
                    clientNameList.clear();
                    mySearchedList.removeAllViews();
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                        DataModel dataModel = documentSnapshot.toObject(DataModel.class);
                        String clientName = dataModel.getClientName();
                        if (clientName.toLowerCase().contains(inputString.toLowerCase())){
                            clientNameList.add(clientName);
                            counter++;
                        }
                        if (counter==15){
                            break;
                        }
                    }

                }
                SearchAdapter searchAdapter = new SearchAdapter(getActivity(),clientNameList);
                mySearchedList.setAdapter(searchAdapter);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "error!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setAdapter2(final String inputString) {

        Query query = projectdesc.whereGreaterThanOrEqualTo("projDesc", inputString);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    int counter =0;
                    clientNameList.clear();
                    mySearchedListSec.removeAllViews();
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                        ProjDescModel projDescModel = documentSnapshot.toObject(ProjDescModel.class);
                        String projectDesc = projDescModel.getProjDesc();
                        if (projectDesc.toLowerCase().contains(inputString.toLowerCase())){
                            clientNameList.add(projectDesc);
                            counter++;
                        }
                        if (counter==15){
                            break;
                        }
                    }

                }
                SearchAdapter2 searchAdapter2 = new SearchAdapter2(getActivity(),clientNameList);
                mySearchedListSec.setAdapter(searchAdapter2);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "error!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void click(ImageView button){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clientNameList.clear();
                mySearchedList.removeAllViews();
                mySearchedListSec.removeAllViews();
                mySearchedList.setVisibility(View.INVISIBLE);
                mySearchedListSec.setVisibility(View.INVISIBLE);
                clientDescEdtTxt.setVisibility(View.VISIBLE);
                partyNameEdtTxt.setVisibility(View.VISIBLE);
                datetxt.setVisibility(View.VISIBLE);
                saveBtn.setVisibility(View.VISIBLE);
                txtdate.setVisibility(View.VISIBLE);
                mydate.setVisibility(View.VISIBLE);
                selectDate.setVisibility(View.VISIBLE);
                haveT.setVisibility(View.VISIBLE);
                haveV.setVisibility(View.VISIBLE);
                titleSpin.setVisibility(View.VISIBLE);
                videoSpin.setVisibility(View.VISIBLE);
                studioNameEdttxt.setVisibility(View.VISIBLE);

            }
        });
    }

    private void saveData(String studioname,String acNo,String clientName){
            DataModel model = new DataModel(studioname,acNo,clientName,"","","","","");
            clientReference.add(model)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(getActivity(), "Data saved :)", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Failed to save! Try again", Toast.LENGTH_SHORT).show();
                        }
                    });


    }
    private void saveProjDesc(String desc){
        ProjDescModel model = new ProjDescModel(desc);
        projectdesc.add(model)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "error !", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
