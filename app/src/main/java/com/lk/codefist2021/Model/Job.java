package com.lk.codefist2021.Model;

import java.util.Date;

public class Job {


    double startlocation_lat;
    double startlocation_lon;
    double endlocation_lat;
    double endlocation_lon;



    String customerName;
    String customerID;
    String tpNumber;
    Date jobcreatedAt;

    String email;
    String status;
    Date statustime;
    double estimatedprice;
    String timeminutes;


    public double getCurrentcustomer_lat() {
        return currentcustomer_lat;
    }

    public void setCurrentcustomer_lat(double currentcustomer_lat) {
        this.currentcustomer_lat = currentcustomer_lat;
    }

    public double getCurrentcustomer_lon() {
        return currentcustomer_lon;
    }

    public void setCurrentcustomer_lon(double currentcustomer_lon) {
        this.currentcustomer_lon = currentcustomer_lon;
    }

    public double getCurrentRider_lat() {
        return currentRider_lat;
    }

    public void setCurrentRider_lat(double currentRider_lat) {
        this.currentRider_lat = currentRider_lat;
    }

    public double getCurrentRider_lon() {
        return currentRider_lon;
    }

    public void setCurrentRider_lon(double currentRider_lon) {
        this.currentRider_lon = currentRider_lon;
    }

    double currentcustomer_lat;
    double currentcustomer_lon;
    double currentRider_lat;
    double currentRider_lon;




    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    public String getTimeminutes() {
        return timeminutes;
    }

    public void setTimeminutes(String timeminutes) {
        this.timeminutes = timeminutes;
    }



    public Job() {



    }

    public Job(double startlocation_lat, double startlocation_lon, double endlocation_lat, double endlocation_lon, String customerName, String customerID, String tpNumber, Date jobcreatedAt, String email, String status, Date statustime, double estimatedprice, String timeminutes, double currentcustomer_lat, double currentcustomer_lon, double currentRider_lat, double currentRider_lon) {
        this.startlocation_lat = startlocation_lat;
        this.startlocation_lon = startlocation_lon;
        this.endlocation_lat = endlocation_lat;
        this.endlocation_lon = endlocation_lon;
        this.customerName = customerName;
        this.customerID = customerID;
        this.tpNumber = tpNumber;
        this.jobcreatedAt = jobcreatedAt;
        this.email = email;
        this.status = status;
        this.statustime = statustime;
        this.estimatedprice = estimatedprice;
        this.timeminutes = timeminutes;
        this.currentcustomer_lat = currentcustomer_lat;
        this.currentcustomer_lon = currentcustomer_lon;
        this.currentRider_lat = currentRider_lat;
        this.currentRider_lon = currentRider_lon;
    }

    public double getStartlocation_lat() {
        return startlocation_lat;
    }

    public void setStartlocation_lat(double startlocation_lat) {
        this.startlocation_lat = startlocation_lat;
    }

    public double getStartlocation_lon() {
        return startlocation_lon;
    }

    public void setStartlocation_lon(double startlocation_lon) {
        this.startlocation_lon = startlocation_lon;
    }

    public double getEndlocation_lat() {
        return endlocation_lat;
    }

    public void setEndlocation_lat(double endlocation_lat) {
        this.endlocation_lat = endlocation_lat;
    }

    public double getEndlocation_lon() {
        return endlocation_lon;
    }

    public void setEndlocation_lon(double endlocation_lon) {
        this.endlocation_lon = endlocation_lon;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getTpNumber() {
        return tpNumber;
    }

    public void setTpNumber(String tpNumber) {
        this.tpNumber = tpNumber;
    }

    public Date getJobcreatedAt() {
        return jobcreatedAt;
    }

    public void setJobcreatedAt(Date jobcreatedAt) {
        this.jobcreatedAt = jobcreatedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getStatustime() {
        return statustime;
    }

    public void setStatustime(Date statustime) {
        this.statustime = statustime;
    }

    public double getEstimatedprice() {
        return estimatedprice;
    }

    public void setEstimatedprice(double estimatedprice) {
        this.estimatedprice = estimatedprice;
    }
}





