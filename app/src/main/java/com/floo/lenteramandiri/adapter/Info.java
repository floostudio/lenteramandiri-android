package com.floo.lenteramandiri.adapter;

/**
 * Created by Floo on 3/4/2016.
 */
public class Info {
    private int info_id;
    private String title;
    private String image;
    private String url;
    private int date;

    public Info(){

    }

    public Info(int info_id, String title, String url, String image, int date){
        this.info_id = info_id;
        this.title = title;
        this.image = image;
        this.url = url;
        this.date = date;
    }

    public int getInfo_id(){
        return info_id;
    }

    public void setInfo_id(int info_id){
        this.info_id = info_id;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getImage(){
        return image;
    }

    public void setImage(String image){
        this.image = image;
    }

    public String getUrl(){
        return url;
    }

    public void setUrl(String url){
        this.url = url;
    }

    public int getDate(){
        return date;
    }

    public void setDate(int date){
        this.date = date;
    }
}
