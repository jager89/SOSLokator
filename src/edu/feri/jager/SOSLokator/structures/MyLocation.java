package edu.feri.jager.SOSLokator.structures;

public class MyLocation {

	private String phoneNumber;
	private String longitude;
	private String latitude;
	private String timestamp;
	private String displayName;
	
	private long id;

	public MyLocation() {
		phoneNumber = new String();
		longitude = new String();
		latitude = new String();
		setDisplayName(new String());
		id = -1;
	}
	
	public MyLocation(String phoneNumber) {
		this.phoneNumber = phoneNumber;
		this.longitude = new String();
		this.latitude = new String();
		this.displayName = new String();
		this.id = -1;
	}
	
	public MyLocation(String phoneNumber, String longitude, String latitude) {
		this.phoneNumber = phoneNumber;
		this.longitude = longitude;
		this.latitude = latitude;
		this.displayName = new String();
		this.id = -1;
	}
	
	public boolean equal(MyLocation structure) {
		if(id != structure.getId()) {
			return false;
		}
		if(!phoneNumber.equalsIgnoreCase(structure.getPhoneNumber())) {
			return false;
		}
		if(!longitude.equalsIgnoreCase(structure.getLongitude())) {
			return false;
		}
		if(!latitude.equalsIgnoreCase(structure.getLatitude())) {
			return false;
		}
		if(!timestamp.equalsIgnoreCase(structure.getTimestamp())) {
			return false;
		}
		if(!displayName.equalsIgnoreCase(structure.getDisplayName())) {
			return false;
		}
		
		return true;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public String getLongitude() {
		return longitude;
	}
	
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	
	public String getLatitude() {
		return latitude;
	}
	
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
}