package edu.feri.jager.SOSLokator;

import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

import android.content.ContentResolver;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.widget.TextView;

public class SearchActivity extends MapActivity {
	private TextView textViewLocation = null;
	private MainApplication mainApp = null;
	private MapView mapView = null;
	private MapController mapController = null;
	private MyPositionOverlay positionOverlay = null;
	private MyPositionOverlay sosPositionOverlay = null;

	private List<Overlay> overlays = null;
	
	public MapView getMapView() {
		if(mapView == null) {
			mapView = (MapView) findViewById(R.id.mapViewSearch);
			mapView.setBuiltInZoomControls(true);
			mapController = mapView.getController();
			mapController.setZoom(10);
			mapView.setSatellite(true);
			mapView.setStreetView(true);
			mapView.displayZoomControls(true);

			overlays = getMapView().getOverlays();
			overlays.add(positionOverlay = new MyPositionOverlay("START"));

			sosPositionOverlay = new MyPositionOverlay("CILJ");
			Location sosLocation = new Location("SOSLocation");
		
			double latitude = Double.parseDouble(mainApp.getCurrentSOSMessage().getLatitude());
			sosLocation.setLatitude(latitude);

			double longitude = Double.parseDouble(mainApp.getCurrentSOSMessage().getLongitude());
			sosLocation.setLongitude(longitude);
			
			sosPositionOverlay.setLocation(sosLocation);
			overlays.add(sosPositionOverlay);
		}
		return mapView;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		mainApp = (MainApplication) getApplication();

		Bundle extras = getIntent().getExtras();

		mainApp.setCurrentSOSMessage(new SOSMessage(extras.getStringArray("smsData")));
		
		getTextViewLocation();

		getMapView();

		if(mainApp.getCurrentLocation() != null)
			setNewPoint(mainApp.getCurrentLocation());
	}

	public TextView getTextViewLocation() {
		if(textViewLocation == null) {
			textViewLocation = (TextView)findViewById(R.id.textViewLocation);
			String sender = getContactDisplayNameByNumber(mainApp.getCurrentSOSMessage().getSender());
			textViewLocation.setText("SOS!!! Pošiljatelj: " + sender);
		}
		return textViewLocation;
	}
	
	public void setNewPoint(Location location) {
		if(location != null && positionOverlay != null && mapController != null) {
			positionOverlay.setLocation(location);

			Double geoLat = location.getLatitude()*1E6;
			Double geoLng = location.getLongitude()*1E6;

			mapController.animateTo(new GeoPoint(geoLat.intValue(), geoLng.intValue()));
		}
	}
	
	public String getContactDisplayNameByNumber(String number) {
	    Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
	    String name = new String(number);

	    ContentResolver contentResolver = getContentResolver();
	    Cursor contactLookup = contentResolver.query(uri, new String[] {BaseColumns._ID,
	            ContactsContract.PhoneLookup.DISPLAY_NAME }, null, null, null);

	    try {
	        if (contactLookup != null && contactLookup.getCount() > 0) {
	            contactLookup.moveToNext();
	            name = contactLookup.getString(contactLookup.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
	            //String contactId = contactLookup.getString(contactLookup.getColumnIndex(BaseColumns._ID));
	        }
	    } finally {
	        if (contactLookup != null) {
	            contactLookup.close();
	        }
	    }

	    return name;
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
}