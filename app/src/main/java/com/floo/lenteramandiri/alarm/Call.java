package com.floo.lenteramandiri.alarm;

import java.util.Arrays;
import java.util.Calendar;

/**
 * Created by Floo on 3/4/2016.
 */
public class Call {
    private int id;
    private String title;
    private long date;
    private boolean active = true;

    public Call(){

    }

    /*public Call(int id, String title, long date, boolean active){
        this.id = id;
        this.title = title;
        this.date = date;
        this.active = active;
    }*/

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }


    public long getDate(){
        return date;
    }

    public void setDate(long date){
        this.date = date;
    }

    public boolean getActive(){
        return active;
    }

    public void setActive(boolean active){
        this.active = active;
    }

}
