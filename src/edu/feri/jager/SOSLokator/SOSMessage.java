package edu.feri.jager.SOSLokator;

public class SOSMessage {
	private String sender;
	private String longitude;
	private String latitude;
	private String adress;
	
	public SOSMessage() {
		sender = longitude = latitude = adress = "N/A";
	}
	
	public SOSMessage(String sender, String latitude, String longitude, String adress) {
		this.sender = sender;
		this.latitude = latitude;
		this.longitude = longitude;
		this.adress = adress;
	}
	
	public String[] getArray() {
		return new String[]{sender, latitude, longitude, adress};
	}
	
	public SOSMessage(String[] array) {
		if(array.length == 4) {
			this.sender = array[0];
			this.latitude = array[1];
			this.longitude = array[2];
			this.adress = array[3];
		}
		else {
			new SOSMessage();
		}
	}
	
	public SOSMessage(String text) {
		String temp = new String(text);
		this.sender = temp.substring(0, temp.indexOf("\n"));
		temp = temp.substring(0, temp.indexOf("\n"));
		this.longitude = temp.substring(0, temp.indexOf("\n"));
		temp = temp.substring(0, temp.indexOf("\n"));
		this.latitude = temp.substring(0, temp.indexOf("\n"));
		temp = temp.substring(0, temp.indexOf("\n"));
		this.adress = temp.substring(0, temp.indexOf("\n"));
	}
	
	public String getText() {
		return sender + "\n" + latitude + "\n" + longitude + "\n" + adress;
	}
	
	public String getSender() {
		return sender;
	}
	
	public void setSender(String sender) {
		this.sender = sender;
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
	
	public String getAdress() {
		return adress;
	}
	
	public void setAdress(String adress) {
		this.adress = adress;
	}
}
