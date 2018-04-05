package com.example.admin.smartclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements OnClickListener, TimePicker.OnTimeChangedListener{

    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    private Boolean repeatAlarm;
    private TimePicker alarmTime;
    private Context context;
//    private ringtone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Button alarmSetButton = findViewById(R.id.setAlarmButton);
        Button alarmResetButton = findViewById(R.id.resetAlarmButton);
        Button alarmCancelButton = findViewById(R.id.cancelAlarmButton);
        this.context = this;
        alarmTime = findViewById(R.id.timePicker);
        alarmSetButton.setOnClickListener(this);
        alarmResetButton.setOnClickListener(this);
        alarmCancelButton.setOnClickListener(this);
        alarmTime.setOnTimeChangedListener(this);
        repeatAlarm = sharedPreferences.getBoolean("repeatPreference", false);

        String alarmIntervalString = sharedPreferences.getString("alarmInterval", null);

        if (alarmIntervalString == null){
            return;
        }else  if (alarmIntervalString.equals("0")){
            String defaultInterval = "10";
            sharedPreferences.edit().putString("alarmInterval",defaultInterval).apply();
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
        int alarmHour =alarmTime.getHour();
        int alarmMinute =alarmTime.getMinute();
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, MainActivity.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, alarmHour);
        calendar.set(Calendar.MINUTE, alarmMinute);
        if(repeatAlarm) {
            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, alarmIntent);
        }else{
            alarmMgr.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),alarmIntent);
        }


    }
    public void cancelAlarm(){
        if (alarmMgr!= null) {
            alarmMgr.cancel(alarmIntent);
        }
    }

    @Override
    public void onTimeChanged(TimePicker timePicker, int hour, int minute) {
        String test = "The hour is" + Integer.toString(hour) + ", The minute is" + Integer.toString(minute);
        Log.i("Test", test);

    }



}
