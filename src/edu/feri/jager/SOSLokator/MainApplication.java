package edu.feri.jager.SOSLokator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import edu.feri.jager.SOSLokator.databases.ContactsDBAdapter;
import edu.feri.jager.SOSLokator.databases.LocationsDBAdapter;
import edu.feri.jager.SOSLokator.structures.MyContacts;
import edu.feri.jager.SOSLokator.structures.MyLocation;
import edu.feri.jager.SOSLokator.structures.MySOSMessage;
import edu.feri.jager.SOSLokator.stuff.MyLocationProvider;

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

	private final int VALUE = 60; 
	private MyLocationProvider locationProvider = null;
	private MainActivity mainActivity = null;
	private List<MyContacts> listContactsID = null;
	private List<MyLocation> listLocations = null;
	private ContactsDBAdapter dbContacts = null;
	private LocationsDBAdapter dbLocation = null;
	private MySOSMessage currentSOSMessage = null;
	private int widgetCounter;
	
	public void onCreate() {
		super.onCreate();
		locationProvider = new MyLocationProvider(this, (LocationManager) this.getSystemService(Context.LOCATION_SERVICE));
		setListContactsID(new Vector<MyContacts>());
		dbContacts = new ContactsDBAdapter(this);
		setDbLocation(new LocationsDBAdapter(this));
		
//		fillFromDBRezultati();

		requestLocation();

	}

	public MySOSMessage getCurrentSOSMessage() {
		return currentSOSMessage;
	}

	public void setCurrentSOSMessage(MySOSMessage currentSOSMessage) {
		this.currentSOSMessage = currentSOSMessage;
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
			return text + "\n" + "Zemljepisna širina: " + zSirina + "\n" + "Zemljepisna dolžina:" + zDolzina + "\n" + "Naslov:\n" + geodata +
					"\n<data>" + location.getLatitude() + " " + location.getLongitude() + "</div>";
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
		value = (value - (int) value) * VALUE; 
		int minutes = (int) value;
		value = (value - (int) value) * VALUE; 
		int seconds = (int) value;

		return degrees + "° " + minutes + "' " + seconds + "\"";
	}

	public void fillContactsFromDB() {
		listContactsID = new Vector<MyContacts>();
		dbContacts.open();
		Cursor c = dbContacts.getAll();
		MyContacts tmp;
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			tmp = new MyContacts();
			tmp.setContactID(c.getString(ContactsDBAdapter.POS_CONTACT_ID));
			tmp.setId(c.getLong(ContactsDBAdapter.POS__ID));
			listContactsID.add(tmp); 
		}
		c.close();

		for(int i = 0; i < listContactsID.size(); i++) {
			Cursor phones = getContentResolver().query(Phone.CONTENT_URI, null, Phone.CONTACT_ID + " = " +  listContactsID.get(i).getContactID(), null, null); 
			if(phones != null && !phones.moveToFirst()) {
				dbContacts.deleteContact(listContactsID.get(i).getId());
				listContactsID.remove(i);
			}
		}
		dbContacts.close();

	}
	
	public void addContactToDB(MyContacts s) {
		dbContacts.open();
		s.setId(dbContacts.insertContact(s));
		dbContacts.close();	
	}

	public void removeContactFromDB(long id) {
		dbContacts.open();
		dbContacts.deleteContact(id);
		dbContacts.close();
	}
	
	public void setDbContatcs(ContactsDBAdapter db1) {
		this.dbContacts = db1;
	}

	public ContactsDBAdapter getDbContatcs() {
		return dbContacts;
	}

	public void setListContactsID(List<MyContacts> listContactsID) {
		this.listContactsID = listContactsID;
	}

	public List<MyContacts> getListContactsID() {
		return listContactsID;
	}
	

	public void fillLocationsFromDB() {
		listLocations = new ArrayList<MyLocation>();
		dbLocation.open();
		Cursor c = dbLocation.getAll();
		MyLocation location;
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			location = new MyLocation();
			location.setPhoneNumber(c.getString(LocationsDBAdapter.POS_PHONE_NUMBER));
			location.setDisplayName(c.getString(LocationsDBAdapter.POS_DISPLAY_NAME));
			location.setLongitude(c.getString(LocationsDBAdapter.POS_LONGITUDE));
			location.setLatitude(c.getString(LocationsDBAdapter.POS_LATITUDE));
			location.setTimestamp(c.getString(LocationsDBAdapter.POS_TIMESTAMP));
			location.setId(c.getLong(LocationsDBAdapter.POS__ID));
			listLocations.add(location); 
		}
		c.close();

		List<MyLocation> tempList = new ArrayList<MyLocation>();
		for(int i = listLocations.size() - 1; i >= 0; i--) {
			tempList.add(listLocations.get(i));
		}
		setListLocations(tempList);
		
		dbLocation.close();
	}
	public void addLocationToDB(MyLocation s) {
		dbLocation.open();
		s.setId(dbLocation.insertContact(s));
		dbLocation.close();	
	}
	
	public void removeLocationFromDB(long id) {
		dbLocation.open();
		dbLocation.deleteContact(id);
		dbLocation.close();
	}
	

	public LocationsDBAdapter getDbLocation() {
		return dbLocation;
	}

	public void setDbLocation(LocationsDBAdapter dbLocation) {
		this.dbLocation = dbLocation;
	}

	public List<MyLocation> getListLocations() {
		return listLocations;
	}

	public void setListLocations(List<MyLocation> listLocations) {
		this.listLocations = listLocations;
	}

	public int getWidgetCounter() {
		return widgetCounter;
	}

	public void setWidgetCounter(int widgetCounter) {
		this.widgetCounter = widgetCounter;
	}
}
