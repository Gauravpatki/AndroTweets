package com.example.Adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.htest.R;
import com.twitter.sdk.android.core.models.Tweet;

public class DetailsList extends BaseAdapter {

	Context acontext;
	boolean aLocation;
	List<Tweet> mResult;
	LayoutInflater layoutInflater = null;

	public DetailsList(Context acontext, List<Tweet> tweets) {
		this.acontext = acontext;
		this.mResult = tweets;
		layoutInflater = LayoutInflater.from(acontext);

	}

	@Override
	public int getCount() {

		return mResult.size();
	}

	@Override
	public Object getItem(int position) {

		return position;
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ListItem listItem = null;
		if (convertView == null) {
			listItem = new ListItem();
			convertView = layoutInflater.inflate(R.layout.hastag_row, null);
			listItem.name = (TextView) convertView.findViewById(R.id.name);
			listItem.details = (TextView) convertView
					.findViewById(R.id.details);

			convertView.setTag(listItem);
		} else {
			listItem = (ListItem) convertView.getTag();
		}
		listItem.name.setText(mResult.get(position).text);
		listItem.details.setText(mResult.get(position).user.name);
		return convertView;
	}

	public class ListItem {

		public TextView name, details;

	}
}
