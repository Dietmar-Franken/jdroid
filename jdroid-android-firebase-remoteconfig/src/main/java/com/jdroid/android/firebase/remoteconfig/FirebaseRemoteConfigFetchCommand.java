package com.jdroid.android.firebase.remoteconfig;

import android.os.Bundle;

import com.jdroid.android.firebase.jobdispatcher.ServiceCommand;

public class FirebaseRemoteConfigFetchCommand extends ServiceCommand {
	
	public static final String CACHE_EXPIRATION_SECONDS = "cacheExpirationSeconds";
	public static final String SET_EXPERIMENT_USER_PROPERTY = "setExperimentUserProperty";
	
	public FirebaseRemoteConfigFetchCommand() {
		setInstantExecutionRequired(false);
	}

	@Override
	protected boolean execute(Bundle bundle) {
		Long cacheExpirationSeconds = bundle.getLong(CACHE_EXPIRATION_SECONDS);
		Boolean setExperimentUserProperty = bundle.getBoolean(SET_EXPERIMENT_USER_PROPERTY);
		FirebaseRemoteConfigHelper.fetch(cacheExpirationSeconds, setExperimentUserProperty);
		return false;
	}
}
