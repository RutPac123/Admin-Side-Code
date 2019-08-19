package com.example.adminside.model;

import java.util.Map;

public class TransModel {
    private long transaction;
    private String cName ;
    private String acNo;
    private String date;
    private long transId;

    public TransModel(){

    }

    public TransModel(long transaction, String cName,String acNo,String date,long transId) {
        this.transaction = transaction;
        this.cName = cName;
        this.acNo = acNo;
        this.date = date;
        this.transId = transId;
    }

    public long getTransaction() {
        return transaction;
    }

    public String getcName() {
        return cName;
    }

    public String getAcNo() {
        return acNo;
    }

    public String getDate() {
        return date;
    }

    public long getTransId() {
        return transId;
    }
}
