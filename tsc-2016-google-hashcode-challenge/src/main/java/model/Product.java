package model;

public class Product {

	private int weight;
	private int id;

	public Product(int id, int weight) {
		this.weight = weight;
		this.id = id;
	}
	
	public int getID(){
	    return id;
	}
	
	public int getWeight(){
	    return weight;
	}

    @Override
    public String toString(){
        return "ID "+id;
    }
}
