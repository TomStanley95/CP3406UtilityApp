package com.example.admin.smartclock;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;




public class AlarmReceiver extends BroadcastReceiver {
    Context applicationContext;



    @Override
    public void onReceive(Context context, Intent intent) {
        String alarmRingtone;
        Ringtone alarm;
        Boolean currentState = intent.getExtras().getBoolean("extra");
        Log.i("Test", "You've made it into the alarm receiver");
        applicationContext = MainActivity.getContextOfApplication();
        createNotification();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        alarmRingtone = sharedPreferences.getString("ringtonePreference","default ringtone");
        Uri ringtone = Uri.parse(alarmRingtone);
        alarm = RingtoneManager.getRingtone(applicationContext,ringtone);
        if (currentState){
            alarm.play();
        }else {
            alarm.stop();
        }


//        Intent ringtoneIntent = new Intent(context,RingtonePlayingService.class);
//        ringtoneIntent.putExtra("extra", currentState);
//        context.startService(ringtoneIntent);
    }
    public void createNotification(){
        String CHANNEL_ID = "SmartAlarm";
        int NOTIFICATION_ID = 1995;
        Intent intent = new Intent(applicationContext,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(applicationContext,0,intent,0);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(applicationContext);
//        // Registering the apps notification channel Read I needed to do it on the documentation. Will not work.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            // Create the NotificationChannel, but only on API 26+ because
//            // the NotificationChannel class is new and not in the support library
//            CharSequence name = "SmartChannel";
//            String description = "This is the channel for SmartAlarm notification";
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
//            channel.setDescription(description);
//            // Register the channel with the system
//            notificationManager.create
//            notificationManager.createNotificationChannel(channel);
//        }
        Notification myNotifier = new NotificationCompat.Builder(applicationContext,CHANNEL_ID )
                .setContentTitle("Smart Alarm is going off" + "!")
                .setContentText("Click me!")
                .setSmallIcon(R.drawable.ic_notifcation_icon)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();
        notificationManager.notify(NOTIFICATION_ID,myNotifier);
    }
}