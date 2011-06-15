package edu.feri.jager.SOSLokator;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import edu.feri.jager.SOSLokator.database.DBAdapterContacts;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.view.View;

public class MainApplication extends Application {
	public static final String PREFS_NAME = "PrefrencesFile";

	private LocationProvider locationProvider = null;
	private MainActivity mainActivity = null;
	private Vector<MyContacts> vecContactsID = null;
	private DBAdapterContacts db1 = null;

	public void onCreate() {
		super.onCreate();
		locationProvider = new LocationProvider(this, (LocationManager) this.getSystemService(Context.LOCATION_SERVICE));
		setVecContactsID(new Vector<MyContacts>());
		db1 = new DBAdapterContacts(this);
//		fillFromDBRezultati();

		requestLocation();

	}

	public void setCurrentLocation(Location location) {
		locationProvider.setLocation(location);
	}

	public void requestLocation() {
		locationProvider.requestLocation();
	}

	public Location getCurrentLocation() {
		return locationProvider.getLocation();
	} 

	public void setMainActivity(MainActivity mainActivity) {
		this.mainActivity = mainActivity;
	}

	public void refreshLocation() {
		mainActivity.getTextViewLocation().setText(LocationToString("Vaša trenutna lokacija:"));
		mainActivity.setNewPoint(locationProvider.getLocation());

		if(mainActivity.getButtonSendSMS().getVisibility() == View.GONE)
			mainActivity.getButtonSendSMS().setVisibility(View.VISIBLE);
	}

	public String LocationToString(String text) {
		Location location = locationProvider.getLocation();
		if(location !=  null) {
			String zDolzina = convertDoubleToDegreesMinutesSeconds(location.getLongitude());
			String zSirina = convertDoubleToDegreesMinutesSeconds(location.getLatitude());

			double lat = location.getLatitude();
			double lng = location.getLongitude();
			String geodata = getLocationInfo(lat, lng);
			return text + "\n" + "Zemljepisna širina: " + zSirina + "\n" + "Zemljepisna dolžina: " + zDolzina + "\nNaslov:\n" + geodata;
		}
		return "N/A";
	}

	private String getLocationInfo(double latitude, double longitude) {
		Geocoder gc = new Geocoder(mainActivity, Locale.getDefault());

		try {
			List<Address> addresses = gc.getFromLocation(latitude, longitude, 1);
			//obstaja tudi obratno iz imena latatude ... getFromLocationName
			StringBuilder sb = new StringBuilder();
			if (addresses.size() > 0) {
				Address address = addresses.get(0);

				for (int i = 0; i < address.getMaxAddressLineIndex(); i++)
				sb.append(address.getAddressLine(i)).append("\n");
				sb.append(address.getLocality()).append("\n");
				sb.append(address.getPostalCode()).append("\n");
				sb.append(address.getCountryName());
			}

			return  sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "Ni podatka";
	}

	private String convertDoubleToDegreesMinutesSeconds(double value) {
		int degrees = (int) value;
		value = Math.abs(value);
		value = (value - (int) value) * 60; 
		int minutes = (int) value;
		value = (value - (int) value) * 60; 
		int seconds = (int) value;

		return degrees + "° " + minutes + "' " + seconds + "\"";
	}

	public void setVecContactsID(Vector<MyContacts> vecContactsID) {
		this.vecContactsID = vecContactsID;
	}

	public Vector<MyContacts> getVecContactsID() {
		return vecContactsID;
	}

	public void setDb1(DBAdapterContacts db1) {
		this.db1 = db1;
	}

	public DBAdapterContacts getDb1() {
		return db1;
	}


	public void fillFromDBRezultati() {
		vecContactsID = new Vector<MyContacts>();
		db1.open();
		Cursor c = db1.getAll();
		MyContacts tmp;
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			tmp = new MyContacts();
			tmp.setContactID(c.getString(DBAdapterContacts.POS_CONTACT_ID));
			tmp.setId(c.getLong(DBAdapterContacts.POS__ID));
			vecContactsID.add(tmp); 
		}
		c.close();

		for(int i = 0; i < vecContactsID.size(); i++) {
			Cursor phones = getContentResolver().query(Phone.CONTENT_URI, null, Phone.CONTACT_ID + " = " +  vecContactsID.get(i).getContactID(), null, null); 
			if(phones != null && !phones.moveToFirst()) {
				db1.deleteContact( vecContactsID.get(i).getId());
				vecContactsID.remove(i);
			}
		}
		db1.close();

	}
	public void addDBRezultat(MyContacts s) {
		db1.open();
		s.setId(db1.insertContact(s));
		db1.close();	
	}

	public void remove(long id) {
		db1.open();
		db1.deleteContact(id);
		db1.close();
	}
}
