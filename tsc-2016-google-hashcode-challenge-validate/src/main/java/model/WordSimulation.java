package model;

public class WordSimulation {
	private int rows;
	private int cols;
	private int drones;
	private int turn;
	private int payload;
	
	public WordSimulation(int rows, int cols, int drones, int turn, int payload) {
		this.rows = rows;
		this.cols = cols;
		this.drones = drones;
		this.turn = turn;
		this.payload = payload;
	}

	public int getRows() {
		return rows;
	}

	public int getCols() {
		return cols;
	}

	public int getDrones() {
		return drones;
	}

	public int getTurn() {
		return turn;
	}

	public int getPayload() {
		return payload;
	}



}
