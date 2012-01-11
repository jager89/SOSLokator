package edu.feri.jager.SOSLokator;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class LocationProvider {
	private final int MIN_DISTANCE = 10;
	private final int MIN_TIME = 2000;
	private static final int TWO_MINUTES = 1000 * 60 * 2;
	private Location location = null;
	private LocationManager locationManager = null;
	private MainApplication app = null;

	private LocationListener locationListener = new LocationListener() {
		public void onLocationChanged(Location location) {
			if(isBetterLocation(location, app.getCurrentLocation()))
				LocationProvider.this.location = location;
			app.refreshLocation();
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
			System.out.println("STATUS SPREMENJEN : " + provider + ", status: " + status + "; extras: " + extras);
		}

		public void onProviderEnabled(String provider) {
			System.out.println("PROVIDER ENABLED: " + provider);
		}

		public void onProviderDisabled(String provider) {
			System.out.println("PROVIDER DISABLED: " + provider);
		}
	};

	public LocationProvider(MainApplication app, LocationManager locationManager) {
		this.app = app;
		this.locationManager = locationManager;
	}

	public void requestLocation() {
		if(locationManager != null) {
			String provider = locationManager.getBestProvider(getCriteria(), true);
			Location location = locationManager.getLastKnownLocation(provider);
			if(location != null)
				app.setCurrentLocation(location);
			locationManager.requestLocationUpdates(provider, MIN_TIME, MIN_DISTANCE, locationListener);
		}

	}

	private Criteria getCriteria() {
		Criteria temp = new Criteria();

		temp.setPowerRequirement(Criteria.POWER_LOW);
		temp.setAccuracy(Criteria.ACCURACY_FINE);
		temp.setAltitudeRequired(false);
		temp.setBearingRequired(false);
		temp.setCostAllowed(true);

		return temp;
	}

	private boolean isBetterLocation(Location location, Location currentBestLocation) {
		if (currentBestLocation == null)
			return true;

		long timeDelta = location.getTime() - currentBestLocation.getTime();
		boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
		boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
		boolean isNewer = timeDelta > 0;

		if (isSignificantlyNewer)
			return true;
		else if (isSignificantlyOlder)
			return false;

		int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
		boolean isLessAccurate = accuracyDelta > 0;
		boolean isMoreAccurate = accuracyDelta < 0;
		boolean isSignificantlyLessAccurate = accuracyDelta > 200;

		boolean isFromSameProvider = isSameProvider(location.getProvider(),
				currentBestLocation.getProvider());

		if (isMoreAccurate)
			return true;
		else if (isNewer && !isLessAccurate)
			return true;
		else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider)
			return true;

		return false;
	}

	private boolean isSameProvider(String provider1, String provider2) {
		if (provider1 == null) {
			return provider2 == null;
		}
		return provider1.equals(provider2);
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Location getLocation() {
		return location;
	}
}
