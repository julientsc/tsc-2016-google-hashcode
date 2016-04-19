package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Order extends Location {

	private final int orderId;
	private List<Product> productIds = new ArrayList<Product>();

	public List<Product> getProductsList() {
		return productIds;
	}

	public Order(int orderId, int cx, int cy, List<Product> products) {
		super(cx, cy);

		this.productIds = products;
		this.orderId = orderId;
	}

	ArrayList<Warehouse> warehouses = null;

	public ArrayList<Warehouse> getWarehouseSortedByDistance() {
		if (warehouses != null)
			return warehouses;
		warehouses = new ArrayList<Warehouse>();
		ArrayList<Warehouse> warehouses = new ArrayList<Warehouse>();
		for (Warehouse warehouse : Data.getInstance().getWarehouses().values()) {
			warehouses.add(warehouse);
		}

		final Order currentOrder = this;

		Collections.sort(warehouses, new Comparator<Location>() {

			public int compare(Location o1, Location o2) {
				return (int) (o1.getDistance(currentOrder) - o2.getDistance(currentOrder));
			}

		});

		return warehouses;
	}

	public int getTotalWeight() {
		int weight = 0;
		for (Product productId : productIds) {
			weight += productId.getWeight();
		}
		return weight;

	}
	
	public boolean isFinished(){
	    for(ProductPile pile: getProducts()){
	        if(pile.getCount() > 0){
	            return false;
	        }
	    }
	    
	    return true;
	}

	public List<ProductPile> getProducts() {
		Map<Integer, ProductPile> map = new HashMap<Integer, ProductPile>();

		for (Product productId : productIds) {
		    int id = productId.getID();
		    
			if (!map.containsKey(id)) {
				map.put(id, new ProductPile(Data.getInstance().getProduct().get(id), 0));
			}
			
			ProductPile pile = map.get(id);
			
			pile.setCount(pile.getCount() + 1);
		}

		return new ArrayList<ProductPile>(map.values());
	}

	public void removeProduct(Product product) {
		for (Product ii : productIds) {
			if (ii.getID() == product.getID()) {
				productIds.remove(ii);
				return;
			}
		}
	}
	
	public int getID(){
	    return orderId;
	}
	

    @Override
    public String toString(){
        return "ID "+orderId+" itemCount "+productIds.size();
    }

}
