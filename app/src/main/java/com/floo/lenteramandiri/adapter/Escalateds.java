package com.floo.lenteramandiri.adapter;

/**
 * Created by Floo on 5/17/2016.
 */
public class Escalateds {
    private String escalate;
    private String bold;


    public Escalateds(){}

    public Escalateds(String escalate, String bold){
        this.escalate = escalate;
        this.bold = bold;

    }

    public String getEscalate(){
        return escalate;
    }

    public void setEscalate(String escalate){
        this.escalate = escalate;
    }

    public String getBold(){
        return bold;
    }

    public void setBold(String bold){
        this.bold = bold;
    }
}
