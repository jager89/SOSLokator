package edu.feri.jager.SOSLokator.services;

import edu.feri.jager.SOSLokator.SOSSearchActivity;
import edu.feri.jager.SOSLokator.structures.MySOSMessage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class SmsReceiver extends BroadcastReceiver {
	private static final String SOS_MESSAGE_CONTENT = new String("SOS!!! Moja trenutna lokacija:");
//	private static final String SOS_LATITUDE_CONTENT = "[LATITUDE]:";
//	private static final String SOS_LONGITUDE_CONTENT = "[LONGITUDE]:";
	private static final String SOS_DATA_START = new String("<data>");
	private static final String SOS_DATA_END = new String("</data>");
	private static final String SOS_ADRESS_CONTENT = new String("Naslov:\n");
	
	public void onReceive(Context context, Intent intent) {

		Bundle bundle = intent.getExtras();

		Object messages[] = (Object[]) bundle.get("pdus");
		SmsMessage smsMessage[] = new SmsMessage[messages.length];
		for (int n = 0; n < messages.length; n++) {
			smsMessage[n] = SmsMessage.createFromPdu((byte[]) messages[n]);
		}
		if(smsMessage.length > 0) {
			String body = "";
			
			for(int i = 0; i < messages.length; i++) {
				body += smsMessage[i].getMessageBody();

			}
			
			System.out.println("SMS!:\n" + body);
//			String body = smsMessage[0].getMessageBody();
			
			if(body.startsWith(SOS_MESSAGE_CONTENT)) {
				String sender = smsMessage[0].getOriginatingAddress();
				if(sender.startsWith("+")) {
					sender = "0" + sender.substring(4);
				}
				System.out.println(sender);
				
//				String geoWidth = getConetentData(body, SOS_LATITUDE_CONTENT).trim();
//				String latitude = geoWidth.substring(geoWidth.indexOf("["));
//				latitude = latitude.substring(0, latitude.length() - 1);
//				
//				String geoHeight = getConetentData(body, SOS_LONGITUDE_CONTENT).trim();
//				String longitude = geoHeight.substring(geoHeight.indexOf("["));
//				longitude = longitude.substring(0, longitude.length() - 1);
				
				String s = body.substring(body.indexOf(SOS_DATA_START) + SOS_DATA_START.length(), body.indexOf(SOS_DATA_END));
				
				String[] data = s.split(" ");
				String latitude = data[0];
				String longitude = data[1];
				String sosAddress = body.substring(body.indexOf(SOS_ADRESS_CONTENT) + SOS_ADRESS_CONTENT.length(), body.indexOf(SOS_DATA_START));
				
				MySOSMessage sosMessage = new MySOSMessage(sender, latitude, longitude, sosAddress);
				
//			this.abortBroadcast();
				
				try {
					Intent i = new Intent(context, SOSSearchActivity.class);  
					i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
					i.putExtra("smsData", sosMessage.getArray());
					context.startActivity(i); 
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}
	}

	private String getConetentData(String message, String content) {
		String startString = message.substring(message.indexOf(content) + content.length());

		return startString.substring(0, startString.indexOf("\n"));
	}
}
