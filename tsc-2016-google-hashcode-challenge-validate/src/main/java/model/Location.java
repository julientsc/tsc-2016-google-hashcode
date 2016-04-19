package model;

public class Location {
	private int cx;
	private int cy;
	
	public int getCx() {
		return cx;
	}
	
	public int getCy() {
		return cy;
	}

	public void setCx(int cx) {
		this.cx = cx;
	}

	public void setCy(int cy) {
		this.cy = cy;
	}

	public Location(int cx, int cy) {
		super();
		this.cx = cx;
		this.cy = cy;
	}	
	
	public double getDistance(Location l) {
		return Math.sqrt(Math.pow(l.cx - cx, 2) + Math.pow(l.cy - cy, 2));
	}
	
}
