package com.floo.lenteramandiri.adapter;

/**
 * Created by Floo on 3/4/2016.
 */
public class Task {
    private int task_id;
    private String title;
    private int expire;
    private String note;
    private String company;

    public Task(){

    }

    public Task(int task_id, String title, int expire, String note, String company){
        this.task_id = task_id;
        this.title = title;
        this.expire = expire;
        this.note = note;
        this.company = company;
    }

    public int getTask_id(){
        return task_id;
    }

    public void setTask_id(int task_id){
        this.task_id = task_id;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public int getExpire(){
        return expire;
    }

    public void setExpire(int expire){
        this.expire = expire;
    }

    public String getNote(){
        return note;
    }

    public void setNote(String note){
        this.note = note;
    }

    public String getCompany(){
        return company;
    }

    public void setCompany(String company){
        this.company = company;
    }
}
