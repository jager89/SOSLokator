package edu.feri.jager.SOSLokator;

import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

import android.location.Location;
import android.os.Bundle;

public class MyMapActivity extends MapActivity {
	private MapView mapView = null;
	private MainApplication mainApp = null;
	private MapController mapController = null;
	private MyPositionOverlay positionOverlay = null;
	private List<Overlay> overlays = null;

	public MapView getMapView() {
		if(mapView == null) {
			mapView = (MapView) findViewById(R.id.mapView);
			mapView.setBuiltInZoomControls(true);
			mapController = mapView.getController();
			mapController.setZoom(6);
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

		setContentView(R.layout.map);

		mainApp = (MainApplication) getApplication();
		mainApp.setMyMapActivity(this);

		getMapView();
		setNewPoint(mainApp.getCurrentLocation());
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