package com.floo.lenteramandiri.adapter;

/**
 * Created by Floo on 5/31/2016.
 */
public class Notifi {
    private String ID;
    private String TITLE;
    private String CONTENT;
    private int DATE;


    public Notifi() {

    }

    public Notifi(String ID, String TITLE, String CONTENT, int DATE){
        this.ID = ID;
        this.TITLE = TITLE;
        this.CONTENT = CONTENT;
        this.DATE = DATE;

    }

    public String getID(){
        return ID;
    }

    public void setID(String ID){
        this.ID = ID;
    }

    public String getTITLE(){
        return TITLE;
    }

    public void setTITLE(String TITLE){
        this.TITLE = TITLE;
    }

    public String getCONTENT(){
        return CONTENT;
    }

    public void setCONTENT(String CONTENT){
        this.CONTENT= CONTENT;
    }

    public int getDATE(){
        return DATE;
    }

    public void setDATE(int DATE){
        this.DATE= DATE;
    }


}
