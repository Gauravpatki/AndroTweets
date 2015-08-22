package com.example.htest;

import io.fabric.sdk.android.Fabric;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import android.app.Application;

public class BaseApplication extends Application {

	private static final String TWITTER_KEY = "4zOutENG2jL3eyGVJlqh92b2R";
	private static final String TWITTER_SECRET = "MPj3EnWs0lB4Pjg4DWTVobwnsMURajhdxH5vgwX23kWCQe15fy";

	@Override
	public void onCreate() {
		super.onCreate();
		TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY,
				TWITTER_SECRET);
		Fabric.with(this, new Twitter(authConfig));
	}
}
