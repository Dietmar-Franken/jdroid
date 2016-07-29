package com.jdroid.android.debug;

import android.app.Activity;
import android.os.Environment;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceCategory;
import android.preference.PreferenceGroup;

import com.jdroid.android.R;
import com.jdroid.android.utils.AppUtils;
import com.jdroid.android.utils.ExternalAppsUtils;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class UriMapperPrefsAppender implements PreferencesAppender {

	@Override
	public void initPreferences(final Activity activity, PreferenceGroup preferenceGroup) {

		PreferenceCategory preferenceCategory = new PreferenceCategory(activity);
		preferenceCategory.setTitle(R.string.uriMapper);
		preferenceGroup.addPreference(preferenceCategory);

		Preference preference = new Preference(activity);
		preference.setTitle(R.string.downloadUrlSample);
		preference.setSummary(R.string.downloadUrlSample);
		preference.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {

				try {
					File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
					dir.mkdirs();

					InputStream openInputStream = activity.getResources().openRawResource(activity.getResources().getIdentifier("url_samples",
							"raw", activity.getPackageName()));
					File file = new File(dir, AppUtils.getApplicationId() + "_url_samples.html");
					FileUtils.copyStream(openInputStream, file);
					openInputStream.close();

					ExternalAppsUtils.openOnChrome(activity, file);
				} catch (IOException e) {
					throw new UnexpectedException(e);
				}

				return true;
			}
		});
		preferenceCategory.addPreference(preference);
	}

	@Override
	public Boolean isEnabled() {
		return true;
	}
}