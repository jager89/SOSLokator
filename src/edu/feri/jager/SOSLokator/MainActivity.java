package edu.feri.jager.SOSLokator;

import java.util.List;
import java.util.Vector;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends MapActivity {
	private Button buttonGetLocation = null;
	private Button buttonSendSMS = null;
	private TextView textViewLocation = null;
	private MainApplication mainApp = null;
	private MapView mapView = null;
	private MapController mapController = null;
	private MyPositionOverlay positionOverlay = null;
	private List<Overlay> overlays = null;

	public MapView getMapView() {
		if(mapView == null) {
			mapView = (MapView) findViewById(R.id.mapView);
			mapView.setBuiltInZoomControls(true);
			mapController = mapView.getController();
			mapController.setZoom(15);
			mapView.setSatellite(true);
			mapView.setStreetView(true);
			mapView.displayZoomControls(true);

			overlays = getMapView().getOverlays();
			overlays.add(positionOverlay = new MyPositionOverlay());
		}
		return mapView;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mainApp = (MainApplication) getApplication();
		mainApp.setMainActivity(this);

		getTextViewLocation();
		getButtonGetLocation();
		getButtonSendSMS();
		getMapView();

		if(mainApp.getCurrentLocation() != null)
			setNewPoint(mainApp.getCurrentLocation());
	}

	@Override
	public void onStart() {
		super.onStart();
		mainApp.fillFromDBRezultati();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.exit:
			System.out.println("EXIT BUTTON");
			finish();
			return true;
		case R.id.numberList:
			System.out.println("PREJEMNIKI BUTTON");
			startActivity(new Intent(this,ContactsListActivity.class));
			return true;
			//		case R.id.prefrences:
			//			System.out.println("NASTAVITVE BUTTON");
			//			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_activity_menu, menu);
		return true;
	}

	public TextView getTextViewLocation() {
		if(textViewLocation == null) {
			textViewLocation = (TextView)findViewById(R.id.textViewLocation);
			textViewLocation.setText("Pridobivam podatke o lokaciji...");
			if(mainApp.getCurrentLocation() !=  null) {
				textViewLocation.setText(mainApp.LocationToString("Zadnja znana lokacija:"));
			}
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
					if(mainApp.getCurrentLocation() != null)
						setNewPoint(mainApp.getCurrentLocation());
				}
			});
		}
		return buttonGetLocation;
	}

	public Button getButtonSendSMS() {
		if(buttonSendSMS == null) {
			buttonSendSMS = (Button) findViewById(R.id.buttonSendSMS);
			buttonSendSMS.setVisibility(View.GONE);
			buttonSendSMS.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					System.out.println("BUTTON SEND_SMS CLICK!!!");
					Vector<edu.feri.jager.SOSLokator.MyContacts> vecID = mainApp.getVecContactsID();

					Vector<String> vec = new Vector<String>();
					for(int j = 0; j < vecID.size(); j++) {
						Cursor phones = getContentResolver().query(Phone.CONTENT_URI, null, Phone.CONTACT_ID + " = " + vecID.get(j).getContactID(), null, null); 

						while (phones.moveToNext()){ 
							String phoneNumber = phones.getString(phones.getColumnIndex(Phone.NUMBER)); 
							
							int type = phones.getInt(phones.getColumnIndex(Phone.TYPE)); 
							if(type == Phone.TYPE_MOBILE) {
								System.out.println("PHONE: " + phoneNumber + "; " + type);
								phoneNumber = phoneNumber.replace("-", "");
								phoneNumber = phoneNumber.replace("(", "");
								phoneNumber = phoneNumber.replace(")", "");
								vec.add(phoneNumber);
							}
							
						}  
					}
					if(vec.size() > 0) {
						ProgressDialog dialog;
						String str;
						if(vec.size() == 1)
							str = "Pošiljam Sporoèilo! Poèakajte prosim...";
						else
							str = "Pošiljam Sporoèila! Poèakajte prosim...";
						dialog = ProgressDialog.show(MainActivity.this, "", str, true);

						int messageCount = 0;
						for(int i  = 0; i < vec.size(); i++) {
							try {
								System.out.println(vec.get(i));
								SmsManager sm = SmsManager.getDefault();
								sm.sendMultipartTextMessage(vec.get(i), null, sm.divideMessage(mainApp.LocationToString("SOS!!! Moja trenutna lokacija:")), null, null);
								messageCount++;
							} catch (Exception e) {
								e.printStackTrace();
									Toast.makeText(MainActivity.this,"Napaka pri pošiljanju!\n" + (messageCount + 1) + ". sporoèilo ni bilo poslano!\n(prejemnik: " + vec.get(i) + ")",Toast.LENGTH_LONG).show();
							}
							System.out.println("SMS " + i + " SENDED!!!");
						}
						dialog.cancel();
						if(vec.size() == 1)
							str = "Sporoèilo je bilo poslano!";
						else
							str = "Sporoèila so bila poslana!\n(poslanih " + messageCount + " od " + vec.size() + " sporoèil)";
						Toast.makeText(MainActivity.this, str ,Toast.LENGTH_LONG).show();

					}
					else
						Toast.makeText(MainActivity.this,"V seznamu ni kontaktov!\nDodajte kontakt",Toast.LENGTH_LONG).show();
				}
			});
		}
		return buttonSendSMS;
	}

	public void setNewPoint(Location location) {
		if(location != null && positionOverlay != null && mapController != null) {
			positionOverlay.setLocation(location);

			Double geoLat = location.getLatitude()*1E6;
			Double geoLng = location.getLongitude()*1E6;

			mapController.animateTo(new GeoPoint(geoLat.intValue(), geoLng.intValue()));
		}
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
}