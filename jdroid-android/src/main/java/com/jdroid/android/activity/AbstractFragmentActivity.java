package com.jdroid.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.application.AppModule;
import com.jdroid.android.fragment.UseCaseFragment;
import com.jdroid.android.loading.ActivityLoading;
import com.jdroid.android.navdrawer.NavDrawer;
import com.jdroid.android.uri.UriHandler;
import com.jdroid.java.exception.UnexpectedException;

/**
 * Base {@link Activity}
 * 
 */
public abstract class AbstractFragmentActivity extends AppCompatActivity implements ActivityIf {
	
	private ActivityHelper activityHelper;
	
	@Override
	public Boolean onBeforeSetContentView() {
		return activityHelper.onBeforeSetContentView();
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#onAfterSetContentView(android.os.Bundle)
	 */
	@Override
	public void onAfterSetContentView(Bundle savedInstanceState) {
		activityHelper.onAfterSetContentView(savedInstanceState);
	}
	
	/**
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		activityHelper = AbstractApplication.get().createActivityHelper(this);
		activityHelper.beforeOnCreate();
		super.onCreate(savedInstanceState);
		activityHelper.onCreate(savedInstanceState);
	}
	
	/**
	 * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		activityHelper.onSaveInstanceState(outState);
	}
	
	@Override
	protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		activityHelper.onRestoreInstanceState(savedInstanceState);
	}
	
	/**
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
		super.onStart();
		activityHelper.onStart();
	}
	
	/**
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		activityHelper.onResume();
	}
	
	/**
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		activityHelper.onBeforePause();
		super.onPause();
		activityHelper.onPause();
	}
	
	/**
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		super.onStop();
		activityHelper.onStop();
	}
	
	/**
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		activityHelper.onBeforeDestroy();
		super.onDestroy();
		activityHelper.onDestroy();
	}
	
	/**
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		activityHelper.onActivityResult(requestCode, resultCode, data);
	}
	
	/**
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return activityHelper.onCreateOptionsMenu(menu);
	}
	
	/**
	 * @see android.app.Activity#onPrepareOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		activityHelper.onPrepareOptionsMenu(menu);
		return super.onPrepareOptionsMenu(menu);
	}
	
	@Override
	public Integer getMenuResourceId() {
		return activityHelper.getMenuResourceId();
	}
	
	@Override
	public void doOnCreateOptionsMenu(Menu menu) {
		activityHelper.doOnCreateOptionsMenu(menu);
	}
	
	/**
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return activityHelper.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#findView(int)
	 */
	@Override
	public <V extends View> V findView(int id) {
		return activityHelper.findView(id);
	}
	
	/**
	 * @see com.jdroid.android.activity.ComponentIf#inflate(int)
	 */
	@Override
	public View inflate(int resource) {
		return activityHelper.inflate(resource);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#getExtra(java.lang.String)
	 */
	@Override
	public <E> E getExtra(String key) {
		return activityHelper.<E>getExtra(key);
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#requiresAuthentication()
	 */
	@Override
	public Boolean requiresAuthentication() {
		return activityHelper.requiresAuthentication();
	}
	
	public void loadUseCaseFragment(Bundle savedInstanceState, Class<? extends UseCaseFragment<?>> useCaseFragmentClass) {
		
		if (savedInstanceState == null) {
			try {
				FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
				fragmentTransaction.add(useCaseFragmentClass.newInstance(), useCaseFragmentClass.getSimpleName());
				fragmentTransaction.commit();
			} catch (InstantiationException e) {
				throw new UnexpectedException(e);
			} catch (IllegalAccessException e) {
				throw new UnexpectedException(e);
			}
		}
	}
	
	public void removeUseCaseFragment(Class<? extends UseCaseFragment<?>> useCaseFragmentClass) {
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.remove(getUseCaseUseCaseFragment(useCaseFragmentClass));
		fragmentTransaction.commit();
	}
	
	public UseCaseFragment<?> getUseCaseUseCaseFragment(Class<? extends UseCaseFragment<?>> useCaseFragmentClass) {
		return (UseCaseFragment<?>)getSupportFragmentManager().findFragmentByTag(useCaseFragmentClass.getSimpleName());
	}
	
	@SuppressWarnings("unchecked")
	public <E extends Fragment> E getFragment(Class<E> fragmentClass) {
		return (E)getSupportFragmentManager().findFragmentByTag(fragmentClass.getSimpleName());
	}
	
	public void addFragment(Fragment newFragment, int containerId, boolean addToBackStack) {
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.add(containerId, newFragment, newFragment.getClass().getSimpleName());
		if (addToBackStack) {
			fragmentTransaction.addToBackStack(newFragment.getClass().getSimpleName());
		}
		fragmentTransaction.commit();
	}

	public void replaceFragment(Fragment newFragment, int containerId, boolean addToBackStack) {
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.replace(containerId, newFragment);
		if (addToBackStack) {
			fragmentTransaction.addToBackStack(newFragment.getClass().getSimpleName());
		}
		fragmentTransaction.commit();
	}

	public <E extends Fragment> E instanceFragment(Class<E> fragmentClass, Bundle bundle) {
		E fragment;
		try {
			fragment = fragmentClass.newInstance();
		} catch (InstantiationException e) {
			throw new UnexpectedException(e);
		} catch (IllegalAccessException e) {
			throw new UnexpectedException(e);
		}
		fragment.setArguments(bundle);
		return fragment;
	}
	
	public void commitFragment(int containerViewId, Fragment fragment) {
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.add(containerViewId, fragment);
		fragmentTransaction.commit();
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#isLauncherActivity()
	 */
	@Override
	public Boolean isLauncherActivity() {
		return activityHelper.isLauncherActivity();
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#getLocationFrequency()
	 */
	@Override
	public Long getLocationFrequency() {
		return activityHelper.getLocationFrequency();
	}

	/**
	 * @see android.app.Activity#onPostCreate(android.os.Bundle)
	 */
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		activityHelper.onPostCreate(savedInstanceState);
	}
	
	/**
	 * @see android.app.Activity#onConfigurationChanged(android.content.res.Configuration)
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		activityHelper.onConfigurationChanged(newConfig);
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#getUpIntent()
	 */
	@Override
	public Intent getUpIntent() {
		return activityHelper.getUpIntent();
	}
	
	/**
	 * @see android.app.Activity#onNewIntent(android.content.Intent)
	 */
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		activityHelper.onNewIntent(intent);
	}
	
	/**
	 * @see com.jdroid.android.activity.ComponentIf#executeOnUIThread(java.lang.Runnable)
	 */
	@Override
	public void executeOnUIThread(Runnable runnable) {
		activityHelper.executeOnUIThread(runnable);
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#isActivityDestroyed()
	 */
	@Override
	public Boolean isActivityDestroyed() {
		return activityHelper.isActivityDestroyed();
	}

	@Override
	public UriHandler getUriHandler() {
		return activityHelper.getUriHandler();
	}

	@Override
	public Boolean isGooglePlayServicesVerificationEnabled() {
		return activityHelper.isGooglePlayServicesVerificationEnabled();
	}

	// //////////////////////// Loading //////////////////////// //
	
	/**
	 * @see com.jdroid.android.activity.ComponentIf#showLoading()
	 */
	@Override
	public void showLoading() {
		activityHelper.showLoading();
	}
	
	/**
	 * @see com.jdroid.android.activity.ComponentIf#dismissLoading()
	 */
	@Override
	public void dismissLoading() {
		activityHelper.dismissLoading();
	}
	
	@NonNull
	@Override
	public ActivityLoading getDefaultLoading() {
		return activityHelper.getDefaultLoading();
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#setLoading(com.jdroid.android.loading.ActivityLoading)
	 */
	@Override
	public void setLoading(ActivityLoading loading) {
		activityHelper.setLoading(loading);
	}

	@Override
	public void onBackPressed() {
		if (!onBackPressedHandled()) {
			super.onBackPressed();
		}
	}

	public Boolean onBackPressedHandled() {
		return activityHelper.onBackPressedHandled();
	}

	// //////////////////////// Navigation Drawer //////////////////////// //

	@Override
	public void initNavDrawer(Toolbar appBar) {
		activityHelper.initNavDrawer(appBar);
	}

	@Override
	public NavDrawer createNavDrawer(AbstractFragmentActivity activity, Toolbar appBar) {
		return activityHelper.createNavDrawer(activity, appBar);
	}

	@Override
	public Boolean isNavDrawerEnabled() {
		return activityHelper.isNavDrawerEnabled();
	}

	@Override
	public ActivityDelegate createActivityDelegate(AppModule appModule) {
		return activityHelper.createActivityDelegate(appModule);
	}

	@Override
	public ActivityDelegate getActivityDelegate(AppModule appModule) {
		return activityHelper.getActivityDelegate(appModule);
	}
}
