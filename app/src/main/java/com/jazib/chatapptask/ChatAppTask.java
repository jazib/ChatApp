package com.jazib.chatapptask;

import android.app.Application;

import com.jazib.chatapptask.models.Message;
import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by Jazib on 1/24/2015.
 */
public class ChatAppTask extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        ParseObject.registerSubclass(Message.class);
        Parse.initialize(this, "dQyMhiKOiP3CKnMgospl8JY9sKkEC1hbfkRjqX9c", "wEUqyLeiRj5UAw6FcpH1rabiHQRxrhzC8V6d0VMU");
    }
}
