package com.example.admin.smartclock;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.net.ConnectException;


public class AlarmReceiver extends BroadcastReceiver {
    Context applicationContext;
    String CHANNEL_ID;
    NotificationManager notificationManager;



    @Override
    public void onReceive(Context context, Intent intent) {
        CHANNEL_ID = "SmartAlarm";
        applicationContext = MainActivity.getContextOfApplication();
        String currentInstruction = intent.getExtras().getString("status","AlarmOn");
        Log.i("Test", currentInstruction);
        Log.i("Test", "You've made it into the alarm receiver");
        notificationManager = (NotificationManager) applicationContext.getSystemService(Context.NOTIFICATION_SERVICE);
        if (currentInstruction.equals("AlarmOn")){
            createNotification();
            Intent startIntent = new Intent(applicationContext,RingtonePlayingService.class);
            applicationContext.startService(startIntent);
            Log.i("Test", "Playing sound and showing notification");

        }else{
            Intent stopIntent = new Intent(applicationContext,RingtonePlayingService.class);
            applicationContext.stopService(stopIntent);
            Log.i("Test", "Turning off sound");
        }
    }
    public void createNotification(){
        Log.i("Test", "Creating notification");
        int NOTIFICATION_ID = 1995;
        NotificationCompat.Builder notificationBuilder;
        Intent notificationIntent = new Intent(applicationContext,MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(applicationContext,0,notificationIntent,0);
        notificationBuilder = new NotificationCompat.Builder(applicationContext,CHANNEL_ID );
        notificationBuilder.setContentTitle("Smart Alarm is going off" + "!");
        notificationBuilder.setContentText("Click me!");
        notificationBuilder.setSmallIcon(R.drawable.ic_notifcation_icon);
        notificationBuilder.setContentIntent(pendingIntent);
        notificationBuilder.setAutoCancel(true);
        Notification notification = notificationBuilder.build();
        notificationManager.notify(NOTIFICATION_ID,notification);
    }
}
