package com.cmput301w17t08.moodr;

import android.location.Location;

import java.io.Serializable;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * Created by ZL on 2/25/2017.
 */
public class Mood implements Serializable{
    private Date date;
    private String owner;
    private int id;
    //private Emotion emotion;
    private String emotion; // Change later
    private String imgUrl;
    private String trigger;
    private String situation;
    private String location;

    public Mood(String owner, String emotion)
    {
        this.date = new Date(System.currentTimeMillis());
        setUsername(owner);
        setEmotion(emotion);
        setImgUrl(imgUrl);
        setTrigger(trigger);
        setSituation(situation);
        setLocation(location);

    }

    public String getUsername(){
        return owner;
    }
    public void setUsername(String Username){
        this.owner=Username;
    }


    public Date getDate(){
        return date;
    }
    public void setDate(Date date){
        this.date = date;
    }


    public String getEmotion(){
        return emotion;
    }
    public void setEmotion(String emotion){
        this.emotion=emotion;
    }
/*
    public Emotion getEmotion(){
        return emotion;
    }
    public void setEmotion(Emotion emotion){
        this.emotion=emotion;
    }
*/

    public int getId(){
        return id;
    }
    public void setId(int lastid){
        this.id=lastid+1;
    }

    public String getImgUrl(){
        return imgUrl;
    }
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTrigger(){
        return trigger;
    }
    public void setTrigger(String trigger){
            this.trigger=trigger;
    }

    public String getSituation(){
        return situation;
    }
    public void setSituation(String situation){
        this.situation=situation;
    }

    public String getLocation(){
        return location;
    }
    public void setLocation(String location){
        this.location=location;
    }
}
