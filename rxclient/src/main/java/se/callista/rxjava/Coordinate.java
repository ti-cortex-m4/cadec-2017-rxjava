package se.callista.rxjava;

public class Coordinate {
	private double longitude;
	private double latitude;

	public Coordinate(double latitude, double longitude) {
		this.longitude = longitude;
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public double getLatitude() {
		return latitude;
	}

}
