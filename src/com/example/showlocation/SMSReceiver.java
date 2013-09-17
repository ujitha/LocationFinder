package com.example.showlocation;

import java.util.StringTokenizer;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.telephony.SmsMessage;

public class SMSReceiver extends BroadcastReceiver {
	SmsMessage currentMessage;

	@Override
	public void onReceive(final Context context, Intent intent) {

		//if (intent.getAction().equals(android.provider.Telephony.SMS_RECEIVED)) {
			final Bundle bundle = intent.getExtras();

			try {
				if (bundle != null) {
					Object[] pduObj = (Object[]) bundle.get("pdus");

					for (int i = 0; i < pduObj.length; i++) {
						currentMessage = SmsMessage
								.createFromPdu((byte[]) pduObj[i]);

					}

					String phoneNumber = currentMessage.getOriginatingAddress();
					String Msg = currentMessage.getDisplayMessageBody()
							.toString();

					if (Msg.startsWith("@locationfinder#")) {
						StringTokenizer st = new StringTokenizer(Msg, "#");
						st.nextToken();
						String smLat = st.nextToken();
						String latitude = smLat.substring(4);
						String smlon = st.nextToken();
						String longitude = smlon.substring(4);
						String date = st.nextToken();

						final LocationObj loc = new LocationObj(phoneNumber,
								latitude, longitude, date);

						AlertDialog.Builder alert = new AlertDialog.Builder(
								context);
						alert.setTitle("New Location received");
						alert.setMessage("New Location received from "
								+ phoneNumber);

						alert.setPositiveButton("Show",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {

										Intent i = new Intent(context,
												MapLocation.class);

										i.putExtra("LocObj", loc);
										i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
										context.startActivity(i);
									}
								});

						alert.setNegativeButton("Ignore",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {

										dialog.cancel();

									}
								});

					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

//}
