package com.floo.lenteramandiri.adapter;

/**
 * Created by Floo on 3/4/2016.
 */
public class News {
    private int news_id;
    private String title;
    private String image;
    private String url;
    private String content;
    private int date;

    public News(){

    }

    public News(int news_id, String title, String url, String image, String content, int date){
        this.news_id = news_id;
        this.title = title;
        this.image = image;
        this.url = url;
        this.content = content;
        this.date = date;
    }

    public int getNews_id(){
        return news_id;
    }

    public void setNews_id(int news_id){
        this.news_id = news_id;
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

    public String getContent(){
        return content;
    }

    public void setContent(String content){
        this.content = content;
    }

    public int getDate(){
        return date;
    }

    public void setDate(int date){
        this.date = date;
    }
}
