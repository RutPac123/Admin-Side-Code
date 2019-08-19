package com.example.adminside.model;

public class DataModel {

    private String studioName;
    private String acNo;
    private String clientName;
    private String address;
    private String email;
    private String phoneNo;
    private String totalAmount;
    private String totalAmountPaid;


    public DataModel(){

    }

    public DataModel(String studioName, String acNo,String clientName,String address,String email,String phoneNo,String totalAmount,String totalAmountPaid) {
        this.studioName = studioName;
        this.acNo = acNo;
        this.clientName = clientName;
        this.address =address;
        this.email =email;
        this.phoneNo =phoneNo;
        this.totalAmount = totalAmount;
        this.totalAmountPaid = totalAmountPaid;
    }

    public String getStudioName() {
        return studioName;
    }


    public String getAcNo() {
        return acNo;
    }

    public String getClientName() {
        return clientName;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public String getTotalAmountPaid() {
        return totalAmountPaid;
    }
}
