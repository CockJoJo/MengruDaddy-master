package com.mengrudaddy.instagram.Models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Like {

    public String username;
    public String userId;
    public String postId;
    public Date date;

    public Like(){

    }

    public Like(String postId, String userId, String username, Date date){
        this.postId = postId;
        this.username = username;
        this.userId =userId;
        this.date = date;
    }


    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("postId", postId);
        result.put("username", username);
        result.put("userId", userId);
        result.put("date", date);

        return result;
    }
}
