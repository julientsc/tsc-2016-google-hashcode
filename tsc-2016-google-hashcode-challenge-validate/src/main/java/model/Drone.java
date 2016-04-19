package model;

import java.util.HashMap;

public class Drone {

	private int id;
	private int maxPayload;
	
	private int costOptimized = 0;
	
	public int getCostOptimized() {
		return costOptimized;
	}

	public void addCostOptimized(int valueToAdd) {
		this.costOptimized += valueToAdd;
	}

	private HashMap<ProductType, Integer> stock = new HashMap<>();
	
	public int getId() {
		return id;
	}

	private Location location;

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Drone(int id, int payload, Location location) {
		super();
		this.id = id;
		this.maxPayload = payload;
		this.location = location;
	}

	public int getRestPayload() {
		int total = 0;
		for (ProductType productType : stock.keySet()) {
			total += productType.getWeight() * stock.get(productType);
		}
		return maxPayload - total;
	}

	/**
	 * Add one product on the warehouse
	 * 
	 * @param productType
	 */
	public void addProduct(ProductType productType) {
		if (!stock.containsKey(productType))
			stock.put(productType, 0);
		stock.put(productType, stock.get(productType) + 1);
	}

	/**
	 * Remove one product from the warehouse
	 * 
	 * @param productType
	 * @return true if successful
	 */
	public boolean removeProduct(ProductType productType) {
		if (stock.containsKey(productType)) {
			stock.put(productType, stock.get(productType) - 1);
			if (stock.get(productType) == 0)
				stock.remove(productType);
			return true;
		}
		return false;
	}
}
