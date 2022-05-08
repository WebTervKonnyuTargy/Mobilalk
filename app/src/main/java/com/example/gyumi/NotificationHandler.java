package com.example.gyumi;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificationHandler {

    private Context mContext;
    private NotificationManager manager;
    private static final String CHANNEL_ID="noti_channel";
    private final int noti_ID=0;

    public NotificationHandler(Context context){
        this.mContext=context;
        this.manager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        createChannel();
    }

    private void createChannel(){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            return;
        }
        NotificationChannel channel=new NotificationChannel(CHANNEL_ID, "Ertesites!!",NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setLightColor(Color.CYAN);
        channel.setDescription("Szia Uram!");
        this.manager.createNotificationChannel(channel);
    }

    public void send(String msg){
        NotificationCompat.Builder builder=new NotificationCompat.Builder(mContext,CHANNEL_ID)
                .setContentTitle("Szia Uram!")
                .setContentText(msg)
                .setSmallIcon(1);

        this.manager.notify(noti_ID,builder.build());
    }

    public void cancel(){
        this.manager.cancel(noti_ID);
    }
}
