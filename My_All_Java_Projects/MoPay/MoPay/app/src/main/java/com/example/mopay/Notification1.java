package com.example.mopay;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

public class Notification1 extends Application {

    public static final String CHANNEL_1_ID ="channel1";

    @Override
    public void onCreate(){
        super.onCreate();

        CreateNotificationChannels();
    }

    private void CreateNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(CHANNEL_1_ID, "channel1",NotificationManager.IMPORTANCE_HIGH );
            channel1.setDescription("Your money in the wallet increased");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
        }
    }
}
