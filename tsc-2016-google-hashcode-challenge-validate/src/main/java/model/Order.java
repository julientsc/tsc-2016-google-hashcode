package model;

import java.util.ArrayList;
import java.util.Comparator;

public class Order extends Warehouse {

	public Order(int id, int cx, int cy) {
		super(id, cx, cy);
	}

	public ArrayList<ProductType> getAvailiableProductsByWeight() {
		ArrayList<ProductType> productTypes = new ArrayList<>();
		for(ProductType productType : getAvailiableProducts()) {
			productTypes.add(productType);
		}
		
		productTypes.sort(new Comparator<ProductType>() {
			@Override
			public int compare(ProductType o1, ProductType o2) {
				return getProductQty(o2) * o2.getWeight() - getProductQty(o1) * o1.getWeight();
			}
		});
		
		return productTypes;
	}




}
