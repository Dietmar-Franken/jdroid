package com.jdroid.android.twitter;

import android.content.Context;
import android.view.View;

import com.jdroid.android.concurrent.SafeRunnable;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.java.utils.LoggerUtils;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.CompactTweetView;

import org.slf4j.Logger;

import java.util.List;

public abstract class CyclingTwitterHelper extends TwitterHelper {
	
	private final static Logger LOGGER = LoggerUtils.getLogger(CyclingTwitterHelper.class);
	
	private final static int TWEET_CYCLE_FREQUENCY = 10000;
	
	private int currentTweetIndex = 0;
	private DisplayTweetRunnable displayTweetRunnable = new DisplayTweetRunnable();
	
	public void onStart() {
		getTweetContainer().postDelayed(displayTweetRunnable, TWEET_CYCLE_FREQUENCY);
	}
	
	public void onStop() {
		getTweetContainer().removeCallbacks(displayTweetRunnable);
	}
	
	@Override
	protected void onSuccess(List<Tweet> tweets) {
		displayTweet(tweets);
	}
	
	private void displayTweet(List<Tweet> tweets) {
		if (tweets.size() > currentTweetIndex) {
			Tweet tweet = tweets.get(currentTweetIndex);
			
			currentTweetIndex++;
			if (tweets.size() <= currentTweetIndex) {
				currentTweetIndex = 0;
			}
			
			AbstractFragment abstractFragment = getAbstractFragment();
			Context context = abstractFragment != null ? abstractFragment.getContext() : null;
			if (context != null) {
				getTweetContainer().removeAllViews();
				getTweetContainer().addView(new CompactTweetView(context, tweet));
				getTweetContainer().setVisibility(View.VISIBLE);
				getTweetContainer().removeCallbacks(displayTweetRunnable);
				getTweetContainer().postDelayed(displayTweetRunnable, TWEET_CYCLE_FREQUENCY);
			}
			
		}
	}
	
	private class DisplayTweetRunnable extends SafeRunnable {
		
		@Override
		public void doRun() {
			displayTweet(getTweets());
		}
	}
}
