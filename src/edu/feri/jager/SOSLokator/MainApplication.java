package edu.feri.jager.SOSLokator;

import android.app.Application;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

public class MainApplication extends Application {
//	private Location location = null;
	private LocationProvider locationProvider = null;
	private MainActivity mainActivity = null;
	private MyMapActivity myMapActivity = null;

	public void onCreate() {
		super.onCreate();
		locationProvider = new LocationProvider(this, (LocationManager) this.getSystemService(Context.LOCATION_SERVICE));

		requestLocation();	
	}

	public void setCurrentLocation(Location location) {
//		this.location = location;
		locationProvider.setLocation(location);
	}

	public void requestLocation() {
		locationProvider.requestLocation();
	}

	public Location getCurrentLocation() {
		return locationProvider.getLocation();
//		return location;
	} 

	public void setMyMapActivity(MyMapActivity myMapActivity) {
		this.myMapActivity = myMapActivity;
	}

	public void setMainActivity(MainActivity mainActivity) {
		this.mainActivity = mainActivity;
	}

	public void refreshLocation() {
		printLocation("Vaša trenutna lokacija:");

		if(!mainActivity.getButtonSendSMS().isEnabled())
			mainActivity.getButtonSendSMS().setEnabled(true);

		if(myMapActivity != null)
			myMapActivity.setNewPoint(locationProvider.getLocation());
	}

	public void printLocation(String text) {
		Location location = locationProvider.getLocation();
		if(location !=  null) {
			String zDolzina = convertDoubleToDegreesMinutesSeconds(location.getLongitude());
			String zSirina = convertDoubleToDegreesMinutesSeconds(location.getLatitude());

			mainActivity.getTextViewLocation().setText("\t" + text + "\n\t" + "Zemljepisna širina:\t\t" + zSirina + "\n\t" + "Zemljepisna dolžina: " + zDolzina);// + location.getLatitude() + "\n" + location.getLongitude());
		}
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
}
