package edu.feri.jager.SOSLokator;

import java.text.DecimalFormat;

public class GeoPoint {
	private double latitude;
	private double longitude;
	private DecimalFormat decimalFormat = null;

	public GeoPoint() {
		this.latitude = new Double(0.0);
		this.longitude = new Double(0.0);
		this.decimalFormat = new DecimalFormat("0.000000");
	}
	
	public GeoPoint(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.decimalFormat = new DecimalFormat("0.000000");
	}
	
	public double getLatitude() {
		return latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public StringBuilder getLatitudeString() {
		if(decimalFormat != null) {
			String value = decimalFormat.format(latitude);
			value = value.replaceAll(",", ".");
			return new StringBuilder(value);
		}
		String value = String.valueOf(latitude);
		value = value.replaceAll(",", ".");
		return new StringBuilder(value);
	}
	public StringBuilder getLongitudeString() {
		if(decimalFormat != null) {
			String value = decimalFormat.format(longitude);
			value = value.replaceAll(",", ".");
			return new StringBuilder(value);
		}
		String value = String.valueOf(longitude);
		value = value.replaceAll(",", ".");
		return new StringBuilder(value);
	}
	
	public void setLatitude(double endLatitude) {
		this.latitude = endLatitude;
	}
	public void setLongitude(double endLongitude) {
		this.longitude = endLongitude;
	}
}
