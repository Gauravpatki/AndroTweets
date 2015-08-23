package com.example.htest;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Adapters.DetailsList;
import com.example.Adapters.UserList;
import com.example.Utils.Connection_Detector;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.Search;
import com.twitter.sdk.android.core.models.User;

public class HomeScreen extends Activity implements
		SearchView.OnQueryTextListener {
	TwitterLoginButton login;
	ListView feed;
	TwitterSession session;
	ProgressDialog pDialog;
	SearchView mSearchView;
	Connection_Detector cd;
	TextView no;
	Menu aMenu;
	ImageView search;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.home_screen);
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.DKGRAY));
		no = (TextView) findViewById(R.id.no);
		login = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
		feed = (ListView) findViewById(R.id.feed);
		session = Twitter.getSessionManager().getActiveSession();
		search = (ImageView) findViewById(R.id.imageView1);
		search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mSearchView.setIconified(false);
			}
		});
		refreshUi();
		login.setCallback(new Callback<TwitterSession>() {

			@Override
			public void success(Result<TwitterSession> result) {
				session = Twitter.getSessionManager().getActiveSession();
				mSearchView.setVisibility(View.VISIBLE);
				// mSearchView.onActionViewExpanded();
				search.setVisibility(View.VISIBLE);
				aMenu.findItem(R.id.action_search).setVisible(true);
				Toast.makeText(HomeScreen.this,
						"Welcome  " + result.data.getUserName(),
						Toast.LENGTH_LONG).show();
				refreshUi();
			}

			@Override
			public void failure(TwitterException exception) {
				mSearchView.setVisibility(View.GONE);
				Toast.makeText(HomeScreen.this,
						"Login Failed Please try again !", Toast.LENGTH_LONG)
						.show();

			}
		});

	}

	private void refreshUi() {
		if (session != null) {
			login.setVisibility(View.GONE);
			search.setVisibility(View.VISIBLE);

		} else {
			login.setVisibility(View.VISIBLE);

		}

	}

	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		aMenu = menu;
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		mSearchView = (SearchView) menu.findItem(R.id.action_search)
				.getActionView();
		mSearchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));
		mSearchView.setQueryHint("@user, #HashTag or Search Word");
		mSearchView.setOnQueryTextListener(this);
		mSearchView.setClickable(false);
		if (session == null) {
			menu.findItem(R.id.action_search).setVisible(false);

		} else {
			menu.findItem(R.id.action_search).setVisible(true);
			// mSearchView.onActionViewExpanded();

		}

		return super.onCreateOptionsMenu(menu);

	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		pDialog = ProgressDialog.show(HomeScreen.this, "",
				"Loading. Please wait...", true);
		Log.v("search", query);
		String charat = "" + query.charAt(0);
		mSearchView.clearFocus();
		cd = new Connection_Detector(this);
		if (cd.isConnectingToInternet()) {
			search.setVisibility(View.GONE);
			if (charat.equalsIgnoreCase("@")) {
				getUserName(query);
			} else {
				getHashTag(query);

			}
		} else {
			cd.showNoInternetPopup();
			pDialog.dismiss();
		}
		return true;
	}

	void getUserName(String query) {
		search.setVisibility(View.GONE);

		UserApi m = new UserApi(session);
		m.getCustomService().Getshow(query, new Callback<List<User>>() {

			@Override
			public void success(Result<List<User>> result) {
				if (result != null && result.data.size() >= 1) {
					feed.setVisibility(View.VISIBLE);
					no.setVisibility(View.GONE);
					UserList detailsAdapter = new UserList(HomeScreen.this,
							result.data);
					feed.setAdapter(detailsAdapter);
				} else {
					feed.setVisibility(View.GONE);
					no.setVisibility(View.VISIBLE);
				}
				pDialog.dismiss();
			}

			@Override
			public void failure(TwitterException arg0) {
				Toast.makeText(HomeScreen.this,
						"Something Went wrong ,Please try again!",
						Toast.LENGTH_LONG).show();
				pDialog.dismiss();
			}
		});
	}

	void getHashTag(String query) {
		TwitterApiClient twitterApiClient = TwitterCore.getInstance()
				.getApiClient(session);
		twitterApiClient.getSearchService().tweets(query, null, null, null,
				null, 100, null, null, null, true, new Callback<Search>() {
					@Override
					public void success(Result<Search> result) {
						if (result != null && result.data.tweets.size() >= 1) {
							feed.setVisibility(View.VISIBLE);
							no.setVisibility(View.GONE);

							DetailsList detailsAdapter = new DetailsList(
									HomeScreen.this, result.data.tweets);
							feed.setAdapter(detailsAdapter);

						} else {
							feed.setVisibility(View.GONE);
							no.setVisibility(View.VISIBLE);
						}
						pDialog.dismiss();
					}

					@Override
					public void failure(TwitterException exception) {
						Toast.makeText(HomeScreen.this,
								"Something Went wrong ,Please try again!",
								Toast.LENGTH_LONG).show();

						pDialog.dismiss();

					}
				});

	}

	@Override
	public boolean onQueryTextChange(String newText) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		login.onActivityResult(requestCode, resultCode, data);
	}
}
