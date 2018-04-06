package com.example.admin.smartclock;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

public class RingtonePlayingService extends Service {
    Context applicationContext;
    Ringtone alarm;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        String alarmRingtone;
        applicationContext = MainActivity.getContextOfApplication();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        alarmRingtone = sharedPreferences.getString("ringtonePreference","default ringtone");
        Uri uri = Uri.parse(alarmRingtone);
        alarm = RingtoneManager.getRingtone(applicationContext,uri);
        alarm.play();
        return START_NOT_STICKY;
    }
    @Override
    public void onDestroy(){
        alarm.stop();
    }
}
