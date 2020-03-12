package com.example.packagecenter;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Doctor {
    private String name;
    private int id;
    private String time;
    private String nameOfSender;
    private boolean collected=false;



    public Doctor(int id,String name) {
        this.id = id;
        this.name = name;
        this.time=time;

    }

    public Doctor(String name, String time, String nameOfSender) {
        this.name = name;
        this.id = id;
        this.time = time;
        this.nameOfSender = nameOfSender;

    }

    public Doctor() {

    }

    public void setCollected(boolean collected) {
        this.collected = collected;
    }

    public boolean isCollected() {
        return collected;
    }

    public String getNameOfSender() {
        return nameOfSender;
    }

    public void setNameOfSender(String nameOfSender) {
        this.nameOfSender = nameOfSender;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public String gettingTheCurrentTime(){

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }


    public String toString()
    {
        return "Doctor/clinic : "+this.getName()+" id: "+this.getId()+" at: "+this.getTime() +" by: "+this.getNameOfSender();
    }
}
