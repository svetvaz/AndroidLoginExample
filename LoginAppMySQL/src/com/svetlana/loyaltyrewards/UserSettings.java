package com.svetlana.loyaltyrewards;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class UserSettings  extends PreferenceActivity {
 
    @SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
        addPreferencesFromResource(R.xml.settings);
 
    }
}

