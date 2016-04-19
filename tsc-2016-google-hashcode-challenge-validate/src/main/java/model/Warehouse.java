package model;

import java.util.HashMap;
import java.util.Set;

public class Warehouse extends Location {

	private int id;
	
	public int getId() {
		return id;
	}

	public Warehouse(int id, int cx, int cy) {
		super(cx, cy);
		this.id = id;
	}
	

	public int getProductWeightTotal() {
		int tot = 0;
		for ( ProductType key : stock.keySet()) {
			int nb =  stock.get(key);
			tot += key.getWeight() * nb;
		}
		return tot;
	}

	private HashMap<ProductType, Integer> stock = new HashMap<>();

	/**
	 * Get all available product
	 * @return
	 */
	public Set<ProductType> getAvailiableProducts() {
		return stock.keySet();
	}
	
	/**
	 * Add one product on the warehouse
	 * 
	 * @param productType
	 */
	public void addProdct(ProductType productType) {
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

	/**
	 * Return the quantity available for a product
	 * @param productType
	 * @return
	 */
	public int getProductQty(ProductType productType) {
		if (stock.containsKey(productType)) {
			return stock.get(productType);
		}
		return 0;
	}

	public boolean contains(Order order) {
		for(ProductType productType : order.getAvailiableProducts()) {
			if(stock.containsKey(productType) && stock.get(productType)>=order.getProductQty(productType)) 
				return true;
		}
		return false;
	}
}
