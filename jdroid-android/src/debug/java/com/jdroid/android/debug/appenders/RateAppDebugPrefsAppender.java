package com.jdroid.android.debug.appenders;

import android.app.Activity;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceGroup;

import com.jdroid.android.R;
import com.jdroid.android.debug.PreferencesAppender;
import com.jdroid.android.feedback.RateAppStats;

public class RateAppDebugPrefsAppender extends PreferencesAppender {

	@Override
	public int getNameResId() {
		return R.string.jdroid_rateApp;
	}

	@Override
	public void initPreferences(final Activity activity, PreferenceGroup preferenceGroup) {
		Preference preference = new Preference(activity);
		preference.setTitle(R.string.jdroid_reset);
		preference.setSummary(R.string.jdroid_reset);
		preference.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				RateAppStats.reset();
				return true;
			}
		});
		preferenceGroup.addPreference(preference);
	}
}
