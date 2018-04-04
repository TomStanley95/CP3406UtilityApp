package com.example.admin.smartclock;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.widget.Toast;

/**
 * A placeholder fragment containing a simple view.
 */
public class SettingsActivityFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        PreferenceManager.getDefaultSharedPreferences(getContext()).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(getContext()).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key){
            case "alarmAmount":
                getActivity().navigateUpTo(new Intent(getContext(),MainActivity.class));
                break;
            case "alarmInterval":
                String alarmIntervalString = sharedPreferences.getString("alarmInterval", null);
//                Log.i("SettingsActivityFrag", "alarmInterval =" + alarmIntervalString);
                if (alarmIntervalString == null){
                    return;
                }
                int alarmInterval = Integer.parseInt(alarmIntervalString);
//                Log.i("Testing", Integer.toString(alarmInterval));
                if (alarmInterval == 0){
                    Toast.makeText(getContext(),"Alarm interval cannot be negative or zero, setting to default value", Toast.LENGTH_LONG).show();
                    String defaultInterval = "10";
                    sharedPreferences.edit().putString("alarmInterval",defaultInterval).apply();
                    EditTextPreference pref = (EditTextPreference) findPreference("alarmInterval");
                    pref.setText(defaultInterval);
                }
                getActivity().navigateUpTo(new Intent(getContext(),MainActivity.class));
                break;

        }



    }
}
