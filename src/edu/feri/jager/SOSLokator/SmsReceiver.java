package edu.feri.jager.SOSLokator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class SmsReceiver extends BroadcastReceiver {
	private static final String SOS_MESSAGE_CONTENT = "SOS!!! Moja trenutna lokacija:";
	private static final String SOS_GEOWIDTH_CONTENT = "Zemljepisna sirina:";
	private static final String SOS_GEOHEIGHT_CONTENT = "Zemljepisna dolzina:";
	private static final String SOS_ADRESS_CONTENT = "Naslov:\n";

	public void onReceive(Context context, Intent intent) {

		Bundle bundle = intent.getExtras();

		Object messages[] = (Object[]) bundle.get("pdus");
		SmsMessage smsMessage[] = new SmsMessage[messages.length];
		for (int n = 0; n < messages.length; n++) {
			smsMessage[n] = SmsMessage.createFromPdu((byte[]) messages[n]);
		}
		String body = smsMessage[0].getMessageBody();

		if(body.startsWith(SOS_MESSAGE_CONTENT)) {
			String sender = smsMessage[0].getOriginatingAddress();
			System.out.println(sender);

			String geoWidth = getConetentData(body, SOS_GEOWIDTH_CONTENT).trim();
			String latitude = geoWidth.substring(geoWidth.indexOf("[") + 1);
			latitude = latitude.substring(0, latitude.length() - 1);

			String geoHeight = getConetentData(body, SOS_GEOHEIGHT_CONTENT).trim();
			String longitude = geoHeight.substring(geoHeight.indexOf("[") + 1);
			longitude = longitude.substring(0, longitude.length() - 1);

			String sosAddress = body.substring(body.indexOf(SOS_ADRESS_CONTENT) + SOS_ADRESS_CONTENT.length());;

			SOSMessage sosMessage = new SOSMessage(sender, latitude, longitude, sosAddress);

//			this.abortBroadcast();

			try {
				Intent i = new Intent(context, SearchActivity.class);  
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
				i.putExtra("smsData", sosMessage.getArray());
				context.startActivity(i); 
			} catch (Exception e) {
				e.printStackTrace();
			}
			 
		}
	}

	private String getConetentData(String message, String content) {
		String startString = message.substring(message.indexOf(content) + content.length());

		return startString.substring(0, startString.indexOf("\n"));
	}
}
