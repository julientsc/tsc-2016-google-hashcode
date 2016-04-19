package command;

import java.util.ArrayList;

import model.Drone;
import model.Order;
import model.ProductType;
import model.Warehouse;

public class Load implements ICommand {

	private Drone drone;
	private String TAG = "L";
	private ProductType productType;
	private int quantity;
	private Warehouse warehouse;
	private Order order;
	
	
	
	public ProductType getProductType() {
		return productType;
	}


	public int getQuantity() {
		return quantity;
	}

	public Warehouse getWarehouse() {
		return warehouse;
	}


	public Order getOrder() {
		return order;
	}

	public Load(Drone drone, Warehouse warehouse, ProductType productType, int quantity) {
		this.drone = drone;
		this.warehouse = warehouse;
		this.productType = productType;
		this.quantity = quantity;
		this.order = null;
	}
	
	public Load(Drone drone, Warehouse warehouse, ProductType productType, int quantity, Order order) {
		this.drone = drone;
		this.warehouse = warehouse;
		this.productType = productType;
		this.quantity = quantity;
		this.order = order;
	}
	
	public void exec(ArrayList<ICommand> commands) {
		drone.addCostOptimized((int) Math.ceil(drone.getLocation().getDistance(warehouse)));
		drone.addCostOptimized(1);
		drone.setLocation(warehouse);
		
		for(int i = 0 ; i < quantity ; i++) {
			warehouse.removeProduct(productType);
			if(order != null)
			order.removeProduct(productType);
			drone.addProduct(productType);
		}
		
		commands.add(this);
//		System.out.println(toString());
	}
	
	public String toString() {
		return drone.getId() +" " +  TAG +" " +  warehouse.getId() +" " +  productType.getId() + " " + quantity;
	}	
	
	
}
