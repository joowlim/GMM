package com.example.limjoowon.gmm;

/**
 * Created by kijong on 2016-11-30.
 */

public class MsgItemData {
    String msg = "";
    int type = 1;
    String senderId = "";
    String senderName = "";
    String senderProfile = "";
    boolean fromMe;

    public  MsgItemData(String msg, int type, String id, String name, String profile, boolean fromMe) {
        this.msg = msg;
        this.type = type;
        this.senderId = id;
        this.senderName = name;
        this.senderProfile = profile;
        this.fromMe = fromMe;
    }
}
