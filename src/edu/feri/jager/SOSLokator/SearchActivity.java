package edu.feri.jager.SOSLokator;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.TextView;

public class SearchActivity extends MapActivity {
	private final int ZOOM = 10;
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
			mapController.setZoom(ZOOM);
			mapView.setSatellite(true);
			mapView.setStreetView(true);
			mapView.displayZoomControls(true);

			overlays = getMapView().getOverlays();
			overlays.clear();
			overlays.add(positionOverlay = new MyPositionOverlay("START"));
			
			sosPositionOverlay = new MyPositionOverlay("CILJ");
			Location sosLocation = new Location("SOSLocation");
		
			double latitude = Double.parseDouble(mainApp.getCurrentSOSMessage().getLatitude());
			sosLocation.setLatitude(latitude);

			double longitude = Double.parseDouble(mainApp.getCurrentSOSMessage().getLongitude());
			sosLocation.setLongitude(longitude);
			
			sosPositionOverlay.setLocation(sosLocation);
			
			
			
			edu.feri.jager.SOSLokator.GeoPoint startPoint = null;
			edu.feri.jager.SOSLokator.GeoPoint endPoint = null;
//			startPoint = new edu.feri.jager.SOSLokator.GeoPoint(46.317730, 15.018700);
			
			Double startLatitude = mainApp.getCurrentLocation().getLatitude();
			Double startLongitude = mainApp.getCurrentLocation().getLongitude();
			startPoint = new edu.feri.jager.SOSLokator.GeoPoint(startLatitude, startLongitude);

			endPoint = new edu.feri.jager.SOSLokator.GeoPoint(46.296810, 15.042570);

			DirectionPathData directionPathData = new DirectionPathData();
//			StringBuilder[][] stringBuilder = directionPathData.getDirectionData(46.317730, 15.018700, 46.296810, 15.042570);
//			StringBuilder[][] stringBuilder = directionPathData.getDirectionData(startPoint, endPoint);

			Vector<Vector<edu.feri.jager.SOSLokator.GeoPoint>> stringBuilder = directionPathData.getDirectionData(startPoint, endPoint);
			
			
//			String pairsss = getDirectionData(mainApp.getCurrentLocation().getLatitude() + "," + mainApp.getCurrentLocation().getLongitude(), latitude + "," + longitude);
//			String[] pairss = pairsss.split(":");
			overlays.add(sosPositionOverlay);
//			for(String pairssss : pairss) {
//				String[] pairs = pairssss.split(";");
			
//			if(pairs != null) {
				
//				for(String s :pairs) {
//					System.out.println(s);
//				}
				
//				LETU�: longitude: 46.317730; latitude:15.018700 
//				String pairs[] = getDirectionData("46.317730,15.018700", "46.288127,15.039947");
//				String pairs[] = getDirectionData("letus", "braslovce");
//				String[] lngLat = pairs[0].split(",");

//			stringBuilder.get(0).get(0).getLatitude();
			// STARTING POINT
				GeoPoint startGP = new GeoPoint(
						(int) (stringBuilder.get(0).get(0).getLatitude() * 1E6), (int) (stringBuilder.get(0).get(0).getLongitude() * 1E6));

				// NAVIGATE THE PATH

				GeoPoint gp1;
				GeoPoint gp2 = startGP;

				for(int i = 0; i < stringBuilder.size(); i++) {
				Vector<edu.feri.jager.SOSLokator.GeoPoint> pairs = stringBuilder.get(i);
				for (int j = 1; j < pairs.size(); j++) {
//					lngLat = pairs[i].split(",");
					gp1 = gp2;
					// watch out! For GeoPoint, first:latitude, second:longitude

					gp2 = new GeoPoint((int) (pairs.get(j).getLatitude() * 1E6),
							(int) (pairs.get(j).getLongitude() * 1E6));
					overlays.add(new PathOverlay(gp1, gp2, Color.RED, 3));
//					Log.d("xxx", "pair:" + pairs[i]);
				}
			}
//			}
			
//			String pairss[][] = getDirectionData(mainApp.getCurrentLocation().getLatitude() + "," + mainApp.getCurrentLocation().getLongitude(), latitude + "," + longitude);
//			String pairsss = getDirectionData(mainApp.getCurrentLocation().getLatitude() + "," + mainApp.getCurrentLocation().getLongitude(), latitude + "," + longitude);
//			String[] pairss = pairsss.split(":");
//			overlays.add(sosPositionOverlay);
//			for(String pairssss : pairss) {
//				String[] pairs = pairssss.split(";");
//			
//			if(pairs != null) {
//				
//				for(String s :pairs) {
//					System.out.println(s);
//				}
//				
////				LETU�: longitude: 46.317730; latitude:15.018700 
////				String pairs[] = getDirectionData("46.317730,15.018700", "46.288127,15.039947");
////				String pairs[] = getDirectionData("letus", "braslovce");
//				String[] lngLat = pairs[0].split(",");
//
//				// STARTING POINT
//				GeoPoint startGP = new GeoPoint(
//						(int) (Double.parseDouble(lngLat[1]) * 1E6), (int) (Double
//								.parseDouble(lngLat[0]) * 1E6));
//
//				// NAVIGATE THE PATH
//
//				GeoPoint gp1;
//				GeoPoint gp2 = startGP;
//
//				for (int i = 1; i < pairs.length; i++) {
//					lngLat = pairs[i].split(",");
//					gp1 = gp2;
//					// watch out! For GeoPoint, first:latitude, second:longitude
//
//					gp2 = new GeoPoint((int) (Double.parseDouble(lngLat[1]) * 1E6),
//							(int) (Double.parseDouble(lngLat[0]) * 1E6));
//					overlays.add(new PathOverlay(gp1, gp2, Color.RED, 3));
//					Log.d("xxx", "pair:" + pairs[i]);
//				}
//			}
//			}
			
		}
		return mapView;
	
	}
//	
//	private String[][] getDirectionData(String srcPlace, String destPlace) {
//
////		String urlString = "http://maps.google.com/maps?saddr="
////				+ srcPlace + "&daddr=" + destPlace
////				+ "&output=kml";
//		
//		String urlString = "http://maps.google.com/maps?f=d&hl=en&saddr="
//				+ srcPlace + "&daddr=" + destPlace
//				+ "&ie=UTF8&0&om=0&output=kml";
//
//		Log.d("URL", urlString);
//		Document doc = null;
//		HttpURLConnection urlConnection = null;
//		URL url = null;
//		String pathConent = "";
//
//		try {
//
//			url = new URL(urlString.toString());
//			urlConnection = (HttpURLConnection) url.openConnection();
//			urlConnection.setRequestMethod("GET");
//			urlConnection.setDoOutput(true);
//			urlConnection.setDoInput(true);
//			urlConnection.connect();
//			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//			DocumentBuilder db = dbf.newDocumentBuilder();
//			doc = db.parse(urlConnection.getInputStream());
//			NodeList nl = doc.getElementsByTagName("LineString");
//			String[][] r = new String[nl.getLength()][];
//
//			for (int s = 0; s < nl.getLength(); s++) {
//				Node rootNode = nl.item(s);
//				NodeList configItems = rootNode.getChildNodes();
//				for (int x = 0; x < configItems.getLength(); x++) {
//					Node lineStringNode = configItems.item(x);
//					NodeList path = lineStringNode.getChildNodes();
//					pathConent = path.item(0).getNodeValue();
//					String[] tempContent = pathConent.split(" ");
//					r[s] = tempContent;
//				}
//			}
//			
//
//			return r;
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}

	private String getDirectionData(String srcPlace, String destPlace) {

//		String urlString = "http://maps.google.com/maps?saddr="
//				+ srcPlace + "&daddr=" + destPlace
//				+ "&output=kml";
		
		String urlString = "http://maps.google.com/maps?f=d&hl=en&saddr="
				+ srcPlace + "&daddr=" + destPlace
				+ "&ie=UTF8&0&om=0&output=kml";

//		Log.d("URL", urlString);
		Document doc = null;
		HttpURLConnection urlConnection = null;
		URL url = null;
		String pathConent = "";

		try {

			url = new URL(urlString.toString());
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoOutput(true);
			urlConnection.setDoInput(true);
			urlConnection.connect();
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			doc = db.parse(urlConnection.getInputStream());
			NodeList nl = doc.getElementsByTagName("LineString");
			String r = "";

			for (int s = 0; s < nl.getLength(); s++) {
				Node rootNode = nl.item(s);
				NodeList configItems = rootNode.getChildNodes();
				for (int x = 0; x < configItems.getLength(); x++) {
					Node lineStringNode = configItems.item(x);
					NodeList path = lineStringNode.getChildNodes();
					r += path.item(0).getNodeValue() + ":";
					
//					String[] tempContent = pathConent.split(" ");
//					r[s] = tempContent;
				}
			}
			

			return r;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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
			textViewLocation = (TextView) findViewById(R.id.textViewLocation);
			String sender = getContactDisplayNameByNumber(mainApp.getCurrentSOSMessage().getSender());
			textViewLocation.setText("SOS!!! Pošiljatelj: " + sender);
		}
		return textViewLocation;
	}
	
	public void setNewPoint(Location location) {
		if(location != null && positionOverlay != null && mapController != null) {
			positionOverlay.setLocation(location);

			Double geoLat = location.getLatitude() * 1E6;
			Double geoLng = location.getLongitude() * 1E6;

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