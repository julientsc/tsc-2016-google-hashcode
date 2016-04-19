package model;

public class ProductType {
	private int weight;
	private int id;
	
	public ProductType(int id, int weight) {
		super();
		this.weight = weight;
		this.id = id;
	}
	
	public int getWeight() {
		return weight;
	}
	
	public int getId() {
		return id;
	}
	
}
