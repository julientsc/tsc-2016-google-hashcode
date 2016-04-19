package model;

public class Warehouse extends Location {
    
    private final int id;
    private final int [] productQuantities;

	public Warehouse(int warehouseId, int cx, int cy, String[] productQty) {
	    super(cx, cy);
	    productQuantities = new int[productQty.length];
	    
	    for(int i = 0; i < productQty.length; i++){
	        productQuantities[i] = Integer.parseInt(productQty[i]);
	    }
	    
	    id = warehouseId;
	}
	
	public int getTotalproductCount() {
		int tot = 0;
		for(int i = 0 ; i < productQuantities.length ; i++) {
			tot += productQuantities[i];
		}
		return tot;
	}
	
	public int getId(){
	    return id;
	}

	public boolean hasProduct(Product item) {
		return productQuantities[item.getID()] > 0;
	}
	
	public int getProductQuantity(int productID){
	    return productQuantities[productID];
	}
	
	public void removeProduct(int product, int count) {
	    for(int i = 0; i < count; i++){
	        removeProduct(product);
	    }
	}
	
	public void removeProduct(int product) {
		if(productQuantities[product]> 0)
			productQuantities[product]--;
		else 
			System.err.println("Monstre bug");
	}
	
	@Override
    public String toString(){
	    return "ID "+id;
	}
}
