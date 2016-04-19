package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Drone extends Location{

    private final int id;
    private final int payloadMax;
    private Location location;
    
    private List<ProductPile> products = new ArrayList<ProductPile>();
    
    public Drone(int id, int payloadMax, Location location) {
        super(location.getX(), location.getY());
        this.id = id;
        this.payloadMax = payloadMax;
        this.location = location;
    }
    
    public void loadProduct(Product product, int count){
        for(ProductPile pile: products){
            if(pile.getProduct().getID() == product.getID()){
                pile.setCount(pile.getCount() + count);
                return;
            }
        }
        
        products.add(new ProductPile(product, count));
    }
    
    public void loadProduct(Product product, int count, Warehouse w){
        if(w.getDistance(this) != 0) {
        	Move(w);
        }
        loadProduct(product, count);
    }
    
    public void unloadProduct(int productId, int count){
        for(ProductPile pile: products){
            if(pile.getProduct().getID() == productId){
                pile.setCount(pile.getCount() - count);
                return;
            }
        }
    }

    private void Move(Warehouse w) {
		// TODO Auto-generated method stub
		
	}

	public int getID(){
        return id;
    }
    
    public int getMaxPayload(){
        return payloadMax;
    }
    
    public int getCurrentPayload(){
        int payLoad = 0;
        for(ProductPile pile: products){
            payLoad += pile.getWeight();
        }
        
        return payLoad;
    }
    
    public boolean hasOrderedItems(Order order){
        for(ProductPile pile: order.getProducts()){
            for(ProductPile loaded: products){
                if(loaded.getProduct().getID() == pile.getProduct().getID()){
                    if(loaded.getCount() < pile.getCount()){
                        return false;
                    }
                }
            }
        }
        
        return true;
    }
    
	public ArrayList<Warehouse> getWarehouseSortedByDistance() {
		ArrayList<Warehouse> warehouses = new ArrayList<Warehouse>();
		for (Warehouse warehouse : Data.getInstance().getWarehouses().values()) {
			warehouses.add(warehouse);
		}

		final Drone currentOrder = this;
		
		Collections.sort(warehouses, new Comparator<Location>() {

			public int compare(Location o1, Location o2) {
				return (int)(o1.getDistance(currentOrder) - o2.getDistance(currentOrder));
			}
			
		});
		
		return warehouses;
	}
    
	public int getProductCount(int productId){
	    for(ProductPile pile: products){
	        if(pile.getProduct().getID() == productId){
	            return pile.getCount();
	        }
	    }
	    
	    return 0;
	}
	
    public Warehouse getClosestWarehouse(Product product){
        Warehouse best = null;
        
        List<Warehouse> wareHouses = new ArrayList<Warehouse>(Data.getInstance().getWarehouses().values());
        
        for(Warehouse candidate: wareHouses){
            if(candidate.getProductQuantity(product.getID()) > 0){
                if(best == null){ //TODO: Write
                    best = candidate;
                }
            }
        }
        
        return best;
    }
    
    public int getMaxCount(Product product){
        return (int)Math.ceil((getMaxPayload() - getCurrentPayload()) / product.getWeight());
    }
    
    public boolean isFull(){
        return getMaxPayload() <= getCurrentPayload();
    }

    @Override
    public String toString(){
        return "ID "+id;
    }

    
	public void deliver(Location o) {
		setX(o.getX());
		setY(o.getY());
		System.out.println("deliver order " +id + " (" + getCurrentPayload() + ")");
	}
	
    @Override
    public int getX(){
        return location.getX();
    }
    
    @Override
    public int getY(){
        return location.getY();
    }
    
    public void setLocation(Location location){
        this.location = location;
        this.setX( location.getX());
        this.setY(location.getY());
    }
    
    public Location getLocation() {
    	return this.location;
    }

	public void unload() {
		this.products = new ArrayList<ProductPile>(); 
	}
}
