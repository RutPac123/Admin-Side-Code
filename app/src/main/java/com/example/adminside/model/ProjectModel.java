package com.example.adminside.model;

public class ProjectModel {
    private String deliveryDate;
    private String diff;
    private String discount;
    private String eventDate;
    private String party_Name;
    private String projectDes;
    private int projectId;
    private String qty;
    private String status;
    private String titleData;
    private String totalAmount;
    private String videoData;
    private String acNo;
    private String admin;
    private String amount;
    private String bookingDate;
    private String completeDate;
    private String deliverTo;
    private String clientName;
    private String studioName;

    public ProjectModel(){

    }

    public ProjectModel(String deliveryDate, String diff, String discount, String eventDate,
                        String party_Name, String projectDes, int projectId, String qty,
                        String status, String titleData, String totalAmount, String videoData,
                        String acNo, String admin, String amount, String bookingDate,
                        String completeDate, String deliverTo,String clientName,String studioName) {
        this.deliveryDate = deliveryDate;
        this.diff = diff;
        this.discount = discount;
        this.eventDate = eventDate;
        this.party_Name = party_Name;
        this.projectDes = projectDes;
        this.projectId = projectId;
        this.qty = qty;
        this.status = status;
        this.titleData = titleData;
        this.totalAmount = totalAmount;
        this.videoData = videoData;
        this.acNo = acNo;
        this.admin = admin;
        this.amount = amount;
        this.bookingDate = bookingDate;
        this.completeDate = completeDate;
        this.deliverTo = deliverTo;
        this.clientName = clientName;
        this.studioName = studioName;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public String getDiff() {
        return diff;
    }

    public String getDiscount() {
        return discount;
    }

    public String getEventDate() {
        return eventDate;
    }

    public String getParty_Name() {
        return party_Name;
    }

    public String getProjectDes() {
        return projectDes;
    }

    public int getProjectId() {
        return projectId;
    }

    public String getQty() {
        return qty;
    }

    public String getStatus() {
        return status;
    }

    public String getTitleData() {
        return titleData;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public String getVideoData() {
        return videoData;
    }

    public String getAcNo() {
        return acNo;
    }

    public String getAdmin() {
        return admin;
    }

    public String getAmount() {
        return amount;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public String getCompleteDate() {
        return completeDate;
    }

    public String getDeliverTo() {
        return deliverTo;
    }

    public String getClientName() {
        return clientName;
    }

    public String getStudioName() {
        return studioName;
    }
}
