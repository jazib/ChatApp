package com.jazib.chatapptask.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Jazib on 1/24/2015.
 */
@ParseClassName("Messages")
public class Message extends ParseObject {

    public String getUserId() {
        return getString("userId");
    }
    public String getUserName() {
        return getString("userName");
    }
    public String getMessage() {
        return getString("message");
    }
    public void setUserId(String userId) {
        put("userId", userId);
    }
    public void setUserName(String userName) {
        put("userName", userName);
    }
    public void setMessage(String message) {
        put("message", message);
    }
}