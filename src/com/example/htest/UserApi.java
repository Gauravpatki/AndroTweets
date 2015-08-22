package com.example.htest;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Query;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.User;

class UserApi extends TwitterApiClient {
	public UserApi(TwitterSession session) {
		super(session);
	}

	/**
	 * Provide CustomService with defined endpoints
	 */
	public CustomService getCustomService() {
		return getService(CustomService.class);
	}
}

// example users/show service endpoint
interface CustomService {
	@GET("/1.1/users/search.json")
	void Getshow(@Query("q") String id, Callback<List<User>> cb);
}
