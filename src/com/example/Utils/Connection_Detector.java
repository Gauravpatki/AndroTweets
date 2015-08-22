package com.example.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Connection_Detector {
	AlertDialog pDialog;
	private Context acontext;

	public Connection_Detector(Context context) {
		this.acontext = context;
	}

	public boolean isConnectingToInternet() {
		ConnectivityManager connectivity = (ConnectivityManager) acontext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						if (info[i].isConnected()) {
							return true;
						}
					}

		}
		return false;
	}

	public void showNoInternetPopup() {
		pDialog = new AlertDialog.Builder(acontext).create();
		pDialog.setMessage("Please make sure you are connected to the internet and try again.");
		pDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Ok",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						pDialog.dismiss();
						((Activity) acontext).finish();

					}
				});
		pDialog.setCancelable(false);
		pDialog.show();
	}
}
