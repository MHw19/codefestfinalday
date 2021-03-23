package com.lk.codefist2021.pojo;

public class Maptimeobj {

    String timetext;
    int timeminiutes;


    public Maptimeobj(String timetext, int timeminiutes) {
        this.timetext = timetext;
        this.timeminiutes = timeminiutes;
    }

    public String getTimetext() {
        return timetext;
    }

    public void setTimetext(String timetext) {
        this.timetext = timetext;
    }

    public int getTimeminiutes() {
        return timeminiutes;
    }

    public void setTimeminiutes(int timeminiutes) {
        this.timeminiutes = timeminiutes;
    }

}
