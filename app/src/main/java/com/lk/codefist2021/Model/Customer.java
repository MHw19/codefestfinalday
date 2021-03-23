package com.lk.codefist2021.Model;

public class Customer {

    String name;
    String email;
    String telephone;
    String address;
    String googleid;
    int userstatus;

    public String getFcmid() {
        return fcmid;
    }

    public void setFcmid(String fcmid) {
        this.fcmid = fcmid;
    }

    String fcmid;




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGoogleid() {
        return googleid;
    }

    public void setGoogleid(String googleid) {
        this.googleid = googleid;
    }

    public int getUserstatus() {
        return userstatus;
    }

    public void setUserstatus(int userstatus) {
        this.userstatus = userstatus;
    }

    public Customer() {
    }

    public Customer(String name, String email, String telephone, String address, String googleid, int userstatus, String fcmid) {
        this.name = name;
        this.email = email;
        this.telephone = telephone;
        this.address = address;
        this.googleid = googleid;
        this.userstatus = userstatus;
        this.fcmid = fcmid;
    }
}
