package com.jdroid.android.sample.ui.cardview;

import android.support.v4.app.Fragment;

import com.jdroid.android.activity.FragmentContainerActivity;

public class CardViewRecyclerViewActivity extends FragmentContainerActivity {

	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return CardViewRecyclerViewFragment.class;
	}
}