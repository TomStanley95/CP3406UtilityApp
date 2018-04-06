package com.example.admin.smartclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements OnClickListener, TimePicker.OnTimeChangedListener{

    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    private Boolean doRepeatAlarm;
    private TimePicker alarmTimePicker;
    private Intent intentToFire;
    private SharedPreferences sharedPreferences;
    public static Context contextOfApplication;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        intentToFire = new Intent(this, AlarmReceiver.class);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        createWidjListeners();
        getPreferenceValues();
        contextOfApplication = getApplicationContext();
//      Init the alarms state as off.
        String ringtone = sharedPreferences.getString("ringtonePreference",null);
        if (ringtone == null){
            ringtone = "Andromeda";
            sharedPreferences.edit().putString("ringtonePreference",ringtone).apply();
        }
        PreferenceManager.setDefaultValues(this, R.xml.preferences,false);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this,SettingsActivity.class );
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onClick(View pressedButton){
        Button button = (Button) pressedButton;
        String buttonText = button.getText().toString();
        switch (buttonText){
            case "Set Alarm":
                setAlarm();
                break;
            case "Reset Alarm":
                cancelAlarm();
                break;
            case "Cancel Alarm":
                cancelAlarm();
                break;
        }

    }

    public void setAlarm(){
        int alarmHour = alarmTimePicker.getHour();
        int alarmMinute = alarmTimePicker.getMinute();
        alarmMgr = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
//      putting the status to "on" (True)
        intentToFire.putExtra("status", "AlarmOn");
        alarmIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intentToFire, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, alarmHour);
        calendar.set(Calendar.MINUTE, alarmMinute);
        if(doRepeatAlarm) {
            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, alarmIntent);
            String test = "We've reached this repeating alarm";
            Log.i("Test", test);
        }else{
            alarmMgr.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),alarmIntent);
            String test = "We've reached this one time alarm";
            Log.i("Test", test);
        }
        String alarmType;
        if (doRepeatAlarm){
            alarmType = "repeating";
        }else{
            alarmType = "once off";
        }
        String alarmTime = Integer.toString(alarmHour) + ":" + Integer.toString(alarmMinute);
        String alarmInfoText = "You set a " + alarmType + " for " + alarmTime;
        Toast.makeText(getApplicationContext(),alarmInfoText, Toast.LENGTH_LONG).show();
        moveTaskToBack(true);
    }

    public void cancelAlarm(){
        Log.i("Test", "Cancelling alarm");
        if (alarmMgr!= null) {
            alarmMgr.cancel(alarmIntent);
        }
        intentToFire.putExtra("status", "AlarmOff");
        sendBroadcast(intentToFire);
        Toast.makeText(getApplicationContext(),"Alarm terminated", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTimeChanged(TimePicker timePicker, int hour, int minute) {
        String test = "The hour is" + Integer.toString(hour) + ", The minute is" + Integer.toString(minute);
        Log.i("Test", test);

    }

    public void createWidjListeners(){
//        Handles the creation of all the buttons + the clock.
        Button alarmSetButton = findViewById(R.id.setAlarmButton);
        Button alarmResetButton = findViewById(R.id.resetAlarmButton);
        Button alarmCancelButton = findViewById(R.id.cancelAlarmButton);
        alarmSetButton.setOnClickListener(this);
        alarmResetButton.setOnClickListener(this);
        alarmCancelButton.setOnClickListener(this);
        alarmTimePicker = findViewById(R.id.timePicker);
        alarmTimePicker.setOnTimeChangedListener(this);
    }

    public void getPreferenceValues(){
        doRepeatAlarm = sharedPreferences.getBoolean("repeatPreference", false);
    }

    public static Context getContextOfApplication(){
        return contextOfApplication;
    }



}
