package edu.feri.jager.SOSLokator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	private Button buttonGetLocation = null;
	private Button buttonShowOnMap = null;
	private Button buttonSendSMS = null;
	private TextView textViewLocation = null;
	private MainApplication mainApp;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mainApp = (MainApplication) getApplication();
		mainApp.setMainActivity(this);

		getTextViewLocation();
		getButtonGetLocation();
		getButtonShowOnMap();
		getButtonSendSMS();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.prefrences:
	        System.out.println("NASTAVITVE BUTTON");
	        return true;
	    case R.id.numberList:
	        System.out.println("PREJEMNIKI BUTTON");
	        return true;
	    case R.id.exit:
	        System.out.println("EXIT BUTTON");
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu, menu);
	    return true;
	}
	
	public TextView getTextViewLocation() {
		if(textViewLocation == null) {
			textViewLocation = (TextView)findViewById(R.id.textViewLocation);
			mainApp.printLocation("Zadnja znana lokacija:");
		}
		return textViewLocation;
	}

	public Button getButtonGetLocation() {
		if(buttonGetLocation == null) {
			buttonGetLocation = (Button) findViewById(R.id.buttonGetLocation);

			buttonGetLocation.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					System.out.println("BUTTON GET_LOCATION CLICK!!!");
					mainApp.requestLocation();
				}
			});
		}
		return buttonGetLocation;
	}

	public Button getButtonShowOnMap() {
		if(buttonShowOnMap == null) {
			buttonShowOnMap = (Button) findViewById(R.id.buttonShowOnMap);

			buttonShowOnMap.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					System.out.println("BUTTON SHOW_ON_MAP CLICK!!!");
					Intent intent = new Intent(MainActivity.this, MyMapActivity.class);
					startActivity(intent);
				}
			});
		}
		return buttonShowOnMap;
	}

	public Button getButtonSendSMS() {
		if(buttonSendSMS == null) {
			buttonSendSMS = (Button) findViewById(R.id.buttonSendSMS);
			buttonSendSMS.setEnabled(false);
			buttonSendSMS.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					System.out.println("BUTTON SEND_SMS CLICK!!!");


					SmsManager sm = SmsManager.getDefault();
					String number = "6508570720";
					sm.sendTextMessage(number, null, "Test SMS Message", null, null);

					System.out.println("SMS SENDED!!!");

					//  number = "6508570720";
					//  startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null)));

				}
			});
		}
		return buttonSendSMS;
	}
}