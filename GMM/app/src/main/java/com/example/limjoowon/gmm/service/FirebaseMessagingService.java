package com.example.limjoowon.gmm.service;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.example.limjoowon.gmm.ChatActivity;
import com.example.limjoowon.gmm.GMMApplication;
import com.example.limjoowon.gmm.R;
import com.example.limjoowon.gmm.config.MsgServerConfig;
import com.example.limjoowon.gmm.module.LocalChatDataManager;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.util.List;

/**
 * Google FCM 메시지를 받는 서비스 클래스
 * Created by kijong on 2016-11-07.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // JSON 형태의 메시지를 파싱한다.
        String msg="";
        String chatRoomId = "";
        String senderId="";
        String senderGoogle = "";
        String senderName = "";
        String senderProfile = "";

        try {
            String jsonStr = remoteMessage.getData().get("message");
            JSONObject obj = new JSONObject(jsonStr);
            msg = obj.getString(MsgServerConfig.KEY_MSG);
            senderId = obj.getString(MsgServerConfig.KEY_SENDER);
            senderGoogle = obj.getString(MsgServerConfig.KEY_SENDER_GOOGLE);
            senderName = obj.getString(MsgServerConfig.KEY_SENDER_NAME);
            senderProfile = obj.getString(MsgServerConfig.KEY_SENDER_PROFILE_URI);
            chatRoomId = obj.getString(MsgServerConfig.KEY_CHAT_ROOM_ID);

            // msg, senderId 등이 null 이거나 empty string 이면 잘못된 메시지로 판단하고 return
            if (msg == null || msg.isEmpty() || senderId == null || senderId.isEmpty()) return;
            // 내가 보냈던 메시지면 그냥 return
            if (GMMApplication.getToken().equals(senderId)) return;

        } catch(Exception e) {
        }
        // 로컬에 저장
        LocalChatDataManager.getInstance().saveNewMessage(chatRoomId, senderId, senderGoogle, senderName, senderProfile, msg);

        // 채팅화면이 활성화 상태이면 broadcast로 바로 메시지를 주고 아니면 Noti를 해준다.
        if (GMMApplication.isChatRoomInForeground()) {
            sendBroadCast(senderId, senderGoogle, senderName, senderProfile, msg);
        } else {
            sendNotification(senderId, msg);
        }
    }

    private void sendNotification(String senderId, String messageBody) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("조모임 메신저")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private void sendBroadCast(String senderId, String googleId, String name, String profile, String msg) {
        Intent broadcastIntent = new Intent();

        broadcastIntent.setAction(ChatActivity.mBroadcastStringAction);
        broadcastIntent.putExtra(MsgServerConfig.KEY_MSG, msg);
        broadcastIntent.putExtra(MsgServerConfig.KEY_SENDER, senderId);
        broadcastIntent.putExtra(MsgServerConfig.KEY_SENDER_GOOGLE, googleId);
        broadcastIntent.putExtra(MsgServerConfig.KEY_SENDER_NAME, name);
        broadcastIntent.putExtra(MsgServerConfig.KEY_SENDER_PROFILE_URI, profile);

        sendBroadcast(broadcastIntent);
    }
}
