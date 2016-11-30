package com.example.limjoowon.gmm;

import android.graphics.drawable.Drawable;

/**
 * Created by LimJoowon on 2016. 11. 30..
 */
public class ListViewItem {
    private Drawable chatroom_image ;
    private String chatroom_name ;
    private String chatroom_last_message ;

    public void setImage(Drawable icon) {
        chatroom_image = icon ;
    }
    public void setName(String title) {
        chatroom_name = title ;
    }
    public void setMessage(String mess) {
        chatroom_last_message = mess ;
    }

    public Drawable getImage() {
        return this.chatroom_image ;
    }
    public String getName() {
        return this.chatroom_name ;
    }
    public String getMessage() {
        return this.chatroom_last_message ;
    }
}